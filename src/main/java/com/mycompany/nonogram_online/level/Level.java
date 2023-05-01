/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.level;

import com.mycompany.nonogram_online.generator.Nonogram;
import com.mycompany.nonogram_online.user.User;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author marton552
 */
public class Level {

    public static java.util.logging.Level SEVERE;

    private ArrayList<String> allData;
    protected ArrayList<LevelMatrix> matrix;
    protected ArrayList<Color> colors;
    protected String name;
    protected double stars;
    protected String creator_name;
    protected String created_date;
    protected boolean approved;

    protected int hanyszorhany;
    protected int matrixStartPosX = 0;
    protected int matrixStartPosY = 30;
    protected int numbersStartPosX = 0;
    protected int numbersStartPosY = 30;
    protected int squareSize = 50;
    protected int newSquareSize = 50;

    protected int matrixMostLeftNumbers;
    protected int matrixMostTopNumbers;
    protected int matrixMostMostNumbers;

    private boolean isEditing = false;
    private boolean isMultisized = false;
    private ArrayList<Integer> completedParts;

    protected int selectedColor = 1;

    public Level(ArrayList<String> allData, String creator_name, String created_date, boolean approved) {
        this.allData = allData;
        completedParts = new ArrayList<>();
        name = allData.get(0);
        this.creator_name = creator_name;
        this.created_date = created_date;
        this.approved = approved;
        matrix = new ArrayList<>();
        colors = new ArrayList<>();
        this.hanyszorhany = parseInt(allData.get(1));
        int layerNum = parseInt(allData.get(2));
        if (layerNum < 0) {
            isMultisized = true;
            layerNum = layerNum * -1;
        }
        int colorNum = parseInt(allData.get(3));
        for (int i = 4; i < 4 + colorNum; i++) {
            String[] c = (allData.get(i).replace("rgb(", "").replace(")", "")).split(",");
            ArrayList<String> cs = new ArrayList<>();
            for (String url : c) {
                cs.add(url);
            }
            colors.add(new Color(parseInt(cs.get(0)), parseInt(cs.get(1)), parseInt(cs.get(2))));
            cs.clear();
        }
        for (int z = 0; z < layerNum; z++) {
            ArrayList<Integer> m = new ArrayList<>();
            for (int i = 4 + colorNum + ((z) * hanyszorhany * hanyszorhany); i < 4 + colorNum + ((z + 1) * hanyszorhany * hanyszorhany); i++) {
                m.add(parseInt(allData.get(i)));
            }
            matrix.add(new LevelMatrix(m, hanyszorhany));
        }
        setMatrixMostLeftNumbers();
        setMatrixMostTopNumbers();
    }

    public ArrayList<String> getAllData() {
        return allData;
    }

    public boolean isThisPartCompleted(Integer n) {
        return completedParts.contains(n);
    }

    public void addCompletedPart(Integer n) {
        completedParts.add(n);
    }

