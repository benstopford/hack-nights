package poker;

import java.util.*;

import static poker.PokerScoring.Card.*;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.shuffle;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PokerScoring {

    public static void main(String[] args) {
        new PokerScoring();
    }


    public PokerScoring() {
        List pack = shuffledPack();

        assertThat(pack.size(), is(52));

        Hand randomHand = deal(7, pack);
        assertThat(randomHand.size(), is(7));
        assertThat(pack.size(), is(52-7));

        assertTrue(Judge.isFullHouse(new Hand(spade(3), diamond(3), club(3), heart(4), heart(4))));
        assertTrue(Judge.isFlush(new Hand(spade(3), diamond(3), club(4), spade(5), diamond(6), diamond(7), diamond(88))));

    }

    static class Card implements Comparable {
        String suit;
        Integer face;

        Card(String suit, int face) {
            this.suit = suit;
            this.face = face;
        }

        static Card spade(int i) {
            return new Card("Spade", i);
        }

        public static Card diamond(int i) {
            return new Card("Diamond", i);
        }

        public static Card heart(int i) {
            return new Card("Heart", i);
        }

        public static Card club(int i) {
            return new Card("Club", i);
        }

        public static Comparator<Card> suitComparator() {
            return new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    Card c1 = (Card) o1;
                    Card c2 = (Card) o2;
                    return c1.suit.compareTo(c2.suit);
                }
            };
        }

        public static Comparator<Card> numberComparator() {
            return new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    Card c1 = (Card) o1;
                    Card c2 = (Card) o2;
                    return c1.face.compareTo(c2.face);
                }
            };
        }

        static class Hand<T> extends ArrayList {
            Set<String> bySuit = new TreeSet<String>();
            Set<Integer> byNumber = new TreeSet<Integer>();

            Hand(Card... cards) {
                build(cards);
            }

            Hand(List<Card> cards) {
                build(cards.toArray(new Card[]{}));
            }

            private void build(Card[] cards) {
                for (Card c : cards) {
                    bySuit.add(c.suit);
                    byNumber.add(c.face);
                    add(c);
                }
            }

        }

        @Override
        public int compareTo(Object o) {
            Card c1 = this;
            Card c2 = (Card) o;

            int numComp = numberComparator().compare(c1, c2);
            if (numComp == 0) {
                return suitComparator().compare(c1, c2);
            } else {
                return numComp;
            }
        }

        @Override
        public String toString() {
            return "{" + suit + ", " + face + '}';
        }
    }

    static List<Card> shuffledPack() {
        ArrayList<Card> pack = newArrayList();
        createPack(pack);
        shuffle(pack);
        return pack;
    }

    static void createPack(List pack) {
        for (int cardNumber = 0; cardNumber < 13; cardNumber++) {
            pack.add(new Card("Spades", cardNumber));
            pack.add(new Card("Hearts", cardNumber));
            pack.add(new Card("Diamonds", cardNumber));
            pack.add(new Card("Clubs", cardNumber));
        }
    }

    static Hand<Card> deal(int numberToDeal, List pack) {
        Hand<Card> hand = new Hand(pack.subList(0, numberToDeal));
        pack.removeAll(hand);
        return hand;
    }


    static class Judge {
        static boolean isFullHouse(Hand hand) {
            return hand.byNumber.size()==2;
        }
        static boolean isFlush(Hand hand) {
            List<Integer> byNumber = new ArrayList(hand.byNumber);
            Collections.sort(byNumber);
            int last = -1;
            int straight = 0;
            for(Integer n: byNumber){
                if(n-1==last){
                    straight++;
                }
                last = n;
            }
            return (straight>=4);

        }
    }
}
