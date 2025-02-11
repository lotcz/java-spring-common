package eu.zavadil.java.spring.common.paging;

import org.springframework.data.domain.Page;

import java.util.List;

public class JsonPageImpl<T> implements JsonPage<T> {

	private final Page<T> page;

	public JsonPageImpl(Page<T> page) {
		this.page = page;
	}

	static <T> JsonPage<T> of(Page<T> page) {
		return new JsonPageImpl<>(page);
	}

	@Override
	public List<T> getContent() {
		return this.page.getContent();
	}

	@Override
	public int getTotalItems() {
		return this.page.getNumberOfElements();
	}

	@Override
	public int getPageSize() {
		return this.page.getSize();
	}

	@Override
	public int getPageNumber() {
		return this.page.getNumber();
	}
}
