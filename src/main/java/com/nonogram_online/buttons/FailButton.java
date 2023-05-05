
package com.nonogram_online.buttons;

import com.nonogram_online.MainFrame;
import com.nonogram_online.Menu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author marton552
 */
public class FailButton extends JPanel {

    protected int screenWidth;
    protected int screenHeight;
    protected int width;
    protected int height;
    protected Menu m;
    protected MainFrame mf;
    protected JPanel thisPanel;
    protected int fontSize = 0;

    public FailButton(Menu m, MainFrame mf, int width, int height) {
        this.m = m;
        this.mf = mf;
        this.screenWidth = m.getWidth();
        this.screenHeight = m.getHeight();
        this.width = width;
        this.height = height;
        thisPanel = this;
        setVisible(true);
        setOrientation(1, 3);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getX() > 50 && e.getX() < 205 && e.getY() > 110 && e.getY() < 175){
                    m.backToMenu(false);
                }
                else if(e.getX() > 260 && e.getX() < 415 && e.getY() > 110 && e.getY() < 175){
                    mf.retry();
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

    public void setOrientation(int width, int height) {
        this.width = (screenWidth - 15) / width;
        this.height = (screenHeight - 40) / height;
    }

    protected void setFontSize() {
        fontSize = (int) (height / 7);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.drawRect(0, 0, width, height);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/button.png")).getImage(), 0, 0, width, height, null);
        
        int buttonWidth = width/3;
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/border.png")).getImage(), buttonWidth/3, height/2, buttonWidth, 60, null);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/border.png")).getImage(), width-buttonWidth-buttonWidth/3, height/2, buttonWidth, 60, null);

        FontRenderContext frc = new FontRenderContext(null, true, true);

        setFontSize();
        Font font = new Font("TimesRoman", Font.PLAIN, fontSize);
        Rectangle2D r2D = font.getStringBounds("Elbuktál!", frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rX = (int) Math.round(r2D.getX());

        int a = (width / 2) - (rWidth / 2) - rX;

        g.setFont(font);
        g.drawString("Elbuktál!", a, 80);
        g.drawString("Vissza", (buttonWidth/3)+buttonWidth/5, (int)(height/1.45));
        g.drawString("Újra", width-buttonWidth-buttonWidth/3+(int)(buttonWidth/3.5), (int)(height/1.45));

    }

}
