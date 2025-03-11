package eu.zavadil.java.spring.common.entity.cache;

import eu.zavadil.java.spring.common.entity.EntityBase;
import eu.zavadil.java.spring.common.entity.EntityRepository;

public class RepositoryHashCache<T extends EntityBase> extends EntityCacheBase<T> {

	private final EntityRepository<T> repository;

	public RepositoryHashCache(EntityRepository<T> repository) {
		this.repository = repository;
	}

	@Override
	protected T load(Integer id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	protected T save(T t) {
		return this.repository.save(t);
	}
}
