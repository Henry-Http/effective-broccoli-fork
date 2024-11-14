package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameEqualsIgnoreCase(String name);

    Optional<Category> findByNameEqualsIgnoreCase(String name);

    List<Category> findAllByIsVisible(Boolean isVisible);

}
