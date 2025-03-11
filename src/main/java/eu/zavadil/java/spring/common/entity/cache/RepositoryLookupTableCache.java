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

public class RepositoryLookupTableCache<T extends EntityWithNameBase> extends EntityCacheBase<T> {

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

	@Override
	protected T load(Integer id) {
		return this.tableCache.get().get(id);
	}

	@Override
	protected T save(T t) {
		T saved = this.repository.save(t);
		if (this.tableCache.getCache() != null) {
			this.tableCache.get().put(t.getId(), t);
		}
		return saved;
	}

	protected T getByName(String name) {
		return this.tableCache.get().values().stream()
			.filter(e -> StringUtils.safeEquals(e.getName(), name))
			.findAny().orElse(null);
	}

	protected T obtainInternal(String name) {
		T existing = this.getByName(name);
		if (existing != null) return existing;
		T n = this.createSupplier.get();
		n.setName(name);
		return n;
	}

	public T obtain(String name) {
		name = StringUtils.safeTrim(name);
		T n = this.obtainInternal(name);
		n.setName(name);
		this.set(n);
		return n;
	}

	public List<T> all() {
		return this.tableCache.get().values().stream().toList();
	}

	@Override
	public HashCacheStats getStats() {
		return (this.tableCache.getCache() == null)
			? new HashCacheStats(0, 0)
			: new HashCacheStats(this.tableCache.get().size(), this.maxItems);
	}

	@Override
	public void reset(Integer key) {
		super.reset(key);
		this.tableCache.reset();
	}

	@Override
	public void reset() {
		this.tableCache.reset();
		super.reset();
	}

}
