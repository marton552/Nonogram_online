/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online;

import com.mycompany.nonogram_online.user.UserPanel;
import com.mycompany.nonogram_online.buttons.BasicButton;
import com.mycompany.nonogram_online.buttons.BasicRate;
import com.mycompany.nonogram_online.level.Level;
import com.mycompany.nonogram_online.level.LevelEditor;
import com.mycompany.nonogram_online.level.LevelIcon;
import com.mycompany.nonogram_online.server.Server;
import com.mycompany.nonogram_online.user.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 *
 * @author marton552
 */
public class Menu extends JFrame {

    private JPanel mainpanel;
    private JPanel menupanel;
    private BasicButton newGameButton;
    private BasicButton exitGameButton;
    private BasicButton login;
    private BasicButton signin;
    private BasicButton guestLogin;
    private BasicButton offlineMaps;
    private BasicButton onlineMaps;
    private BasicButton levelCreator;
    private BasicButton easyLevel;
    private BasicButton mediumLevel;
    private BasicButton hardLevel;
    private BasicButton backButton;
    private BasicButton userButton;

    private JPanel sizePanel;
    private JLabel sizeLabel;
    private JSlider sizeSlider;
    private JPanel layerPanel;
    private JLabel layerLabel;
    private JButton layerYES;
    private JButton layerNO;
    boolean layerEnable = false;
    private JPanel colorPanel;
    private JLabel colorLabel;
    private JButton colorYES;
    private JButton colorNO;
    boolean colorEnable = false;
    private JPanel gridPanel;
    private JLabel gridLabel;
    private JButton grid1x1;
    private JButton grid2x2;
    private JButton grid3x3;
    int gridEnable = 1;
    private JButton startEdit;

    private MainFrame game;
    private LoginPanel loginPanel;
    private UserPanel userPanel;
    private Menu menuMe;
    private int levelPerPage = 7;

    //user specific
    private User user;

    //folder spcific
    private String filePath;
    Level lvl;
    private int width = 480;
    private int height = 720;

    private ArrayList<String> history;
    private Server server;

    public Menu(String title) {
        super(title + " menü");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(width, height));
        this.setLocation(100, 100);

