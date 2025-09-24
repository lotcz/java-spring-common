package eu.zavadil.java.spring.common.client;

import eu.zavadil.java.UrlBuilder;
import eu.zavadil.java.caching.Lazy;
import eu.zavadil.java.util.ExceptionUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HttpApiClientBase {

	protected final String baseUrl;

	protected final Lazy<HttpHeaders> defaultHeaders;

	public HttpApiClientBase(String baseUrl) {
		this.baseUrl = baseUrl;
		this.defaultHeaders = new Lazy<>(
			() -> {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", "application/json");
				return headers;
			}
		);
	}

	public HttpHeaders getHttpHeaders(String path) {
		return this.defaultHeaders.get();
	}

	protected String getUrl(String path, Map<String, String> queryParams) {
		UrlBuilder builder = UrlBuilder.of(this.baseUrl);
		builder.addPath(path);
		if (queryParams != null) {
			queryParams.forEach(builder::addQuery);
		}
		return builder.buildAsString();
	}

	protected <T> HttpEntity<T> createHttpEntity(String path, T obj) {
		return new HttpEntity<T>(obj, this.getHttpHeaders(path));
	}

	private <TRes> TRes processResponse(ResponseEntity<TRes> response) {
		try {
			TRes result = response.getBody();
			if (response.getStatusCode().isError()) {
				throw new RuntimeException(String.format("Http error %d: %s", response.getStatusCode().value(), result));
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Cannot get body from response!", e);
		}
	}

	protected <TReq, TRes> TRes exchange(HttpMethod method, String path, Map<String, String> queryParams, TReq request, Class<TRes> cls) {
		String url = this.getUrl(path, queryParams);
		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<TRes> response = restTemplate.exchange(url, method, this.createHttpEntity(path, request), cls);
			return this.processResponse(response);
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

	protected <TReq, TRes> TRes exchange(HttpMethod method, String path, Map<String, String> queryParams, TReq request, ParameterizedTypeReference<TRes> tref) {
		String url = this.getUrl(path, queryParams);
		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<TRes> response = restTemplate.exchange(url, method, this.createHttpEntity(path, request), tref);
			return this.processResponse(response);
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

	protected <TReq, TRes> TRes exchange(HttpMethod method, String path, TReq request, ParameterizedTypeReference<TRes> responseClass) {
		return this.exchange(method, path, null, request, responseClass);
	}

	protected String exchange(HttpMethod method, String path, Map<String, String> queryParams) {
		return this.exchange(method, path, queryParams, null, String.class);
	}

	protected <TRes> TRes get(String path, Map<String, String> queryParams, Class<TRes> responseClass) {
		return this.exchange(HttpMethod.GET, path, queryParams, null, responseClass);
	}

	protected <TRes> TRes get(String path, Class<TRes> responseClass) {
		return this.get(path, null, responseClass);
	}

	protected <TRes> TRes get(String path, Map<String, String> queryParams, ParameterizedTypeReference<TRes> responseType) {
		return this.exchange(HttpMethod.GET, path, queryParams, null, responseType);
	}

	protected <TRes> TRes get(String path, ParameterizedTypeReference<TRes> responseType) {
		return this.get(path, null, responseType);
	}

	protected <TReq, TRes> TRes put(String path, Map<String, String> queryParams, TReq request, Class<TRes> responseClass) {
		return this.exchange(HttpMethod.PUT, path, queryParams, request, responseClass);
	}

	protected <TReq, TRes> TRes put(String path, TReq request, Class<TRes> responseClass) {
		return this.put(path, null, request, responseClass);
	}

	protected <TReq, TRes> TRes post(String path, Map<String, String> queryParams, TReq request, Class<TRes> responseClass) {
		return this.exchange(HttpMethod.POST, path, queryParams, request, responseClass);
	}

	protected <TReq, TRes> TRes post(String path, TReq request, Class<TRes> responseClass) {
		return this.post(path, null, request, responseClass);
	}

	protected void delete(String path, Map<String, String> queryParams) {
		this.exchange(HttpMethod.DELETE, path, queryParams);
	}

	protected void delete(String path) {
		this.delete(path, null);
	}

}

