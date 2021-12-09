package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.State;
import br.com.alison.purchases.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private StateRepository repository;

    public List<State> findAll(){
        return repository.findAllByOrderByName();
    }
}
