/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.user;

import com.mycompany.nonogram_online.MainFrame;
import com.mycompany.nonogram_online.Menu;
import com.mycompany.nonogram_online.buttons.BasicButton;
import com.mycompany.nonogram_online.server.Response;
import com.mycompany.nonogram_online.server.Server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author marton552
 */
public class UserPanel extends JPanel {

    private Menu m;
    private Server server;
    private int screenWidth;
    private int screenHeight;
    private ArrayList<MissionIcon> missions;
    private String buttonTitle;
    Timer timer;
    private int animationTimer = 1;

    public UserPanel(Menu m) {
        this.setLayout(new BorderLayout());
        this.m = m;
        server = new Server();
        this.screenWidth = m.getWidth() - 15;
        this.screenHeight = m.getHeight() - 15;
        missions = server.getMissionList(m.getUser().getRank());
        refreshMissions();
        setVisible(true);
        repaint();
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (animationTimer < 30) {
                    repaint();
                    animationTimer++;
                } else {
                    animationTimer = 1;
                    repaint();
                }
            }
        });
        timer.start();

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX() + ", " + e.getY());
                if (e.getY() >= screenHeight - screenHeight / 6 - 30) {
                    if (buttonTitle.equals("Vissza")) {
                        timer.stop();
                        m.backToMenu(false);
                    } else {
                        m.getUser().lvlUp();
                        server.lvlUpUser(m.getUser().getUsername(), m.getUser().getUsercode(), m.getUser().getRank());
                        refreshMissions();
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

    public void refreshMissions() {
        missions = server.getMissionList(m.getUser().getRank());
        for (int i = 0; i < missions.size(); i++) {
            if (missions.get(i).getIcon().equals("offlineX")) {
                int done = collectOfflineLevels();
                missions.get(i).setCurrentCount(done);
            } else if (missions.get(i).getIcon().equals("onlineX")) {
                int done = collectOnlineLevels();
                missions.get(i).setCurrentCount(done);
            } else if (missions.get(i).getIcon().equals("makeX")) {
                //todo offline map implementation
            } else if (missions.get(i).getIcon().equals("getplayedX")) {
                //todo offline map implementation
            } else if (missions.get(i).getIcon().equals("getratedX")) {
                //todo offline map implementation
            }
        }
        repaint();
    }

    private int collectOnlineLevels() {
        Response res = server.getUserCompletedOnlineMaps(m.getUser().getFullUsername());
        return Integer.parseInt(res.getMessage());
    }

    private int collectOfflineLevels() {
        InputStream is2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("levels/saved_data.txt");
        String result2 = new BufferedReader(new InputStreamReader(is2)).lines().collect(Collectors.joining("\n"));
        ArrayList<String> completed_levels = new ArrayList<String>(Arrays.asList(result2.split("\n")));
        int completed = 0;
        for (int j = 0; j < completed_levels.size(); j++) {
            if (completed_levels.get(j).split(";")[0].equals(m.getUser().getFullUsername() )) {
                completed++;
            }
        }
        return completed;
    }

    private boolean isAllMissionsCompleted() {
        for (int i = 0; i < missions.size(); i++) {
            if (missions.get(i).getCurrentCount() < missions.get(i).getNeedCount()) {
                return false;
            }
        }
        return true;
    }

    private boolean isCompleted(int i) {
        return missions.get(i).getCurrentCount() >= missions.get(i).getNeedCount();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int profpicSize = screenWidth / 3;
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/background/sun" + animationTimer + ".png")).getImage(), 0, 0, screenWidth, (int) (profpicSize * 1.5), null);
        g.setColor(Color.white);
        g.fillRect(0, profpicSize, screenWidth, screenHeight);
        g.setColor(Color.black);
        g.drawLine(0, profpicSize, screenWidth, profpicSize);
        if (m.getUser().getUsercode().startsWith("0") && !m.getUser().getUsercode().equals("0000")) {
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/ranks/admin.png")).getImage(), screenWidth / 2 - profpicSize / 2, (profpicSize / 2), profpicSize, profpicSize, null);
        } else {
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/ranks/" + m.getUser().getRank() + ".png")).getImage(), screenWidth / 2 - profpicSize / 2, (profpicSize / 2), profpicSize, profpicSize, null);
        }

        FontRenderContext frc = new FontRenderContext(null, true, true);

        Font font;
        if (m.getUser().getUsername().length() > 10) {
            font = new Font("TimesRoman", Font.PLAIN, (int) (screenWidth / 10));
        } else {
            font = new Font("TimesRoman", Font.PLAIN, (int) (screenWidth / 8));
        }
        Rectangle2D r2D = font.getStringBounds(m.getUser().getUsername(), frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rX = (int) Math.round(r2D.getX());
        int a = (screenWidth / 2) - (rWidth / 2) - rX;
        g.setFont(font);
        g.drawString(m.getUser().getUsername(), a, (int) (profpicSize * 1.7));

        g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (screenWidth / 20)));
        g.drawString("A következő szint eléréséhez:", 10, (int) (profpicSize * 2));

        for (int i = 0; i < missions.size(); i++) {
            g.setColor(Color.green);
            g.fillRect(0, (int) (screenWidth / 2 + ((i + 1.5) * (screenHeight / 8))), (int) (screenWidth * (missions.get(i).getCurrentCount() / (double) missions.get(i).getNeedCount())), screenHeight / 16);
            g.setColor(Color.black);
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/missions/" + (isCompleted(i) ? "completed_border" : "border") + ".png")).getImage(), 0, screenWidth / 2 + ((i + 1) * (screenHeight / 8)), screenWidth, screenHeight / 8, null);
            g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (screenWidth / 25)));
            g.drawString(missions.get(i).getTitle(), 10, screenWidth / 2 + 30 + ((i + 1) * (screenHeight / 8)));
            g.setFont(new Font("TimesRoman", Font.PLAIN, (int) (screenWidth / 30)));
            g.drawString("Ebből kész: " + missions.get(i).getCurrentCount(), 10, screenWidth / 2 + 50 + ((i + 1) * (screenHeight / 8)));
            g.drawImage(new ImageIcon(this.getClass().getResource("/images/missions/" + missions.get(i).getIcon() + ".png")).getImage(), screenWidth - (screenHeight / 7), screenWidth / 2 + ((i + 1) * (screenHeight / 8)), screenHeight / 8, screenHeight / 8, null);

        }

        if (isAllMissionsCompleted()) {
            buttonTitle = "Szintlépés";
        } else {
            buttonTitle = "Vissza";
        }

        g.setColor(Color.gray);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/button.png")).getImage(), 0, screenHeight - screenHeight / 6 - 30, screenWidth, screenHeight / 6, null);
        frc = new FontRenderContext(null, true, true);

        font = new Font("TimesRoman", Font.PLAIN, (int) (screenWidth / 10));
        r2D = font.getStringBounds(buttonTitle, frc);
        rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        a = (screenWidth / 2) - (rWidth / 2) - rX;

        g.setFont(font);
        g.drawString(buttonTitle, a, screenHeight - screenHeight / 10);

    }

}
