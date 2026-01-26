package edu.brandeis.cosi103a.ip1;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Unit tests for the PlayerDeck class.
 */
public class PlayerDeckTest {
    
    private PlayerDeck emptyDeck;
    private PlayerDeck starterDeck;
    private ArrayList<Card> starterCards;
    
    @Before
    public void setUp() {
        emptyDeck = new PlayerDeck();
        
        starterCards = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            starterCards.add(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        }
        for (int i = 0; i < 3; i++) {
            starterCards.add(new Card(CardType.AUTOMATION, "Method", 2, 1));
        }
        
        starterDeck = new PlayerDeck(starterCards);
    }
    
    @Test
    public void testConstructor_Empty() {
        PlayerDeck deck = new PlayerDeck();
        assertNotNull(deck);
        assertEquals(0, deck.size());
        assertEquals(0, deck.discardSize());
    }
    
    @Test
    public void testConstructor_WithCards() {
        assertEquals(10, starterDeck.size());
        assertEquals(0, starterDeck.discardSize());
    }
    
    @Test
    public void testSetRandom() {
        Random customRandom = new Random(42);
        emptyDeck.setRandom(customRandom);
        // If no exception, test passes
    }
    
    @Test
    public void testShuffle() {
        starterDeck.shuffle();
        assertEquals(10, starterDeck.size()); // Size should remain the same
    }
    
    @Test
    public void testDrawOne_FromNonEmptyDeck() {
        Card card = starterDeck.drawOne();
        assertNotNull(card);
        assertEquals(9, starterDeck.size());
    }
    
    @Test
    public void testDrawOne_FromEmptyDeck() {
        Card card = emptyDeck.drawOne();
        assertNull(card);
    }
    
    @Test
    public void testDrawOne_AllCards() {
        for (int i = 0; i < 10; i++) {
            Card card = starterDeck.drawOne();
            assertNotNull(card);
        }
        assertEquals(0, starterDeck.size());
        
        // Next draw should return null
        Card card = starterDeck.drawOne();
        assertNull(card);
    }
    
    @Test
    public void testDiscard() {
        Card card = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        emptyDeck.discard(card);
        
        assertEquals(0, emptyDeck.size());
        assertEquals(1, emptyDeck.discardSize());
    }
    
    @Test
    public void testDiscard_Null() {
        emptyDeck.discard(null);
        assertEquals(0, emptyDeck.discardSize());
    }
    
    @Test
    public void testDiscard_Multiple() {
        for (int i = 0; i < 5; i++) {
            Card card = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
            emptyDeck.discard(card);
        }
        assertEquals(5, emptyDeck.discardSize());
    }
    
    @Test
    public void testIsEmpty_EmptyDeck() {
        assertTrue(emptyDeck.isEmpty());
    }
    
    @Test
    public void testIsEmpty_NonEmptyDeck() {
        assertFalse(starterDeck.isEmpty());
    }
    
    @Test
    public void testSize() {
        assertEquals(10, starterDeck.size());
        starterDeck.drawOne();
        assertEquals(9, starterDeck.size());
    }
    
    @Test
    public void testDiscardSize() {
        assertEquals(0, starterDeck.discardSize());
        Card card = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        starterDeck.discard(card);
        assertEquals(1, starterDeck.discardSize());
    }
    
    @Test
    public void testReshuffleDiscardIntoDraw() {
        // Draw all cards
        for (int i = 0; i < 10; i++) {
            Card card = starterDeck.drawOne();
            starterDeck.discard(card);
        }
        
        assertEquals(0, starterDeck.size());
        assertEquals(10, starterDeck.discardSize());
        
        starterDeck.reshuffleDiscardIntoDraw();
        
        assertEquals(10, starterDeck.size());
        assertEquals(0, starterDeck.discardSize());
    }
    
    @Test
    public void testReshuffleDiscardIntoDraw_EmptyDiscard() {
        int originalSize = starterDeck.size();
        starterDeck.reshuffleDiscardIntoDraw();
        assertEquals(originalSize, starterDeck.size());
    }
    
    @Test
    public void testDrawOne_AutoReshuffle() {
        // Draw all cards to empty the draw pile
        for (int i = 0; i < 10; i++) {
            Card card = starterDeck.drawOne();
            starterDeck.discard(card);
        }
        
        assertEquals(0, starterDeck.size());
        assertEquals(10, starterDeck.discardSize());
        
        // Draw should trigger reshuffle
        Card card = starterDeck.drawOne();
        assertNotNull(card);
        assertEquals(9, starterDeck.size());
        assertEquals(0, starterDeck.discardSize());
    }
    
    @Test
    public void testAddToDrawPile() {
        Card card = new Card(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2);
        int originalSize = starterDeck.size();
        
        starterDeck.addToDrawPile(card);
        
        assertEquals(originalSize + 1, starterDeck.size());
    }
    
    @Test
    public void testAddToDrawPile_Null() {
        int originalSize = starterDeck.size();
        starterDeck.addToDrawPile(null);
        assertEquals(originalSize, starterDeck.size());
    }
    
    @Test
    public void testTotalCards() {
        assertEquals(10, starterDeck.totalCards());
        
        // Draw 5 cards
        for (int i = 0; i < 5; i++) {
            Card card = starterDeck.drawOne();
            starterDeck.discard(card);
        }
        
        assertEquals(10, starterDeck.totalCards()); // Total should still be 10
    }
    
    @Test
    public void testCalculateTotalValue() {
        // Starter deck: 7 Bitcoin (value 1 each) + 3 Method (value 1 each) = 10
        assertEquals(10, starterDeck.calculateTotalValue());
    }
    
    @Test
    public void testCalculateTotalValue_Mixed() {
        PlayerDeck deck = new PlayerDeck();
        deck.addToDrawPile(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        deck.addToDrawPile(new Card(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2));
        
        Card dogecoin = new Card(CardType.CRYPTOCURRENCY, "Dogecoin", 6, 3);
        deck.discard(dogecoin);
        
        // 1 + 2 + 3 = 6
        assertEquals(6, deck.calculateTotalValue());
    }
    
    @Test
    public void testToString() {
        String result = starterDeck.toString();
        assertTrue(result.contains("PlayerDeck"));
        assertTrue(result.contains("10"));
    }
}
