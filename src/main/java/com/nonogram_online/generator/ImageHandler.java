
package com.nonogram_online.generator;

import com.nonogram_online.level.Level;
import com.nonogram_online.level.LevelEditor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author marton552
 */
public class ImageHandler {

    private final BufferedImage image;
    private final BufferedImage pixelisedImage;
    private final BufferedImage nonogramImage;
    private int firstSize;
    private final int secondSize;
    private int colorNum;
    private ArrayList<Color> resultColors;
    private String error = "";
    private ArrayList<ArrayList<Color>> selectionColors;
    private LevelEditor res;

    public ImageHandler(BufferedImage image, int firstSize, int secondSize, int colorNum) {
        this.image = image;
        int val = firstSize / secondSize;
        this.firstSize = val * secondSize;
        this.secondSize = secondSize;
        this.colorNum = colorNum;
        if (colorNum > 10) {
            this.colorNum = 10;
        }
        if (colorNum < 2) {
            this.colorNum = 2;
        }
        resultColors = new ArrayList<>();
        selectionColors = new ArrayList<>();
        if (this.firstSize < this.secondSize) {
            this.firstSize = this.secondSize;
        }
        pixelisedImage = new BufferedImage(this.firstSize, this.firstSize, BufferedImage.TYPE_INT_RGB);
        nonogramImage = new BufferedImage(secondSize, secondSize, BufferedImage.TYPE_INT_RGB);
    }

    public boolean createSelectionImage() {
        pixeliseImage();
        ArrayList<Color> selectionColors = collectColors();
        if (selectionColors.size() == colorNum) {
            resultColors = selectionColors;
            compressBySelection(selectionColors);
            return true;
        } else {
            return false;
        }
    }

