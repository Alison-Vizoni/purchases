package br.com.alison.purchases.repository;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//    @Query("SELECT DISTINCT p FROM Product p INNER JOIN p.categories c WHERE p.name LIKE %:name% AND c IN :categories")
//    Page<Product> search(@Param("name") String name, @Param("categories") List<Category> categories, Pageable pageRequest);

    @Transactional(readOnly = true)
    Page<Product> findDistinctByNameContainingAndCategoriesIn(@Param("name") String name, @Param("categories") List<Category> categories, Pageable pageRequest);
}
