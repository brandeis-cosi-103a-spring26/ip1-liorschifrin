package edu.brandeis.cosi103a.ip1;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the CardTemplate class.
 */
public class CardTemplateTest {
    
    private CardTemplate bitcoinTemplate;
    private CardTemplate frameworkTemplate;
    
    @Before
    public void setUp() {
        bitcoinTemplate = new CardTemplate(CardType.CRYPTOCURRENCY, "Bitcoin", 0, 1, 60);
        frameworkTemplate = new CardTemplate(CardType.AUTOMATION, "Framework", 8, 6, 8);
    }
    
    @Test
    public void testConstructor() {
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Test", 5, 3, 10);
        assertNotNull(template);
    }
    
    @Test
    public void testGetType() {
        assertEquals(CardType.CRYPTOCURRENCY, bitcoinTemplate.getType());
        assertEquals(CardType.AUTOMATION, frameworkTemplate.getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("Bitcoin", bitcoinTemplate.getName());
        assertEquals("Framework", frameworkTemplate.getName());
    }
    
    @Test
    public void testGetCost() {
        assertEquals(0, bitcoinTemplate.getCost());
        assertEquals(8, frameworkTemplate.getCost());
    }
    
    @Test
    public void testGetValue() {
        assertEquals(1, bitcoinTemplate.getValue());
        assertEquals(6, frameworkTemplate.getValue());
    }
    
    @Test
    public void testGetQuantity() {
        assertEquals(60, bitcoinTemplate.getQuantity());
        assertEquals(8, frameworkTemplate.getQuantity());
    }
    
    @Test
    public void testCreateCard() {
        Card card = bitcoinTemplate.createCard();
        assertNotNull(card);
        assertEquals(CardType.CRYPTOCURRENCY, card.getType());
        assertEquals("Bitcoin", card.getName());
        assertEquals(0, card.getCost());
        assertEquals(1, card.getValue());
    }
    
    @Test
    public void testCreateCard_MultipleInstances() {
        Card card1 = bitcoinTemplate.createCard();
        Card card2 = bitcoinTemplate.createCard();
        assertNotSame(card1, card2);
        assertEquals(card1, card2); // Should be equal but different objects
    }
    
    @Test
    public void testAddCardsToList() {
        List<Card> list = new ArrayList<>();
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Test", 2, 1, 5);
        
        template.addCardsToList(list);
        
        assertEquals(5, list.size());
        for (Card card : list) {
            assertEquals("Test", card.getName());
            assertEquals(2, card.getCost());
            assertEquals(1, card.getValue());
        }
    }
    
    @Test
    public void testAddCardsToList_ZeroQuantity() {
        List<Card> list = new ArrayList<>();
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Test", 2, 1, 0);
        
        template.addCardsToList(list);
        
        assertEquals(0, list.size());
    }
    
    @Test
    public void testAddCardsToList_ExistingList() {
        List<Card> list = new ArrayList<>();
        Card existing = new Card(CardType.AUTOMATION, "Existing", 1, 1);
        list.add(existing);
        
        CardTemplate template = new CardTemplate(CardType.CRYPTOCURRENCY, "Test", 2, 1, 3);
        template.addCardsToList(list);
        
        assertEquals(4, list.size());
        assertEquals(existing, list.get(0));
    }
}
