package br.com.alison.purchases.dto;

import br.com.alison.purchases.domain.Client;
import br.com.alison.purchases.service.validation.ClientUpdate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@ClientUpdate
public class ClientDTO {

    private Long id;

    @NotEmpty(message = "Name is required")
    @Length(min=3, max=255, message = "Size must be between 3 and 255")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    public ClientDTO() {
    }

    public ClientDTO(Client client) {
        id = client.getId();
        name = client.getName();
        email = client.getEmail();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
