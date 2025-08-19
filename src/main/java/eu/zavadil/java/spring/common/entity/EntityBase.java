package eu.zavadil.java.spring.common.entity;

import eu.zavadil.java.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class EntityBase implements Entity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@CreationTimestamp
	@Column(nullable = false)
	private Instant createdOn = Instant.now();

	@UpdateTimestamp
	@Column(nullable = false)
	private Instant lastUpdatedOn = Instant.now();

	protected String sanitizeString(String input) {
		return StringUtils.blankToNull(StringUtils.safeTrim(input));
	}

	protected String truncateString(String input, int size) {
		return StringUtils.safeTruncate(this.sanitizeString(input), size);
	}

	/**
	 * Clone everything, but reset ID
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		EntityBase t = (EntityBase) super.clone();
		t.setId(null);
		return t;
	}

}
