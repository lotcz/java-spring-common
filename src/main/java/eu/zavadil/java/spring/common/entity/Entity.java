package eu.zavadil.java.spring.common.entity;

import java.time.Instant;

public interface Entity extends Cloneable {

	Integer getId();

	Instant getCreatedOn();

	Instant getLastUpdatedOn();
}
