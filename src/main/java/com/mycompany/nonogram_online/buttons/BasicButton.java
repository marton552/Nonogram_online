/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.buttons;

import com.mycompany.nonogram_online.Menu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class BasicButton extends JPanel {

    protected int screenWidth;
    protected int screenHeight;
    protected int width;
    protected int height;
    protected Menu m;
    protected boolean isFlashing = false;
    protected int animationTimer = 0;
    protected Timer timer;
    protected JPanel thisPanel;
    protected String text;
    protected int hgap = 0;
    protected boolean enabled = true;
    protected int clicked = 0;
    protected int fontSize = 0;
    protected boolean draw = true;

    public BasicButton(Menu m, String text, int width, int height) {
        this.m = m;
        this.text = text;
        this.screenWidth = m.getWidth();
        this.screenHeight = m.getHeight();
        this.width = width;
        this.height = height;
        thisPanel = this;
        setVisible(true);

        timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (animationTimer > 0) {
                    animationTimer -= 20;
                    thisPanel.repaint();
                } else {
                    animationTimer = 0;
                    isFlashing = false;
                    timer.stop();
                }
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (enabled) {
                    click();
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
                if (enabled) {
                    flash();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void click() {
        m.menuActions(text);
    }

    public int getClicked() {
        return clicked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        repaint();
    }

    public void flash() {
        if (!isFlashing) {
            isFlashing = true;
            animationTimer = width + (height) + 1;
            timer.start();
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHeight(int h) {
        this.hgap = h;
    }

    public void setOrientation(int width, int height) {
        this.width = (screenWidth - 15) / width;
        this.height = (screenHeight - 40) / height - hgap;
    }
    
    public void setHeightManualy(int height){
        this.screenHeight = height+40;
    }

    protected void setFontSize() {
        fontSize = (int) (width / 12);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (draw) {
            g.setColor(Color.gray);
            g.drawRect(0, 0, width, height);
            if (enabled) {
                if (clicked == 0) {
                    g.drawImage(new ImageIcon(this.getClass().getResource("/images/button.png")).getImage(), 0, 0, width, height, null);
                } else if (clicked == 10) {
                    g.drawImage(new ImageIcon(this.getClass().getResource("/images/button_filter.png")).getImage(), 0, 0, width, height, null);
                } else {
                    g.drawImage(new ImageIcon(this.getClass().getResource("/images/button_clicked.png")).getImage(), 0, 0, width, height, null);
                }
            } else {
                g.drawImage(new ImageIcon(this.getClass().getResource("/images/button_disabled.png")).getImage(), 0, 0, width, height, null);
            }
            if (animationTimer > 0) {
                g.drawImage(new ImageIcon(this.getClass().getResource("/images/flash.png")).getImage(), screenWidth - animationTimer, height / 20, height, height - (height / 10), null);
            }
            FontRenderContext frc = new FontRenderContext(null, true, true);

            setFontSize();
            String newText = text;
            if (clicked == 1) {
                newText += " ▼";
            } else if (clicked == 2) {
                newText += " ▲";
            } else if (clicked == 10) {
                newText += " ☇";
            }
            Font font = new Font("TimesRoman", Font.PLAIN, fontSize);
            Rectangle2D r2D = font.getStringBounds(newText, frc);
            int rWidth = (int) Math.round(r2D.getWidth());
            int rHeight = (int) Math.round(r2D.getHeight());
            int rX = (int) Math.round(r2D.getX());
            int rY = (int) Math.round(r2D.getY());

            int a = (width / 2) - (rWidth / 2) - rX;
            int b = (height / 2) - (rHeight / 2) - rY;

            g.setFont(font);
            g.drawString(newText, a, b);
        }
    }
}
