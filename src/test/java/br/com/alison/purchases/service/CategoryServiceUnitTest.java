package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.domain.Product;
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

import static br.com.alison.purchases.utils.GeneratorUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceUnitTest {

    private final Logger LOG = Logger.getLogger(CategoryServiceUnitTest.class.getName());

    private Category category;

    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void whenSaveCategory_thenReturnsCategoryWithId(){
        LOG.info("TEST SAVING");

        String name = generateCategoryName();
        category = new Category(null, name);
        Category categoryExpected = new Category(1L, name);

        when(categoryRepository.save(any(Category.class))).thenReturn(categoryExpected);
        Category categorySaved = categoryService.insert(category);

        assertNotNull(categorySaved);
        assertEquals(categoryExpected.getId(), categorySaved.getId());
        assertEquals(categoryExpected.getName(), categorySaved.getName());
    }

    @Test
    void whenFindAllCategories_thenReturnListCategories(){
        LOG.info("TEST FIND ALL CATEGORIES");
        Category categoryExpected_1 = new Category(1L, generateCategoryName());
        Category categoryExpected_2 = new Category(2L, generateCategoryName());

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(categoryExpected_1, categoryExpected_2));

        List<Category> categories = categoryService.findAll();

        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        assertEquals(2, categories.size());
    }

    @Test
    void whenFindCategoryById_thenReturnCategory(){
        LOG.info("TEST FIND CATEGORY BY ID");

        category = generateNewCategory();

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        Optional<Category> categoryFound = Optional.ofNullable(categoryService.findCategoryById(category.getId()));

        assertTrue(categoryFound.isPresent());
        assertSame(categoryFound.get(), category);
    }

    @Test
    void whenFindCategoryById_thenThrowObjectNotFoundException(){
        LOG.info("TEST FIND CATEGORY BY NON-EXISTING ID");
        Long idCategory = 1L;

        doThrow(new ObjectNotFoundException("Object not found: Id"))
                .when(categoryRepository).findById(idCategory);

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> categoryService.findCategoryById(idCategory));

        assertTrue(exception.getMessage().contains("Object not found"));
    }

    @Test
    void whenUpdateCategory_thenReturnCategoryUpdated(){
        LOG.info("TEST UPDATE CATEGORY");

        category = generateNewCategory();
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));

        String name = generateCategoryName() + generateNumber(1);
        Category categoryExpected = new Category(category.getId(), name);
        Category categoryToUpdate = new Category(1L, name);

        when(categoryRepository.save(any(Category.class))).thenReturn(categoryExpected);
        Category categoryUpdated = categoryService.update(categoryToUpdate);

        assertEquals(categoryExpected.getName(), categoryUpdated.getName());
    }

    @Test
    void deleteCategory(){
        LOG.info("TEST DELETE CATEGORY");

        category = generateNewCategory();
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));

        categoryService.delete(category.getId());

        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @Test
    void whenDeleteCategoryWithProducts_thenThrowDataIntegrityException(){
        LOG.info("TEST DELETE CATEGORY");

        category = generateNewCategory();
        Product product = generateNewProduct();
        category.setProducts(List.of(product));
        product.setCategories(List.of(category));

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));

        doThrow(new DataIntegrityException("Cannot delete a category that has products."))
                .when(categoryRepository).deleteById(category.getId());

        DataIntegrityException exception = assertThrows(DataIntegrityException.class,
                () -> categoryService.delete(category.getId()));

        assertTrue(exception.getMessage().contains("Cannot delete a category"));
    }
}
