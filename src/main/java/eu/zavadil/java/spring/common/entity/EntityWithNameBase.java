package eu.zavadil.java.spring.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class EntityWithNameBase extends EntityBase implements EntityWithName, LookupTable {

	private String name;

}
