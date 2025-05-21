package eu.zavadil.java.spring.common.client;

import eu.zavadil.java.spring.common.entity.Entity;
import org.springframework.http.HttpMethod;

public class HttpEntityApiClient<T extends Entity> implements EntityApiClient<T> {

	protected final String baseUrl;

	protected final HttpApiClientBase client;

	protected final Class<T> clazz;

	public HttpEntityApiClient(HttpApiClientBase client, String baseUrl, Class<T> clazz) {
		this.client = client;
		this.baseUrl = baseUrl;
		this.clazz = clazz;
	}

	@Override
	public T save(T data) {
		return this.client.exchange(
			data.getId() == null ? HttpMethod.POST : HttpMethod.PUT,
			data.getId() == null ? this.baseUrl : String.format("%s/%d", this.baseUrl, data.getId()),
			data,
			this.clazz
		);
	}

	@Override
	public T loadById(int id) {
		return this.client.exchange(
			HttpMethod.GET,
			String.format("%s/%d", this.baseUrl, id),
			null,
			this.clazz
		);
	}
}
