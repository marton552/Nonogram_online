
package com.nonogram_online.buttons;

import com.nonogram_online.Menu;
import com.nonogram_online.level.Level;
import com.nonogram_online.server.Server;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author marton552
 */
public class BasicRate extends JPanel {

    private int screenWidth;
    private final int screenHeight;
    private int width;
    private int height;
    private JPanel thisPanel;
    private int hgap = 0;
    private int stars;
    private int animationTimer = 0;
    private Timer timer;
    private Server server;

    public BasicRate(Menu m, Level lvl, int stars, int width, int height) {
        this.stars = stars;
        this.screenWidth = m.getWidth();
        this.screenHeight = m.getHeight();
        this.width = width;
        this.height = height;
        server = new Server();
        thisPanel = this;
        setVisible(true);
        setOrientation(width, height);
        
        timer = new Timer(100, (ActionEvent e) -> {
            if (animationTimer > 0) {
                animationTimer--;
                thisPanel.repaint();
            } else {
                animationTimer = 0;
                timer.stop();
                server.rateLevel(lvl.getName(), lvl.getCreator_name(), m.getUser().getFullUsername(),getStars());
                
                m.backToMenu(false);
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (stars == 0) {
                    if (e.getY() > 60 && e.getY() < 125) {
                        int s = e.getX() / (screenWidth / 5) + 1;
                        setStars(s);
                        repaint();
                        animationTimer = 10;
                        timer.start();
                    }
                }
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
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }

    public void drawStars(Graphics g) {
        for (int i = 0; i < 5; i++) {
            if (stars > i) {
                g.drawImage(new ImageIcon(this.getClass().getResource("/images/fullstar.png")).getImage(), (screenWidth / 5) * i, 50, (screenWidth / 5) - 10, (screenWidth / 5) - 10, null);
            } else {
                g.drawImage(new ImageIcon(this.getClass().getResource("/images/emptystar.png")).getImage(), (screenWidth / 5) * i, 50, (screenWidth / 5) - 10, (screenWidth / 5) - 10, null);
            }
        }
    }

    public void setHeight(int h) {
        this.hgap = h;
    }

    public void setOrientation(int width, int height) {
        this.width = (screenWidth - 15) / width;
        this.height = (screenHeight - 15) / height - hgap;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width, height);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/border.png")).getImage(), 0, 0, width, height, null);

        drawStars(g);

        FontRenderContext frc = new FontRenderContext(null, true, true);

        Font font = new Font("TimesRoman", Font.PLAIN, (height / 5));
        Rectangle2D r2D = font.getStringBounds("Értékeld a grafilogikát:", frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rX = (int) Math.round(r2D.getX());

        int a = (width / 2) - (rWidth / 2) - rX;

        g.setFont(font);
        g.drawString("Értékeld a grafilogikát:", a, 40);
    }

}
