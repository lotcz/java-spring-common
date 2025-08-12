package eu.zavadil.java.spring.common.paging;

import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.Map;

public class PagingUtils {

	public static PageRequest of(int page, int size, String sorting) {
		return PageRequest.of(page, size, SortingUtils.fromString(sorting));
	}

	public static PageRequest of(int page, int size) {
		return PageRequest.of(page, size);
	}

	public static PageRequest of(JsonPage<?> page) {
		return PageRequest.of(page.getPageNumber(), page.getPageSize());
	}

	public static Map<String, String> toQueryParams(PageRequest pr) {
		Map<String, String> result = new HashMap<>();
		result.put("page", String.valueOf(pr.getPageNumber()));
		result.put("size", String.valueOf(pr.getPageSize()));
		if (pr.getSort().isSorted()) {
			result.put("sorting", SortingUtils.toString(pr.getSort()));
		}
		return result;
	}

}
