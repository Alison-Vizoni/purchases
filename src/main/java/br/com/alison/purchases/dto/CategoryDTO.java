package br.com.alison.purchases.dto;

import br.com.alison.purchases.domain.Category;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

import java.io.Serializable;

public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotEmpty(message = "Name is required.")
    @Length(min=3, max=80, message = "Size must be between 4 and 80")
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Category category){
        id = category.getId();
        name = category.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
