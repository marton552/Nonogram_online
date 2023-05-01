/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online;

import com.mycompany.nonogram_online.user.UserPanel;
import com.mycompany.nonogram_online.buttons.BasicButton;
import com.mycompany.nonogram_online.buttons.FailButton;
import com.mycompany.nonogram_online.buttons.SearchButton;
import com.mycompany.nonogram_online.buttons.SortButton;
import com.mycompany.nonogram_online.buttons.SwitchButton;
import com.mycompany.nonogram_online.generator.ImageHandler;
import com.mycompany.nonogram_online.level.ImageToLevel;
import com.mycompany.nonogram_online.level.Level;
import com.mycompany.nonogram_online.level.LevelEditor;
import com.mycompany.nonogram_online.level.LevelIcon;
import com.mycompany.nonogram_online.level.SuperProject;
import com.mycompany.nonogram_online.server.SearchResponse;
import com.mycompany.nonogram_online.server.Server;
import com.mycompany.nonogram_online.server.SortResponse;
import com.mycompany.nonogram_online.user.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
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
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author marton552
 */
public class Menu extends JFrame {

    private JPanel mainpanel;
    private JPanel menupanel;
    private BasicButton newGameButton;
    private BasicButton exitGameButton;
    private BasicButton logoutGameButton;
    private BasicButton login;
    private BasicButton signin;
    private BasicButton guestLogin;
    private BasicButton offlineMaps;
    private BasicButton onlineMaps;
    private BasicButton onlineCollection;
    private BasicButton superProjects;
    private BasicButton levelCreator;
    private BasicButton levelByHand;
    private BasicButton levelByImage;
    private BasicButton easyLevel;
    private BasicButton mediumLevel;
    private BasicButton hardLevel;
    private BasicButton backButton;
    private BasicButton userButton;

    private JPanel prevNextPanel;
    private BasicButton prevButton;
    private BasicButton nextButton;
    private JPanel sortSearchPanel;
    private SortButton sortByDateButton;
    private SortButton sortByNameButton;
    private SortButton sortByRateButton;
    private JTextField searchTextField;
    private SearchButton searchByLevel;
    private SearchButton searchByUser;
    private SortResponse sortState;
    private SearchResponse searchState;

    private JPanel sizePanel;
    private JLabel sizeLabel;
    private JSlider sizeSlider;
    private JPanel layerPanel;
    private JLabel layerLabel;
    private SwitchButton layerButton;
    boolean layerEnable = false;
    private JPanel colorPanel;
    private JLabel colorLabel;
    private SwitchButton colorButton;
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
    private int levelStartNum = 0;
    private int levelPerPage = 6;

    //user specific
    private User user;

    //folder spcific
    private String filePath;
    Level lvl;
    private int width = 480;
    private int height = 720;

    private ArrayList<String> history;
    private Server server;
    private ImageToLevel itl;
    private File selectedFile;
    private SuperProject sp;

