package br.com.alison.purchases.dto;

import br.com.alison.purchases.domain.City;

public class CityDTO {

    private Long id;
    private String name;

    public CityDTO() {
    }

    public CityDTO(City city) {
        this.id = city.getId();
        this.name = city.getName();
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
