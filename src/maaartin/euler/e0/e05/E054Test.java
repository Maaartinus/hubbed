package maaartin.euler.e0.e05;

import static maaartin.euler.e0.e05.Rank.EIGHT;
import static maaartin.euler.e0.e05.Rank.NINE;
import static maaartin.euler.e0.e05.Rank.TEN;
import static maaartin.euler.e0.e05.Suit.CLUBS;
import static maaartin.euler.e0.e05.Suit.DIAMONDS;
import static maaartin.euler.e0.e05.Suit.SPADES;

import com.google.common.collect.ImmutableList;

import junit.framework.TestCase;

public class E054Test extends TestCase {
	public void testHandForString() {
		final ImmutableList<Card> cards =
				ImmutableList.of(TEN.of(SPADES), TEN.of(DIAMONDS), NINE.of(SPADES), EIGHT.of(SPADES), EIGHT.of(CLUBS));
		final Hand hand = new Hand(cards);
		assertEquals(hand, Hand.forString("TS TD 9S 8S 8C"));
	}

	public void testHandValue() {
		checkHandValue("2S JD 9H 4C QC", HandValue.HIGH_CARD);
		checkHandValue("2C 2D 2H 3C 3D", HandValue.FULL_HOUSE);
		checkHandValue("2C 3D 4H 5C 6D", HandValue.STRAIGHT);
		checkHandValue("2C 2D 4H 5C 6D", HandValue.PAIR);
		checkHandValue("2C 3C 4C 5C 6C", HandValue.STRAIGHT_FLUSH);
	}

	public void testThreeOfAKind() {
		final Hand h = Hand.forString("2S 2D 2H 4C QC");
		assertFalse(HandValue.TWO_PAIRS.apply(h));
		assertTrue(HandValue.THREE_OF_A_KIND.apply(h));
		assertSame(HandValue.THREE_OF_A_KIND, h.toHandValue());
	}

	public void testTwoPairs() {
		final Hand h = Hand.forString("TS TD 9S 8S 8C");
		assertTrue(HandValue.TWO_PAIRS.apply(h));
		assertFalse(HandValue.THREE_OF_A_KIND.apply(h));
		assertSame(HandValue.TWO_PAIRS, h.toHandValue());
	}

	public void testcompareTo() {
		checkCompareFromLine("5H 5C 6S 7S KD 2C 3S 8S 8D TD", -1);
		checkCompareFromLine("5D 8C 9S JS AC 2C 5C 7D 8S QH", +1);
		checkCompareFromLine("2D 9C AS AH AC 3D 6D 7D TD QD", -1);
		checkCompareFromLine("4D 6S 9H QH QC 3D 6D 7H QD QS", +1);
		// Commented out as neither implemented nor needed for the problem 54 itself.
		// checkCompareFromLine("2H 2D 4C 4D 4S 3C 3D 3S 9S 9D", +1);
	}

	private void checkCompareFromLine(String line, int expected) {
		assertEquals(expected, Integer.signum(HandPair.forString(line).compare()));
	}

	private void checkHandValue(String cards, HandValue expected) {
		assertEquals(expected, toHandValue(cards));
	}

	private HandValue toHandValue(String s) {
		return Hand.forString(s).toHandValue();
	}
}
