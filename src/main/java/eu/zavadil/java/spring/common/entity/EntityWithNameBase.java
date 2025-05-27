package eu.zavadil.java.spring.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class EntityWithNameBase extends EntityBase implements EntityWithName, LookupTable {

	private static final int NAME_SIZE = 255;

	@Column(length = NAME_SIZE)
	private String name;

	public void setName(String name) {
		this.name = this.truncateString(name, NAME_SIZE);
	}

}
