package edu.brandeis.cosi103a.ip1;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Unit tests for the Player class.
 */
public class PlayerTest {
    
    private Player player;
    private Kingdom kingdom;
    
    @Before
    public void setUp() {
        player = new Player("Test Player");
        kingdom = Kingdom.createStandardKingdom();
    }
    
    @Test
    public void testConstructor_WithName() {
        Player p = new Player("Alice");
        assertNotNull(p);
        assertEquals("Alice", p.getName());
        assertEquals(0, p.handSize());
        assertEquals(0, p.getCoins());
        assertEquals(0, p.getBuys());
    }
    
    @Test
    public void testConstructor_WithDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        PlayerDeck deck = new PlayerDeck(cards);
        
        Player p = new Player("Bob", deck);
        assertEquals("Bob", p.getName());
        assertEquals(1, p.getDeck().size());
    }
    
    @Test
    public void testGetName() {
        assertEquals("Test Player", player.getName());
    }
    
    @Test
    public void testGetDeck() {
        assertNotNull(player.getDeck());
    }
    
    @Test
    public void testGetHand() {
        assertNotNull(player.getHand());
        assertEquals(0, player.getHand().size());
    }
    
    @Test
    public void testGetPlayedCards() {
        assertNotNull(player.getPlayedCards());
        assertEquals(0, player.getPlayedCards().size());
    }
    
    @Test
    public void testGetCoins() {
        assertEquals(0, player.getCoins());
    }
    
    @Test
    public void testGetBuys() {
        assertEquals(0, player.getBuys());
    }
    
    @Test
    public void testStartTurn() {
        player.startTurn();
        assertEquals(0, player.getCoins());
        assertEquals(1, player.getBuys());
    }
    
    @Test
    public void testStartTurn_ResetsValues() {
        player.startTurn();
        player.addCoins(10);
        assertEquals(10, player.getCoins());
        
        player.startTurn(); // Should reset
        assertEquals(0, player.getCoins());
        assertEquals(1, player.getBuys());
    }
    
    @Test
    public void testAddCoins() {
        player.addCoins(5);
        assertEquals(5, player.getCoins());
        
        player.addCoins(3);
        assertEquals(8, player.getCoins());
    }
    
    @Test
    public void testDrawCard_Success() {
        player.getDeck().addToDrawPile(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        
        boolean result = player.drawCard();
        
        assertTrue(result);
        assertEquals(1, player.handSize());
    }
    
    @Test
    public void testDrawCard_EmptyDeck() {
        boolean result = player.drawCard();
        assertFalse(result);
        assertEquals(0, player.handSize());
    }
    
    @Test
    public void testDrawCards() {
        for (int i = 0; i < 10; i++) {
            player.getDeck().addToDrawPile(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        }
        
        int drawn = player.drawCards(5);
        
        assertEquals(5, drawn);
        assertEquals(5, player.handSize());
    }
    
    @Test
    public void testDrawCards_MoreThanAvailable() {
        player.getDeck().addToDrawPile(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        player.getDeck().addToDrawPile(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        
        int drawn = player.drawCards(5);
        
        assertEquals(2, drawn); // Only 2 were available
        assertEquals(2, player.handSize());
    }
    
    @Test
    public void testPlayCard_ByIndex() {
        Card card = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        player.getHand().add(card);
        
        Card played = player.playCard(0);
        
        assertNotNull(played);
        assertEquals(0, player.handSize());
        assertEquals(1, player.getPlayedCards().size());
    }
    
    @Test
    public void testPlayCard_ByIndex_InvalidIndex() {
        Card played = player.playCard(5);
        assertNull(played);
    }
    
    @Test
    public void testPlayCard_ByCard() {
        Card card = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        player.getHand().add(card);
        
        boolean result = player.playCard(card);
        
        assertTrue(result);
        assertEquals(0, player.handSize());
        assertEquals(1, player.getPlayedCards().size());
    }
    
    @Test
    public void testPlayCard_ByCard_NotInHand() {
        Card card = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        boolean result = player.playCard(card);
        assertFalse(result);
    }
    
    @Test
    public void testPlayAllCryptoCards() {
        player.getHand().add(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        player.getHand().add(new Card(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2));
        player.getHand().add(new Card(CardType.AUTOMATION, "Method", 2, 1));
        
        int coinsGenerated = player.playAllCryptoCards();
        
        assertEquals(3, coinsGenerated); // 1 + 2
        assertEquals(1, player.handSize()); // Method should remain
        assertEquals(2, player.getPlayedCards().size());
        assertEquals(3, player.getCoins());
    }
    
    @Test
    public void testPlayAllCryptoCards_NoCoins() {
        player.getHand().add(new Card(CardType.AUTOMATION, "Method", 2, 1));
        
        int coinsGenerated = player.playAllCryptoCards();
        
        assertEquals(0, coinsGenerated);
        assertEquals(1, player.handSize());
    }
    
    @Test
    public void testBuyCard_Success() {
        player.startTurn();
        player.addCoins(5);
        
        boolean result = player.buyCard(kingdom, "Ethereum"); // Cost 3
        
        assertTrue(result);
        assertEquals(2, player.getCoins()); // 5 - 3
        assertEquals(0, player.getBuys()); // 1 - 1
        assertEquals(39, kingdom.getRemainingQuantity("Ethereum"));
    }
    
    @Test
    public void testBuyCard_InsufficientCoins() {
        player.startTurn();
        player.addCoins(2);
        
        boolean result = player.buyCard(kingdom, "Ethereum"); // Cost 3
        
        assertFalse(result);
        assertEquals(2, player.getCoins()); // Unchanged
        assertEquals(1, player.getBuys()); // Unchanged
    }
    
    @Test
    public void testBuyCard_NoBuys() {
        player.addCoins(10);
        // Don't start turn, so buys = 0
        
        boolean result = player.buyCard(kingdom, "Bitcoin");
        
        assertFalse(result);
    }
    
    @Test
    public void testBuyCard_CardNotAvailable() {
        player.startTurn();
        player.addCoins(10);
        
        boolean result = player.buyCard(kingdom, "NonExistent");
        
        assertFalse(result);
        assertEquals(1, player.getBuys());
    }
    
    @Test
    public void testBuyCard_PileEmpty() {
        // Create kingdom with only 1 card
        Kingdom smallKingdom = new Kingdom();
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 1);
        smallKingdom.addPile(template, 1);
        
        player.startTurn();
        player.buyCard(smallKingdom, "Bitcoin");
        
        // Try to buy again
        player.startTurn();
        boolean result = player.buyCard(smallKingdom, "Bitcoin");
        
        assertFalse(result);
    }
    
    @Test
    public void testDiscardFromHand() {
        Card card = new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1);
        player.getHand().add(card);
        
        Card discarded = player.discardFromHand(0);
        
        assertNotNull(discarded);
        assertEquals(0, player.handSize());
        assertEquals(1, player.getDeck().discardSize());
    }
    
    @Test
    public void testDiscardFromHand_InvalidIndex() {
        Card discarded = player.discardFromHand(5);
        assertNull(discarded);
    }
    
    @Test
    public void testEndTurn() {
        // Add cards to hand
        player.getHand().add(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        player.getHand().add(new Card(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2));
        
        // Add cards to played
        player.getPlayedCards().add(new Card(CardType.AUTOMATION, "Method", 2, 1));
        
        player.endTurn();
        
        assertEquals(0, player.handSize());
        assertEquals(0, player.getPlayedCards().size());
        assertEquals(3, player.getDeck().discardSize());
    }
    
    @Test
    public void testHandSize() {
        assertEquals(0, player.handSize());
        
        player.getHand().add(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1));
        assertEquals(1, player.handSize());
        
        player.getHand().add(new Card(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2));
        assertEquals(2, player.handSize());
    }
    
    @Test
    public void testCalculateScore() {
        // Add cards to hand
        player.getHand().add(new Card(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1)); // Value 1
        
        // Add cards to played
        player.getPlayedCards().add(new Card(CardType.CRYPTOCURRENCY, "Ethereum", 3, 2)); // Value 2
        
        // Add cards to deck
        player.getDeck().addToDrawPile(new Card(CardType.AUTOMATION, "Framework", 8, 6)); // Value 6
        player.getDeck().discard(new Card(CardType.AUTOMATION, "Method", 2, 1)); // Value 1
        
        int score = player.calculateScore();
        
        assertEquals(10, score); // 1 + 2 + 6 + 1
    }
    
    @Test
    public void testCalculateScore_EmptyDeck() {
        int score = player.calculateScore();
        assertEquals(0, score);
    }
    
    @Test
    public void testToString() {
        String result = player.toString();
        assertTrue(result.contains("Test Player"));
        assertTrue(result.contains("hand"));
    }
}
