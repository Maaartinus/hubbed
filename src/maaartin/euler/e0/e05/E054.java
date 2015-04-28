package maaartin.euler.e0.e05;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EnumSet;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.EnumMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;

import maaartin.euler.EulerUtils;

@SuppressWarnings("boxing")
public class E054 {
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

	@RequiredArgsConstructor @Getter @EqualsAndHashCode
	static class Card implements Comparable<Card> {
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

	@RequiredArgsConstructor enum HandValue implements Predicate<Hand> {
		HIGH_CARD {
			@Override public boolean apply(Hand input) {
				return input.rankHistogram().equals(ImmutableMultiset.of(1, 1, 1, 1, 1));
			}

			@Override List<Card> reorderedCards(Hand hand) {
				// Order the biggest cards last, which is trivial as hand.cards() are already sorted.
				return ImmutableList.copyOf(hand.cards());
			}
		},
		PAIR {
			@Override public boolean apply(Hand input) {
				return input.rankHistogram().equals(ImmutableMultiset.of(1, 1, 1, 2));
			}

			@Override List<Card> reorderedCards(Hand hand) {
				final List<Card> result = Lists.newArrayList();
				final Rank rank = findRepeatedRank(hand.cards());
				// Insert the unpaired cards first in their original order.
				for (final Card c : hand.cards()) {
					if (!c.rank().equals(rank)) result.add(c);
				}
				// Then insert the cards of the pair.
				for (final Card c : hand.cards()) {
					if (c.rank().equals(rank)) result.add(c);
				}
				assert ImmutableSet.copyOf(result).equals(ImmutableSet.copyOf(hand.cards()));
				return result;
			}

			/** Get the first (and only) rank occuring more then once. */
			private Rank findRepeatedRank(Iterable<Card> cards) {
				final EnumSet<Rank> set = EnumSet.noneOf(Rank.class);
				for (final Card c : cards) {
					if (!set.add(c.rank())) return c.rank();
				}
				throw new RuntimeException("Impossible.");
			}
		},
		TWO_PAIRS {
			@Override public boolean apply(Hand input) {
				return input.rankHistogram().equals(ImmutableMultiset.of(1, 2, 2));
			}
		},
		THREE_OF_A_KIND {
			@Override public boolean apply(Hand input) {
				return input.rankHistogram().equals(ImmutableMultiset.of(1, 1, 3));
			}
		},
		STRAIGHT {
			@Override public boolean apply(Hand input) {
				return input.isStraight();
			}
		},
		FLUSH {
			@Override public boolean apply(Hand input) {
				return input.suitHistogram().equals(ImmutableMultiset.of(5));
			}
		},
		FULL_HOUSE {
			@Override public boolean apply(Hand input) {
				return input.rankHistogram().equals(ImmutableMultiset.of(2, 3));
			}
		},
		FOUR_OF_A_KIND {
			@Override public boolean apply(Hand input) {
				return input.rankHistogram().equals(ImmutableMultiset.of(1, 4));
			}
		},
		STRAIGHT_FLUSH {
			@Override public boolean apply(Hand input) {
				return input.isStraight() && input.suitHistogram().equals(ImmutableMultiset.of(5));
			}
		},
		;

		/**
		 * Return the cards reordered so that the ranked cards come last.
		 * This way a backward lexicographical comparison of the list can be used to determine which hand is stronger.
		 * 
		 * <p>See also this description copied from <a href="https://projecteuler.net/problem=66">Problem 66</a>:
		 * 
		 * <p>If two players have the same ranked hands then the rank made up of the highest value wins;
		 * for example, a pair of eights beats a pair of fives (see example 1 below).
		 * But if two ranks tie, for example, both players have a pair of queens,
		 * then highest cards in each hand are compared (see example 4 below);
		 * if the highest cards tie then the next highest cards are compared, and so on.
		 */
		List<Card> reorderedCards(Hand hand) {
			checkArgument(hand.toHandValue().equals(this));
			// Only the methods needed for the problem 54 were implemented.
			throw new RuntimeException("Not implemented: " + this);
		}
	}

