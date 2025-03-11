package eu.zavadil.java.spring.common.entity;

import java.time.Instant;

public interface Entity {

	Integer getId();

	Instant getCreatedOn();

	Instant getLastUpdatedOn();
}
