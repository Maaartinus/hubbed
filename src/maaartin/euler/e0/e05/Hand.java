package maaartin.euler.e0.e05;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.EnumMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;

@Getter @EqualsAndHashCode(of="cards") class Hand implements Comparable<Hand> {
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