	@Getter @EqualsAndHashCode(of="cards") static class Hand implements Comparable<Hand> {
		Hand(Iterable<Card> cards) {
			assert Iterables.size(cards) == CARDS_IN_HAND;
			this.cards = ImmutableSortedSet.copyOf(cards);
			checkArgument(this.cards.size() == 5, cards);
			final Multiset<Rank> ranks = EnumMultiset.create(Rank.class);
			final Multiset<Suit> suits = EnumMultiset.create(Suit.class);
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			for (final Card c : this.cards) {
				final Rank rank = c.rank();
				min = Math.min(min, rank.value());
				max = Math.max(max, rank.value());
				ranks.add(rank);
				suits.add(c.suit());
			}
			rankHistogram = histogram(ranks);
			suitHistogram = histogram(suits);
			isStraight = max - min == CARDS_IN_HAND - 1 && rankHistogram.equals(ImmutableMultiset.of(1, 1, 1, 1, 1));
		}

		/**
		 * The inverse method to {@link #toString().}
		 * 
		 * <p>The input must be exactly of the form "vs vs vs vs vs",
		 * where {@code v} is a {@code Value.symbol()} and {@code s} is {@code Rank.symbol()},
		 * for example {@code "5H 5C 6S 7S KD"}
		 */
		static Hand forString(String cards) {
			checkArgument(cards.length() == TO_STRING_LENGTH);
			final List<Card> list = Lists.newArrayList();
			for (final String s : Splitter.on(' ').splitToList(cards)) list.add(Card.forString(s));
			return new Hand(list);
		}

		/**
		 * Returns a positive number if {@code this} is stronger than {@code other},
		 * a negative number if it's weaker,
		 * and 0 if they're equally strong.
		 */
		@Override public int compareTo(Hand other) {
			final HandValue myHandValue = toHandValue();
			final HandValue otherHandValue = other.toHandValue();
			int result = myHandValue.compareTo(otherHandValue);
			if (result!=0) return result;

			assert myHandValue == otherHandValue;
			final List<Card> myReorderedCards = myHandValue.reorderedCards(this);
			final List<Card> otherReorderedCards = otherHandValue.reorderedCards(other);
			assert myReorderedCards.size() == otherReorderedCards.size();

			// A backward lexicographical comparison of card ranks.
			for (int i=myReorderedCards.size(); i-->0; ) {
				final Rank myRank = myReorderedCards.get(i).rank();
				final Rank otherRank = otherReorderedCards.get(i).rank();
				result = Integer.compare(myRank.value(), otherRank.value());
				if (result!=0) return result;
			}
			return 0;
		}

		@Override public String toString() {
			return Joiner.on(' ').join(cards);
		}

		private static <E> ImmutableMultiset<Integer> histogram(Multiset<E> multiset) {
			final List<Integer> result = Lists.newArrayList();
			for (final Multiset.Entry<E> e : multiset.entrySet()) result.add(e.getCount());
			return ImmutableMultiset.copyOf(result);
		}

		HandValue toHandValue() {
			for (final HandValue v : HAND_VALUES) {
				if (v.apply(this)) return v;
			}
			throw new RuntimeException("Impossible: " + cards);
		}

		private static final int CARDS_IN_HAND = 5;
		static final int TO_STRING_LENGTH = CARDS_IN_HAND * 3 - 1;

		private static final ImmutableList<HandValue> HAND_VALUES = ImmutableList.copyOf(HandValue.values()).reverse();

		private final ImmutableSortedSet<Card> cards;
		private final ImmutableMultiset<Integer> rankHistogram;
		private final ImmutableMultiset<Integer> suitHistogram;
		private final boolean isStraight;
	}

	@RequiredArgsConstructor @Getter static class HandPair {
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
