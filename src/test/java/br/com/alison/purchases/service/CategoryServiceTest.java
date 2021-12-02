package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.repository.CategoryRepository;
import br.com.alison.purchases.service.exceptions.DataIntegrityException;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static br.com.alison.purchases.utils.GeneratorUtil.generateCategory;
import static br.com.alison.purchases.utils.GeneratorUtil.generateNumber;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    private final Logger logger = Logger.getLogger(CategoryServiceTest.class.getName());

    private Category category;

    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void saveCategoryReturnsCategoryWithId(){
        logger.info("SAVING TEST");

        String name = generateCategory();
        category = new Category(null, name);
        Category categoryExpected = new Category(1L, name);

        when(categoryRepository.save(any(Category.class))).thenReturn(categoryExpected);
        Category categorySaved = categoryService.insert(category);

        assertNotNull(categorySaved);
        assertEquals(categoryExpected.getId(), categorySaved.getId());
        assertEquals(categoryExpected.getName(), categorySaved.getName());
    }

    @Test
    void findAllCategoriesReturnListCategories(){
        logger.info("FIND ALL CATEGORIES");
        Category categoryExpected_1 = new Category(1L, generateCategory());
        Category categoryExpected_2 = new Category(2L, generateCategory());

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(categoryExpected_1, categoryExpected_2));

        List<Category> categories = categoryService.findAll();

        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        assertEquals(2, categories.size());
    }

    @Test
    void findCategoryByIdReturnCategory(){
        logger.info("FIND CATEGORY BY ID");

        category = new Category(1L, generateCategory());
        Long idCategory = 1L;

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        Optional<Category> categoryFound = Optional.ofNullable(categoryService.findCategoryById(idCategory));

        assertTrue(categoryFound.isPresent());
        assertSame(categoryFound.get(), category);
    }

    @Test
    void findCategoryByIdThrowObjectNotFoundException(){
        logger.info("FIND CATEGORY BY NON-EXISTING ID ");
        Long idCategory = 1L;

        when(categoryRepository.findById(any(Long.class))).thenThrow(ObjectNotFoundException.class);

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> categoryService.findCategoryById(idCategory));

        assertTrue(exception.getMessage().contains("Object not found: Id " + idCategory));
    }

    @Test
    void updateCategoryReturnCategoryUpdated(){
        logger.info("UPDATE CATEGORY");

        category = new Category(1L, "category name");
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));

        String name = generateCategory() + generateNumber(1);
        Category categoryExpected = new Category(category.getId(), name);
        Category categoryToUpdate = new Category(1L, name);

        when(categoryRepository.save(any(Category.class))).thenReturn(categoryExpected);
        Category categoryUpdated = categoryService.update(categoryToUpdate);

        assertEquals(categoryExpected.getName(), categoryUpdated.getName());
    }

}
