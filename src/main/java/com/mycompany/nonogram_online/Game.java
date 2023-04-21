/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online;

import com.mycompany.nonogram_online.level.Level;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author marton552
 */
public class Game {

    public Level lvl;
    private int actualLayer = 0;
    private int remainingHelp = 3;
    private int hp = 3;

    private boolean isEditing = false;

    public Game(Level lvl, boolean isEditing) {
        this.lvl = lvl;
        lvl.newLvl(isEditing);
        this.isEditing = isEditing;
    }

    public void clickedOn(int x, int y) {
        if (hp > 0) {
            lvl.clickOnTile(x, y, actualLayer);
            if (lvl.testTile(x, y, actualLayer)) {
                hp--;
            }
            if (isFinished()) {
                System.out.println(isFinished());
            }
        }
    }

    public boolean isFinished() {
        return lvl.isFinished();
    }

    public void winGame(Graphics g) {
        for (int i = 0; i < getLayerCount(); i++) {
            lvl.drawMatrix(g, i, true, true);
        }
    }

    public int getLayerCount() {
        return lvl.getLayerCount();
    }

    public int getActualLayer() {
        return actualLayer;
    }

    public void setActualLayer(int actualLayer) {
        this.actualLayer = actualLayer;
    }

    public void setLayer(int change) {
        actualLayer += change;
    }

    public int getRemainingHelp() {
        return remainingHelp;
    }

    public void useRemainingHelp() {
        if (hp > 0) {
            this.remainingHelp--;
            lvl.randomHint();
        }
    }

    public void draw(Graphics g, int width, int height) {
        lvl.setSquareSize(width, height);
        if (isFinished()) {
            winGame(g);
        } else {
            lvl.drawNumbers(g, getActualLayer());
            if (lvl.isIsMultisized()) {
                lvl.drawMatrix(g, actualLayer, true, true);
            } else {
                for (int i = 0; i < getLayerCount(); i++) {
                    lvl.drawMatrix(g, i, true, (i == actualLayer));
                }
            }
        }
        if (!isEditing) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (width / 20)));
            String title = "Hátralévő próbálkozások: " + hp;
            g.drawChars(title.toCharArray(), 0, title.toCharArray().length, 10, lvl.getSquareSize() * (lvl.getHeight() + 3));
        }
    }

    public void addLayer() {
        lvl.addLayer();
    }
}
