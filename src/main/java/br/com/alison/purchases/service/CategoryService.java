package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import br.com.alison.purchases.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repositry;

    public Category findCategoryById(Long id){
        Optional<Category> category = repositry.findById(id);

        return category.orElseThrow(() -> new ObjectNotFoundException(new StringBuilder()
                .append("Object not found! Id: ")
                .append(id)
                .append(", type: ")
                .append(Category.class.getName()).toString()));
    }
}
