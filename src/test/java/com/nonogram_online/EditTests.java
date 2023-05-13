/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online;

import com.nonogram_online.level.Level;
import com.nonogram_online.level.LevelEditor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author marton552
 */
public class EditTests {

    String defaultLevel = "elso;5;1;2;rgb(255,255,255);rgb(0,0,0);0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0";
    LevelEditor lvl;

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
        lvl = new LevelEditor(new ArrayList<String>(Arrays.asList(defaultLevel.split(";"))),5, "test", "test", true);
        lvl.setMatrixStartPos(0, 0);
        lvl.setTrueSquareSize(1);
    }
    
    @Test
    @DisplayName("Test: A created level is empty")
    public void testEmpty() {
        assertFalse(hasAnyTileDone());
    }
    
    @Test
    @DisplayName("Test: Add a simple tile")
    public void testAddTile() {
        assertFalse(hasAnyTileDone());
        lvl.clickOnTileByExact(0,0,0);
        assertTrue(hasAnyTileDone());
    }
    
    @Test
    @DisplayName("Test: Add a whole row tile")
    public void testAddRow() {
        assertFalse(hasAnyTileDone());
        for (int i = 0; i < 5; i++) {
            lvl.clickOnTileByExact(i,0,0);
        }
        assertEquals(lvl.getMatrix().get(0).getTopNumbers().get(0).get(0).getNum(), 5);
    }
    
    @Test
    @DisplayName("Test: Add color")
    public void testAddColor() {
        assertEquals(lvl.getColors().size(), 2);
        lvl.addColor(Color.red);
        assertEquals(lvl.getColors().size(), 3);
    }
    
    @Test
    @DisplayName("Test: Add layer")
    public void testAddLayer() {
        assertEquals(lvl.getMatrix().size(), 1);
        lvl.addLayer();
        assertEquals(lvl.getMatrix().size(), 2);
    }
    
    @Test
    @DisplayName("Test: While editing finished always false")
    public void testFinishedFalse() {
        assertFalse(lvl.isFinished());
        for (int i = 0; i < 5; i++) {
            lvl.clickOnTileByExact(i,0,0);
        }
        assertFalse(lvl.isFinished());
    }
}
