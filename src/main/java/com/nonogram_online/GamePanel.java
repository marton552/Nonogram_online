
package com.nonogram_online;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author marton552
 */
public class GamePanel extends JPanel {

    private final int width;
    private final int height;
    Game game;
    MainFrame mf;
    Timer timer;
    private int animationTimer = 0;

    public GamePanel(MainFrame mf, Game game, int width, int height) {
        this.mf = mf;
        this.game = game;
        this.width = width;
        this.height = height;

        timer = new Timer(3, (ActionEvent e) -> {
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
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getY() > mf.getMenu().getHeight()-170-50 && e.getX() > mf.getMenu().getWidth()-70){
                    if(game.isIsZoomed())game.setIsZoomed(false);
                    else game.setIsZoomed(true);
                }
                game.clickedOn(e.getX(), e.getY());
                if (game.isFinished()) {
                    mf.finishLevel();
                }
                if (game.lvl.isLayerFinished(game.getActualLayer())) {
                    mf.finishLayer();
                }
                if(game.getHp() == 0){
                    mf.loseGame();
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
        if (!game.isIsEditing()) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.PLAIN, (width / 20)));
            String title = "Hátralévő próbálkozások: " + game.getHp();
            g.drawChars(title.toCharArray(), 0, title.toCharArray().length, 10, mf.getMenu().getHeight()-170);
            
        }
        if(game.isIsZoomed()) g.drawImage(new ImageIcon(this.getClass().getResource("/images/zoom_out.png")).getImage(), mf.getMenu().getWidth()-70, mf.getMenu().getHeight()-170-50, 50,50,null);
        else g.drawImage(new ImageIcon(this.getClass().getResource("/images/zoom_in.png")).getImage(), mf.getMenu().getWidth()-70, mf.getMenu().getHeight()-170-50, 50,50,null);
    }
}
