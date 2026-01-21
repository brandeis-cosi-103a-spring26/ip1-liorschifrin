package edu.brandeis.cosi103a.ip1;

import java.util.Scanner;
import java.util.Random;

/**
 * Dice Game
 * 2 players take turns rolling a die, can re-roll up to 2 times per turn
 * Each player gets 10 turns, highest score wins
 */
public class App 
{
    private static final int NUM_PLAYERS = 2;
    private static final int TURNS_PER_PLAYER = 10;
    private static final int MAX_REROLLS = 2;
    private static final int DIE_SIDES = 6;
    
    private static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main( String[] args )
    {
        System.out.println("========== DICE GAME ==========");
        System.out.println("2 players, 10 turns each");
        System.out.println("Roll a die and decide to keep or re-roll (up to 2 re-rolls)");
        System.out.println("Highest score wins!\n");
        
        // Initialize player scores
        int[] scores = new int[NUM_PLAYERS];
        String[] playerNames = new String[NUM_PLAYERS];
        
        // Get player names
        for (int i = 0; i < NUM_PLAYERS; i++) {
            System.out.print("Enter name for Player " + (i + 1) + ": ");
            playerNames[i] = scanner.nextLine();
        }
        
        System.out.println("\n========== GAME START ==========\n");
        
        // Play 10 turns for each player
        for (int turn = 0; turn < TURNS_PER_PLAYER; turn++) {
            for (int player = 0; player < NUM_PLAYERS; player++) {
                System.out.println(playerNames[player] + "'s turn (Turn " + (turn + 1) + "/10):");
                int turnScore = playTurn(playerNames[player]);
                scores[player] += turnScore;
                System.out.println(playerNames[player] + " scored " + turnScore + " points this turn");
                System.out.println(playerNames[player] + "'s total score: " + scores[player] + "\n");
            }
        }
        
        // Determine winner
        System.out.println("========== GAME OVER ==========");
        System.out.println("\nFinal Scores:");
        System.out.println(playerNames[0] + ": " + scores[0]);
        System.out.println(playerNames[1] + ": " + scores[1]);
        
        if (scores[0] > scores[1]) {
            System.out.println("\n🎉 " + playerNames[0] + " wins with " + scores[0] + " points!");
        } else if (scores[1] > scores[0]) {
            System.out.println("\n🎉 " + playerNames[1] + " wins with " + scores[1] + " points!");
        } else {
            System.out.println("\n🎉 It's a tie! Both players scored " + scores[0] + " points!");
        }
        
        scanner.close();
    }
    
    /**
     * Plays a single turn for a player
     * @param playerName the name of the player
     * @return the score for this turn
     */
    private static int playTurn(String playerName) {
        int currentRoll = rollDie();
        System.out.println("  Rolled: " + currentRoll);
        
        int rerollCount = 0;
        
        // Allow up to 2 re-rolls
        while (rerollCount < MAX_REROLLS) {
            System.out.print("  Do you want to re-roll? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("yes") || response.equals("y")) {
                currentRoll = rollDie();
                System.out.println("  New roll: " + currentRoll);
                rerollCount++;
            } else if (response.equals("no") || response.equals("n")) {
                break;
            } else {
                System.out.println("  Invalid input. Please enter 'yes' or 'no'.");
            }
        }
        
        // If player used all re-rolls, they must keep current roll
        if (rerollCount == MAX_REROLLS) {
            System.out.println("  No more re-rolls available. Final score for turn: " + currentRoll);
        } else {
            System.out.println("  Final score for turn: " + currentRoll);
        }
        
        return currentRoll;
    }
    
    /**
     * Rolls a 6-sided die
     * @return a random number between 1 and 6
     */
    private static int rollDie() {
        return random.nextInt(DIE_SIDES) + 1;
    }
}
