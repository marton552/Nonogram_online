/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online;

import com.nonogram_online.level.Level;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author marton552
 */
public class LevelTests {

    String defaultLevel = "elso;5;1;3;rgb(255,255,255);rgb(0,0,0);rgb(200,0,0);1;0;1;0;1;1;0;1;0;1;1;1;1;1;1;0;0;1;0;0;2;2;1;0;0";
    Level lvl;

    boolean hasAnyTileDone() {
        boolean res = false;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!res) {
                    res = lvl.getMatrix().get(0).getTileByIsDone(i, j);
                }
            }
        }
        return res;
    }

    @BeforeEach
    void createLevel() {
        lvl = new Level(new ArrayList<String>(Arrays.asList(defaultLevel.split(";"))), "test", "test", true);
        lvl.setMatrixStartPos(0, 0);
        lvl.setTrueSquareSize(1);
    }

    @Test
    @DisplayName("Test: A level has the basic parameters.")
    public void testBasicparams() {
        assertEquals(lvl.getColors().size(), 3);
        assertEquals(lvl.getCreator_name(), "test");
        assertEquals(lvl.getCreated_date(), "test");
        assertTrue(lvl.isApproved());
        assertTrue(lvl.isSolvable());
    }

    @Test
    @DisplayName("Test: Setup top numbers.")
    public void testTopNumbers() {
        assertEquals(lvl.getMatrix().get(0).getTopNumbers().get(0).get(0).getNum(), 3);
        assertEquals(lvl.getMatrix().get(0).getTopNumbers().get(0).get(1).getNum(), 1);
    }

    @Test
    @DisplayName("Test: Click on a tile.")
    public void testClickOnTile() {
        lvl.clickOnTile(0, 0, 0);
        assertTrue(lvl.getMatrix().get(0).getTileByIsDone(0, 0));
    }

    @Test
    @DisplayName("Test: Click on a tile and top numbers changed.")
    public void testClickOnTileAndTopNumbers() {
        assertFalse(lvl.getMatrix().get(0).getTopNumbers().get(0).get(0).isSolved());
        lvl.clickOnTile(0, 0, 0);
        lvl.clickOnTile(0, 1, 0);
        lvl.clickOnTile(0, 2, 0);
        assertTrue(lvl.getMatrix().get(0).getTopNumbers().get(0).get(0).isSolved());
    }

    @Test
    @DisplayName("Test: Click on a wrong tile.")
    public void testClickOnWrongTile() {
        assertFalse(lvl.getMatrix().get(0).getTileByIsFailed(0, 1));
        lvl.clickOnTile(1, 0, 0);
        assertTrue(lvl.getMatrix().get(0).getTileByIsFailed(0, 1));
    }

    @Test
    @DisplayName("Test: Click on a right tile with wrong color.")
    public void testClickOnRightWrongTile() {
        assertFalse(lvl.getMatrix().get(0).getTileByIsFailed(0, 1));
        lvl.clickOnTile(1, 0, 0);
        assertTrue(lvl.getMatrix().get(0).getTileByIsFailed(0, 1));
        assertTrue(lvl.getMatrix().get(0).getTileByIsDone(0, 1));
    }

    @Test
    @DisplayName("Test: Random hint works")
    public void testRandomHint() {
        assertFalse(hasAnyTileDone());
        lvl.randomHint();
        assertTrue(hasAnyTileDone());
    }

    @Test
    @DisplayName("Test: Done level")
    public void testDoneLevel() {
        assertFalse(lvl.isFinished());
        lvl.finishGame();
        assertTrue(lvl.isFinished());
    }
    
    @Test
    @DisplayName("Test: Retry level")
    public void testRetryLevel() {
        assertFalse(lvl.isFinished());
        lvl.finishGame();
        assertTrue(lvl.isFinished());
        lvl.retry();
        assertFalse(lvl.isFinished());
    }
}
