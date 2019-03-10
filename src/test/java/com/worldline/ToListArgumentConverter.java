package com.worldline;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.params.converter.SimpleArgumentConverter;

/**
 * Converts a csv entry into a list of long integers.
 * @author mohsen
 *
 */
public class ToListArgumentConverter extends SimpleArgumentConverter {

	@Override
	protected Object convert(Object source, Class<?> targetType) {
		Scanner scanner = new Scanner(((String) source).replaceAll("\\[|\\]", ""));
		List<Long> list = new ArrayList<Long>();
		while (scanner.hasNextLong()) {
		    list.add(scanner.nextLong());
		}
		scanner.close();
		return list;
	}

}