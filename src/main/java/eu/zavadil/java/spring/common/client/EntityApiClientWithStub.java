package eu.zavadil.java.spring.common.client;

import eu.zavadil.java.spring.common.entity.Entity;

public interface EntityApiClientWithStub<T extends Entity, TStub extends Entity> extends EntityApiClient<T> {

	void saveStub(TStub data);

	TStub loadStubById(int id);

}
