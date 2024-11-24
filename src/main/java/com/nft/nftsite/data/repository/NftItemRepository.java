package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.NftItem;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.enumerations.NftStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NftItemRepository extends JpaRepository<NftItem, Long> {

    boolean existsByNameEqualsIgnoreCase(String name);

    boolean existsBySlug(String slug);

    Page<NftItem> findAllByOwner(User user, Pageable pageable);

    @Query("SELECT n FROM NftItem n " +
            "WHERE (CAST(:search AS text) IS NULL OR LOWER(n.name) LIKE :search) " +
            "AND (:categoryId IS NULL OR n.category.id = :categoryId) " +
            "AND (n.nftStatus = :nftStatus)")
    Page<NftItem> findAllNftsWithFilters(
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            @Param("nftStatus") NftStatus nftStatus,
            Pageable pageable);

    Long countAllByCategory_Id(Long categoryId);

    /**
     * @Query("SELECT n FROM NftItem n " +
     *             "WHERE (:search IS NULL OR LOWER(n.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
     *             "AND (:categoryId IS NULL OR n.category.id = :categoryId) " +
     *             "AND (:minPrice IS NULL OR n.price >= :minPrice) " +
     *             "AND (:maxPrice IS NULL OR n.price <= :maxPrice) " +
     *             "AND (:currentBid IS NULL OR n.currentBid = :currentBid)")
     */


}
