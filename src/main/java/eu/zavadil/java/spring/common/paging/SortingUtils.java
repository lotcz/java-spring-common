package eu.zavadil.java.spring.common.paging;

import eu.zavadil.java.util.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SortingUtils {

	public static Sort.Order fieldFromString(String str) {
		String[] arr = str.split("-");
		String name = arr[0];
		String direction = arr.length > 1 && StringUtils.safeEqualsIgnoreCase(arr[1], "desc") ? arr[1] : "asc";
		Sort.NullHandling nh = Sort.NullHandling.NATIVE;
		if (arr.length > 2 && StringUtils.safeEquals(arr[2], "nl")) nh = Sort.NullHandling.NULLS_LAST;
		return new Sort.Order(Sort.Direction.fromString(direction), name, nh);
	}

	public static Sort fromString(String str) {
		if (str == null || str.isBlank()) {
			return Sort.unsorted();
		}
		return Sort.by(Arrays.stream(str.split("\\+")).map(SortingUtils::fieldFromString).toList());
	}

	public static String fieldToString(Sort.Order field) {
		List<String> parts = new ArrayList<>();
		parts.add(field.getProperty());
		if (field.isDescending()) {
			parts.add("desc");
		}
		if (field.getNullHandling().equals(Sort.NullHandling.NULLS_LAST)) {
			parts.add("nl");
		}
		return String.join("-", parts);
	}

	public static String toString(Sort sort) {
		if (sort.isUnsorted()) {
			return "";
		}
		return sort.get().map(SortingUtils::fieldToString).collect(Collectors.joining("+"));
	}

	public static String fieldToSqlString(Sort.Order field) {
		List<String> parts = new ArrayList<>();
		parts.add(field.getProperty());
		if (field.isDescending()) {
			parts.add("DESC");
		}
		return String.join(" ", parts);
	}

	public static String toSqlString(Sort sort) {
		if (sort.isUnsorted()) {
			return "";
		}
		return sort.get().map(SortingUtils::fieldToSqlString).collect(Collectors.joining(", "));
	}
}
