package eu.zavadil.java.spring.common.client;

import eu.zavadil.java.spring.common.paging.JsonPage;
import eu.zavadil.java.spring.common.paging.JsonPageImpl;
import eu.zavadil.java.spring.common.paging.PagingUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class HttpPagedApiClient<T> implements PagedApiClient<T> {

	protected final String baseUrl;

	protected final HttpApiClientBase client;

	public HttpPagedApiClient(HttpApiClientBase client, String baseUrl) {
		this.client = client;
		this.baseUrl = baseUrl;
	}

	@Override
	public Page<T> loadPage(PageRequest pr) {
		JsonPage<T> page = this.client.get(
			this.baseUrl,
			PagingUtils.toQueryParams(pr),
			new ParameterizedTypeReference<JsonPageImpl<T>>() {
			}
		);
		return new PageImpl<>(page.getContent(), PagingUtils.of(page), page.getTotalItems());
	}
}
