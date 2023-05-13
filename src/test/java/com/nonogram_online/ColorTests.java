/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online;

import com.nonogram_online.generator.ColorHandler;
import java.awt.Color;
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
public class ColorTests {

    @Test
    @DisplayName("Test: 2 colos are truly too close.")
    public void test2ColorsTrue() {
        assertTrue(ColorHandler.are2ColorsClose(new Color(122, 122, 122), new Color(112, 112, 112)));
    }

    @Test
    @DisplayName("Test: 2 colos arent too close.")
    public void test2ColorsFalse() {
        assertFalse(ColorHandler.are2ColorsClose(new Color(122, 122, 122), new Color(255, 112, 112)));
    }

    @Test
    @DisplayName("Test: Find close colors in a list")
    public void testFindCloseColors() {
        ArrayList<Color> colors = new ArrayList();
        colors.add(new Color(10, 10, 10));
        colors.add(new Color(10, 100, 100));
        colors.add(new Color(10, 20, 20));
        assertEquals((int) (ColorHandler.findCloseColors(colors)).get(0), 2);
    }

    @Test
    @DisplayName("Test: Get avg color")
    public void testAvgColor() {
        ArrayList<Color> colors = new ArrayList();
        colors.add(new Color(10, 10, 10));
        colors.add(new Color(20, 20, 20));
        assertEquals((ColorHandler.getAverageRGB(colors)), (new Color(15, 15, 15)));
    }

    @Test
    @DisplayName("Test: Get avg of one color")
    public void testAvgOneColor() {
        assertEquals((ColorHandler.avgColor(new Color(10, 10, 10))), 10);
    }

    @Test
    @DisplayName("Test: Change a color to the closest one")
    public void testChangeClosest() {
        ArrayList<Color> colors = new ArrayList();
        colors.add(new Color(10, 10, 10));
        colors.add(new Color(100, 100, 100));
        colors.add(new Color(200, 200, 200));
        assertEquals(ColorHandler.changeToClosest(new Color(30, 30, 30), colors), colors.get(0));
    }

    @Test
    @DisplayName("Test: If a color near to an other one. But no")
    public void testColorNearFalse() {
        assertFalse(ColorHandler.isColorNear(new Color(30, 30, 30), new Color(100, 100, 100), 10));
    }

    @Test
    @DisplayName("Test: If a color near to an other one. And YES")
    public void testColorNearTrue() {
        assertTrue(ColorHandler.isColorNear(new Color(30, 30, 30), new Color(100, 100, 100), 100));
    }

    @Test
    @DisplayName("Test: Find the most common color")
    public void testMostCommon() {
        ArrayList<Color> colors = new ArrayList();
        colors.add(new Color(10, 10, 10));
        colors.add(new Color(10, 10, 10));
        colors.add(new Color(200, 200, 200));
        assertEquals(ColorHandler.mostCommonColor(colors), colors.get(0));
    }

    @Test
    @DisplayName("Test: Find the most common color in an empty list")
    public void testMostCommonEmpty() {
        ArrayList<Color> colors = new ArrayList();
        assertEquals(ColorHandler.mostCommonColor(colors), null);
    }
}
