package br.com.alison.purchases.dto;

import br.com.alison.purchases.domain.State;

import java.io.Serializable;

public class StateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public StateDTO() {
    }

    public StateDTO(State state) {
        this.id = state.getId();
        this.name = state.getName();
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
