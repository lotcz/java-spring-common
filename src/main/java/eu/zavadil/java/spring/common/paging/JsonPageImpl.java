package eu.zavadil.java.spring.common.paging;

import org.springframework.data.domain.Page;

import java.util.List;

public class JsonPageImpl<T> implements JsonPage<T> {

	private List<T> content;

	private int pageNumber;

	private int pageSize;

	private long totalItems;

	public JsonPageImpl(List<T> content, int pageNumber, int pageSize, long totalItems) {
		this.content = content;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
	}

	public JsonPageImpl(Page<T> page) {
		this(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
	}

	public static <T> JsonPageImpl<T> of(Page<T> page) {
		return new JsonPageImpl<>(page);
	}

	public static <T> JsonPageImpl<T> of(List<T> content, int pageNumber, int pageSize, long totalItems) {
		return new JsonPageImpl<>(content, pageNumber, pageSize, totalItems);
	}

	@Override
	public List<T> getContent() {
		return this.content;
	}

	@Override
	public long getTotalItems() {
		return this.totalItems;
	}

	@Override
	public int getPageSize() {
		return this.pageSize;
	}

	@Override
	public int getPageNumber() {
		return this.pageNumber;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

}
