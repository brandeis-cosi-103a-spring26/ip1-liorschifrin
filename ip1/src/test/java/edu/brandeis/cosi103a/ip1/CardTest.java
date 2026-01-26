package edu.brandeis.cosi103a.ip1;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Unit tests for the Card class.
 */
public class CardTest {
    
    private Card bitcoinCard;
    private Card methodCard;
    private Card frameworkCard;
    
    @Before
    public void setUp() {
        bitcoinCard = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        methodCard = new Card(CardType.AUTOMATION, "Method", 2, 1);
        frameworkCard = new Card(CardType.AUTOMATION, "Framework", 8, 6);
    }
    
    @Test
    public void testConstructor() {
        Card card = new Card(CardType.CRYPTOCURRENCY, "Test", 5, 3);
        assertNotNull(card);
    }
    
    @Test
    public void testGetType() {
        assertEquals(CardType.CRYPTOCURRENCY, bitcoinCard.getType());
        assertEquals(CardType.AUTOMATION, methodCard.getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("Bitcoin", bitcoinCard.getName());
        assertEquals("Method", methodCard.getName());
        assertEquals("Framework", frameworkCard.getName());
    }
    
    @Test
    public void testGetCost() {
        assertEquals(0, bitcoinCard.getCost());
        assertEquals(2, methodCard.getCost());
        assertEquals(8, frameworkCard.getCost());
    }
    
    @Test
    public void testGetValue() {
        assertEquals(1, bitcoinCard.getValue());
        assertEquals(1, methodCard.getValue());
        assertEquals(6, frameworkCard.getValue());
    }
    
    @Test
    public void testEquals_SameObject() {
        assertTrue(bitcoinCard.equals(bitcoinCard));
    }
    
    @Test
    public void testEquals_EqualCards() {
        Card bitcoin2 = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        assertTrue(bitcoinCard.equals(bitcoin2));
        assertTrue(bitcoin2.equals(bitcoinCard));
    }
    
    @Test
    public void testEquals_DifferentType() {
        assertFalse(bitcoinCard.equals(methodCard));
    }
    
    @Test
    public void testEquals_DifferentCost() {
        Card card1 = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        Card card2 = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 1, 1);
        assertFalse(card1.equals(card2));
    }
    
    @Test
    public void testEquals_DifferentValue() {
        Card card1 = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        Card card2 = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 2);
        assertFalse(card1.equals(card2));
    }
    
    @Test
    public void testEquals_DifferentName() {
        Card card1 = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        Card card2 = new Card(CardType.CRYPTOCURRENCY, "Ethereum", 0, 1);
        assertFalse(card1.equals(card2));
    }
    
    @Test
    public void testEquals_Null() {
        assertFalse(bitcoinCard.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        assertFalse(bitcoinCard.equals("Bitcoin"));
    }
    
    @Test
    public void testHashCode_EqualCards() {
        Card bitcoin2 = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        assertEquals(bitcoinCard.hashCode(), bitcoin2.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentCards() {
        assertNotEquals(bitcoinCard.hashCode(), methodCard.hashCode());
    }
    
    @Test
    public void testToString() {
        String result = bitcoinCard.toString();
        assertTrue(result.contains("Bitcoin"));
        assertTrue(result.contains("CRYPTOCURRENCY"));
        assertTrue(result.contains("0"));
        assertTrue(result.contains("1"));
    }
}
