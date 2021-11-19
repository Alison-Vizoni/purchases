package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.dto.CategoryDTO;
import br.com.alison.purchases.repository.CategoryRepository;
import br.com.alison.purchases.service.exceptions.DataIntegrityException;
import br.com.alison.purchases.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Category insert(Category category) {
        category.setId(null);
        return repositry.save(category);
    }

    public Category update(Category category) {
        findCategoryById(category.getId());
        return repositry.save(category);
    }

    public void delete(Long id) {
        findCategoryById(id);
        try {
            repositry.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw  new DataIntegrityException("Cannot delete a category that has products.");
        }
    }

    public List<Category> findAll() {
        return repositry.findAll();
    }

    public Page<Category> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repositry.findAll(pageRequest);
    }

    public Category fromDTO(CategoryDTO categoryDTO){
        return new Category(categoryDTO.getId(), categoryDTO.getName());
    }
}
