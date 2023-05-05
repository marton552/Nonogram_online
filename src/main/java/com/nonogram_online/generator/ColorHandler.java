/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online.generator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marton552
 */
public class ColorHandler {

    public static boolean are2ColorsClose(Color one, Color two) {
        int threshold = 32;
        if (one.getRed() + threshold > two.getRed() && one.getRed() - threshold < two.getRed()) {
            if (one.getGreen() + threshold > two.getGreen() && one.getGreen() - threshold < two.getGreen()) {
                if (one.getBlue() + threshold > two.getBlue() && one.getBlue() - threshold < two.getBlue()) {
                    return true;
                }
            }
        }
        if (two.getRed() + threshold > one.getRed() && two.getRed() - threshold < one.getRed()) {
            if (two.getGreen() + threshold > one.getGreen() && two.getGreen() - threshold < one.getGreen()) {
                if (two.getBlue() + threshold > one.getBlue() && two.getBlue() - threshold < one.getBlue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static HashMap<Integer, Integer> findCloseColors(ArrayList<Color> colors, Color c) {
        ArrayList<Color> newColors = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            newColors.add(colors.get(i));
        }
        newColors.add(c);
        System.out.println(newColors);
        return findCloseColors(newColors);
    }

    public static HashMap<Integer, Integer> findCloseColors(ArrayList<Color> colors) {
        HashMap<Integer, Integer> closePairs = new HashMap<Integer, Integer>();
        for (int i = 0; i < colors.size(); i++) {
            for (int j = i + 1; j < colors.size(); j++) {
                if (are2ColorsClose(colors.get(i), colors.get(j))) {
                    closePairs.put(i, j);
                }
            }
        }
        System.out.println(closePairs);
        return closePairs;
    }

    public static Color getAverageRGB(ArrayList<Color> colors) {
        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;

        for (int i = 0; i < colors.size(); i++) {

            totalRed += colors.get(i).getRed();
            totalGreen += colors.get(i).getGreen();
            totalBlue += colors.get(i).getBlue();
        }

        int avgRed = totalRed / colors.size();
        int avgGreen = totalGreen / colors.size();
        int avgBlue = totalBlue / colors.size();

        return new Color(avgRed, avgGreen, avgBlue);
    }

    public static double avgColor(Color c) {
        return (c.getRed() + c.getGreen() + c.getBlue()) / 3;
    }

    public static Color changeToClosest(Color color, ArrayList<Color> averages) {
        double closest = Integer.MAX_VALUE;
        int index = -1;
        int i = 0;
        for (Color average : averages) {
            if (Math.abs(avgColor(color) - avgColor(average)) < closest) {
                closest = Math.abs(avgColor(color) - avgColor(average));
                index = i;
            }
            i++;
        }
        return averages.get(index);
    }

    public static boolean isColorNear(Color existing, Color newColor, int threshold) {
        if (existing.getRed() + threshold > newColor.getRed() && existing.getRed() - threshold < newColor.getRed()) {
            if (existing.getGreen() + threshold > newColor.getGreen() && existing.getGreen() - threshold < newColor.getGreen()) {
                if (existing.getBlue() + threshold > newColor.getBlue() && existing.getBlue() - threshold < newColor.getBlue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Color mostCommonColor(ArrayList<Color> colors) {
        Map<Color, Integer> colorCounts = new HashMap<Color, Integer>();
        for (Color color : colors) {
            if (colorCounts.containsKey(color)) {
                colorCounts.put(color, colorCounts.get(color) + 1);
            } else {
                colorCounts.put(color, 1);
            }
        }
        int maxCount = 0;
        Color mostCommon = null;
        for (Map.Entry<Color, Integer> entry : colorCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommon = entry.getKey();
            }
        }
        return mostCommon;
    }
}
