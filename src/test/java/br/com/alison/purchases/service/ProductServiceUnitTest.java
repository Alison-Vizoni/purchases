package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.domain.Product;
import br.com.alison.purchases.repository.ProductRepository;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static br.com.alison.purchases.utils.GeneratorUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceUnitTest {

    private final Logger LOG = Logger.getLogger(ProductServiceUnitTest.class.getName());

    private Product product;

    @MockBean
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void givenIdProduct_whenFindProductById_thenReturnProduct(){
        LOG.info("TEST ");

        Category category = generateNewCategory();
        product = generateNewProduct();
        category.getProducts().addAll(List.of(product));
        product.getCategories().addAll(List.of(category));

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Optional<Product> productFound = Optional.ofNullable(productService.findProductById(product.getId()));

        assertTrue(productFound.isPresent());
        assertSame(productFound.get(), product);
    }

    @Test
    void givenIdProductNonExistent_whenFindProductById_thenThrowObjectNotFoundException(){
        LOG.info("");

        Long idProduct = 1L;
        doThrow(new ObjectNotFoundException("Object not found: Id"))
                .when(productRepository).findById(idProduct);

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> productService.findProductById(idProduct));

        assertTrue(exception.getMessage().contains("Object not found"));
    }
}
