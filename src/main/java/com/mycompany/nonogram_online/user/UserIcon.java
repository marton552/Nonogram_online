/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.user;

import com.mycompany.nonogram_online.MainFrame;
import com.mycompany.nonogram_online.Menu;
import com.mycompany.nonogram_online.level.Level;
import com.mycompany.nonogram_online.server.Server;
import com.mycompany.nonogram_online.user.User;
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
public class UserIcon extends JPanel {

    protected int screenWidth;
    protected int screenHeight;
    protected int width;
    protected int height;
    protected Menu m;
    protected JPanel thisPanel;
    protected String text;
    protected int hgap = 0;
    protected int fontSize = 0;
    protected boolean draw = true;
    protected User user;
    protected Server server;
    protected UserPanel p;
    
    /*
    Rendezés:
    - Névsorrend
    - Rank
    + keresés/szűrés
    
    Megjelenített adat:
    - username (mellette ha good for moderator)
    - pályáinak száma / approveolt pályáinak a száma
    + see all maps
    + promote user
    + bann user (set role = banned)
    */

    public UserIcon(Menu m, UserPanel p, User user, int width, int height) {
        this.m = m;
        this.p = p;
        this.user = user;
        this.screenWidth = m.getWidth();
        this.screenHeight = m.getHeight();
        this.width = width;
        this.height = height;
        server = new Server();
        thisPanel = this;
        setVisible(true);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX());
                if(e.getX() > 260 && e.getX() < 300){
                    m.menuActions("listusermaps:"+user.getUsername());
                }
                else if(e.getX() > 335 && e.getX() < 375){
                    if(!(user.isAdmin() || user.isMod())){
                        server.promoteUser(user.getUsername(),user.getUsercode());
                    }
                    p.setupUsers();
                }
                else if(e.getX() > 410 && e.getX() < 445){
                    if(!user.isAdmin()){
                        server.deleteUser(user.getUsername(),user.getUsercode());
                    }
                    p.setupUsers();
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

    protected void setFontSize() {
        fontSize = (int) (height / 5);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int offset = 7;
        g.setColor(Color.gray);
        g.drawRect(0, 0, width, height);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/border_user.png")).getImage(), 0, 0, width, height, null);
        
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/ranks/"+((user.isAdmin() || user.isMod()) ? "admin" : user.getRank())+".png")).getImage(), offset, offset, height-20, height-20, null);

        g.setColor(Color.BLACK);
        String name = user.getUsername()+" ("+user.getRole()+")";
        if(name.toCharArray().length > 15) g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 6)));
        else if(name.toCharArray().length > 10) g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 4)));
        else g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 3)));
        g.drawChars(name.toCharArray(), 0, name.toCharArray().length, height + offset, (height / 3) + offset);

        name = "Szint: : "+user.getRank();
        g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 6)));
        g.drawChars(name.toCharArray(), 0, name.toCharArray().length, height + offset, (height / 2) + offset);
        
        name = "Pályái: "+server.getUserCreatedOnlineMaps(user.getFullUsername()).getMessage()+" ebből elfogadott:"+server.getUserApprovedOnlineMaps(user.getFullUsername()).getMessage();
        g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 6)));
        g.drawChars(name.toCharArray(), 0, name.toCharArray().length, height + offset, (int)(height / 1.5) + offset);
        
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/list_levels.png")).getImage(), screenWidth - (height * 3), offset * 2, (height - offset * 4), (height - offset * 4), null);
        if(!(user.isAdmin() || user.isMod())) g.drawImage(new ImageIcon(this.getClass().getResource("/images/promote.png")).getImage(), screenWidth - (height*2), offset * 2, (height - offset * 4), (height - offset * 4), null);
        if(!user.isAdmin())g.drawImage(new ImageIcon(this.getClass().getResource("/images/trash.png")).getImage(), screenWidth - (height), offset * 2, (height - offset * 4), (height - offset * 4), null);
        
    }
}
