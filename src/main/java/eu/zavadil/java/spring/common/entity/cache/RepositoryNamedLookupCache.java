package eu.zavadil.java.spring.common.entity.cache;

import eu.zavadil.java.spring.common.entity.EntityRepository;
import eu.zavadil.java.spring.common.entity.EntityWithNameBase;
import eu.zavadil.java.spring.common.paging.PagingUtils;
import eu.zavadil.java.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.function.Supplier;

/**
 * Cache that keeps all records from table loaded in memory.
 * Good for small tables.
 */
public class RepositoryNamedLookupCache<T extends EntityWithNameBase> extends RepositoryLookupTableCache<T> {

	protected final Supplier<T> createSupplier;

	public RepositoryNamedLookupCache(
		EntityRepository<T> repository,
		Supplier<T> createSupplier
	) {
		super(repository);
		this.createSupplier = createSupplier;
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
		return this.set(n);
	}

	public Page<T> search(String search, PageRequest pr) {
		List<T> filtered = (StringUtils.isBlank(search)) ? this.all()
			: this.all().stream().filter(item -> StringUtils.safeContainsIgnoreCase(item.getName(), search)).toList();
		return PagingUtils.getPage(filtered, pr);
	}

}
