package maaartin.euler.e0.e05;

import static com.google.common.base.Preconditions.checkArgument;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter class HandPair {
	/** Accepts one line of <a href="http://projecteuler.net/project/resources/p054_poker.txt">poker.txt</a>. */
	static HandPair forString(String line) {
		checkArgument(line.length() == 2 * Hand.TO_STRING_LENGTH + 1);
		checkArgument(line.charAt(Hand.TO_STRING_LENGTH) == ' ');
		final Hand hand1 = Hand.forString(line.substring(0, Hand.TO_STRING_LENGTH));
		final Hand hand2 = Hand.forString(line.substring(Hand.TO_STRING_LENGTH + 1));
		return new HandPair(hand1, hand2);
	}

	int compare() {
		return hand1.compareTo(hand2);
	}

	private final Hand hand1;
	private final Hand hand2;
}