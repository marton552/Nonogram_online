/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.generator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author marton552
 */
public class ColorHandler {
    
    public static boolean are2ColorsClose(Color one, Color two){
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
    
    public static HashMap<Integer, Integer> findCloseColors(ArrayList<Color> colors, Color c){
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
            for (int j = i+1; j < colors.size(); j++) {
                if (are2ColorsClose(colors.get(i), colors.get(j))) {
                    closePairs.put(i, j);
                }
            }
        }
        System.out.println(closePairs);
        return closePairs;
    }
}
