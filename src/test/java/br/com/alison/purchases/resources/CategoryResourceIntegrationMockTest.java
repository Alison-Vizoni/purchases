package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.dto.CategoryDTO;
import br.com.alison.purchases.security.JWTUtil;
import br.com.alison.purchases.service.CategoryService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static br.com.alison.purchases.utils.GeneratorUtil.generateCategoryName;
import static br.com.alison.purchases.utils.GeneratorUtil.generateNewCategory;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CategoryResources.class)
public class CategoryResourceIntegrationTest {

    private final Logger logger = Logger.getLogger(CategoryResourceIntegrationTest.class.getName());

    private Category category;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp(){
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    void givenCategories_whenGetAllCategories_thenReturnJsonArray() throws Exception {
        logger.info("TEST GET ALL CATEGORIES");

        category = generateNewCategory();
        Category category_1 = new Category(2L, generateCategoryName());
        List<Category> allCategories = Arrays.asList(category, category_1);

        given(categoryService.findAll()).willReturn(allCategories);

        mvc.perform(get("/categories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(category.getName())))
                .andExpect(jsonPath("$[1].name", is(category_1.getName())));

        verify(categoryService, times(1)).findAll();
        reset(categoryService);
    }

    @Test
    void givenCategory_whenGetCategoryById_thenReturnJson() throws Exception {
        logger.info("TEST GET CATEGORY BY ID");

        category = generateNewCategory();
        given(categoryService.findCategoryById(anyLong())).willReturn(category);

        mvc.perform(get("/categories/" + category.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(category.getId().intValue())))
                .andExpect(jsonPath("name", is(category.getName())));

        verify(categoryService, times(1)).findCategoryById(category.getId());
        reset(categoryService);
    }

    @Test
    void evaluatesCategoryPageableParameters() throws Exception {
        logger.info("TEST GET ALL CATEGORIES WITH PAGEABLE PARAMETERS");

        List<Category> categories = new ArrayList<>();
        Page<Category> categoryPage = new PageImpl<>(categories);

        given(categoryService.findPage(anyInt(), anyInt(), anyString(), anyString())).willReturn(categoryPage);

        mvc.perform(get("/categories/page")
                .param("page", "5")
                .param("linesPerPage", "10")
                .param("orderBy", "id")
                .param("direction", "DESC"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).findPage(5, 10, "id", "DESC");
        reset(categoryService);
    }

    @Test
    void givenNewCategory_whenAuthUserValidPostCategory_thenReturnHttpStatusCREATED(){
        logger.info("TEST POST CATEGORY AUTH USER VALID");

        category = generateNewCategory();

        Category categoryFromDTO = new Category(null, category.getName());
        when(categoryService.fromDTO(any(CategoryDTO.class))).thenReturn(categoryFromDTO);

        given(categoryService.insert(any(Category.class))).willReturn(category);

        JSONObject json = new JSONObject();
        json.put("name", category.getName());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(json.toString())
                .when().post("/categories")
                .then().statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/categories/" + category.getId()));
    }

    @Test
    void givenNewCategory_whenAuthUserInvalidPostCategory_thenHttpStatusFORBIDDEN(){
        logger.info("TEST POST CATEGORY AUTH USER INVALID");

        JSONObject json = new JSONObject();
        json.put("name", generateCategoryName());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT"))
                .contentType("application/json")
                .body(json.toString())
                .when().post("/categories")
                .then().statusCode(HttpStatus.FORBIDDEN.value())
                .header("X-Frame-Options", equalTo("DENY"));
    }

    @Test
    void givenCategoryToUpdate_whenAuthUserValidPutCategory_thenHttpStatusNO_CONTENT(){
        logger.info("TEST PUT CATEGORY AUTH USER VALID");

        category = generateNewCategory();
        when(categoryService.fromDTO(any(CategoryDTO.class))).thenReturn(category);
        given(categoryService.update(any(Category.class))).willReturn(category);

        JSONObject json = new JSONObject();
        json.put("id", category.getId());
        json.put("name", category.getName());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(json.toString())
                .when().put("/categories/" + category.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void givenCategoryToUpdate_whenAuthUserInvalidPutCategory_thenHttpStatusFORBIDDEN(){
        logger.info("TEST PUT CATEGORY AUTH USER INVALID");

        category = generateNewCategory();
        JSONObject json = new JSONObject();
        json.put("id", category.getId());
        json.put("name", category.getName());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT"))
                .contentType(ContentType.JSON)
                .body(json.toString())
                .when().put("/categories/" + category.getId())
                .then().statusCode(HttpStatus.FORBIDDEN.value())
                .header("X-Frame-Options", equalTo("DENY"));
    }

    @Test
    void givenCategoryToDelete_whenAuthUserValidDeleteCategory_thenHttpStatusNO_CONTENT(){
        logger.info("TEST DELETE CATEGORY AUTH USER VALID");

        category = generateNewCategory();
        categoryService.delete(anyLong());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .when().delete("/categories/" + category.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void givenCategoryToDelete_whenAuthUserInvalidDeleteCategory_thenHttpStatusFORBIDDEN(){
        logger.info("TEST DELETE CATEGORY AUTH USER INVALID");

        category = generateNewCategory();

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT"))
                .contentType(ContentType.JSON)
                .when().delete("/categories/" + category.getId())
                .then().statusCode(HttpStatus.FORBIDDEN.value());
    }
}
