package com.example.kadai_002.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.kadai_002.entity.House;

public interface HouseRepository extends JpaRepository<House, Long> {

	 @Query("SELECT h FROM House h WHERE h.name LIKE %:keyword% OR h.category.name LIKE %:keyword%")
	    Page<House> findByNameOrCategoryNameContaining(@Param("keyword") String keyword, Pageable pageable);

	    Page<House> findByCategory_NameContaining(String name, Pageable pageable);


    Page<House> findByPriceMaxLessThanEqual(Integer priceMax, Pageable pageable);

    Page<House> findAll(Pageable pageable);

    List<House> findTop10ByOrderByCreatedAtDesc();

    House getReferenceById(Integer id);
    
    
}
