package eu.zavadil.java.spring.common.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityRepository<T extends EntityBase> extends JpaRepository<T, Integer> {

}
