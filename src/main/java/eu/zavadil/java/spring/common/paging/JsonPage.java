package eu.zavadil.java.spring.common.paging;

import org.springframework.data.domain.Page;

import java.util.List;

public interface JsonPage<T> {

	List<T> getContent();

	int getTotalItems();

	int getPageSize();

	int getPageNumber();

}
