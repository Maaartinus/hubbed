package maaartin.euler;

import java.io.File;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class EulerUtils {
	public static List<List<Integer>> readNumbers(Class<?> clazz) {
		final List<List<Integer>> result = Lists.newArrayList();
		for (final String line : readLines(clazz)) {
			final List<Integer> l = Lists.newArrayList();
			for (final String s : Splitter.onPattern("[^-\\d]+").omitEmptyStrings().split(line)) l.add(Integer.valueOf(s));
			result.add(l);
		}
		return result;
	}

	public static List<String> readLines(Class<?> clazz) {
		final String name = clazz.getSimpleName() + ".txt";
		try {
			return Files.readLines(new File(clazz.getResource(name).toURI()), Charsets.UTF_8);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
