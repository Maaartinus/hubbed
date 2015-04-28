package maaartin.euler.e0.e05;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;



import maaartin.euler.EulerUtils;

@SuppressWarnings("boxing")
public class E054 {
	public static void main(String[] args) {
		final List<String> lines = EulerUtils.readLines(E054.class);
		checkArgument(lines.size() == 1000);
		int win = 0;
		for (final String s : lines) {
			final HandPair handPair = HandPair.forString(s);
			final int cmp = handPair.compare();

			if (cmp > 0) {
				++win;
			} else if (cmp == 0) {
				throw new RuntimeException("No two hands are equally strong in the given input! " + s);
			}
		}
		System.out.println(win);
	}
}
