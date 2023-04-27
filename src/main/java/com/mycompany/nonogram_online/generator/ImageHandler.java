/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
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

    public ImageHandler(BufferedImage image, int size) {
        this.image = image;
        this.size = size;
        pixelisedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    }

    public void pixeliseImage() {
        int width = image.getWidth();
        int height = image.getHeight();
        int stepX = width/size;
        int stepY = height/size;
        int xX=0;
        int yY=0;
        
        for (int i = 0; xX < size; i+=stepX) {
            for (int j = 0; yY < size; j+=stepY) {
                ArrayList<Color> colors = new ArrayList<>();
                for (int z = i; z < i+stepX; z++) {
                    for (int y = j; y < j+stepY; y++) {
                        Color color = new Color(image.getRGB(i, j));
                        colors.add(color);
                    }
                }
                Color c = getAverageRGB(colors);
                System.out.println(xX+", "+yY);
                pixelisedImage.setRGB(xX,yY, (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue());
                yY++;
            }
            xX++;
            yY=0;
        }
        
        File outputfile = new File("image.jpg");
        try {
            ImageIO.write(pixelisedImage, "jpg", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(ImageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    return new Color(avgRed,avgGreen,avgBlue);
}

}
