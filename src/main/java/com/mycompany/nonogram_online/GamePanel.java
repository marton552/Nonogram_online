/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online;

import com.mycompany.nonogram_online.level.Level;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author marton552
 */
public class GamePanel extends JPanel {

    private int width;
    private int height;
    Game game;
    MainFrame mf;
    Timer timer;
    private int animationTimer = 0;

    public GamePanel(MainFrame mf, Game game, int width, int height) {
        this.mf = mf;
        this.game = game;
        this.width = width;
        this.height = height;

        timer = new Timer(3, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (animationTimer > 0) {
                    animationTimer--;
                    if(game.lvl.getNewSquareSize() > game.lvl.getSquareSize()) game.lvl.setTrueSquareSize(game.lvl.getSquareSize() + 1);
                    else game.lvl.setTrueSquareSize(game.lvl.getSquareSize() - 1);
                    repaint();
                } else {
                    animationTimer = 0;
                    game.lvl.setTrueSquareSize(game.lvl.getNewSquareSize());
                    repaint();
                    timer.stop();
                }
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                game.clickedOn(e.getX(), e.getY());
                if (game.isFinished()) {
                    mf.finishLevel();
                }
                if (game.lvl.isLayerFinished(game.getActualLayer())) {
                    mf.finishLayer();
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        repaint();
        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.draw(g, width, height);
        if (game.lvl.isSquareSizeChanged()) {
            animationTimer = Math.abs(game.lvl.getNewSquareSize() - game.lvl.getSquareSize());
            timer.start();
        }
    }
}
