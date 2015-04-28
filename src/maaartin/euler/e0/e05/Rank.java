package maaartin.euler.e0.e05;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor @Getter enum Rank {
	TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9),
	TEN(10, 'T'), JACK(11, 'J'), QUEEN(12, 'Q'), KING(13, 'K'), ACE(14, 'A');

	private Rank(int value) {
		this(value, (char) ('0' + value));
		checkArgument(value < 10);
	}

	static Rank forSymbol(char symbol) {
		return checkNotNull(FOR_SYMBOL[symbol], symbol);
	}

	Card of(Suit suit) {
		return new Card(this, suit);
	}

	private static final Rank[] FOR_SYMBOL = new Rank[128]; // ASCII sized
	static {
		for (final Rank r : Rank.values()) FOR_SYMBOL[r.symbol] = r;
	}

	private final int value;
	private final char symbol;
}