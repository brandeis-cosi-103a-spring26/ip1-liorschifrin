package edu.brandeis.cosi103a.ip1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Represents a player's deck with a draw pile and discard pile.
 */
public class PlayerDeck {
    private ArrayList<Card> drawPile;
    private ArrayList<Card> discardPile;
    private Random random;

    /**
     * Constructs an empty PlayerDeck.
     */
    public PlayerDeck() {
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Constructs a PlayerDeck with a starter deck of cards.
     * 
     * @param starterCards the initial cards to add to the draw pile
     */
    public PlayerDeck(ArrayList<Card> starterCards) {
        this.drawPile = new ArrayList<>(starterCards);
        this.discardPile = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Sets the Random instance used for shuffling.
     * 
     * @param random the Random instance to use
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Shuffles the draw pile.
     */
    public void shuffle() {
        Collections.shuffle(drawPile, random);
    }

    /**
     * Draws one card from the draw pile.
     * If the draw pile is empty, reshuffles the discard pile into it first.
     * 
     * @return the drawn Card, or null if both piles are empty
     */
    public Card drawOne() {
        if (drawPile.isEmpty()) {
            reshuffleDiscardIntoDraw();
        }
        
        if (drawPile.isEmpty()) {
            return null;
        }
        
        return drawPile.remove(drawPile.size() - 1);
    }

    /**
     * Discards a card to the discard pile.
     * 
     * @param card the card to discard
     */
    public void discard(Card card) {
        if (card != null) {
            discardPile.add(card);
        }
    }

    /**
     * Checks if the draw pile is empty.
     * 
     * @return true if the draw pile is empty, false otherwise
     */
    public boolean isEmpty() {
        return drawPile.isEmpty();
    }

    /**
     * Gets the size of the draw pile.
     * 
     * @return the number of cards in the draw pile
     */
    public int size() {
        return drawPile.size();
    }

    /**
     * Gets the size of the discard pile.
     * 
     * @return the number of cards in the discard pile
     */
    public int discardSize() {
        return discardPile.size();
    }

    // /**
    //  * Peeks at the top card of the draw pile without removing it.
    //  * 
    //  * @return the top Card, or null if the draw pile is empty
    //  */
    // public Card peek() {
    //     if (drawPile.isEmpty()) {
    //         return null;
    //     }
    //     return drawPile.get(drawPile.size() - 1);
    // }

    /**
     * Reshuffles the discard pile into the draw pile.
     * The discard pile becomes empty after this operation.
     */
    public void reshuffleDiscardIntoDraw() {
        if (!discardPile.isEmpty()) {
            drawPile.addAll(discardPile);
            discardPile.clear();
            shuffle();
        }
    }

    /**
     * Adds a card to the draw pile.
     * 
     * @param card the card to add
     */
    public void addToDrawPile(Card card) {
        if (card != null) {
            drawPile.add(card);
        }
    }

    /**
     * Gets the total number of cards in both piles.
     * 
     * @return the total card count
     */
    public int totalCards() {
        return drawPile.size() + discardPile.size();
    }
    
    /**
     * Calculates the total value of all cards in both piles.
     * 
     * @return the sum of all card values
     */
    public int calculateTotalValue() {
        int total = 0;
        
        for (Card card : drawPile) {
            total += card.getValue();
        }
        
        for (Card card : discardPile) {
            total += card.getValue();
        }
        
        return total;
    }

    @Override
    public String toString() {
        return String.format("PlayerDeck[drawPile=%d cards, discardPile=%d cards]", 
                           drawPile.size(), discardPile.size());
    }
}
