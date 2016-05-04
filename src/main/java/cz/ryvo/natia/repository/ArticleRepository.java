package cz.ryvo.natia.repository;

import cz.ryvo.natia.domain.ArticleVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<ArticleVO, String> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.code LIKE CONCAT(:pattern, '%') OR e.description LIKE CONCAT('%', :pattern, '%')")
    List<ArticleVO> findManyByPattern(@Param("pattern") String pattern);
}
