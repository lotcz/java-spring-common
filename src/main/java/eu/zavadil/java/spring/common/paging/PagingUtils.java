package eu.zavadil.java.spring.common.paging;

import org.springframework.data.domain.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private static class SortComparator<T> implements Comparator<T> {
		private final Sort sort;

		public SortComparator(Sort sort) {
			this.sort = sort;
		}

		@Override
		public int compare(T o1, T o2) {
			for (Sort.Order order : sort) {
				try {
					Comparable val1 = (Comparable) getPropertyValue(o1, order.getProperty());
					Comparable val2 = (Comparable) getPropertyValue(o2, order.getProperty());

					if (val1 == null && val2 == null) continue;
					if (val1 == null) return order.isAscending() ? -1 : 1;
					if (val2 == null) return order.isAscending() ? 1 : -1;

					int cmp = val1.compareTo(val2);
					if (cmp != 0) {
						return order.isAscending() ? cmp : -cmp;
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return 0;
		}

		private Object getPropertyValue(Object obj, String property) throws Exception {
			Class<?> clazz = obj.getClass();
			// try direct field first
			try {
				Field field = clazz.getDeclaredField(property);
				field.setAccessible(true);
				return field.get(obj);
			} catch (NoSuchFieldException ignored) {
			}

			// try getter method
			String capitalized = property.substring(0, 1).toUpperCase() + property.substring(1);
			String[] candidates = new String[]{
				"get" + capitalized,
				"is" + capitalized
			};
			for (String methodName : candidates) {
				try {
					Method m = clazz.getMethod(methodName);
					return m.invoke(obj);
				} catch (NoSuchMethodException ignored) {
				}
			}

			throw new NoSuchFieldException("No field or getter for property: " + property);
		}
	}

	public static <T> Page<T> getPage(List<T> allData, Pageable pageable) {
		// Apply sorting
		List<T> sortedData = allData;
		if (pageable.getSort().isSorted()) {
			sortedData = allData.stream()
				.sorted(new SortComparator<T>(pageable.getSort()))
				.collect(Collectors.toList());
		}

		// Apply paging
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), sortedData.size());

		List<T> pageContent = sortedData.subList(start, end);

		return new PageImpl<>(pageContent, pageable, sortedData.size());
	}

}
