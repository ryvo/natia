package cz.ryvo.natia.repository;

import cz.ryvo.natia.domain.ArticlePK;
import cz.ryvo.natia.domain.ArticleVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleVO, ArticlePK> {
}
