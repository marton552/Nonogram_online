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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private boolean deletedUsers = false;
    private boolean uploadedMissions = false;

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
                if (m.getUser().getUsercode().startsWith("0") && !m.getUser().getUsercode().equals("0000")) {
                    if (e.getY() > 290 && e.getY() < 380) {
                        if (!deletedUsers) {
                            deletedUsers = true;
                            server.deleteGuestUsers();
                        }
                    } else if (e.getY() > 380 && e.getY() < 470) {
                        //felhasználókezelés
                    } else if (e.getY() > 470 && e.getY() < 560) {
                        if (!uploadedMissions) {
                            uploadedMissions = true;
                            server.refreshMissions(collectMissions());
                        }
                    } else if (e.getY() > 560 && e.getY() < 650) {
                        timer.stop();
                        m.backToMenu(false);
                    }
                    repaint();
                } else {
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
            } else if (missions.get(i).getIcon().equals("createX")) {
                int done = collectCreatedLevels();
                missions.get(i).setCurrentCount(done);
            } else if (missions.get(i).getIcon().equals("getratedX")) {
                int done = collectRatedLevels();
                missions.get(i).setCurrentCount(done);
            }
        }
        repaint();
    }
    
    private ArrayList<String> collectMissions() {
        BufferedReader input = null;
        ArrayList<String> res = new ArrayList<>();
        try {
            input = new BufferedReader(new FileReader("src/main/resources/levels/missions.txt"));
            String data = input.lines().collect(Collectors.joining("\n"));
            ArrayList<String> missions = new ArrayList<String>(Arrays.asList(data.split("\n")));

            for (int j = 0; j < missions.size(); j++) {
                res.add(missions.get(j));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return res;
    }
        
    private int collectRatedLevels() {
        Response res = server.getUserRatedOnlineMaps(m.getUser().getFullUsername());
        return Integer.parseInt(res.getMessage());
    }

    private int collectCreatedLevels() {
        Response res = server.getUserCreatedOnlineMaps(m.getUser().getFullUsername());
        return Integer.parseInt(res.getMessage());
    }

    private int collectOnlineLevels() {
        Response res = server.getUserCompletedOnlineMaps(m.getUser().getFullUsername());
        return Integer.parseInt(res.getMessage());
    }

    private int collectOfflineLevels() {
        BufferedReader input = null;
        int completed = 0;
        try {
            input = new BufferedReader(new FileReader("src/main/resources/levels/saved_data.txt"));
            String data = input.lines().collect(Collectors.joining("\n"));
            ArrayList<String> completed_levels = new ArrayList<String>(Arrays.asList(data.split("\n")));

            for (int j = 0; j < completed_levels.size(); j++) {
                if (completed_levels.get(j).split(";")[0].equals(m.getUser().getFullUsername())) {
                    completed++;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
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

        int buttonNum = 1;

        if (m.getUser().getUsercode().startsWith("0") && !m.getUser().getUsercode().equals("0000")) {
            buttonTitle = "Vissza";
            buttonNum = 4;
        } else {
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
        }

        for (int i = 0; i < buttonNum; i++) {
            if (i == 1) {
                buttonTitle = "Küldetések frissítése";
            } else if (i == 2) {
                buttonTitle = "Felhasználók kezelése";
            } else if (i == 3) {
                buttonTitle = "Vendég profilok törlése";
            }
            g.setColor(Color.gray);
            if ((i == 1 && uploadedMissions) || (i == 3 && deletedUsers)) {
                g.drawImage(new ImageIcon(this.getClass().getResource("/images/button_filter.png")).getImage(), 0, screenWidth / 2 + (((4 - i)) * (screenHeight / 8)) - 30, screenWidth, screenHeight / 8, null);
            } else {
                g.drawImage(new ImageIcon(this.getClass().getResource("/images/button.png")).getImage(), 0, screenWidth / 2 + (((4 - i)) * (screenHeight / 8)) - 30, screenWidth, screenHeight / 8, null);
            }
            frc = new FontRenderContext(null, true, true);

            font = new Font("TimesRoman", Font.PLAIN, (int) (screenWidth / 12));
            r2D = font.getStringBounds(buttonTitle, frc);
            rWidth = (int) Math.round(r2D.getWidth());
            int rHeight = (int) Math.round(r2D.getHeight());
            rX = (int) Math.round(r2D.getX());
            int rY = (int) Math.round(r2D.getY());

            a = (screenWidth / 2) - (rWidth / 2) - rX;

            g.setFont(font);
            g.drawString(buttonTitle, a, screenWidth / 2 + (((4 - i)) * (screenHeight / 8)) + 30);
        }

    }

}
