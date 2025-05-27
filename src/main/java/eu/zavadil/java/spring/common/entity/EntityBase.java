package eu.zavadil.java.spring.common.entity;

import eu.zavadil.java.util.StringUtils;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
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
	private Instant createdOn = Instant.now();

	@UpdateTimestamp
	private Instant lastUpdatedOn = Instant.now();
	
	protected String sanitizeString(String input) {
		return StringUtils.blankToNull(StringUtils.safeTrim(input));
	}

	protected String truncateString(String input, int size) {
		return StringUtils.safeTruncate(this.sanitizeString(input), size);
	}
}
