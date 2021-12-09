package br.com.alison.purchases.repository;

import br.com.alison.purchases.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    @Transactional(readOnly = true)
    List<State> findAllByOrderByName();
}
