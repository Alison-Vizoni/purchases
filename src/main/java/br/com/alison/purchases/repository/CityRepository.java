package br.com.alison.purchases.repository;

import br.com.alison.purchases.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT c FROM City c WHERE c.state.id = :id_state ORDER BY c.name")
    List<City> findCities(@Param("id_state") Long id_state);
}
