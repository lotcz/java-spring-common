package eu.zavadil.java.spring.common.entity.cache;

import eu.zavadil.java.caching.HashCache;
import eu.zavadil.java.spring.common.entity.EntityBase;

public abstract class EntityCacheBase<T extends EntityBase> extends HashCache<Integer, T> {

	@Override
	protected Integer extractKey(T t) {
		return t.getId();
	}
}
