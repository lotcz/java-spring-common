package eu.zavadil.java.spring.common.entity.cache;

import eu.zavadil.java.spring.common.entity.EntityBase;
import eu.zavadil.java.spring.common.entity.EntityRepository;
import java.time.Duration;

/**
 * Cache layer over repository.
 * Load items when requested and keep only limited number of items in memory.
 */
public class RepositoryHashCache<T extends EntityBase> extends EntityCacheBase<T> {

	protected final EntityRepository<T> repository;

	public RepositoryHashCache(EntityRepository<T> repository) {
		super();
		this.repository = repository;
	}

	public RepositoryHashCache(EntityRepository<T> repository, int maxItems, Duration maxDuration) {
		super(maxItems, maxDuration);
		this.repository = repository;
	}

	/**
	 * Other components should use get() method
	 */
	@Override
	protected T load(Integer id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public T save(T t) {
		return this.repository.save(t);
	}

	public void deleteById(int id) {
		this.reset(id);
		this.repository.deleteById(id);
	}

	public void delete(T t) {
		this.deleteById(t.getId());
	}
}
