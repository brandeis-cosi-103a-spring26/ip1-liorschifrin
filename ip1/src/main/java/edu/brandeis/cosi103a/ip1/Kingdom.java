package edu.brandeis.cosi103a.ip1;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the Kingdom (supply) of cards available for purchase/acquisition.
 * Tracks card templates and their remaining quantities.
 */
public class Kingdom {
    
    private final Map<String, KingdomPile> piles;
    
    public Kingdom() {
        this.piles = new HashMap<>();
    }
    
    /**
     * Adds a pile of cards to the kingdom.
     * @param template The card template
     * @param quantity How many cards are available
     */
    public void addPile(CardTemplate template, int quantity) {
        piles.put(template.getName(), new KingdomPile(template, quantity));
    }
    
    /**
     * Attempts to take a card from the kingdom.
     * @param cardName The name of the card to take
     * @return The card if available, null if pile is empty or doesn't exist
     */
    public Card takeCard(String cardName) {
        KingdomPile pile = piles.get(cardName);
        if (pile != null && pile.hasCards()) {
            pile.decrementQuantity();
            return pile.getTemplate().createCard();
        }
        return null;
    }
    
    /**
     * Checks if a card is available in the kingdom.
     * @param cardName The name of the card
     * @return true if at least one card is available
     */
    public boolean isAvailable(String cardName) {
        KingdomPile pile = piles.get(cardName);
        return pile != null && pile.hasCards();
    }
    
    /**
     * Gets the remaining quantity of a card.
     * @param cardName The name of the card
     * @return The remaining quantity, or 0 if not found
     */
    public int getRemainingQuantity(String cardName) {
        KingdomPile pile = piles.get(cardName);
        return pile != null ? pile.getRemainingQuantity() : 0;
    }
    
    /**
     * Gets all card names in the kingdom.
     */
    public List<String> getAvailableCardNames() {
        return new ArrayList<>(piles.keySet());
    }
    
    /**
     * Gets all piles (for display purposes).
     */
    public Map<String, KingdomPile> getAllPiles() {
        return new HashMap<>(piles);
    }
    
    /**
     * Checks if the game should end.
     * Game ends when Framework pile is empty OR 3+ piles are empty.
     */
    public boolean isGameOver() {
        // Check if Framework pile is empty
        KingdomPile frameworkPile = piles.get("Framework");
        if (frameworkPile != null && !frameworkPile.hasCards()) {
            return true;
        }
        
        // Check if 3 or more piles are empty
        int emptyPiles = 0;
        for (KingdomPile pile : piles.values()) {
            if (!pile.hasCards()) {
                emptyPiles++;
            }
        }
        return emptyPiles >= 3;
    }
    
    /**
     * Gets a formatted string showing all piles and quantities.
     */
    public String displayKingdom() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== KINGDOM ===\n");
        for (Map.Entry<String, KingdomPile> entry : piles.entrySet()) {
            KingdomPile pile = entry.getValue();
            CardTemplate template = pile.getTemplate();
            sb.append(String.format("%s (Cost: %d, Value: %d) - %d remaining\n",
                template.getName(),
                template.getCost(),
                template.getValue(),
                pile.getRemainingQuantity()));
        }
        return sb.toString();
    }
    
    /**
     * Inner class representing a pile of cards in the kingdom.
     */
    public static class KingdomPile {
        private final CardTemplate template;
        private int remainingQuantity;
        
        public KingdomPile(CardTemplate template, int quantity) {
            this.template = template;
            this.remainingQuantity = quantity;
        }
        
        public CardTemplate getTemplate() {
            return template;
        }
        
        public int getRemainingQuantity() {
            return remainingQuantity;
        }
        
        public boolean hasCards() {
            return remainingQuantity > 0;
        }
        
        public void decrementQuantity() {
            if (remainingQuantity > 0) {
                remainingQuantity--;
            }
        }
    }
    
    /**
     * Factory method: Creates a standard kingdom with all card types.
     */
    public static Kingdom createStandardKingdom() {
        Kingdom kingdom = new Kingdom();
        
        for (CardTemplate template : CardRegistry.getAllTemplates()) {
            kingdom.addPile(template, template.getQuantity());
        }
        
        return kingdom;
    }
    
    /**
     * Factory method: Creates a custom kingdom with specific cards.
     */
    public static Kingdom createCustomKingdom(String[] cardNames, int[] quantities) {
        Kingdom kingdom = new Kingdom();
        
        for (int i = 0; i < cardNames.length && i < quantities.length; i++) {
            CardTemplate template = CardRegistry.getTemplate(cardNames[i]);
            if (template != null) {
                kingdom.addPile(template, quantities[i]);
            }
        }
        
        return kingdom;
    }
}