package eu.zavadil.java.spring.restclient;

import eu.zavadil.java.UrlBuilder;
import eu.zavadil.java.util.ExceptionUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class HttpApiClientBase {

	protected final String baseUrl;

	protected final HttpHeaders defaultHeaders = new HttpHeaders();

	public HttpApiClientBase(String baseUrl) {
		this.baseUrl = baseUrl;
		this.defaultHeaders.add("Content-Type", "application/json");
	}

	public HttpHeaders getHttpHeaders() {
		return this.defaultHeaders;
	}

	protected String getUrl(String path, Map<String, String> queryParams) {
		UrlBuilder builder = UrlBuilder.of(this.baseUrl);
		builder.addPath(path);
		if (queryParams != null) {
			queryParams.forEach(builder::addQuery);
		}
		return builder.buildAsString();
	}

	protected <T> HttpEntity<T> createHttpEntity(T obj) {
		return new HttpEntity<T>(obj, this.getHttpHeaders());
	}

	protected <TReq, TRes> TRes exchange(HttpMethod method, String path, Map<String, String> queryParams, TReq request, Class<TRes> cls) {
		String url = this.getUrl(path, queryParams);
		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<TRes> response = restTemplate.exchange(url, method, this.createHttpEntity(request), cls);
			try {
				TRes result = response.getBody();
				if (response.getStatusCode().isError()) {
					throw new RuntimeException(String.format("Http error %d: %s", response.getStatusCode().value(), result));
				}
				return result;
			} catch (Exception e) {
				throw new RuntimeException("Cannot get body from response!", e);
			}
		} catch (Exception e) {
			throw new RuntimeException(
				String.format(
					"Error when invoking remote endpoint %s: %s",
					url,
					ExceptionUtils.getMessage(e)
				),
				e
			);
		}
	}

	protected <TReq, TRes> TRes exchange(HttpMethod method, String path, TReq request, Class<TRes> responseClass) {
		return this.exchange(method, path, null, request, responseClass);
	}

	protected <TRes> TRes get(String path, Map<String, String> queryParams, Class<TRes> responseClass) {
		return this.exchange(HttpMethod.GET, path, queryParams, null, responseClass);
	}

	protected <TRes> TRes get(String path, Class<TRes> responseClass) {
		return this.get(path, null, responseClass);
	}

}
