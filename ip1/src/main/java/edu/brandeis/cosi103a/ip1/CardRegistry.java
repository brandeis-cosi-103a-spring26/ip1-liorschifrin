package edu.brandeis.cosi103a.ip1;

import java.util.ArrayList;
import java.util.List;

/**
 * Central registry for all card types in the game.
 * Easy to add new cards - just add a new template to the list.
 */
public class CardRegistry {
    
    private static final List<CardTemplate> ALL_TEMPLATES = new ArrayList<>();
    
    static {
        // Automation cards
        ALL_TEMPLATES.add(new CardTemplate(CardType.AUTOMATION, "Method", 2, 1, 14));
        ALL_TEMPLATES.add(new CardTemplate(CardType.AUTOMATION, "Module", 5, 3, 8));
        ALL_TEMPLATES.add(new CardTemplate(CardType.AUTOMATION, "Framework", 8, 6, 8));
        
        // Cryptocurrency cards
        ALL_TEMPLATES.add(new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60));
        ALL_TEMPLATES.add(new CardTemplate(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2, 40));
        ALL_TEMPLATES.add(new CardTemplate(CardType.CRYPTOCURRENCY, "Dogecoin", 6, 3, 30));
    }
    
    /**
     * Get a specific card template by name.
     */
    public static CardTemplate getTemplate(String name) {
        return ALL_TEMPLATES.stream()
            .filter(t -> t.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Get all templates of a specific type.
     */
    public static List<CardTemplate> getTemplatesByType(CardType type) {
        List<CardTemplate> result = new ArrayList<>();
        for (CardTemplate template : ALL_TEMPLATES) {
            if (template.getType() == type) {
                result.add(template);
            }
        }
        return result;
    }
    
    /**
     * Get all card templates.
     */
    public static List<CardTemplate> getAllTemplates() {
        return new ArrayList<>(ALL_TEMPLATES);
    }
    
    /**
     * Creates a full game deck with all cards.
     */
    public static List<Card> createFullDeck() {
        List<Card> deck = new ArrayList<>();
        for (CardTemplate template : ALL_TEMPLATES) {
            template.addCardsToList(deck);
        }
        return deck;
    }
    
    /**
     * Creates a starter deck (customize as needed).
     */
    public static List<Card> createStarterDeck() {
        List<Card> deck = new ArrayList<>();
        
        CardTemplate bitcoin = getTemplate("Bitcoin");
        CardTemplate method = getTemplate("Method");
        
        if (bitcoin != null) {
            for (int i = 0; i < 7; i++) {
                deck.add(bitcoin.createCard());
            }
        }
        
        if (method != null) {
            for (int i = 0; i < 3; i++) {
                deck.add(method.createCard());
            }
        }
        
        return deck;
    }
    
    /**
     * Create a custom deck from card names and quantities.
     */
    public static List<Card> createCustomDeck(String[][] cardNamesAndQuantities) {
        List<Card> deck = new ArrayList<>();
        
        for (String[] pair : cardNamesAndQuantities) {
            String name = pair[0];
            int quantity = Integer.parseInt(pair[1]);
            CardTemplate template = getTemplate(name);
            
            if (template != null) {
                for (int i = 0; i < quantity; i++) {
                    deck.add(template.createCard());
                }
            }
        }
        
        return deck;
    }
}