    public Menu(String title) {
        super(title + " menü");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(width, height));
        this.setLocation(100, 100);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        server = new Server();
        history = new ArrayList<>();
        loginPanel = new LoginPanel(this, "");
        user = new User("", "", 0, "");
        userPanel = new UserPanel(this);
        mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout());
        this.setContentPane(mainpanel);
        menupanel = new JPanel();
        menupanel.setLayout(new GridLayout(4, 1));

        menuMe = this;
        sortState = new SortResponse();
        searchState = new SearchResponse("");
        sp = new SuperProject(menuMe);

        try {
            itl = new ImageToLevel(menuMe, ImageIO.read(Menu.class.getResourceAsStream("/images/villam.png")));
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        login = new BasicButton(menuMe, "Bejelentkezés", 1, 4);
        signin = new BasicButton(menuMe, "Regisztráció", 1, 4);
        guestLogin = new BasicButton(menuMe, "Belépés vendégként", 1, 4);
        offlineMaps = new BasicButton(menuMe, "Offline pályák", 1, 4);
        onlineMaps = new BasicButton(menuMe, "Online pályák", 1, 4);
        onlineCollection = new BasicButton(menuMe, "Online pályatár", 1, 3);
        superProjects = new BasicButton(menuMe, "Szuperprojekt", 1, 3);
        levelCreator = new BasicButton(menuMe, "Saját pálya készítése", 1, 4);
        levelByHand = new BasicButton(menuMe, "Pálya kézzel készítése", 1, 4);
        levelByImage = new BasicButton(menuMe, "Pálya képfeltöltéssel", 1, 4);
        easyLevel = new BasicButton(menuMe, "Könnyű", 1, 4);
        mediumLevel = new BasicButton(menuMe, "Közepes", 1, 4);
        hardLevel = new BasicButton(menuMe, "Nehéz", 1, 4);
        backButton = new BasicButton(menuMe, "Vissza", 1, 4);
        userButton = new BasicButton(menuMe, "Saját profil", 1, 4);
        prevButton = new BasicButton(menuMe, "Előző", 2, 4);
        nextButton = new BasicButton(menuMe, "Következő", 2, 4);

        sortByDateButton = new SortButton(menuMe, "Dátum", 2, 4);
        sortByNameButton = new SortButton(menuMe, "Név", 2, 4);
        sortByRateButton = new SortButton(menuMe, "Értékelés", 2, 4);
        searchTextField = new JTextField("");
        searchTextField = new JTextField("Keresés");
        searchTextField.setForeground(Color.GRAY);
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchTextField.getText().equals("Keresés")) {
                    searchTextField.setText("");
                    searchTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setForeground(Color.GRAY);
                    searchTextField.setText("Keresés");
                }
            }
        });
        searchByLevel = new SearchButton(menuMe, "Pálya keresés", 2, 4);
        searchByUser = new SearchButton(menuMe, "Felhasználó keresés", 2, 4);

        exitGameButton = new BasicButton(menuMe, "Exit", 1, 4);
        logoutGameButton = new BasicButton(menuMe, "Kijelentkezés", 1, 4);

        mainpanel.add(menupanel, BorderLayout.CENTER);

        history.add("start");
        this.setVisible(true);
        this.pack();
        setupFirstMenu();
    }

    public void menuActions(String text) {
        if (text.startsWith("&")) {
            itl.changeEditorMenu("&", 0);
        } else if (text.startsWith("#")) {
            itl.changeEditorMenu("#", 0);
        } else if (text.startsWith("@")) {
            itl.changeEditorMenu("@", 0);
        } else if (text.startsWith("$")) {
            itl.changeEditorMenu("$", 0);
        } else if (text.startsWith("+")) {
            itl.changeEditorMenu("+", 0);
        } else if (text.startsWith("-")) {
            itl.changeEditorMenu("-", 0);
        } else if (text == "imageEdit") {
            menupanel.removeAll();
            menupanel.revalidate();
            menupanel.repaint();
            menupanel.setLayout(new BorderLayout());
            game = new MainFrame("Új pálya", menuMe, new LevelEditor(new ArrayList<String>(Arrays.asList(itl.getLvl().export().split(";"))), itl.getLvlSize(), "", "", true), true, itl.isLayered(), itl.isColored(), itl.getGrid());
            menupanel.add(game, BorderLayout.CENTER);
            menupanel.repaint();
        } else if (text == "Exit") {
            System.exit(0);
        } else if (text == "Kijelentkezés") {
            backToMenu(true);
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
            history.add("user");
            setupUserProfile();
        } else if (text == "Offline pályák") {
            history.add("diff");
            setupDifficultyMenu();
        } else if (text == "Online pályák") {
            history.add("online");
            setupOnlineChooser();
        } else if (text == "Online pályatár") {
            history.add("online_maps");
            levelStartNum = 0;
            setupOnlineLevelsMenu();
        } else if (text == "Szuperprojekt") {
            history.add("super");
            setupSuperProject();
        } else if (text == "Saját pálya készítése") {
            history.add("editor");
            setupMakeLevelMenu();
        } else if (text == "Pálya kézzel készítése") {
            setupLevelEditorMenu();
        } else if (text == "Pálya képfeltöltéssel") {
            history.add("uploadImage");
            setupImageLoad(true);
        } else if (text == "Könnyű") {
            filePath += "easy/";
            levelStartNum = 0;
            history.add("offline");
            setupOfflineLevelsMenu();
        } else if (text == "Közepes") {
            filePath += "medium/";
            levelStartNum = 0;
            history.add("offline");
            setupOfflineLevelsMenu();
        } else if (text == "Nehéz") {
            filePath += "hard/";
            levelStartNum = 0;
            history.add("offline");
            setupOfflineLevelsMenu();
        } else if (text == "Vissza") {
            backToMenu(true);
        } else if (text == " Vissza") {
            backToMenu(false);
        } else if (text == "Előző") {
            if (history.contains("offline")) {
                levelStartNum -= levelPerPage;
                setupOfflineLevelsMenu();
            } else if (history.contains("online")) {
                levelStartNum -= levelPerPage;
                setupOnlineLevelsMenu();
            }
        } else if (text == "Következő") {
            if (history.contains("offline")) {
                levelStartNum += levelPerPage;
                setupOfflineLevelsMenu();
            } else if (history.contains("online")) {
                levelStartNum += levelPerPage;
                setupOnlineLevelsMenu();
            }
        } else if (text.contains("Dátum")) {
            sortState.setDate();
            setupOnlineLevelsMenu();
        } else if (text.contains("Név")) {
            sortState.setName();
            setupOnlineLevelsMenu();
        } else if (text.contains("Értékelés")) {
            sortState.setRate();
            setupOnlineLevelsMenu();
        } else if (text.contains("Pálya keresés")) {
            searchState.setSearch(searchTextField.getText());
            searchState.setLevelName();
            setupOnlineLevelsMenu();
        } else if (text.contains("Felhasználó keresés")) {
            searchState.setSearch(searchTextField.getText());
            searchState.setUserName();
            setupOnlineLevelsMenu();
        } else if (text.startsWith("listusermaps")) {
            searchState.setSearch(text.split(":")[1]);
            searchState.setUserName();
            setupOnlineLevelsMenu();
        } else {
            loginPanel.menuActions(text);
        }
    }

    public void setUser(String name, String usercode, int rank, String role) {
        user = new User(name, usercode, rank, role);
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
            setupOnlineChooser();
        } else if (history.get(history.size() - 1).equals("online_maps")) {
            setupOnlineLevelsMenu();
        } else if (history.get(history.size() - 1).equals("user")) {
            setupUserProfile();
        } else if (history.get(history.size() - 1).equals("editor")) {
            setupMakeLevelMenu();
        } else if (history.get(history.size() - 1).equals("uploadImage")) {
            setupImageLoad(false);
        } else if (history.get(history.size() - 1).equals("super")) {
            setupSuperProject();
        }
    }

    private void setupUserProfile() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new BorderLayout());
        userPanel = new UserPanel(this);
        userPanel.refreshMissions();
        menupanel.add(userPanel, BorderLayout.CENTER);
    }

    private void setupImageLoad(boolean backPressed) {
        if (backPressed) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop/super"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int result = fileChooser.showOpenDialog(menuMe);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    selectedFile = fileChooser.getSelectedFile();
                    BufferedImage bi = ImageIO.read(selectedFile);
                    menupanel.removeAll();
                    menupanel.revalidate();
                    menupanel.repaint();
                    menupanel.setLayout(new BorderLayout());
                    itl.setup();
                    itl.addImage(bi);
                    itl.generate(true);
                    menupanel.add(itl, BorderLayout.CENTER);
                } catch (IOException ex) {
                    Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        } else {
            BufferedImage bi;
            try {
                bi = ImageIO.read(selectedFile);
                menupanel.removeAll();
                menupanel.revalidate();
                menupanel.repaint();
                menupanel.setLayout(new BorderLayout());
                itl.setup();
                itl.addImage(bi);
                itl.generate(true);
                menupanel.add(itl, BorderLayout.CENTER);
            } catch (IOException ex) {
                Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

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
            logoutGameButton.setOrientation(1, 5);
        } else {
            menupanel.setLayout(new GridLayout(5, 1));
            offlineMaps.setOrientation(1, 5);
            onlineMaps.setOrientation(1, 5);
            levelCreator.setOrientation(1, 5);
            userButton.setOrientation(1, 5);
            logoutGameButton.setOrientation(1, 5);
        }
        menupanel.add(offlineMaps);
        menupanel.add(onlineMaps);
        menupanel.add(levelCreator);
        if (user.isGuest()) {
            signin.setText("Megkezdett fiók regisztrálása");
            menupanel.add(signin);
        } else {
            menupanel.add(userButton);
        }
        menupanel.add(logoutGameButton);
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
        menupanel.setLayout(new GridLayout((levelPerPage + 2), 1));
        try {
            ArrayList<String> filenames = getResourceFiles(filePath);
            for (int i = levelStartNum; i < levelStartNum + levelPerPage; i++) {
                if (filenames.size() > i) {
                    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath + filenames.get(i));
                    String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
                    lvl = new Level(new ArrayList<String>(Arrays.asList(result.split(";"))), "", "", true);

                    BufferedReader input = new BufferedReader(new FileReader("src/main/resources/levels/saved_data.txt"));
                    String data1 = input.lines().collect(Collectors.joining("\n"));
                    ArrayList<String> completed_levels = new ArrayList<String>(Arrays.asList(data1.split("\n")));
                    boolean completed = false;
                    for (int j = 0; j < completed_levels.size(); j++) {
                        if (completed_levels.get(j).equals(user.getFullUsername() + ";" + lvl.getName())) {
                            completed = true;
                        }
                    }
                    LevelIcon icon = new LevelIcon(menuMe, lvl, levelPerPage + 2, 0, completed);
                    setupIcon(icon);
                    menupanel.add(icon);
                    icon.repaint();
                }
            }
            prevNextPanel = new JPanel(new GridLayout(1, 2));
            prevButton.setOrientation(2, (levelPerPage + 2));
            if (levelStartNum == 0) {
                prevButton.setEnabled(false);
            } else {
                prevButton.setEnabled(true);
            }
            prevNextPanel.add(prevButton);
            nextButton.setOrientation(2, (levelPerPage + 2));
            if (levelStartNum + levelPerPage > filenames.size()) {
                nextButton.setEnabled(false);
            } else {
                nextButton.setEnabled(true);
            }
            prevNextPanel.add(nextButton);
            menupanel.add(prevNextPanel);
        } catch (IOException ex) {
            System.out.println(ex);;
        }
        menupanel.add(backButton);
        backButton.setOrientation(1, (levelPerPage + 2));
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

    private void setupOnlineChooser() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new GridLayout(3, 1));
        onlineCollection.setOrientation(1, 3);
        menupanel.add(onlineCollection);
        superProjects.setOrientation(1, 3);
        menupanel.add(superProjects);
        backButton.setOrientation(1, 3);
        menupanel.add(backButton);
    }

    private void setupSuperProject() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new BorderLayout());
        sp.setup();
        menupanel.add(sp);
        sp.repaint();
    }

    public void playSuperProject(Level lvl) {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new BorderLayout());
        game = new MainFrame(lvl.getName(), menuMe, lvl, false, false, false, 0);
        menupanel.add(game, BorderLayout.CENTER);
        menupanel.repaint();
    }

    private void setupOnlineLevelsMenu() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new GridLayout((levelPerPage + 2 + 1), 1));
        sortSearchPanel = new JPanel(new GridLayout(2, 3));
        sortByDateButton.setOrientation(3, (levelPerPage + 2 + 1) * 2);
        sortSearchPanel.add(sortByDateButton);
        sortByNameButton.setOrientation(3, (levelPerPage + 2 + 1) * 2);
        sortSearchPanel.add(sortByNameButton);
        sortByRateButton.setOrientation(3, (levelPerPage + 2 + 1) * 2);
        sortSearchPanel.add(sortByRateButton);
        sortSearchPanel.add(searchTextField);
        searchByLevel.setOrientation(3, (levelPerPage + 2 + 1) * 2);
        sortSearchPanel.add(searchByLevel);
        searchByUser.setOrientation(3, (levelPerPage + 2 + 1) * 2);
        sortSearchPanel.add(searchByUser);
        menupanel.add(sortSearchPanel);

        ArrayList<Level> levels = new ArrayList<>();
        levels = server.getXLevels(sortState, searchState, levelStartNum, levelStartNum + levelPerPage + 1);
        boolean fullFilled = (levels.size() > levelPerPage);
        int size = (fullFilled) ? levelPerPage : levels.size();
        for (int i = 0; i < size; i++) {
            LevelIcon icon;
            if (server.isLevelFinishedByUser(levels.get(i).getName(), levels.get(i).getCreator_name(), user.getFullUsername()).getStatusCode() == 200) {
                icon = new LevelIcon(menuMe, levels.get(i), levelPerPage + 3, 0, true);
            } else {
                icon = new LevelIcon(menuMe, levels.get(i), levelPerPage + 3, 0, false);
            }
            setupIcon(icon);
            menupanel.add(icon);
            icon.repaint();
        }
        prevNextPanel = new JPanel(new GridLayout(1, 2));
        prevButton.setOrientation(2, (levelPerPage + 3));
        if (levelStartNum == 0) {
            prevButton.setEnabled(false);
        } else {
            prevButton.setEnabled(true);
        }
        prevNextPanel.add(prevButton);
        nextButton.setOrientation(2, (levelPerPage + 3));
        if (fullFilled) {
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
        }
        prevNextPanel.add(nextButton);
        menupanel.add(prevNextPanel);
        menupanel.add(backButton);
        backButton.setOrientation(1, (levelPerPage + 3));
    }

    private void setupMakeLevelMenu() {
        menupanel.removeAll();
        menupanel.revalidate();
        menupanel.repaint();
        menupanel.setLayout(new GridLayout(3, 1));
        levelByHand.setOrientation(1, 3);
        menupanel.add(levelByHand);
        levelByImage.setOrientation(1, 3);
        menupanel.add(levelByImage);
        backButton.setOrientation(1, 3);
        menupanel.add(backButton);
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
        sizeLabel = new JLabel(" Pálya mérete:", SwingConstants.CENTER);
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
        layerPanel.setLayout(new GridLayout(1, 2));
        layerLabel = new JLabel(" Többrétegűség:", SwingConstants.CENTER);
        layerButton = new SwitchButton(menuMe, "layer", 3, 6);
        layerPanel.add(layerLabel);
        layerPanel.add(layerButton);
        menupanel.add(layerPanel);

        colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 2));
        colorLabel = new JLabel(" Többszínűség:", SwingConstants.CENTER);
        colorButton = new SwitchButton(menuMe, "color", 3, 6);
        colorPanel.add(colorLabel);
        colorPanel.add(colorButton);
        menupanel.add(colorPanel);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 4));
        gridLabel = new JLabel(" Felosztás:", SwingConstants.CENTER);
        grid1x1 = new JButton(new ImageIcon((new ImageIcon(this.getClass().getResource("/images/1x1.png")).getImage()).getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)));
        grid1x1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gridEnable = 1;
                changeEditorMenu("grid", 0);
            }
        });
        grid2x2 = new JButton(new ImageIcon((new ImageIcon(this.getClass().getResource("/images/2x2.png")).getImage()).getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)));
        grid2x2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gridEnable = -4;
                changeEditorMenu("grid", 1);
            }
        });
        grid3x3 = new JButton(new ImageIcon((new ImageIcon(this.getClass().getResource("/images/3x3.png")).getImage()).getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)));
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
                String data = LevelEditor.templateData[0] + sizeSlider.getValue() + ";" + gridEnable + LevelEditor.templateData[1];
                if (gridEnable < 0) {
                    gridEnable *= -1;
                }
                for (int i = 0; i < (gridEnable) * ((Integer) sizeSlider.getValue() * (Integer) sizeSlider.getValue()); i++) {
                    data += ";0";
                }
                game = new MainFrame("Új pálya", menuMe, new LevelEditor(new ArrayList<String>(Arrays.asList(data.split(";"))), (Integer) sizeSlider.getValue(), "", "", true), true, layerButton.isState(), colorButton.isState(), gridEnable);
                menupanel.add(game, BorderLayout.CENTER);
                menupanel.repaint();
            }
        });
        menupanel.add(startEdit);
    }

    public void changeEditorMenu(String option, int button) {
        if (option == "grid") {
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
        } else if (option == "layer") {
            if (layerButton.isState()) {
                gridPanel.setVisible(false);
            } else {
                gridPanel.setVisible(true);
            }
        } else if (option == "&color") {
            itl.changeEditorMenu(option, button);
        } else if (option == "&layer") {
            itl.changeEditorMenu(option, button);
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
                //300-360 ig kuka  395-445 approve height 20-70ig
                if (icon.isAdminVisible()) {
                    if (e.getX() < 70) {
                        showLevel = true;
                    } else {
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

                if (showLevel) {
                    icon.showLevelToAdmin();
                } else if (deleteLevel) {
                    server.deleteLevel(icon.getLvl().getName(), icon.getLvl().getCreator_name());
                    setupOnlineLevelsMenu();
                } else if (approveLevel) {
                    server.approveLevel(icon.getLvl().getName(), icon.getLvl().getCreator_name());
                    setupOnlineLevelsMenu();
                } else if (playLevel) {
                    menupanel.removeAll();
                    menupanel.revalidate();
                    menupanel.repaint();
                    menupanel.setLayout(new BorderLayout());
                    game = new MainFrame(icon.getLvl().getName(), menuMe, icon.getLvl(), false, false, false, 0);
                    menupanel.add(game, BorderLayout.CENTER);
                    menupanel.repaint();
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
