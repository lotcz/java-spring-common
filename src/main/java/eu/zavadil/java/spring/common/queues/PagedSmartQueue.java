package eu.zavadil.java.spring.common.queues;

import eu.zavadil.java.queues.SmartQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;

@Slf4j
public abstract class PagedSmartQueue<T> implements SmartQueue<T> {

	@Getter
	private int currentItemNumber;

	@Getter
	private boolean loading;

	private int processed;

	protected Page<T> currentPage = null;

	public PagedSmartQueue() {
	}

	public void reload() {
		this.loading = true;
		this.currentPage = this.loadRemaining();
		this.currentItemNumber = 0;
		this.loading = false;
	}

	@Override
	public void reset() {
		this.processed = 0;
		this.currentPage = null;
		this.currentItemNumber = 0;
	}

	public abstract Page<T> loadRemaining();

	public boolean needsReload() {
		return (this.currentPage == null || this.currentPage.getNumberOfElements() <= this.currentItemNumber)
			&& !this.loading;
	}

	protected synchronized void checkReload() {
		if (this.needsReload()) {
			this.reload();
		}
	}

	@Override
	public T next() {
		this.checkReload();
		List<T> content = this.currentPage.getContent();
		T result = content.size() > this.currentItemNumber ? content.get(this.currentItemNumber) : null;
		this.currentItemNumber++;
		this.processed++;
		return result;
	}

	@Override
	public long getRemaining() {
		this.checkReload();
		return this.currentPage == null ? 0 : this.currentPage.getTotalElements() - this.processed;
	}

	@Override
	public int getLoaded() {
		return this.currentPage == null ? 0 : this.currentPage.getNumberOfElements();
	}

	@Override
	public int getProcessed() {
		return this.processed;
	}

}
