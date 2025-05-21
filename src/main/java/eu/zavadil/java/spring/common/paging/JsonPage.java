package eu.zavadil.java.spring.common.paging;

import org.springframework.data.domain.Page;

import java.util.List;

public interface JsonPage<T> {

	List<T> getContent();

	long getTotalItems();

	int getPageSize();

	int getPageNumber();

	static <T> JsonPage<T> of(Page<T> page) {
		return JsonPageImpl.of(page);
	}

	static <T> JsonPage<T> of(List<T> content, int pageNumber, int pageSize, long totalItems) {
		return JsonPageImpl.of(content, pageNumber, pageSize, totalItems);
	}
}
