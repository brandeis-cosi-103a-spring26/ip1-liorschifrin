package edu.brandeis.cosi103a.ip1;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for the Kingdom class.
 */
public class KingdomTest {
    
    private Kingdom kingdom;
    
    @Before
    public void setUp() {
        kingdom = new Kingdom();
    }
    
    @Test
    public void testConstructor() {
        Kingdom k = new Kingdom();
        assertNotNull(k);
    }
    
    @Test
    public void testAddPile() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        kingdom.addPile(template, 60);
        
        assertTrue(kingdom.isAvailable("Bitcoin"));
        assertEquals(60, kingdom.getRemainingQuantity("Bitcoin"));
    }
    
    @Test
    public void testAddPile_CustomQuantity() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        kingdom.addPile(template, 10); // Add only 10 instead of 60
        
        assertEquals(10, kingdom.getRemainingQuantity("Bitcoin"));
    }
    
    @Test
    public void testTakeCard_Available() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        kingdom.addPile(template, 5);
        
        Card card = kingdom.takeCard("Bitcoin");
        
        assertNotNull(card);
        assertEquals("Bitcoin", card.getName());
        assertEquals(4, kingdom.getRemainingQuantity("Bitcoin"));
    }
    
    @Test
    public void testTakeCard_NotAvailable() {
        Card card = kingdom.takeCard("NonExistent");
        assertNull(card);
    }
    
    @Test
    public void testTakeCard_EmptyPile() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 1);
        kingdom.addPile(template, 1);
        
        // Take the only card
        kingdom.takeCard("Bitcoin");
        
        // Try to take another
        Card card = kingdom.takeCard("Bitcoin");
        assertNull(card);
    }
    
    @Test
    public void testTakeCard_MultipleCards() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 10);
        kingdom.addPile(template, 10);
        
        for (int i = 0; i < 10; i++) {
            Card card = kingdom.takeCard("Bitcoin");
            assertNotNull(card);
            assertEquals(9 - i, kingdom.getRemainingQuantity("Bitcoin"));
        }
        
        assertFalse(kingdom.isAvailable("Bitcoin"));
    }
    
    @Test
    public void testIsAvailable_True() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 5);
        kingdom.addPile(template, 5);
        
        assertTrue(kingdom.isAvailable("Bitcoin"));
    }
    
    @Test
    public void testIsAvailable_False_NotAdded() {
        assertFalse(kingdom.isAvailable("Bitcoin"));
    }
    
    @Test
    public void testIsAvailable_False_EmptyPile() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 1);
        kingdom.addPile(template, 1);
        kingdom.takeCard("Bitcoin");
        
        assertFalse(kingdom.isAvailable("Bitcoin"));
    }
    
    @Test
    public void testGetRemainingQuantity() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        kingdom.addPile(template, 60);
        
        assertEquals(60, kingdom.getRemainingQuantity("Bitcoin"));
    }
    
    @Test
    public void testGetRemainingQuantity_NotFound() {
        assertEquals(0, kingdom.getRemainingQuantity("NonExistent"));
    }
    
    @Test
    public void testGetAvailableCardNames() {
        CardTemplate bitcoin = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        CardTemplate ethereum = new CardTemplate(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2, 40);
        
        kingdom.addPile(bitcoin, 60);
        kingdom.addPile(ethereum, 40);
        
        List<String> names = kingdom.getAvailableCardNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("Bitcoin"));
        assertTrue(names.contains("Ethereum"));
    }
    
    @Test
    public void testGetAllPiles() {
        CardTemplate bitcoin = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        kingdom.addPile(bitcoin, 60);
        
        Map<String, Kingdom.KingdomPile> piles = kingdom.getAllPiles();
        assertEquals(1, piles.size());
        assertTrue(piles.containsKey("Bitcoin"));
    }
    
    @Test
    public void testIsGameOver_NoEmptyPiles() {
        CardTemplate bitcoin = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        CardTemplate ethereum = new CardTemplate(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2, 40);
        
        kingdom.addPile(bitcoin, 60);
        kingdom.addPile(ethereum, 40);
        
        assertFalse(kingdom.isGameOver());
    }
    
    @Test
    public void testIsGameOver_FrameworkEmpty() {
        CardTemplate framework = new CardTemplate(CardType.AUTOMATION, "Framework", 8, 6, 1);
        CardTemplate bitcoin = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        
        kingdom.addPile(framework, 1);
        kingdom.addPile(bitcoin, 60);
        
        assertFalse(kingdom.isGameOver());
        
        // Take the last Framework
        kingdom.takeCard("Framework");
        
        assertTrue(kingdom.isGameOver());
    }
    
    @Test
    public void testIsGameOver_ThreeEmptyPiles() {
        CardTemplate template1 = new CardTemplate(CardType.CRYPTOCURRENCY, "Card1", 0, 1, 1);
        CardTemplate template2 = new CardTemplate(CardType.CRYPTOCURRENCY, "Card2", 0, 1, 1);
        CardTemplate template3 = new CardTemplate(CardType.CRYPTOCURRENCY, "Card3", 0, 1, 1);
        CardTemplate template4 = new CardTemplate(CardType.CRYPTOCURRENCY, "Card4", 0, 1, 10);
        
        kingdom.addPile(template1, 1);
        kingdom.addPile(template2, 1);
        kingdom.addPile(template3, 1);
        kingdom.addPile(template4, 10);
        
        assertFalse(kingdom.isGameOver());
        
        // Empty 2 piles - game should not be over
        kingdom.takeCard("Card1");
        kingdom.takeCard("Card2");
        assertFalse(kingdom.isGameOver());
        
        // Empty 3rd pile - game should be over
        kingdom.takeCard("Card3");
        assertTrue(kingdom.isGameOver());
    }
    
    @Test
    public void testDisplayKingdom() {
        CardTemplate bitcoin = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        kingdom.addPile(bitcoin, 60);
        
        String display = kingdom.displayKingdom();
        
        assertTrue(display.contains("KINGDOM"));
        assertTrue(display.contains("Bitcoin"));
        assertTrue(display.contains("60"));
    }
    
    @Test
    public void testCreateStandardKingdom() {
        Kingdom standard = Kingdom.createStandardKingdom();
        
        assertNotNull(standard);
        assertTrue(standard.isAvailable("Bitcoin"));
        assertTrue(standard.isAvailable("Ethereum"));
        assertTrue(standard.isAvailable("Dogecoin"));
        assertTrue(standard.isAvailable("Method"));
        assertTrue(standard.isAvailable("Module"));
        assertTrue(standard.isAvailable("Framework"));
        
        assertEquals(60, standard.getRemainingQuantity("Bitcoin"));
        assertEquals(40, standard.getRemainingQuantity("Ethereum"));
        assertEquals(30, standard.getRemainingQuantity("Dogecoin"));
        assertEquals(14, standard.getRemainingQuantity("Method"));
        assertEquals(8, standard.getRemainingQuantity("Module"));
        assertEquals(8, standard.getRemainingQuantity("Framework"));
    }
    
    @Test
    public void testCreateCustomKingdom() {
        String[] names = {"Bitcoin", "Ethereum", "Method"};
        int[] quantities = {10, 5, 3};
        
        Kingdom custom = Kingdom.createCustomKingdom(names, quantities);
        
        assertEquals(10, custom.getRemainingQuantity("Bitcoin"));
        assertEquals(5, custom.getRemainingQuantity("Ethereum"));
        assertEquals(3, custom.getRemainingQuantity("Method"));
        assertFalse(custom.isAvailable("Framework"));
    }
    
    @Test
    public void testKingdomPile_HasCards() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 5);
        Kingdom.KingdomPile pile = new Kingdom.KingdomPile(template, 5);
        
        assertTrue(pile.hasCards());
        assertEquals(5, pile.getRemainingQuantity());
    }
    
    @Test
    public void testKingdomPile_DecrementQuantity() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 5);
        Kingdom.KingdomPile pile = new Kingdom.KingdomPile(template, 5);
        
        pile.decrementQuantity();
        assertEquals(4, pile.getRemainingQuantity());
    }
    
    @Test
    public void testKingdomPile_DecrementToZero() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 1);
        Kingdom.KingdomPile pile = new Kingdom.KingdomPile(template, 1);
        
        pile.decrementQuantity();
        assertFalse(pile.hasCards());
        assertEquals(0, pile.getRemainingQuantity());
        
        // Should not go negative
        pile.decrementQuantity();
        assertEquals(0, pile.getRemainingQuantity());
    }
}
