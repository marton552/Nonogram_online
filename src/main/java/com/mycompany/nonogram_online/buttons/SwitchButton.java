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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;

/**
 *
 * @author marton552
 */
public class SwitchButton extends BasicButton {
    
    private boolean state = false;
    private String type = "";

    public SwitchButton(Menu m, String type, int width, int height) {
        super(m, "off", width, height);
        setOrientation(width,height);
        draw = false;
        this.type = type;
        
            this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m.changeEditorMenu(type,0);
                if(state){
                    state = false;
                    text = "off";
                }
                    else{
                    state = true;
                    text = "on";
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
    }

    public boolean isState() {
        return state;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/toggle_"+text+".png")).getImage(), 0,0, width, height, null);
        FontRenderContext frc = new FontRenderContext(null, true, true);

        setFontSize();
        String newText = text;

        Font font = new Font("TimesRoman", Font.PLAIN, fontSize);
        Rectangle2D r2D = font.getStringBounds(newText, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rX = (int) Math.round(r2D.getX());

        int a = (width / 2) - (rWidth / 2) - rX;

        g.setFont(font);
        g.drawString(newText, a, (int)(height*0.55));
    }
    
}
