package eu.zavadil.java.spring.common.paging;

import eu.zavadil.java.iterators.SmartIterator;
import eu.zavadil.java.spring.common.iterators.PageSourceIterator;
import org.springframework.data.domain.Page;

public interface PageSource<T> {

	Page<T> loadPage(int pageNumber, int pageSize);

	default SmartIterator<T> iterator() {
		return new PageSourceIterator<>(this);
	}

	default SmartIterator<T> iterator(int pageSize) {
		return new PageSourceIterator<>(this, pageSize);
	}

}