    public ArrayList<LevelMatrix> getMatrix() {
        return matrix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStars() {
        return stars;
    }

    public void setColorBackGroundColor(Color back) {
        int index = getColors().indexOf(back);
        Color tmp = getColors().get(0);
        colors.set(0, back);
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    if (matrix.get(i).getTileBy(j, k) == 0) {
                        matrix.get(i).addTileBy(j, k, 100);
                    }
                }
            }
        }
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    if (matrix.get(i).getTileBy(j, k) == index) {
                        matrix.get(i).addTileBy(j, k, 0);
                    }
                }
            }
        }
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    if (matrix.get(i).getTileBy(j, k) == 100) {
                        matrix.get(i).addTileBy(j, k, index);
                    }
                }
            }
        }
        colors.set(index, tmp);
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public void setColor(int num, Color c) {
        colors.set(num, c);
    }

    public void removeColor(int num, int newColor) {
        colors.remove(num);
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    if (matrix.get(i).getTileBy(j, k) == num) {
                        matrix.get(i).addTileBy(j, k, newColor);
                    }
                }
            }
        }
    }

    public void afterAllRemoved(int removedNum) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    if (matrix.get(i).getTileBy(j, k) >= colors.size()) {
                        matrix.get(i).addTileBy(j, k, matrix.get(i).getTileBy(j, k) - removedNum);
                    }
                }
            }
        }
    }

    public void addColor(Color c) {
        colors.add(c);
    }

    public String getCreator_name() {
        return creator_name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isIsMultisized() {
        return isMultisized;
    }

    public void removeColor(Color c) {
        int pos = 0;
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).equals(c)) {
                pos = i;
            }
        }
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    if (matrix.get(i).getTileBy(j, k) == pos) {
                        matrix.get(i).resetTileBy(j, k);
                    }
                }
            }
        }
        colors.remove(c);
    }

    public int getHeight() {
        int r = matrixMostMostNumbers > matrixMostTopNumbers ? matrixMostMostNumbers : matrixMostTopNumbers;
        return r + hanyszorhany;
    }

    public void setSquareSize(int width, int height, boolean zoomed) {
        int x = 2;
        if(zoomed) x = 1;
        matrixMostMostNumbers = matrixMostLeftNumbers > matrixMostTopNumbers ? matrixMostLeftNumbers : matrixMostTopNumbers;
        newSquareSize = width / (matrixMostMostNumbers * x + hanyszorhany);
    }

    public boolean isSquareSizeChanged() {
        return newSquareSize != squareSize;
    }

    public void setTrueSquareSize(int size) {
        squareSize = size;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public int getNewSquareSize() {
        return newSquareSize;
    }

    public void setMenuSquareSize(int width) {
        squareSize = width / hanyszorhany;
    }

    public void setMatrixStartPos(int x, int y) {
        matrixStartPosX = x;
        matrixStartPosY = y;
    }

    public boolean isSolvable() {
        for (int i = 0; i < matrix.size(); i++) {
            System.out.println(matrix.get(i).getTopOnlyNumbers());
            System.out.println(matrix.get(i).getLeftOnlyNumbers());
            Nonogram n = new Nonogram(matrix.get(i).getTopOnlyNumbers(), matrix.get(i).getLeftOnlyNumbers());
            if (!n.solveAndCheck()) {
                return false;
            }
        }
        return true;
    }

    public void setMatrixMostLeftNumbers() {
        int max = 0;
        for (int i = 0; i < matrix.size(); i++) {
            int actual = matrix.get(i).mostLeftNumber();
            if (actual > max) {
                max = actual;
            }
        }
        matrixMostLeftNumbers = max;
    }

    public void setMatrixMostTopNumbers() {
        int max = 0;
        for (int i = 0; i < matrix.size(); i++) {
            int actual = matrix.get(i).mostTopNumber();
            if (actual > max) {
                max = actual;
            }
        }
        matrixMostTopNumbers = max;
    }

    public void clickOnTile(int x, int y, int layer) {
        int posX = (x - matrixStartPosX) / squareSize;
        int posY = (y - matrixStartPosY) / squareSize;
        if (posX >= 0 && posX < hanyszorhany && posY >= 0 && posY < hanyszorhany) {
            matrix.get(layer).setTileBy(posX, posY, selectedColor);
        }
    }

    public boolean testTile(int x, int y, int layer) {
        int posX = (x - matrixStartPosX) / squareSize;
        int posY = (y - matrixStartPosY) / squareSize;
        if (posX >= 0 && posX < hanyszorhany && posY >= 0 && posY < hanyszorhany) {
            return matrix.get(layer).getTileByIsFailed(posY, posX);
        }
        return false;
    }

    public void drawMatrix(Graphics g, int layer, boolean isInGame, boolean isFocusedLayer) {
        matrixStartPosX = isInGame ? matrixMostMostNumbers * squareSize : matrixStartPosX;
        matrixStartPosY = isInGame ? matrixMostMostNumbers * squareSize + 30 : matrixStartPosY;
        g.setColor(colors.get(0));
        g.fillRect(matrixStartPosX, matrixStartPosY, squareSize * hanyszorhany, squareSize * hanyszorhany);
        for (int i = 0; i < hanyszorhany; i++) {
            for (int k = 0; k < hanyszorhany; k++) {
                Color color = matrix.get(layer).getTileByIsDone(i, k) ? colors.get(matrix.get(layer).getTileBy(i, k)) : colors.get(0);
                Color color2 = new Color((color.getRed()) / 255.0f, (color.getGreen()) / 255.0f, (color.getBlue()) / 255.0f, (isFocusedLayer ? 1.0f : 0.2f));
                g.setColor(color2);
                if (matrix.get(layer).getTileByIsDone(i, k)) {
                    g.fillRect(matrixStartPosX + (k * squareSize), matrixStartPosY + (i * squareSize), squareSize, squareSize);
                }
                Color invertColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                if (isInGame) {
                    g.setColor(invertColor);
                } else {
                    g.setColor(Color.BLACK);
                }
                if (!isFinished() || !isInGame) {
                    g.drawRect(matrixStartPosX + (k * squareSize), matrixStartPosY + (i * squareSize), squareSize, squareSize);
                }
                if (matrix.get(layer).getTileByIsFailed(i, k) && !isFinished()) {
                    g.setColor(invertColor);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (squareSize * 1.3)));
                    g.drawChars("X".toCharArray(), 0, 1, matrixStartPosX + (k * squareSize) + (squareSize / 10), matrixStartPosY + ((i + 1) * squareSize));
                }
                if (matrix.get(layer).getTileByIsFlagged(i, k) && !isFinished()) {
                    g.drawImage(new ImageIcon(this.getClass().getResource("/images/flag.png")).getImage(), matrixStartPosX + (k * squareSize) + (squareSize / 10), matrixStartPosY + ((i) * (squareSize)) + (int) (squareSize * 0.1), (int) (squareSize * 0.9), (int) (squareSize * 0.9), null);
                }
            }
        }
        if (!isFinished()) {
            for (int i = 0; i < hanyszorhany / 5 - (isInGame ? 1 : 0); i++) {
                g.setColor(Color.BLACK);
                g.fillRect(matrixStartPosX + (((i + 1) * 5) * squareSize) - 1, matrixStartPosY, 3, (hanyszorhany * squareSize));
                g.fillRect(matrixStartPosX, matrixStartPosY + (((i + 1) * 5) * squareSize) - 1, (hanyszorhany * squareSize), 3);
            }
        }
    }

    public void drawSPMatrix(Graphics g, int actSize) {
        g.setColor(Color.BLACK);
        g.drawRect(matrixStartPosX, matrixStartPosY, squareSize * actSize, squareSize * actSize);
        for (int i = 0; i < hanyszorhany; i++) {
            for (int k = 0; k < hanyszorhany; k++) {
                Color color = matrix.get(0).getTileByIsDone(i, k) ? colors.get(matrix.get(0).getTileBy(i, k)) : colors.get(0);
                Color color2 = new Color((color.getRed()) / 255.0f, (color.getGreen()) / 255.0f, (color.getBlue()) / 255.0f, (1.0f));
                g.setColor(color2);
                if (matrix.get(0).getTileByIsDone(i, k)) {
                    g.fillRect(matrixStartPosX + (k * squareSize), matrixStartPosY + (i * squareSize), squareSize, squareSize);
                }
            }
        }
    }

    public void drawNumbers(Graphics g, int layer) {
        for (int i = 0; i < hanyszorhany; i++) {
            for (int k = 0; k < matrix.get(layer).getLeftNumbers().get(i).size(); k++) {
                int shiftLeft = (matrixMostMostNumbers - matrix.get(layer).getLeftNumbers().get(i).size()) * squareSize;
                Color color = colors.get(matrix.get(layer).getLeftNumbers().get(i).get(k).getColor());
                float alpha = matrix.get(layer).getLeftNumbers().get(i).get(k).isSolved() ? 0.4f : 1;
                if (isEditing) {
                    alpha = 1;
                }
                Color invertColor = new Color((255 - color.getRed()) / 255.0f, (255 - color.getGreen()) / 255.0f, (255 - color.getBlue()) / 255.0f, alpha);
                g.setColor(color);
                g.fillRect(shiftLeft + numbersStartPosX + (k * squareSize), numbersStartPosY + matrixMostMostNumbers * squareSize + (i * squareSize), squareSize, squareSize);
                g.setColor(invertColor);
                g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (squareSize / 1.5)));
                char[] text = ("" + matrix.get(layer).getLeftNumbers().get(i).get(k).getNum()).toCharArray();
                g.drawChars(text, 0, text.length, (text.length > 1 ? (int) (-1 * squareSize / 4) : 0) + shiftLeft + numbersStartPosX + (k * squareSize) + (squareSize / 3), numbersStartPosY + matrixMostMostNumbers * squareSize + (i * squareSize) + (int) (squareSize / 1.3));
                if (matrix.get(layer).getLeftNumbers().get(i).get(k).getColor() == selectedColor) {
                    g.setColor(invertColor);
                    g.drawRect(shiftLeft + numbersStartPosX + (k * squareSize), numbersStartPosY + matrixMostMostNumbers * squareSize + (i * squareSize), squareSize, squareSize);
                }
            }
        }
        for (int i = 0; i < hanyszorhany; i++) {
            for (int k = 0; k < matrix.get(layer).getTopNumbers().get(i).size(); k++) {
                int shiftTop = (matrixMostMostNumbers - matrix.get(layer).getTopNumbers().get(i).size()) * squareSize;
                Color color = colors.get(matrix.get(layer).getTopNumbers().get(i).get(k).getColor());
                float alpha = matrix.get(layer).getTopNumbers().get(i).get(k).isSolved() ? 0.4f : 1;
                if (isEditing) {
                    alpha = 1;
                }
                Color invertColor = new Color((255 - color.getRed()) / 255.0f, (255 - color.getGreen()) / 255.0f, (255 - color.getBlue()) / 255.0f, alpha);
                g.setColor(color);
                g.fillRect(numbersStartPosX + (i * squareSize) + matrixMostMostNumbers * squareSize, shiftTop + numbersStartPosY + (k * squareSize), squareSize, squareSize);
                g.setColor(invertColor);
                g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (squareSize / 1.5)));
                char[] text = ("" + matrix.get(layer).getTopNumbers().get(i).get(k).getNum()).toCharArray();
                g.drawChars(text, 0, text.length, (text.length > 1 ? (int) (-1 * squareSize / 4) : 0) + numbersStartPosX + matrixMostMostNumbers * squareSize + (i * squareSize) + (squareSize / 3), shiftTop + numbersStartPosY + (k * squareSize) + (int) (squareSize / 1.3));
                if (matrix.get(layer).getTopNumbers().get(i).get(k).getColor() == selectedColor) {
                    g.setColor(invertColor);
                    g.drawRect(numbersStartPosX + (i * squareSize) + matrixMostMostNumbers * squareSize, shiftTop + numbersStartPosY + (k * squareSize), squareSize, squareSize);
                }
            }
        }
    }

    public boolean isLayerFinished(int layer) {
        for (int j = 0; j < matrix.get(layer).getLeftNumbers().size(); j++) {
            for (int k = 0; k < matrix.get(layer).getLeftNumbers().get(j).size(); k++) {
                if (!matrix.get(layer).getLeftNumbers().get(j).get(k).isSolved()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isFinished() {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).getLeftNumbers().size(); j++) {
                for (int k = 0; k < matrix.get(i).getLeftNumbers().get(j).size(); k++) {
                    if (!matrix.get(i).getLeftNumbers().get(j).get(k).isSolved()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void finishGame() {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    if (matrix.get(i).getTileBy(j, k) != 0 && !matrix.get(i).getTileByIsDone(j, k)) {
                        int color = matrix.get(i).getTileBy(j, k);
                        matrix.get(i).setTileBy(k, j, color);
                    }
                }
            }
        }
    }

    public void newLvl(boolean isEditing) {
        this.isEditing = isEditing;
        for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < hanyszorhany; j++) {
                    for (int k = 0; k < hanyszorhany; k++) {
                        matrix.get(i).clearTileBy(j, k);
                    }
                }
            }
        if (isEditing) {
            for (int l = 0; l < matrix.size(); l++) {
                for (int i = 0; i < hanyszorhany; i++) {
                    for (int j = 0; j < hanyszorhany; j++) {
                        if (matrix.get(l).getTileBy(i, j) != 0) {
                            matrix.get(l).addTileBy(i, j, matrix.get(l).getTileBy(i, j));
                        }
                    }
                }
            }
        }
    }

    public void randomHint() {
        Random r = new Random();
        int rnd = r.nextInt((hanyszorhany * hanyszorhany));
        while (rnd > 0) {
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < hanyszorhany; j++) {
                    for (int k = 0; k < hanyszorhany; k++) {
                        if (matrix.get(i).getTileBy(j, k) != 0 && !matrix.get(i).getTileByIsDone(j, k)) {
                            rnd--;
                            if (rnd == 0) {
                                int color = matrix.get(i).getTileBy(j, k);
                                matrix.get(i).setTileBy(k, j, color);
                            }
                        }
                    }
                }
            }
        }
    }

    public String export() {
        String saveData = getName();
        saveData += ";" + hanyszorhany;
        saveData += ";" + (isIsMultisized() ? matrix.size() * -1 : matrix.size());
        saveData += ";" + colors.size();
        for (int i = 0; i < colors.size(); i++) {
            saveData += ";rgb(" + colors.get(i).getRed() + "," + colors.get(i).getGreen() + "," + colors.get(i).getBlue() + ")";
        }
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    saveData += ";" + matrix.get(i).getTileBy(j, k);
                }
            }
        }
        System.out.println(saveData);
        return saveData;
    }

    public int getLayerCount() {
        return matrix.size();
    }

    public void addLayer() {
    }

    public void setSelectedColor(int c) {
        selectedColor = c;
    }

    public void save(User user) {
    }

    public void retry() {
        newLvl(false);
    }

    public boolean hasEmptyLayer(int backColor) {
        for (LevelMatrix levelMatrix : matrix) {
            if (levelMatrix.isLayerEmpty(backColor)) {
                return true;
            }
        }
        return false;
    }
}
