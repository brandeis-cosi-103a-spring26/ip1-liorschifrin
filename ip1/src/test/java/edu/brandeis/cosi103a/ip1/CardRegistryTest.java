package edu.brandeis.cosi103a.ip1;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Unit tests for the CardRegistry class.
 */
public class CardRegistryTest {
    
    @Test
    public void testGetTemplate_Bitcoin() {
        CardTemplate template = CardRegistry.getTemplate("Bitcoin");
        assertNotNull(template);
        assertEquals("Bitcoin", template.getName());
        assertEquals(CardType.CRYPTOCURRENCY, template.getType());
        assertEquals(0, template.getCost());
        assertEquals(1, template.getValue());
        assertEquals(60, template.getQuantity());
    }
    
    @Test
    public void testGetTemplate_Ethereum() {
        CardTemplate template = CardRegistry.getTemplate("Ethereum");
        assertNotNull(template);
        assertEquals("Ethereum", template.getName());
        assertEquals(3, template.getCost());
        assertEquals(2, template.getValue());
        assertEquals(40, template.getQuantity());
    }
    
    @Test
    public void testGetTemplate_Dogecoin() {
        CardTemplate template = CardRegistry.getTemplate("Dogecoin");
        assertNotNull(template);
        assertEquals("Dogecoin", template.getName());
        assertEquals(6, template.getCost());
        assertEquals(3, template.getValue());
        assertEquals(30, template.getQuantity());
    }
    
    @Test
    public void testGetTemplate_Method() {
        CardTemplate template = CardRegistry.getTemplate("Method");
        assertNotNull(template);
        assertEquals("Method", template.getName());
        assertEquals(CardType.AUTOMATION, template.getType());
        assertEquals(2, template.getCost());
        assertEquals(1, template.getValue());
        assertEquals(14, template.getQuantity());
    }
    
    @Test
    public void testGetTemplate_Module() {
        CardTemplate template = CardRegistry.getTemplate("Module");
        assertNotNull(template);
        assertEquals("Module", template.getName());
        assertEquals(5, template.getCost());
        assertEquals(3, template.getValue());
        assertEquals(8, template.getQuantity());
    }
    
    @Test
    public void testGetTemplate_Framework() {
        CardTemplate template = CardRegistry.getTemplate("Framework");
        assertNotNull(template);
        assertEquals("Framework", template.getName());
        assertEquals(8, template.getCost());
        assertEquals(6, template.getValue());
        assertEquals(8, template.getQuantity());
    }
    
    @Test
    public void testGetTemplate_CaseInsensitive() {
        CardTemplate template = CardRegistry.getTemplate("bitcoin");
        assertNotNull(template);
        assertEquals("Bitcoin", template.getName());
    }
    
    @Test
    public void testGetTemplate_NotFound() {
        CardTemplate template = CardRegistry.getTemplate("NonExistent");
        assertNull(template);
    }
    
    @Test
    public void testGetTemplatesByType_Cryptocurrency() {
        List<CardTemplate> templates = CardRegistry.getTemplatesByType(CardType.CRYPTOCURRENCY);
        assertEquals(3, templates.size());
        
        boolean hasBitcoin = false;
        boolean hasEthereum = false;
        boolean hasDogecoin = false;
        
        for (CardTemplate t : templates) {
            if (t.getName().equals("Bitcoin")) hasBitcoin = true;
            if (t.getName().equals("Ethereum")) hasEthereum = true;
            if (t.getName().equals("Dogecoin")) hasDogecoin = true;
        }
        
        assertTrue(hasBitcoin);
        assertTrue(hasEthereum);
        assertTrue(hasDogecoin);
    }
    
    @Test
    public void testGetTemplatesByType_Automation() {
        List<CardTemplate> templates = CardRegistry.getTemplatesByType(CardType.AUTOMATION);
        assertEquals(3, templates.size());
        
        boolean hasMethod = false;
        boolean hasModule = false;
        boolean hasFramework = false;
        
        for (CardTemplate t : templates) {
            if (t.getName().equals("Method")) hasMethod = true;
            if (t.getName().equals("Module")) hasModule = true;
            if (t.getName().equals("Framework")) hasFramework = true;
        }
        
        assertTrue(hasMethod);
        assertTrue(hasModule);
        assertTrue(hasFramework);
    }
    
    @Test
    public void testGetAllTemplates() {
        List<CardTemplate> templates = CardRegistry.getAllTemplates();
        assertEquals(6, templates.size());
    }
    
    @Test
    public void testCreateFullDeck() {
        List<Card> deck = CardRegistry.createFullDeck();
        // 60 Bitcoin + 40 Ethereum + 30 Dogecoin + 14 Method + 8 Module + 8 Framework = 160
        assertEquals(160, deck.size());
    }
    
    @Test
    public void testCreateStarterDeck() {
        List<Card> deck = CardRegistry.createStarterDeck();
        assertEquals(10, deck.size()); // 7 Bitcoin + 3 Method
        
        int bitcoinCount = 0;
        int methodCount = 0;
        
        for (Card card : deck) {
            if (card.getName().equals("Bitcoin")) bitcoinCount++;
            if (card.getName().equals("Method")) methodCount++;
        }
        
        assertEquals(7, bitcoinCount);
        assertEquals(3, methodCount);
    }
    
    @Test
    public void testCreateCustomDeck() {
        String[][] config = {
            {"Bitcoin", "5"},
            {"Ethereum", "3"},
            {"Method", "2"}
        };
        
        List<Card> deck = CardRegistry.createCustomDeck(config);
        assertEquals(10, deck.size());
        
        int bitcoinCount = 0;
        int ethereumCount = 0;
        int methodCount = 0;
        
        for (Card card : deck) {
            if (card.getName().equals("Bitcoin")) bitcoinCount++;
            if (card.getName().equals("Ethereum")) ethereumCount++;
            if (card.getName().equals("Method")) methodCount++;
        }
        
        assertEquals(5, bitcoinCount);
        assertEquals(3, ethereumCount);
        assertEquals(2, methodCount);
    }
    
    @Test
    public void testCreateCustomDeck_InvalidCard() {
        String[][] config = {
            {"Bitcoin", "5"},
            {"NonExistent", "3"}
        };
        
        List<Card> deck = CardRegistry.createCustomDeck(config);
        assertEquals(5, deck.size()); // Only Bitcoin cards added
    }
}
