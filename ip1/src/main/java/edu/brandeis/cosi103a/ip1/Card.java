package edu.brandeis.cosi103a.ip1;

/**
 * Represents a card in the game with a type, cost, and value.
 */
public class Card {
    private final CardType type;
    private final int cost;
    private final int value;
    private final String name;

    /**
     * Constructs a new Card.
     * 
     * @param type the type of card (AUTOMATION or CRYPTOCURRENCY)
     * @param cost the cost to play this card
     * @param value the value this card provides
     */
    public Card(CardType type, String name, int cost, int value) {
        this.type = type;
        this.cost = cost;
        this.value = value;
        this.name = name;
    }

    /**
     * Gets the type of this card.
     * 
     * @return the CardType
     */
    public CardType getType() {
        return type;
    }

    /**
     * Gets the name of this card.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the cost of this card.
     * 
     * @return the cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the value of this card.
     * 
     * @return the value
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Card[name=%s, type=%s, cost=%d, value=%d]", name, type, cost, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return cost == card.cost && value == card.value && type == card.type && name.equals(card.name);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + cost;
        result = 31 * result + value;
        return result;
    }
}
