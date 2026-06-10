package eu.zavadil.java.spring.common.entity.cache;

import eu.zavadil.java.caching.HashCache;
import eu.zavadil.java.spring.common.entity.EntityBase;
import java.time.Duration;

public abstract class EntityCacheBase<T extends EntityBase> extends HashCache<Integer, T> {

	public EntityCacheBase() {
		super();
	}

	public EntityCacheBase(int maxItems, Duration maxDuration) {
		super(maxItems, maxDuration);
	}

	@Override
	protected Integer extractKey(T t) {
		return t.getId();
	}
}
