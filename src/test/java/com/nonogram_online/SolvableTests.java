/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online;

import com.nonogram_online.generator.Nonogram;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author marton552
 */
public class SolvableTests {
    
    Nonogram nono;
    
    ArrayList<ArrayList<Integer>> generateNums(int[] nums){
        ArrayList<ArrayList<Integer>> res = new ArrayList();
        for (int i = 0; i < nums.length; i++) {
            res.add(new ArrayList<>());
            res.get(i).add(nums[i]);
        }
        return res;
    }
    
        @Test
    @DisplayName("Test: An empty level.")
    public void testEmpty() {
        int[] param1 = {0,0};
        nono = new Nonogram(generateNums(param1), generateNums(param1));
            assertTrue(nono.solveAndCheck());
    }
    
    @Test
    @DisplayName("Test: An invalid level shows as solvable.")
    public void testInvalid() {
        int[] param1 = {2,2};
        int[] param2 = {0,0};
        nono = new Nonogram(generateNums(param1), generateNums(param2));
        assertTrue(nono.solveAndCheck());
    }
    
    @Test
    @DisplayName("Test: A valid level.")
    public void testValid() {
        int[] param1 = {1,0};
        int[] param2 = {1,0};
        nono = new Nonogram(generateNums(param1), generateNums(param2));
        assertTrue(nono.solveAndCheck());
    }
    
        @Test
    @DisplayName("Test: A valid full level.")
    public void testValidFull() {
        int[] param1 = {2,0};
        int[] param2 = {1,1};
        nono = new Nonogram(generateNums(param1), generateNums(param2));
        assertTrue(nono.solveAndCheck());
    }
    
            @Test
    @DisplayName("Test: A level that has multiple solutions.")
    public void testMultipleSolutions() {
        int[] param1 = {1,1};
        int[] param2 = {1,1};
        nono = new Nonogram(generateNums(param1), generateNums(param2));
        assertFalse(nono.solveAndCheck());
    }
    
}
