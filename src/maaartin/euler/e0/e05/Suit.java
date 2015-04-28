package maaartin.euler.e0.e05;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter enum Suit {
	SPADES('S'),
	HEARTS('H'),
	CLUBS('C'),
	DIAMONDS('D'),
	;

	static Suit forSymbol(char symbol) {
		return checkNotNull(FOR_SYMBOL[symbol], symbol);
	}

	private static final Suit[] FOR_SYMBOL = new Suit[128]; // ASCII sized
	static {
		for (final Suit s : Suit.values()) FOR_SYMBOL[s.symbol] = s;
	}

	private final char symbol;
}