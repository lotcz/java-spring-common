package eu.zavadil.java.spring.common.iterators;

import eu.zavadil.java.iterators.SmartIterator;
import eu.zavadil.java.spring.common.paging.PageSource;
import org.springframework.data.domain.Page;

import java.util.Iterator;

public class PageSourceIterator<T> implements SmartIterator<T> {

	private final PageSource<T> pageSource;

	private final int pageSize;

	private int nextPageNumber = 0;

	private long totalElements = 0;

	private int processedCount = 0;

	private Iterator<T> pageIterator = null;

	public PageSourceIterator(PageSource<T> pageSource, int pageSize) {
		this.pageSource = pageSource;
		this.pageSize = pageSize;
	}

	public PageSourceIterator(PageSource<T> pageSource) {
		this(pageSource, 100);
	}

	private void loadNextPage() {
		Page<T> page = this.pageSource.loadPage(this.nextPageNumber, this.pageSize);
		this.pageIterator = page.iterator();
		this.totalElements = page.getTotalElements();
		this.nextPageNumber++;
	}

	private synchronized void checkInitialized() {
		if (this.pageIterator == null) {
			this.loadNextPage();
		}
	}

	@Override
	public long remaining() {
		this.checkInitialized();
		return this.totalElements - this.processed();
	}

	@Override
	public long processed() {
		return this.processedCount;
	}

	@Override
	public T next() {
		this.checkInitialized();
		if (!this.pageIterator.hasNext()) {
			this.loadNextPage();
		}
		this.processedCount++;
		return this.pageIterator.next();
	}
}
