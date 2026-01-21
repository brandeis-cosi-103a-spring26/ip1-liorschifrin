package edu.brandeis.cosi103a.ip1;

import static org.junit.Assert.*;

import org.junit.Test;
import java.lang.reflect.Method;

/**
 * Unit test for Dice Game App.
 */
public class AppTest 
{
    /**
     * Test that die roll returns a value between 1 and 6
     */
    @Test
    public void testDieRollRange()
    {
        try {
            Method rollDieMethod = App.class.getDeclaredMethod("rollDie");
            rollDieMethod.setAccessible(true);
            
            // Test multiple rolls to ensure they're always in valid range
            for (int i = 0; i < 100; i++) {
                int roll = (int) rollDieMethod.invoke(null);
                assertTrue("Die roll should be between 1 and 6", roll >= 1 && roll <= 6);
            }
        } catch (Exception e) {
            fail("Could not test die roll method: " + e.getMessage());
        }
    }
    
    /**
     * Test that die roll produces variety (not always same value)
     */
    @Test
    public void testDieRollVariety()
    {
        try {
            Method rollDieMethod = App.class.getDeclaredMethod("rollDie");
            rollDieMethod.setAccessible(true);
            
            boolean[] numbersRolled = new boolean[7]; // indices 1-6
            
            // Roll die 100 times and check if we get variety
            for (int i = 0; i < 100; i++) {
                int roll = (int) rollDieMethod.invoke(null);
                numbersRolled[roll] = true;
            }
            
            // Check that we got at least 3 different numbers (statistically very likely)
            int varietyCount = 0;
            for (int i = 1; i <= 6; i++) {
                if (numbersRolled[i]) varietyCount++;
            }
            assertTrue("Die rolls should produce variety", varietyCount >= 3);
        } catch (Exception e) {
            fail("Could not test die roll variety: " + e.getMessage());
        }
    }
    
    /**
     * Test that die roll produces all possible values over many rolls
     */
    @Test
    public void testDieRollAllValues()
    {
        try {
            Method rollDieMethod = App.class.getDeclaredMethod("rollDie");
            rollDieMethod.setAccessible(true);
            
            boolean[] numbersRolled = new boolean[7]; // indices 1-6
            
            // Roll die 1000 times to statistically ensure all values appear
            for (int i = 0; i < 1000; i++) {
                int roll = (int) rollDieMethod.invoke(null);
                numbersRolled[roll] = true;
            }
            
            // Check that all numbers 1-6 have been rolled
            for (int i = 1; i <= 6; i++) {
                assertTrue("Die should roll " + i, numbersRolled[i]);
            }
        } catch (Exception e) {
            fail("Could not test die roll all values: " + e.getMessage());
        }
    }
    
    /**
     * Test die roll distribution is roughly uniform
     */
    @Test
    public void testDieRollDistribution()
    {
        try {
            Method rollDieMethod = App.class.getDeclaredMethod("rollDie");
            rollDieMethod.setAccessible(true);
            
            int[] counts = new int[7]; // indices 1-6
            int rolls = 6000;
            
            // Roll die 6000 times (should get ~1000 of each)
            for (int i = 0; i < rolls; i++) {
                int roll = (int) rollDieMethod.invoke(null);
                counts[roll]++;
            }
            
            // Check that each number appears roughly 1/6 of the time (within 15%)
            int expected = rolls / 6;
            int tolerance = (int)(expected * 0.15);
            
            for (int i = 1; i <= 6; i++) {
                assertTrue("Number " + i + " should appear ~" + expected + " times (got " + counts[i] + ")",
                    counts[i] >= expected - tolerance && counts[i] <= expected + tolerance);
            }
        } catch (Exception e) {
            fail("Could not test die roll distribution: " + e.getMessage());
        }
    }
}
