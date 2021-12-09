package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.City;
import br.com.alison.purchases.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository repository;

    public List<City> findByIdState(Long id_state){
        return repository.findCities(id_state);
    }
}
