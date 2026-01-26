package edu.brandeis.cosi103a.ip1;

import java.util.ArrayList;

/**
 * Represents a player in the game with a deck, hand, and played cards.
 */
public class Player {
    private PlayerDeck deck;
    private ArrayList<Card> hand;
    private ArrayList<Card> playedCards;
    private String name;
    private int coins;
    private int buys;

    /**
     * Constructs a new Player with an empty deck.
     * 
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;
        this.deck = new PlayerDeck();
        this.hand = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.coins = 0;
        this.buys = 0;
    }

    /**
     * Constructs a new Player with a starter deck.
     * 
     * @param name the player's name
     * @param starterDeck the initial deck for the player
     */
    public Player(String name, PlayerDeck starterDeck) {
        this.name = name;
        this.deck = starterDeck;
        this.hand = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.coins = 0;
        this.buys = 0;
    }

    /**
     * Gets the player's name.
     * 
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's deck.
     * 
     * @return the PlayerDeck
     */
    public PlayerDeck getDeck() {
        return deck;
    }

    /**
     * Gets the player's hand.
     * 
     * @return the hand as an ArrayList of Cards
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Gets the player's played cards.
     * 
     * @return the played cards as an ArrayList of Cards
     */
    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }
    
    /**
     * Gets the player's current coins.
     * 
     * @return the number of coins
     */
    public int getCoins() {
        return coins;
    }
    
    /**
     * Gets the player's remaining buys.
     * 
     * @return the number of buys remaining
     */
    public int getBuys() {
        return buys;
    }

    /**
     * Draws one card from the deck into the hand.
     * 
     * @return true if a card was drawn, false if the deck was empty
     */
    public boolean drawCard() {
        Card card = deck.drawOne();
        if (card != null) {
            hand.add(card);
            return true;
        }
        return false;
    }

    /**
     * Draws multiple cards from the deck into the hand.
     * 
     * @param n the number of cards to draw
     * @return the actual number of cards drawn
     */
    public int drawCards(int n) {
        int drawn = 0;
        for (int i = 0; i < n; i++) {
            if (drawCard()) {
                drawn++;
            } else {
                break;
            }
        }
        return drawn;
    }

    /**
     * Plays a card from the hand.
     * 
     * @param index the index of the card in the hand to play
     * @return the played Card, or null if the index is invalid
     */
    public Card playCard(int index) {
        if (index >= 0 && index < hand.size()) {
            Card card = hand.remove(index);
            playedCards.add(card);
            return card;
        }
        return null;
    }

    /**
     * Plays a specific card from the hand.
     * 
     * @param card the card to play
     * @return true if the card was found and played, false otherwise
     */
    public boolean playCard(Card card) {
        if (hand.remove(card)) {
            playedCards.add(card);
            return true;
        }
        return false;
    }

    /**
     * Discards a card from the hand to the deck's discard pile.
     * 
     * @param index the index of the card in the hand to discard
     * @return the discarded Card, or null if the index is invalid
     */
    public Card discardFromHand(int index) {
        if (index >= 0 && index < hand.size()) {
            Card card = hand.remove(index);
            deck.discard(card);
            return card;
        }
        return null;
    }

    /**
     * Starts a new turn by resetting coins and buys.
     */
    public void startTurn() {
        this.coins = 0;
        this.buys = 1;
    }
    
    /**
     * Adds coins to the player's current total.
     * 
     * @param amount the number of coins to add
     */
    public void addCoins(int amount) {
        this.coins += amount;
    }
    
    /**
     * Plays all cryptocurrency cards from hand to generate coins.
     * Each crypto card adds its value as coins.
     * 
     * @return the total coins generated
     */
    public int playAllCryptoCards() {
        int coinsGenerated = 0;
        ArrayList<Card> cryptoCards = new ArrayList<>();
        
        // Find all crypto cards in hand
        for (Card card : hand) {
            if (card.getType() == CardType.CRYPTOCURRENCY) {
                cryptoCards.add(card);
            }
        }
        
        // Play each crypto card
        for (Card card : cryptoCards) {
            hand.remove(card);
            playedCards.add(card);
            coinsGenerated += card.getValue();
        }
        
        this.coins += coinsGenerated;
        return coinsGenerated;
    }
    
    /**
     * Attempts to buy a card from the kingdom.
     * 
     * @param kingdom the kingdom to buy from
     * @param cardName the name of the card to buy
     * @return true if purchase was successful, false otherwise
     */
    public boolean buyCard(Kingdom kingdom, String cardName) {
        // Check if we have buys remaining
        if (buys <= 0) {
            return false;
        }
        
        // Check if card is available
        if (!kingdom.isAvailable(cardName)) {
            return false;
        }
        
        // Get the template to check cost
        CardTemplate template = CardRegistry.getTemplate(cardName);
        if (template == null) {
            return false;
        }
        
        // Check if we can afford it
        if (coins < template.getCost()) {
            return false;
        }
        
        // Take the card from kingdom
        Card card = kingdom.takeCard(cardName);
        if (card != null) {
            // Add to discard pile
            deck.discard(card);
            // Deduct cost and buy
            coins -= template.getCost();
            buys--;
            return true;
        }
        
        return false;
    }
    
    /**
     * Calculates the player's total score from all cards in their deck.
     * Score is the sum of all card values across draw pile, discard pile, hand, and played cards.
     * 
     * @return the total score
     */
    public int calculateScore() {
        int score = 0;
        
        // Count cards in hand
        for (Card card : hand) {
            score += card.getValue();
        }
        
        // Count played cards
        for (Card card : playedCards) {
            score += card.getValue();
        }
        
        // To count deck cards, we need to temporarily collect them
        // This is a bit tricky since they're in draw and discard piles
        // We'll need to add helper methods to PlayerDeck
        score += deck.calculateTotalValue();
        
        return score;
    }
    
    /**
     * Ends the turn by moving all hand and played cards to the discard pile.
     */
    public void endTurn() {
        // Discard all cards from hand
        for (Card card : hand) {
            deck.discard(card);
        }
        hand.clear();
        
        // Discard all played cards
        for (Card card : playedCards) {
            deck.discard(card);
        }
        playedCards.clear();
    }

    /**
     * Gets the size of the player's hand.
     * 
     * @return the number of cards in hand
     */
    public int handSize() {
        return hand.size();
    }

    // /**
    //  * Calculates the total value of cards in the hand.
    //  * 
    //  * @return the sum of all card values in hand
    //  */
    // public int calculateHandValue() {
    //     int total = 0;
    //     for (Card card : hand) {
    //         total += card.getValue();
    //     }
    //     return total;
    // }

    // /**
    //  * Calculates the total cost of cards in the hand.
    //  * 
    //  * @return the sum of all card costs in hand
    //  */
    // public int calculateHandCost() {
    //     int total = 0;
    //     for (Card card : hand) {
    //         total += card.getCost();
    //     }
    //     return total;
    // }

    @Override
    public String toString() {
        return String.format("Player[name=%s, hand=%d cards, played=%d cards, deck=%s]",
                           name, hand.size(), playedCards.size(), deck.toString());
    }
}
