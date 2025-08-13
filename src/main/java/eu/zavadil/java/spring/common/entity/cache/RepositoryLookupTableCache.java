package eu.zavadil.java.spring.common.entity.cache;

import eu.zavadil.java.caching.HashCacheStats;
import eu.zavadil.java.caching.Lazy;
import eu.zavadil.java.spring.common.entity.EntityBase;
import eu.zavadil.java.spring.common.entity.EntityRepository;
import eu.zavadil.java.spring.common.paging.PagingUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Cache that keeps all records from table loaded in memory.
 * Good for small tables.
 */
public class RepositoryLookupTableCache<T extends EntityBase> {

	protected final EntityRepository<T> repository;

	protected final Lazy<Map<Integer, T>> tableCache;

	public RepositoryLookupTableCache(
		EntityRepository<T> repository
	) {
		this.repository = repository;
		this.tableCache = new Lazy<>(this::loadCache);
	}

	private Map<Integer, T> loadCache() {
		List<T> list = this.repository.findAll();
		return list.stream().collect(Collectors.toMap(EntityBase::getId, item -> item));
	}

	public T get(Integer id) {
		return this.tableCache.get().get(id);
	}

	public T set(T t) {
		T saved = this.repository.save(t);
		this.tableCache.get().put(saved.getId(), saved);
		return t;
	}

	public List<T> all() {
		return this.tableCache.get().values().stream().toList();
	}

	public Page<T> page(PageRequest pr) {
		return PagingUtils.getPage(this.all(), pr);
	}

	public HashCacheStats getStats() {
		return (this.tableCache.getCache() == null)
			? new HashCacheStats(0, 0)
			: new HashCacheStats(this.tableCache.get().size(), 0);
	}

	public void reset(Integer key) {
		if (this.tableCache.getCache() == null) return;
		this.tableCache.get().remove(key);
	}

	public void reset() {
		this.tableCache.reset();
	}

	public void deleteById(int id) {
		this.reset(id);
		this.repository.deleteById(id);
	}

	public void delete(T t) {
		this.deleteById(t.getId());
	}

}
