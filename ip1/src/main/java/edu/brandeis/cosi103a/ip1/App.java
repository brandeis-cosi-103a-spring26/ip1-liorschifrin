package edu.brandeis.cosi103a.ip1;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class App 
{
    
    private static Random random = new Random();
    private Player[] players;
    private Kingdom kingdom;
    private int turnCount;
    
    public static void main(String[] args) {
        App game = new App();
        game.setupGame(2);
        game.playGame();
    }

    /**
     * Sets up the game with the specified number of players.
     */
    public void setupGame(int numPlayers) {
        System.out.println("=".repeat(50));
        System.out.println("DOMINION-STYLE CARD GAME");
        System.out.println("=".repeat(50));
        System.out.println();
        
        // Create the kingdom with all card types
        kingdom = Kingdom.createStandardKingdom();
        System.out.println("Kingdom created with all card types.");
        System.out.println(kingdom.displayKingdom());
        
        // Create players with starter decks
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            String playerName = "AI Player " + (i + 1);
            
            // Create starter deck (7 Bitcoin, 3 Method)
            List<Card> starterCards = CardRegistry.createStarterDeck();
            ArrayList<Card> starterDeck = new ArrayList<>(starterCards);
            
            // Create PlayerDeck with starter cards
            PlayerDeck deck = new PlayerDeck(starterDeck);
            deck.setRandom(random);
            deck.shuffle();
            
            // Create player with this deck
            players[i] = new Player(playerName, deck);
            
            // Draw initial hand of 5 cards
            players[i].drawCards(5);
            
            System.out.println(playerName + " created with starter deck (7 Bitcoin, 3 Method)");
            System.out.println("  Initial hand drawn: " + players[i].handSize() + " cards");
        }
        
        System.out.println();
        System.out.println("Game setup complete! Starting game...");
        System.out.println();
        
        turnCount = 0;
    }
    
    /**
     * Main game loop.
     */
    public void playGame() {
        int currentPlayerIndex = 0;
        
        while (!kingdom.isGameOver()) {
            turnCount++;
            Player currentPlayer = players[currentPlayerIndex];
            
            System.out.println("=".repeat(50));
            System.out.println("TURN " + turnCount + " - " + currentPlayer.getName());
            System.out.println("=".repeat(50));
            
            playTurn(currentPlayer);
            
            // Move to next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
            
            System.out.println();
        }
        
        // Game over - display results
        endGame();
    }
    
    /**
     * Plays a single turn for a player.
     */
    private void playTurn(Player player) {
        // Start turn
        player.startTurn();
        System.out.println(player.getName() + "'s hand: " + player.handSize() + " cards");
        
        // Play all cryptocurrency cards
        System.out.println("\n--- Playing Cryptocurrency Cards ---");
        int coinsGenerated = 0;
        ArrayList<Card> cryptoCards = new ArrayList<>();
        
        // Collect crypto cards
        for (Card card : player.getHand()) {
            if (card.getType() == CardType.CRYPTOCURRENCY) {
                cryptoCards.add(card);
            }
        }
        
        // Play each crypto card and log it
        for (Card card : cryptoCards) {
            player.playCard(card);
            coinsGenerated += card.getValue();
            System.out.println("  Playing " + card.getName() + " for " + card.getValue() + " coin(s)");
        }
        
        player.addCoins(coinsGenerated);
        System.out.println("Total coins available: " + player.getCoins());
        System.out.println("Buys available: " + player.getBuys());
        
        // Buy phase
        System.out.println("\n--- Buy Phase ---");
        while (player.getBuys() > 0) {
            boolean purchased = aiMakePurchase(player, kingdom);
            if (!purchased) {
                break; // No more purchases possible
            }
        }
        
        if (player.getBuys() > 0) {
            System.out.println(player.getName() + " ends buy phase with " + 
                             player.getCoins() + " coins remaining");
        }
        
        // Cleanup phase
        System.out.println("\n--- Cleanup Phase ---");
        player.endTurn();
        int cardsDrawn = player.drawCards(5);
        System.out.println("Discarded hand and played cards");
        System.out.println("Drew " + cardsDrawn + " new cards for next turn");
    }
    
    /**
     * AI decision-making for purchasing cards.
     * Strategy: Buy Framework if possible, otherwise buy best crypto card affordable.
     */
    private boolean aiMakePurchase(Player player, Kingdom kingdom) {
        int coins = player.getCoins();
        
        // Try to buy Framework (8 cost)
        if (coins >= 8 && kingdom.isAvailable("Framework")) {
            if (player.buyCard(kingdom, "Framework")) {
                System.out.println("  " + player.getName() + " buys Framework (8 coins, 6 value)");
                System.out.println("    Remaining coins: " + player.getCoins() + ", Remaining buys: " + player.getBuys());
                return true;
            }
        }
        
        // Try to buy Dogecoin (6 cost)
        if (coins >= 6 && kingdom.isAvailable("Dogecoin")) {
            if (player.buyCard(kingdom, "Dogecoin")) {
                System.out.println("  " + player.getName() + " buys Dogecoin (6 coins, 3 value)");
                System.out.println("    Remaining coins: " + player.getCoins() + ", Remaining buys: " + player.getBuys());
                return true;
            }
        }
        
        // Try to buy Ethereum (3 cost)
        if (coins >= 3 && kingdom.isAvailable("Ethereum")) {
            if (player.buyCard(kingdom, "Ethereum")) {
                System.out.println("  " + player.getName() + " buys Ethereum (3 coins, 2 value)");
                System.out.println("    Remaining coins: " + player.getCoins() + ", Remaining buys: " + player.getBuys());
                return true;
            }
        }
        
        // Try to buy Bitcoin (0 cost)
        if (kingdom.isAvailable("Bitcoin")) {
            if (player.buyCard(kingdom, "Bitcoin")) {
                System.out.println("  " + player.getName() + " buys Bitcoin (0 coins, 1 value)");
                System.out.println("    Remaining coins: " + player.getCoins() + ", Remaining buys: " + player.getBuys());
                return true;
            }
        }
        
        // Could not make any purchase
        System.out.println("  " + player.getName() + " cannot afford any available cards (has " + coins + " coins)");
        return false;
    }
    
    /**
     * Ends the game and displays results.
     */
    private void endGame() {
        System.out.println("=".repeat(50));
        System.out.println("GAME OVER!");
        System.out.println("=".repeat(50));
        System.out.println();
        
        // Check why game ended
        if (!kingdom.isAvailable("Framework")) {
            System.out.println("Game ended: All Framework cards have been purchased!");
        } else {
            System.out.println("Game ended: Three or more kingdom piles are empty!");
        }
        
        System.out.println();
        System.out.println(kingdom.displayKingdom());
        System.out.println();
        
        // Calculate scores
        System.out.println("=".repeat(50));
        System.out.println("FINAL SCORES");
        System.out.println("=".repeat(50));
        
        int highestScore = -1;
        Player winner = null;
        boolean tie = false;
        
        for (Player player : players) {
            int score = player.calculateScore();
            System.out.println(player.getName() + ": " + score + " points");
            System.out.println("  Total cards in deck: " + player.getDeck().totalCards());
            System.out.println("  Cards in hand: " + player.handSize());
            System.out.println();
            
            if (score > highestScore) {
                highestScore = score;
                winner = player;
                tie = false;
            } else if (score == highestScore) {
                tie = true;
            }
        }
        
        System.out.println("=".repeat(50));
        if (tie) {
            System.out.println("IT'S A TIE!");
            System.out.println("Winner: " + players[players.length - 1].getName() + " (went second)");
        } else {
            System.out.println("WINNER: " + winner.getName() + " with " + highestScore + " points!");
        }
        System.out.println("=".repeat(50));
        System.out.println("Total turns played: " + turnCount);
    }
    
}
