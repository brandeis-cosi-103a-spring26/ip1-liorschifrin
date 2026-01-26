package edu.brandeis.cosi103a.ip1;

/**
 * Template for creating multiple instances of the same card type.
 * Makes it easy to define and expand card types in the game.
 */
public class CardTemplate {
    private final CardType type;
    private final String name;
    private final int cost;
    private final int value;
    private final int quantity;
    
    public CardTemplate(CardType type, String name, int cost, int value, int quantity) {
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.value = value;
        this.quantity = quantity;
    }
    
    /**
     * Creates a single Card instance from this template.
     */
    public Card createCard() {
        return new Card(type, name, cost, value);
    }
    
    /**
     * Creates multiple Card instances from this template.
     */
    public void addCardsToList(java.util.List<Card> list) {
        for (int i = 0; i < quantity; i++) {
            list.add(createCard());
        }
    }
    
    // Getters
    public CardType getType() { return type; }
    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getValue() { return value; }
    public int getQuantity() { return quantity; }
}