        server = new Server();
        history = new ArrayList<>();
        loginPanel = new LoginPanel(this, "");
        user = new User("", "", 0);
        userPanel = new UserPanel(this);
        mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout());
        this.setContentPane(mainpanel);
        menupanel = new JPanel();
        menupanel.setLayout(new GridLayout(4, 1));

        menuMe = this;

        login = new BasicButton(menuMe, "Bejelentkezés", 1, 4);
        signin = new BasicButton(menuMe, "Regisztráció", 1, 4);
        guestLogin = new BasicButton(menuMe, "Belépés vendégként", 1, 4);
        offlineMaps = new BasicButton(menuMe, "Offline pályák", 1, 4);
        onlineMaps = new BasicButton(menuMe, "Online pályák", 1, 4);
        levelCreator = new BasicButton(menuMe, "Saját pálya készítése", 1, 4);
        easyLevel = new BasicButton(menuMe, "Könnyű", 1, 4);
        mediumLevel = new BasicButton(menuMe, "Közepes", 1, 4);
        hardLevel = new BasicButton(menuMe, "Nehéz", 1, 4);
        backButton = new BasicButton(menuMe, "Vissza", 1, 4);
        userButton = new BasicButton(menuMe, "Saját profil", 1, 4);

        exitGameButton = new BasicButton(menuMe, "Exit", 1, 4);

        mainpanel.add(menupanel, BorderLayout.CENTER);

        history.add("start");
        this.setVisible(true);
        this.pack();
        setupFirstMenu();
    }

    public void menuActions(String text) {
        if (text == "Exit") {
            System.exit(0);
        } else if (text == "Bejelentkezés") {
            loginPanel = new LoginPanel(menuMe, " Bejelentkezés");
            menupanel.removeAll();
            menupanel.revalidate();
            menupanel.repaint();
            menupanel.setLayout(new BorderLayout());
            menupanel.add(loginPanel, BorderLayout.CENTER);
        } else if (text == "Regisztráció") {
            if (history.get(history.size() - 1) == "main") {
                loginPanel = new LoginPanel(menuMe, " Vendég regisztrálása");
                menupanel.removeAll();
                menupanel.revalidate();
                menupanel.repaint();
                menupanel.setLayout(new BorderLayout());
                menupanel.add(loginPanel, BorderLayout.CENTER);
            } else {
                loginPanel = new LoginPanel(menuMe, " Regisztráció");
                menupanel.removeAll();
                menupanel.revalidate();
                menupanel.repaint();
                menupanel.setLayout(new BorderLayout());
                menupanel.add(loginPanel, BorderLayout.CENTER);
            }
        } else if (text == "Belépés vendégként") {
            loginPanel = new LoginPanel(menuMe, " Belépés vendégként");
            menupanel.removeAll();
            menupanel.revalidate();
            menupanel.repaint();
            menupanel.setLayout(new BorderLayout());
            menupanel.add(loginPanel, BorderLayout.CENTER);
        } else if (text == "Saját profil") {
            loginPanel = new LoginPanel(menuMe, " Belépés vendégként");
            menupanel.removeAll();
            menupanel.revalidate();
            menupanel.repaint();
            menupanel.setLayout(new BorderLayout());
            userPanel = new UserPanel(this);
            userPanel.refreshMissions();
            menupanel.add(userPanel, BorderLayout.CENTER);
        } else if (text == "Offline pályák") {
            history.add("diff");
            setupDifficultyMenu();
        } else if (text == "Online pályák") {
            history.add("online");
            setupOnlineLevelsMenu();
        } else if (text == "Saját pálya készítése") {
            setupLevelEditorMenu();
        } else if (text == "Könnyű") {
            filePath += "easy/";
            history.add("offline");
            setupOfflineLevelsMenu();
        } else if (text == "Közepes") {
            filePath += "medium/";
            history.add("offline");
            setupOfflineLevelsMenu();
        } else if (text == "Nehéz") {
            filePath += "hard/";
            history.add("offline");
            setupOfflineLevelsMenu();
        } else if (text == "Vissza") {
            backToMenu(true);
        } else if (text == " Vissza") {
            backToMenu(false);
        } else {
            loginPanel.menuActions(text);
        }
    }

    public void setUser(String name, String usercode,int rank) {
        user = new User(name, usercode,rank);
    }

    public User getUser() {
        return user;
    }

    public void login() {
        history.add("main");
        setupMainMenu();
    }

    public void backToMenu(boolean backButtonPressed) {
        if (backButtonPressed) {
            history.remove(history.size() - 1);
        }
        if (history.get(history.size() - 1).equals("start")) {
            setupFirstMenu();
        } else if (history.get(history.size() - 1).equals("main")) {
            setupMainMenu();
        } else if (history.get(history.size() - 1).equals("diff")) {
            setupDifficultyMenu();
        } else if (history.get(history.size() - 1).equals("offline")) {
            setupOfflineLevelsMenu();
        } else if (history.get(history.size() - 1).equals("online")) {
            setupOnlineLevelsMenu();
        }
    }

    private void setupFirstMenu() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new GridLayout(4, 1));
        login.setOrientation(1, 4);
        menupanel.add(login);
        signin.setOrientation(1, 4);
        menupanel.add(signin);
        guestLogin.setOrientation(1, 4);
        menupanel.add(guestLogin);
        exitGameButton.setOrientation(1, 4);
        menupanel.add(exitGameButton);

    }

    private void setupMainMenu() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        if (user.isGuest()) {
            menupanel.setLayout(new GridLayout(5, 1));
            signin.setOrientation(1, 5);
            offlineMaps.setOrientation(1, 5);
            onlineMaps.setOrientation(1, 5);
            levelCreator.setOrientation(1, 5);
            exitGameButton.setOrientation(1, 5);
        } else {
            menupanel.setLayout(new GridLayout(5, 1));
            offlineMaps.setOrientation(1, 5);
            onlineMaps.setOrientation(1, 5);
            levelCreator.setOrientation(1, 5);
            userButton.setOrientation(1, 5);
            exitGameButton.setOrientation(1, 5);
        }
        menupanel.add(offlineMaps);
        menupanel.add(onlineMaps);
        menupanel.add(levelCreator);
        if (user.isGuest()) {
            signin.setText("Megkezdett fiók regisztrálása");
            menupanel.add(signin);
        }
        else menupanel.add(userButton);
        menupanel.add(exitGameButton);
    }

    private void setupDifficultyMenu() {
        filePath = "levels/";
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new GridLayout(4, 1));
        menupanel.add(easyLevel);
        easyLevel.setOrientation(1, 4);
        menupanel.add(mediumLevel);
        mediumLevel.setOrientation(1, 4);
        menupanel.add(hardLevel);
        hardLevel.setOrientation(1, 4);
        menupanel.add(backButton);
        backButton.setOrientation(1, 4);
    }

    private void setupOfflineLevelsMenu() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new GridLayout((levelPerPage + 1), 1));
        try {
            ArrayList<String> filenames = getResourceFiles(filePath);
            for (int i = 0; i < levelPerPage; i++) {
                if (filenames.size() > i) {
                    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath + filenames.get(i));
                    String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
                    lvl = new Level(new ArrayList<String>(Arrays.asList(result.split(";"))), "", "", true);
                    
                    BufferedReader input = new BufferedReader(new FileReader("src/main/resources/levels/saved_data.txt"));
                    String data1 = input.lines().collect(Collectors.joining("\n"));
                    ArrayList<String> completed_levels = new ArrayList<String>(Arrays.asList(data1.split("\n")));
                    boolean completed = false;
                    for (int j = 0; j < completed_levels.size(); j++) {
                        if(completed_levels.get(j).equals(user.getFullUsername()+";"+lvl.getName())) completed = true;
                    }
                    System.out.println(completed);
                    LevelIcon icon = new LevelIcon(menuMe, lvl, levelPerPage + 1, 0,completed);
                    setupIcon(icon);
                    menupanel.add(icon);
                    icon.repaint();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);;
        }
        menupanel.add(backButton);
        backButton.setOrientation(1, (levelPerPage + 1));
    }
    
    public ArrayList<String> getResourceFiles(String path) throws IOException {
        ArrayList<String> filenames = new ArrayList<>();

        try (
                 InputStream in = getResourceAsStream(path);  BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }

        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private void setupOnlineLevelsMenu() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new GridLayout((levelPerPage + 1), 1));
        ArrayList<Level> levels = new ArrayList<>();
        levels = server.getXLevels("id", 0, levelPerPage + 1);
        for (int i = 0; i < levels.size(); i++) {
            LevelIcon icon;
            if(server.isLevelFinishedByUser(levels.get(i).getName(), levels.get(i).getCreator_name(), user.getFullUsername()).getStatusCode() == 200){
                icon = new LevelIcon(menuMe, levels.get(i), levelPerPage + 1, 0,true);
            }
            else {
                icon = new LevelIcon(menuMe, levels.get(i), levelPerPage + 1, 0,false);
            }
            setupIcon(icon);
            menupanel.add(icon);
            icon.repaint();
        }
        menupanel.add(backButton);
        backButton.setOrientation(1, (levelPerPage + 1));
    }

    private void setupLevelEditorMenu() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        GridLayout edit = new GridLayout(5, 1);
        edit.setVgap(64);
        menupanel.setLayout(edit);
        sizePanel = new JPanel();
        GridLayout size = new GridLayout(1, 2);
        size.setHgap(10);
        sizePanel.setLayout(size);
        sizeLabel = new JLabel(" Pálya mérete:");
        sizeSlider = new JSlider(5, 20);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setSnapToTicks(true);
        sizeSlider.setMinorTickSpacing(5);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeSlider);
        menupanel.add(sizePanel);

        layerPanel = new JPanel();
        layerPanel.setLayout(new GridLayout(1, 3));
        layerLabel = new JLabel(" Többrétegűség:");
        layerYES = new JButton("Igen");
        layerYES.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layerEnable = true;
                changeEditorMenu("layer", 1);
            }
        });
        layerNO = new JButton("Nem");
        layerNO.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layerEnable = false;
                changeEditorMenu("layer", 0);
            }
        });
        layerPanel.add(layerLabel);
        layerPanel.add(layerNO);
        layerPanel.add(layerYES);
        changeEditorMenu("layer", 0);
        menupanel.add(layerPanel);

        colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 3));
        colorLabel = new JLabel(" Többszínűség:");
        colorYES = new JButton("Igen");
        colorYES.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorEnable = true;
                changeEditorMenu("color", 1);
            }
        });
        colorNO = new JButton("Nem");
        colorNO.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeEditorMenu("color", 0);
                colorEnable = false;
            }
        });
        colorPanel.add(colorLabel);
        colorPanel.add(colorNO);
        colorPanel.add(colorYES);
        changeEditorMenu("color", 0);
        menupanel.add(colorPanel);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 4));
        gridLabel = new JLabel(" Felosztás:");
        grid1x1 = new JButton("1x1");
        grid1x1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gridEnable = 1;
                changeEditorMenu("grid", 0);
            }
        });
        grid2x2 = new JButton("2x2");
        grid2x2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gridEnable = -4;
                changeEditorMenu("grid", 1);
            }
        });
        grid3x3 = new JButton("3x3");
        grid3x3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gridEnable = -9;
                changeEditorMenu("grid", 2);
            }
        });
        gridPanel.add(gridLabel);
        gridPanel.add(grid1x1);
        gridPanel.add(grid2x2);
        gridPanel.add(grid3x3);
        changeEditorMenu("grid", 0);
        menupanel.add(gridPanel);

        startEdit = new JButton("Szerkesztés megkezdése");
        startEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menupanel.removeAll();
                menupanel.revalidate();
                menupanel.repaint();
                menupanel.setLayout(new BorderLayout());
                String data = LevelEditor.templateData[0] + sizeSlider.getValue()+";"+ gridEnable+ LevelEditor.templateData[1];
                if(gridEnable < 0) gridEnable*=-1;
                for (int i = 0; i < (gridEnable) * ((Integer) sizeSlider.getValue() * (Integer) sizeSlider.getValue()); i++) {
                    data += ";0";
                }
                game = new MainFrame("Új pálya", menuMe, new LevelEditor(new ArrayList<String>(Arrays.asList(data.split(";"))), (Integer) sizeSlider.getValue(), "", "", true), true, layerEnable, colorEnable, gridEnable);
                menupanel.add(game, BorderLayout.CENTER);
                menupanel.repaint();
            }
        });
        menupanel.add(startEdit);
    }

    private void changeEditorMenu(String option, int button) {
        if (option == "layer") {
            if (button == 0) {
                layerNO.setEnabled(false);
                layerYES.setEnabled(true);
            } else {
                layerNO.setEnabled(true);
                layerYES.setEnabled(false);
            }
        } else if (option == "color") {
            if (button == 0) {
                colorNO.setEnabled(false);
                colorYES.setEnabled(true);
            } else {
                colorNO.setEnabled(true);
                colorYES.setEnabled(false);
            }
        } else if (option == "grid") {
            grid1x1.setEnabled(true);
            grid2x2.setEnabled(true);
            grid3x3.setEnabled(true);
            if (button == 0) {
                grid1x1.setEnabled(false);
            } else if (button == 1) {
                grid2x2.setEnabled(false);
            } else {
                grid3x3.setEnabled(false);
            }
        }
    }

    private void setupIcon(LevelIcon icon) {
        icon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean playLevel = false;
                boolean deleteLevel = false;
                boolean approveLevel = false;
                boolean showLevel = false;
                System.out.println(e.getX() + ", " + e.getY());
                //300-360 ig kuka  395-445 approve height 20-70ig
                if(icon.isAdminVisible()){
                    if (e.getX() < 70){
                        showLevel = true;
                    }
                    else {
                        playLevel = true;
                    }
                    
                }
                if (icon.isApproveVisible()) {
                    if (e.getX() < 300) {
                        playLevel = true;
                    } else if (e.getX() >= 300 && e.getX() <= 360 && e.getY() >= 20 && e.getY() <= 70) {
                        deleteLevel = true;
                    } else if (e.getX() >= 395 && e.getX() <= 445 && e.getY() >= 20 && e.getY() <= 70) {
                        approveLevel = true;
                    }
                } else if (icon.isTrashVisible()) {
                    if (e.getX() < 395) {
                        playLevel = true;
                    } else if (e.getX() >= 395 && e.getX() <= 445 && e.getY() >= 20 && e.getY() <= 70) {
                        deleteLevel = true;
                    }
                } else {
                    playLevel = true;
                }

                if(showLevel){
                    icon.showLevelToAdmin();
                }
                else if (playLevel) {
                    menupanel.removeAll();
                    menupanel.revalidate();
                    menupanel.repaint();
                    menupanel.setLayout(new BorderLayout());
                    game = new MainFrame(icon.getLvl().getName(), menuMe, icon.getLvl(), false, false, false, 0);
                    menupanel.add(game, BorderLayout.CENTER);
                    menupanel.repaint();
                }
                else if(deleteLevel){
                    server.deleteLevel(icon.getLvl().getName(),icon.getLvl().getCreator_name());
                    setupOnlineLevelsMenu();
                }
                else if(approveLevel){
                    server.approveLevel(icon.getLvl().getName(),icon.getLvl().getCreator_name());
                    setupOnlineLevelsMenu();
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
                icon.flash();
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
