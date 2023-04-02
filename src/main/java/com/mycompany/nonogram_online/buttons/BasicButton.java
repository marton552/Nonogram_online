/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.buttons;

import com.mycompany.nonogram_online.MainFrame;
import com.mycompany.nonogram_online.Menu;
import com.mycompany.nonogram_online.level.Level;
import java.awt.BorderLayout;
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

    private int screenWidth;
    private int screenHeight;
    private int width;
    private int height;
    private Menu m;
    private boolean isFlashing = false;
    private int animationTimer = 0;
    private Timer timer;
    private JPanel thisPanel;
    private String text;
    private int hgap = 0;

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
                m.menuActions(text);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                flash();
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void flash() {
        if (!isFlashing) {
            isFlashing = true;
            animationTimer = width + (height) + 1;
            timer.start();
        }
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void setHeight(int h){
        this.hgap = h;
    }
    
    public void setOrientation(int width, int height){
        this.width = (screenWidth-15)/width;
        this.height = (screenHeight-15)/height-hgap;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.drawRect(0, 0, width, height);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/button.png")).getImage(), 0, 0, width, height, null);
        if (animationTimer > 0) {
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/flash.png")).getImage(), screenWidth - animationTimer, height / 20, height, height - (height / 10), null);
        }
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Font font = new Font("TimesRoman", Font.PLAIN, (int) (height / 5));
        Rectangle2D r2D = font.getStringBounds(text, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (width / 2) - (rWidth / 2) - rX;
        int b = (height / 2) - (rHeight / 2) - rY;

        g.setFont(font);
        g.drawString(text, a, b);
    }

}
