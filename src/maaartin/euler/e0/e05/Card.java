package maaartin.euler.e0.e05;

import static com.google.common.base.Preconditions.checkArgument;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter @EqualsAndHashCode class Card implements Comparable<Card> {
	@Override public int compareTo(Card o) {
		int result = rank.compareTo(o.rank);
		if (result==0) result = suit.compareTo(o.suit);
		return result;
	}

	static Card forString(String s) {
		checkArgument(s.length() == 2);
		return new Card(Rank.forSymbol(s.charAt(0)), Suit.forSymbol(s.charAt(1)));
	}

	@Override public String toString() {
		return rank.symbol() + "" + suit.symbol();
	}

	private final Rank rank;
	private final Suit suit;
}