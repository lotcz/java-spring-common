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

	protected Page<T> currentPage = null;

	public PagedSmartQueue() {
	}

	public void reload() {
		this.loading = true;
		this.currentPage = this.loadRemaining();
		this.loading = false;
		this.currentItemNumber = 0;
	}

	@Override
	public void reset() {
		this.currentPage = null;
		this.currentItemNumber = 0;
	}

	public abstract Page<T> loadRemaining();

	public boolean needsReload() {
		return (this.currentPage == null || this.currentPage.getNumberOfElements() <= this.currentItemNumber);
	}

	@Override
	public T next() {
		if (this.needsReload()) {
			this.reload();
		}
		List<T> content = this.currentPage.getContent();
		T result = content.size() > this.currentItemNumber ? content.get(this.currentItemNumber) : null;
		this.currentItemNumber++;
		return result;
	}

	@Override
	public long getRemaining() {
		if (this.needsReload()) {
			this.reload();
		}
		return this.currentPage.getTotalElements() - this.currentItemNumber;
	}

	@Override
	public int getLoaded() {
		return this.currentPage == null ? 0 : this.currentPage.getNumberOfElements();
	}

}
