package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.dto.CategoryDTO;
import br.com.alison.purchases.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResources {

    @Autowired
    private CategoryService service;

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){
        Category category = service.findCategoryById(id);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> findAll(){
        List<Category> categories = service.findAll();
        List<CategoryDTO> categoriesDTO = categories.stream()
                .map(category -> new CategoryDTO(category)).collect(Collectors.toList());
        return ResponseEntity.ok().body(categoriesDTO);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CategoryDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        Page<Category> categories = service.findPage(page, linesPerPage, orderBy, direction);
        Page<CategoryDTO> categoriesDTO = categories.map(category -> new CategoryDTO(category));
        return ResponseEntity.ok().body(categoriesDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody  CategoryDTO categoryDTO){
        Category category = service.fromDTO(categoryDTO);
        category = service.insert(category);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody  CategoryDTO categoryDTO, @PathVariable Long id){
        Category category = service.fromDTO(categoryDTO);
        category.setId(id);
        service.update(category);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
