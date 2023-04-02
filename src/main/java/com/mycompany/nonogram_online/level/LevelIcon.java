/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.level;

import com.mycompany.nonogram_online.Menu;
import com.mycompany.nonogram_online.user.User;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author marton552
 */
public class LevelIcon extends JPanel {

    private Level lvl;
    private int screenWidth;
    private int screenHeight;
    private int count;
    private int nth;
    private Menu m;
    private boolean isFlashing = false;
    private int animationTimer = 0;
    private Timer timer;
    private JPanel thisPanel;
    private boolean completed;
    private Level placeholder;
    private boolean show_admin = false;
    private int rankSize;

    public LevelIcon(Menu m, Level lvl, int count, int nth, boolean isCompleted) {
        this.m = m;
        this.lvl = lvl;
        this.screenWidth = m.getWidth()-15;
        this.screenHeight = m.getHeight()-15;
        this.count = count;
        this.nth = nth;
        this.completed = isCompleted;
        this.show_admin = m.getUser().getUsercode().startsWith("0") && !m.getUser().getUsercode().equals("0000");
        if (!completed) {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("levels/questionmark.txt");
            String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
            placeholder = new Level(new ArrayList<String>(Arrays.asList(result.split(";"))), "", "", true);
        }
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
    }

    public void flash() {
        if (!isFlashing) {
            isFlashing = true;
            animationTimer = screenWidth + (screenHeight / count) + 1;
            timer.start();
        }
    }

    public boolean isTrashVisible() {
        return lvl.getCreator_name() != "" && (lvl.getCreator_name().equals(m.getUser().getFullUsername()) || (m.getUser().getUsercode().startsWith("0") && !m.getUser().getUsercode().equals("0000")));
    }

    public boolean isApproveVisible() {
        return lvl.getCreator_name() != "" && m.getUser().getUsercode().startsWith("0") && !m.getUser().getUsercode().equals("0000") && !lvl.isApproved();
    }
    
    public boolean isAdminVisible(){
        return lvl.getCreator_name() != "" && m.getUser().getUsercode().startsWith("0") && !m.getUser().getUsercode().equals("0000") && show_admin;
    }
    
    public void showLevelToAdmin(){
        show_admin = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int offset = 7;
        int width = screenWidth;
        int height = screenHeight / count;
        rankSize = (int)(height / 4);
        g.setColor(Color.gray);
        g.drawRect(0, 0, width, height);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/"+(completed ? "completed_border" : "border")+".png")).getImage(), 0, 0, width, height, null);
        if (animationTimer > 0) {
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/flash.png")).getImage(), screenWidth - animationTimer, 0, height, height, null);
        }
        if (completed || (!show_admin && m.getUser().getUsercode().startsWith("0") && !m.getUser().getUsercode().equals("0000"))) {
            lvl.setMatrixStartPos(offset, offset);
            for (int i = 0; i < lvl.getMatrix().size(); i++) {
                lvl.setMenuSquareSize(height - 20);
                lvl.finishGame();
                lvl.drawMatrix(g, i, false, true);
            }
        } else {
            placeholder.setMatrixStartPos(offset, offset);
            for (int i = 0; i < placeholder.getMatrix().size(); i++) {
                placeholder.setMenuSquareSize(height - 20);
                placeholder.finishGame();
                placeholder.drawMatrix(g, i, false, true);
            }
        }

        g.setColor(Color.BLACK);
        if(lvl.getName().toCharArray().length > 15) g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 6)));
        else if(lvl.getName().toCharArray().length > 10) g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 4)));
        else g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 3)));
        g.drawChars(lvl.getName().toCharArray(), 0, lvl.getName().toCharArray().length, height + offset, (height / 3) + offset);

        if (lvl.isApproved()) {
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/tick.png")).getImage(), height - (height / 3) - offset, -1 * offset, (int) (height / 2.5) + offset, (int) (height / 2.5) + offset, null);
        }
        if (lvl.getCreator_name() != "") {
            if(lvl.creator_name.split("#")[1].startsWith("0") && !lvl.creator_name.split("#")[1].equals("0000")) g.drawImage(new ImageIcon(this.getClass().getResource("/images/ranks/admin.png")).getImage(), height + offset, (int) (height / 1.8) + offset-rankSize+(rankSize/4)+3,rankSize, rankSize, null);
            else g.drawImage(new ImageIcon(this.getClass().getResource("/images/ranks/"+m.getUser().getRank()+".png")).getImage(), height + offset, (int) (height / 1.8) + offset-rankSize+(rankSize/4)+3,rankSize, rankSize, null);
            g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 6)));
            char[] name = lvl.creator_name.split("#")[1].startsWith("0") ? lvl.getCreator_name().split("#")[0].toCharArray() : lvl.getCreator_name().toCharArray();
            g.drawChars(name, 0, name.length, rankSize + height + offset, (int) (height / 1.8) + offset);
        }
        if (lvl.getCreated_date() != "") {
            g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (height / 6.5)));
            g.drawChars(lvl.getCreated_date().toCharArray(), 0, lvl.getCreated_date().toCharArray().length, height + offset, (int) (height / 1.3) + offset);
        }
        if (isApproveVisible()) {
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/trash.png")).getImage(), screenWidth - (height * 2), offset * 2, (height - offset * 4), (height - offset * 4), null);
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/approve.png")).getImage(), screenWidth - (height), offset * 2, (height - offset * 4), (height - offset * 4), null);
        } else if (isTrashVisible()) {
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/trash.png")).getImage(), screenWidth - (height), offset * 2, (height - offset * 4), (height - offset * 4), null);
        }
        if(isAdminVisible() && !completed){
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/show_button.png")).getImage(), (height / 3), (height/2)-offset, (int) (height / 2), (int) (height / 2), null);
        }
    }

    public Level getLvl() {
        return lvl;
    }

}
