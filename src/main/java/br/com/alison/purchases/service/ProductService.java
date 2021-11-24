package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.domain.Product;
import br.com.alison.purchases.repository.CategoryRepository;
import br.com.alison.purchases.repository.ProductRepository;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repositry;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product findProductById(Long id){
        Optional<Product> product = repositry.findById(id);

        return product.orElseThrow(() -> new ObjectNotFoundException(new StringBuilder()
                .append("Object not found! Id: ")
                .append(id)
                .append(", type: ")
                .append(Product.class.getName()).toString()));
    }

    public Page<Product> findAll(String name, List<Long> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        List<Category> categories = categoryRepository.findAllById(ids);
        return repositry.findDistinctByNameContainingAndCategoriesIn(name, categories, pageRequest);
    }
}
