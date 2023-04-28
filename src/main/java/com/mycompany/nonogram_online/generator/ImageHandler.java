/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.generator;

import com.mycompany.nonogram_online.level.Level;
import com.mycompany.nonogram_online.level.LevelEditor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author marton552
 */
public class ImageHandler {

    private BufferedImage image;
    private BufferedImage pixelisedImage;
    private int size;
    private int colorNum;
    private ArrayList<Color> resultColors;

    public ImageHandler(BufferedImage image, int size, int colorNum) {
        this.image = image;
        this.size = size;
        this.colorNum = colorNum;
        if (colorNum > 10) {
            colorNum = 10;
        }
        if(colorNum < 2){
            colorNum = 1;
        }
        resultColors = new ArrayList<>();
        pixelisedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    }

    public boolean createImage() {
        pixeliseImage();
        if (splitIntoNColor()) {
            return true;
        } else {
            return false;
        }
    }

    public void pixeliseImage() {
        int width = image.getWidth();
        int height = image.getHeight();
        int stepX = width / size;
        int stepY = height / size;
        int xX = 0;
        int yY = 0;

        for (int i = 0; xX < size; i += stepX) {
            for (int j = 0; yY < size; j += stepY) {
                ArrayList<Color> colors = new ArrayList<>();
                for (int z = i; z < i + stepX; z++) {
                    for (int y = j; y < j + stepY; y++) {
                        Color color = new Color(image.getRGB(i, j));
                        colors.add(color);
                    }
                }
                Color c = getAverageRGB(colors);
                pixelisedImage.setRGB(xX, yY, (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue());
                yY++;
            }
            xX++;
            yY = 0;
        }

    }

    public boolean splitIntoNColor() {
        ArrayList<Color> allColor = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                allColor.add(new Color(pixelisedImage.getRGB(i, j)));
            }
        }
        ArrayList<ArrayList<Color>> n = splitByAvg(allColor, colorNum);
        for (int i = 0; i < n.size(); i++) {
            if (n.get(i).size() == 0) {
                return false;
            }
        }
        ArrayList<Color> averages = new ArrayList<>();
        for (int i = 0; i < n.size(); i++) {
            averages.add(getAverageRGB(n.get(i)));
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Color newColor = changeToClosest(new Color(pixelisedImage.getRGB(i, j)), averages);
                if (!resultColors.contains(newColor)) {
                    resultColors.add(newColor);
                }
                pixelisedImage.setRGB(i, j, (newColor.getRed() << 16) | (newColor.getGreen() << 8) | newColor.getBlue());
            }
        }
        System.out.println(resultColors.size());
        return true;
    }

    public Color changeToClosest(Color color, ArrayList<Color> averages) {
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

    public ArrayList<ArrayList<Color>> splitByAvg(ArrayList<Color> colors, int splitInto) {
        double avg = avgColor(getAverageRGB(colors));
        ArrayList<ArrayList<Color>> res = new ArrayList<>();
        ArrayList<Color> part1 = new ArrayList<>();
        ArrayList<Color> part2 = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            if (avgColor(colors.get(i)) < avg) {
                part1.add(colors.get(i));
            } else {
                part2.add(colors.get(i));
            }
        }
        if (splitInto == 2) {
            res.add(part1);
            res.add(part2);
        } else if (splitInto == 3) {
            ArrayList<ArrayList<Color>> resPart;
            if (part1.size() > part2.size()) {
                resPart = splitByAvg(part1, 2);
                res.add(resPart.get(0));
                res.add(resPart.get(1));
                res.add(part2);
            } else {
                resPart = splitByAvg(part2, 2);
                res.add(part1);
                res.add(resPart.get(0));
                res.add(resPart.get(1));
            }
        } else if (splitInto >= 4 && splitInto < 8) {
            ArrayList<ArrayList<Color>> resPart1 = splitByAvg(part1, 2);
            ArrayList<ArrayList<Color>> resPart2 = splitByAvg(part2, 2);
            res.add(resPart1.get(0));
            res.add(resPart1.get(1));
            res.add(resPart2.get(0));
            res.add(resPart2.get(1));
            int moreThan4 = splitInto - 4;
            res = order4bySize(res);
            for (int i = 0; i < moreThan4; i++) {
                ArrayList<ArrayList<Color>> resPart = splitByAvg(res.get(i), 2);
                res.add(resPart.get(0));
                res.add(resPart.get(1));
            }
        } else if (splitInto >= 8) {
            int firstNum = 4;
            int secondNum = 4;
            if (splitInto == 9) {
                firstNum = 5;
            } else if (splitInto == 10) {
                secondNum = 5;
            }
            ArrayList<ArrayList<Color>> resPart1 = splitByAvg(part1, firstNum);
            ArrayList<ArrayList<Color>> resPart2 = splitByAvg(part2, secondNum);
            for (int i = 0; i < firstNum; i++) {
                res.add(resPart1.get(i));
            }
            for (int i = 0; i < secondNum; i++) {
                res.add(resPart2.get(i));
            }
        }
        //amelyikbe több van azt tovább splitejük páratlan/nem négyzetszám esetén
        return res;
    }

    private ArrayList<ArrayList<Color>> order4bySize(ArrayList<ArrayList<Color>> allTheLists) {
        Collections.sort(allTheLists, new Comparator<ArrayList>() {
            public int compare(ArrayList a1, ArrayList a2) {
                return a2.size() - a1.size();
            }
        });
        return allTheLists;
    }

    private double avgColor(Color c) {
        return (c.getRed() + c.getGreen() + c.getBlue()) / 3;
    }

    public Color getAverageRGB(ArrayList<Color> colors) {
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

    public Level getImageAsLevel(int blackAndWhite) {
        if(blackAndWhite == 0){
            resultColors.add(Color.WHITE);
            resultColors.add(Color.BLACK);
        }
        else if(blackAndWhite == 1){
            resultColors.add(Color.BLACK);
            resultColors.add(Color.WHITE);
        }
        String data = LevelEditor.templateData[0] + size + ";1" + LevelEditor.templateData[1];
        for (int i = 0; i < ((Integer) size * (Integer) size); i++) {
            data += ";0";
        }
        LevelEditor res = new LevelEditor(new ArrayList<String>(Arrays.asList(data.split(";"))),size, "", "", true);
        for (int i = 0; i < resultColors.size(); i++) {
            res.addColor(resultColors.get(i));
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int c = res.getColors().indexOf(new Color(pixelisedImage.getRGB(i, j)));
                if(blackAndWhite == 0) res.setSelectedColor(c+2);
                else if(blackAndWhite == 1) res.setSelectedColor(c+2);
                else res.setSelectedColor(c);
                res.clickOnTile(j, i);
            }
        }
        return res;
    }

}
