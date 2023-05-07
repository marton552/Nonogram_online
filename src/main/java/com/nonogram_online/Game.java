
package com.nonogram_online;

import com.nonogram_online.level.Level;
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
    private boolean isZoomed = false;

    private boolean isEditing;

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
        }
    }

    public boolean isIsZoomed() {
        return isZoomed;
    }

    public void setIsZoomed(boolean isZoomed) {
        this.isZoomed = isZoomed;
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
        if (hp > 0 && !lvl.isFinished()) {
            this.remainingHelp--;
            lvl.randomHint();
        }
    }

    public int getHp() {
        return hp;
    }

    public boolean isIsEditing() {
        return isEditing;
    }

    public void draw(Graphics g, int width, int height) {
        lvl.setSquareSize(width, height, isZoomed);
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
    }

    public void addLayer() {
        lvl.addLayer();
    }

    void retry() {
        hp = 3;
        remainingHelp = 3;
        lvl.retry();
    }
}
