package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.security.JWTAuthorizationFilter;
import br.com.alison.purchases.security.JWTUtil;
import br.com.alison.purchases.service.CategoryService;
import static br.com.alison.purchases.utils.GeneratorUtil.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CategoryResources.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class CategoryResourceUnitTest {

    private final Logger logger = Logger.getLogger(CategoryResourceUnitTest.class.getName());

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

// unsolved test
    @WithMockUser(username = "testAdmin", roles = {"CLIENT", "ADMIN"})
    @Test
    void givenNewCategory_whenPostCategory_thenReturnJsonId(){
        logger.info("TEST POST CATEGORY");

        category = generateNewCategory();
        given(categoryService.insert(any(Category.class))).willReturn(category);

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin").roles("CLIENT").roles("ADMIN"))
                .contentType("application/json")
                .body("{\"name\": \"new category5454\"}")
                .when().post("/categories")
                .then().statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.containsString("/categories/" + category.getId()));
    }
}
