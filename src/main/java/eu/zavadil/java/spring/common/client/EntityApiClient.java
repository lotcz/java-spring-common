package eu.zavadil.java.spring.common.client;

import eu.zavadil.java.spring.common.entity.Entity;

public interface EntityApiClient<T extends Entity> {

	T save(T data);

	T loadById(int id);

}
