package maaartin.euler.e0.e05;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.EnumSet;
import java.util.List;

import lombok.RequiredArgsConstructor;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


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