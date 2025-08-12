package eu.zavadil.java.spring.common.entity.cache;

import eu.zavadil.java.caching.HashCacheStats;
import eu.zavadil.java.caching.Lazy;
import eu.zavadil.java.spring.common.entity.EntityBase;
import eu.zavadil.java.spring.common.entity.EntityRepository;
import eu.zavadil.java.spring.common.entity.EntityWithNameBase;
import eu.zavadil.java.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Cache that keeps all records from table loaded in memory.
 * Good for small tables.
 */
public class RepositoryLookupTableCache<T extends EntityWithNameBase> {

	private final EntityRepository<T> repository;

	protected final Supplier<T> createSupplier;

	protected final Lazy<Map<Integer, T>> tableCache;

	public RepositoryLookupTableCache(
		EntityRepository<T> repository,
		Supplier<T> createSupplier
	) {
		this.repository = repository;
		this.createSupplier = createSupplier;
		this.tableCache = new Lazy<>(this::loadCache);
	}

	private Map<Integer, T> loadCache() {
		List<T> list = this.repository.findAll();
		return list.stream().collect(Collectors.toMap(EntityBase::getId, item -> item));
	}

	public T get(Integer id) {
		return this.tableCache.get().get(id);
	}

	public T set(int id, T t) {
		return this.tableCache.get().put(t.getId(), t);
	}

	public T save(T t) {
		T saved = this.repository.save(t);
		return this.set(t.getId(), saved);
	}

	public T getByName(String name) {
		return this.tableCache.get().values().stream()
			.filter(e -> StringUtils.safeEquals(e.getName(), name))
			.findAny().orElse(null);
	}

	protected T obtain(String name) {
		T existing = this.getByName(name);
		if (existing != null) return existing;
		T n = this.createSupplier.get();
		n.setName(name);
		return this.save(n);
	}

	public List<T> all() {
		return this.tableCache.get().values().stream().toList();
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
