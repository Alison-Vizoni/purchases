package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.repository.CategoryRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.logging.Logger;

import static br.com.alison.purchases.utils.GeneratorUtil.generateCategoryName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Profile("integration_test_with_docker")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryResourceIntegrationTest {

    private final Logger LOG = Logger.getLogger(CategoryResourceIntegrationTest.class.getName());

    @LocalServerPort
    int randomServerPort;

    private Category category;

    private String url;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp(){
        RestAssuredMockMvc.mockMvc(mvc);
        url = "http://localhost:" + randomServerPort + "/categories";
    }

    @Test
    void getAllCategories_returnJsonArray() throws Exception {
        LOG.info("TEST GET ALL CATEGORIES");

        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryById_returnJson() throws Exception {
        LOG.info("TEST GET CATEGORY BY ID");

        findCategoryInDatabase();
        mvc.perform(get(url + "/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(category.getId().intValue())))
                .andExpect(jsonPath("name", is(category.getName())));
    }

    @Test
    void evaluatesCategoryPageableParameters() throws Exception {
        LOG.info("TEST GET ALL CATEGORIES WITH PAGEABLE PARAMETERS");

        mvc.perform(get(url + "/page")
                        .param("page", "5")
                        .param("linesPerPage", "10")
                        .param("orderBy", "id")
                        .param("direction", "DESC"))
                .andExpect(status().isOk());
    }

    @Test
    void postCategory_returnHttpStatusCREATED(){
        LOG.info("TEST POST CATEGORY AUTH USER VALID");

        category = new Category(null, generateCategoryName());

        JSONObject json = new JSONObject();
        json.put("name", category.getName());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(json.toString())
                .when().post(url)
                .then().statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/categories/"));
    }

    @Test
    void postCategoryWithInvalidUser_thenHttpStatusFORBIDDEN(){
        LOG.info("TEST POST CATEGORY AUTH USER INVALID");

        category = new Category(null, generateCategoryName());

        JSONObject json = new JSONObject();
        json.put("name", category.getName());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@email.com").password("test123")
                        .roles("CLIENT"))
                .contentType("application/json")
                .body(json.toString())
                .when().post(url)
                .then().statusCode(HttpStatus.FORBIDDEN.value())
                .header("X-Frame-Options", equalTo("DENY"));
    }

    @Test
    void putCategory_returnHttpStatusNO_CONTENT(){
        LOG.info("TEST PUT CATEGORY AUTH USER VALID");

        findCategoryInDatabase();

        JSONObject json = new JSONObject();
        json.put("id", category.getId());
        json.put("name", generateCategoryName());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(json.toString())
                .when().put(url + "/" + category.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void putCategoryWithInvalidUser_thenHttpStatusFORBIDDEN() {
        LOG.info("TEST PUT CATEGORY USER INVALID");

        findCategoryInDatabase();

        JSONObject json = new JSONObject();
        json.put("id", category.getId());
        json.put("name", generateCategoryName());

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@email.com").password("test123")
                        .roles("CLIENT"))
                .contentType(ContentType.JSON)
                .body(json.toString())
                .when().put(url + "/" + category.getId())
                .then().statusCode(HttpStatus.FORBIDDEN.value())
                .header("X-Frame-Options", equalTo("DENY"));
    }

    @Test
    void deleteCategory_returnHttpStatusNO_CONTENT(){
        postCategory_returnHttpStatusCREATED();
        List<Category> categories = categoryRepository.findAll();
        category = categories.get(categories.size() - 1);

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("testAdmin@email.com").password("test123")
                        .roles("CLIENT").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .when().delete(url + "/" + category.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deleteCategoryWithInvalidUser_thenHttpStatusFORBIDDEN(){
        findCategoryInDatabase();

        RestAssuredMockMvc.given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("test@email.com").password("test123")
                        .roles("CLIENT"))
                .contentType(ContentType.JSON)
                .when().delete("/categories/" + category.getId())
                .then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    private void findCategoryInDatabase() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            postCategory_returnHttpStatusCREATED();
            categories = categoryRepository.findAll();
        }
        category = categories.get(0);
    }
}