    public boolean createAvgImage() {
        pixeliseImage();
        if (splitIntoNColor()) {
            File outputfile = new File("image.jpg");
            try {
                ImageIO.write(pixelisedImage, "jpg", outputfile);
            } catch (IOException ex) {
                Logger.getLogger(ImageHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            compressPixelImage();
            return true;
        } else {
            return false;
        }
    }

    public void compressBySelection(ArrayList<Color> selectionColors) {
        int width = pixelisedImage.getWidth();
        int height = pixelisedImage.getHeight();
        int stepX = width / secondSize;
        int stepY = height / secondSize;
        int xX = 0;
        int yY = 0;

        for (int i = 0; xX < secondSize; i += stepX) {
            for (int j = 0; yY < secondSize; j += stepY) {
                ArrayList<Color> colors = new ArrayList<>();
                for (int z = i; z < i + stepX; z++) {
                    for (int y = j; y < j + stepY; y++) {
                        Color color = new Color(pixelisedImage.getRGB(i, j));
                        colors.add(color);
                    }
                }
                Color c = ColorHandler.mostCommonColor(colors);
                c = ColorHandler.changeToClosest(c, selectionColors);
                nonogramImage.setRGB(xX, yY, (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue());
                yY++;
            }
            xX++;
            yY = 0;
        }
    }

    public void compressPixelImage() {
        int width = pixelisedImage.getWidth();
        int height = pixelisedImage.getHeight();
        int stepX = width / secondSize;
        int stepY = height / secondSize;
        int xX = 0;
        int yY = 0;

        for (int i = 0; xX < secondSize; i += stepX) {
            for (int j = 0; yY < secondSize; j += stepY) {
                ArrayList<Color> colors = new ArrayList<>();
                for (int z = i; z < i + stepX; z++) {
                    for (int y = j; y < j + stepY; y++) {
                        Color color = new Color(pixelisedImage.getRGB(i, j));
                        colors.add(color);
                    }
                }
                Color c = ColorHandler.mostCommonColor(colors);
                nonogramImage.setRGB(xX, yY, (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue());
                yY++;
            }
            xX++;
            yY = 0;
        }
    }

    public void pixeliseImage() {
        double width = image.getWidth();
        double height = image.getHeight();
        double stepX = width / firstSize;
        double stepY = height / firstSize;
        int xX = 0;
        int yY = 0;

        for (double i = 0; xX < firstSize; i += stepX) {
            for (double j = 0; yY < firstSize; j += stepY) {
                ArrayList<Color> colors = new ArrayList<>();
                for (double z = i; z < i + stepX; z++) {
                    for (double y = j; y < j + stepY; y++) {
                        Color color = new Color(image.getRGB((int) Math.floor(i), (int) Math.floor(j)));
                        colors.add(color);
                    }
                }
                Color c = ColorHandler.getAverageRGB(colors);
                pixelisedImage.setRGB(xX, yY, (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue());
                yY++;
            }
            xX++;
            yY = 0;
        }

    }

    public ArrayList<Color> collectColors() {
        int width = pixelisedImage.getWidth();
        int height = pixelisedImage.getHeight();
        int threshold = 2;
        selectionColors.add(new ArrayList<>());
        ArrayList<Color> pixelList = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixelList.add(new Color(pixelisedImage.getRGB(i, j)));
            }
        }
        while (pixelList.size() > colorNum) {
            selectionColors = new ArrayList<>();
            selectionColors.add(new ArrayList<>());
            for (int i = 0; i < pixelList.size(); i++) {
                boolean inList = false;
                boolean nearList = false;
                for (int colorList = 0; colorList < selectionColors.size(); colorList++) {
                    if (selectionColors.get(colorList).contains(pixelList.get(i))) {
                        inList = true;
                        selectionColors.get(colorList).add(pixelList.get(i));
                    }
                }
                if (!inList) {
                    for (int colorList = 0; colorList < selectionColors.size(); colorList++) {
                        if ((selectionColors.get(colorList).size()) > 0 && ColorHandler.isColorNear(selectionColors.get(colorList).get(0), pixelList.get(i), threshold)) {
                            nearList = true;
                            selectionColors.get(colorList).add(pixelList.get(i));
                        }
                    }
                    if (!nearList) {
                        ArrayList<Color> newC = new ArrayList<>();
                        newC.add(pixelList.get(i));
                        selectionColors.add(newC);
                    }
                }
            }
            pixelList = new ArrayList<>();
            for (ArrayList<Color> selectionColor : selectionColors) {
                if (selectionColor.size() > 0) {
                    pixelList.add(ColorHandler.mostCommonColor(selectionColor));
                }
            }
            threshold = threshold + 2;
        }
        return pixelList;
    }

    public boolean splitIntoNColor() {
        ArrayList<Color> allColor = new ArrayList<>();
        for (int i = 0; i < firstSize; i++) {
            for (int j = 0; j < firstSize; j++) {
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
            averages.add(ColorHandler.getAverageRGB(n.get(i)));
        }
        for (int i = 0; i < firstSize; i++) {
            for (int j = 0; j < firstSize; j++) {
                Color newColor = ColorHandler.changeToClosest(new Color(pixelisedImage.getRGB(i, j)), averages);
                if (!resultColors.contains(newColor)) {
                    resultColors.add(newColor);
                }
                pixelisedImage.setRGB(i, j, (newColor.getRed() << 16) | (newColor.getGreen() << 8) | newColor.getBlue());
            }
        }
        return true;
    }

    public ArrayList<ArrayList<Color>> splitByAvg(ArrayList<Color> colors, int splitInto) {
        double avg = ColorHandler.avgColor(ColorHandler.getAverageRGB(colors));
        ArrayList<ArrayList<Color>> res = new ArrayList<>();
        ArrayList<Color> part1 = new ArrayList<>();
        ArrayList<Color> part2 = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            if (ColorHandler.avgColor(colors.get(i)) < avg) {
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private void makeNonogramBlackAndWhite(int baw) {
        Color start = new Color(nonogramImage.getRGB(0, 0));
        for (int i = 0; i < nonogramImage.getWidth(); i++) {
            for (int j = 0; j < nonogramImage.getHeight(); j++) {
                Color c = new Color(nonogramImage.getRGB(i, j));
                if (baw == 1) {
                    if (c.equals(start)) {
                        nonogramImage.setRGB(i, j, ((Color.BLACK.getRed() << 16) | (Color.BLACK.getGreen() << 8) | Color.BLACK.getBlue()));
                    } else {
                        nonogramImage.setRGB(i, j, ((Color.WHITE.getRed() << 16) | (Color.WHITE.getGreen() << 8) | Color.WHITE.getBlue()));
                    }
                } else {
                    if (c.equals(start)) {
                        nonogramImage.setRGB(i, j, ((Color.WHITE.getRed() << 16) | (Color.WHITE.getGreen() << 8) | Color.WHITE.getBlue()));
                    } else {
                        nonogramImage.setRGB(i, j, ((Color.BLACK.getRed() << 16) | (Color.BLACK.getGreen() << 8) | Color.BLACK.getBlue()));
                    }
                }
            }
        }
    }

    public Level getImageAsLevel(int blackAndWhite, int layers, int backColor, int grid) {
        int gridSize = (grid == 1 ? 1 : grid * -1);
        int actualSize = (secondSize / (int) Math.sqrt(gridSize));
        int bw = 0;
        if (blackAndWhite != -1) {
            makeNonogramBlackAndWhite(blackAndWhite);
        }
        String error = "";
        do {
            String data = LevelEditor.templateData[0] + actualSize + ";" + (layers > 1 ? layers : grid) + LevelEditor.templateData[bw + 1];
            for (int i = 0; i < ((layers > 1 ? layers : gridSize) * actualSize * actualSize); i++) {
                data = data.concat(";0");
            }
            res = new LevelEditor(new ArrayList<>(Arrays.asList(data.split(";"))), actualSize, "", "", true);
            if (layers > 1 && blackAndWhite == -1) {
                res.addColor(Color.WHITE);
            }
            if (blackAndWhite == -1) {
                for (int i = 0; i < resultColors.size(); i++) {
                    res.addColor(resultColors.get(i));
                }
            }
            int notBackNum = 0;
            Random r = new Random();
            int gridLayer = 0;
            for (int g = 0; g < Math.sqrt(gridSize); g++) {
                for (int h = 0; h < Math.sqrt(gridSize); h++) {
                    for (int i = 0; i < actualSize; i++) {
                        for (int j = 0; j < actualSize; j++) {
                            int c = res.getColors().indexOf(new Color(nonogramImage.getRGB(i + (g * actualSize), j + (h * actualSize))));
                            res.setSelectedColor(c);
                            if (c != backColor) {
                                notBackNum++;
                            }

                            int low = 0;
                            int result = r.nextInt(layers - low) + low;
                            res.clickOnTileByExact(j, i, (res.isIsMultisized() ? gridLayer : result));
                        }
                    }
                    gridLayer++;
                }
            }
            if (notBackNum < layers) {
                error = "layer";
            }
            if (res.isIsMultisized()) {
                error = "multi";
            }
        } while (res.hasEmptyLayer(backColor) && error.equals(""));
        if (error.equals("layer")) {
            setError("Túl sok réteg enny pixelhez!");
            return null;
        }
        if (error.equals("multi") && res.hasEmptyLayer(backColor)) {
            setError("Nem minden darabra került pixel!");
        }
        return res;
    }

    public boolean save() {
        HashMap<Integer, Integer> closes = ColorHandler.findCloseColors(res.getColors());
        return closes.isEmpty();
    }

    public Level getFullLevel() {
        return res;
    }

    public void colorSum() {
        ArrayList<Integer> removes = new ArrayList<>();
        HashMap<Integer, Integer> closes;
        do {
            closes = ColorHandler.findCloseColors(res.getColors());
            boolean happenedRemove = false;
            for (int i = 0; i < res.getColors().size() && !happenedRemove; i++) {
                if (closes.get(i) != null) {
                    ArrayList<Color> cs = new ArrayList<>();
                    cs.add(res.getColors().get(i));
                    cs.add(res.getColors().get(closes.get(i)));
                    Color avgColor = ColorHandler.getAverageRGB(cs);
                    res.removeColor(closes.get(i), i);
                    res.setColor(i, avgColor);
                    removes.add(i);
                    happenedRemove = true;
                }
            }

        } while (!closes.isEmpty());
        res.afterAllRemoved(removes);
    }

    public void setBackgroundColor(Color backColor) {
        Color newBackColor = ColorHandler.changeToClosest(backColor, res.getColors());
        res.setColorBackGroundColor(newBackColor);
    }
}
