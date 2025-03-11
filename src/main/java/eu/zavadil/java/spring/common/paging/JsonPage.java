package eu.zavadil.java.spring.common.paging;

import java.util.List;

public interface JsonPage<T> {

	List<T> getContent();

	long getTotalItems();

	int getPageSize();

	int getPageNumber();

}
