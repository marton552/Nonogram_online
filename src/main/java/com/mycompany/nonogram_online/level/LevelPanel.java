/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.level;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author marton552
 */
public class LevelPanel extends JPanel {

    Level lvl;
    int widthHeight;

    public LevelPanel(Level lvl, int widthHeight) {
        this.lvl = lvl;
        this.widthHeight = widthHeight;
    }
    
    public void addLevel(Level lvl){
        this.lvl = lvl;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (lvl != null) {
            lvl.setMatrixStartPos(100, 0);
            for (int i = 0; i < lvl.getMatrix().size(); i++) {
                lvl.setMenuSquareSize(widthHeight - 20);
                lvl.finishGame();
                lvl.drawMatrix(g, i, false, true);
            }
        }
    }
}
