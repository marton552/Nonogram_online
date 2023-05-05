/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online.level;

import com.nonogram_online.buttons.BasicButton;
import com.nonogram_online.buttons.SwitchButton;
import com.nonogram_online.Menu;
import com.nonogram_online.buttons.ImageGeneratorButton;
import com.nonogram_online.generator.ImageHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author marton552
 */
public class ImageToLevel extends JPanel {

    private BufferedImage image;
    private Menu m;

    private JPanel topPanel;
    private LevelPanel centerPanel;
    private JPanel bottomPanel;

    private BasicButton backButton;
    private ImageGeneratorButton generateButton;
    private ImageGeneratorButton saveButton;

    private JLabel sizeLabel;
    private JSlider sizeSlider;
    private JPanel colorPanel;
    private JTextField colorNumField;
    private JLabel colorLabel;
    private SwitchButton colorButton;
    private ImageGeneratorButton blackWhiteSwitch;
    private JLabel layerLabel;
    private JPanel layerPanel;
    private SwitchButton layerButton;
    private JTextField layerNumField;
    private JPanel leftMultisizedPanel;
    private JPanel rightMultisizedPanel;
    private JLabel multiLabel;
    private JButton grid1x1;
    private JButton grid2x2;
    private JButton grid3x3;
    private JLabel convertAvrageLabel;
    private JLabel convertSelectionLabel;
    private ImageGeneratorButton convertAvrageButton;
    private ImageGeneratorButton convertSelectionButton;
    private JLabel compressLabel;
    private JSlider compressSlider;

    private int grid = 1;
    private boolean converter = false;

    private int topHeight = 50;
    private int bottomHeight = 350;
    private int centerHeight = 0;
    private int blackAndWhiteState = 0;

    private Level lvl;
    private ImageHandler ih;
    private Color backColor;

    public ImageToLevel(Menu m, BufferedImage image) {
        this.m = m;
        this.image = image;
        lvl = null;

        centerHeight = m.getHeight() - topHeight - bottomHeight;
        backColor = Color.WHITE;

        backButton = new BasicButton(m, "Vissza", 3, 6);
        generateButton = new ImageGeneratorButton(m, "Újra generálás", 3, 6);
        saveButton = new ImageGeneratorButton(m, "Mentés", 3, 6);

        sizeLabel = new JLabel("Nonogram mérete:", SwingConstants.CENTER);
        sizeSlider = new JSlider(5, 20);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setSnapToTicks(true);
        sizeSlider.setMinorTickSpacing(5);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);

        colorLabel = new JLabel("Legyen színes:", SwingConstants.CENTER);
        colorPanel = new JPanel(new GridLayout(1, 3));
        colorNumField = new JTextField("2");
        colorNumField.setVisible(false);
        colorButton = new SwitchButton(m, "&color", 3, 6);
        blackWhiteSwitch = new ImageGeneratorButton(m, "@", 4, 7);

        layerLabel = new JLabel("Legyen többrétegű:", SwingConstants.CENTER);
        layerPanel = new JPanel(new GridLayout(1, 2));
        layerButton = new SwitchButton(m, "&layer", 3, 6);
        layerNumField = new JTextField("2");

        leftMultisizedPanel = new JPanel(new GridLayout(1, 2));
        rightMultisizedPanel = new JPanel(new GridLayout(1, 2));
        multiLabel = new JLabel("Felosztás:", SwingConstants.CENTER);
        grid1x1 = new JButton(new ImageIcon((new ImageIcon(this.getClass().getResource("/images/1x1.png")).getImage()).getScaledInstance(bottomHeight / 7, bottomHeight / 7, java.awt.Image.SCALE_SMOOTH)));
        grid1x1.setSize(bottomHeight / 7, bottomHeight / 7);
        grid1x1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeEditorMenu("grid", 0);
            }
        });
        grid1x1.setEnabled(false);
        grid2x2 = new JButton(new ImageIcon((new ImageIcon(this.getClass().getResource("/images/2x2.png")).getImage()).getScaledInstance(bottomHeight / 7, bottomHeight / 7, java.awt.Image.SCALE_SMOOTH)));
        grid2x2.setSize(bottomHeight / 7, bottomHeight / 7);
        grid2x2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeEditorMenu("grid", 1);
            }
        });
        grid2x2.setEnabled(true);
        grid3x3 = new JButton(new ImageIcon((new ImageIcon(this.getClass().getResource("/images/3x3.png")).getImage()).getScaledInstance(bottomHeight / 7, bottomHeight / 7, java.awt.Image.SCALE_SMOOTH)));
        grid3x3.setSize(bottomHeight / 7, bottomHeight / 7);
        grid3x3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeEditorMenu("grid", 2);
            }
        });
        grid3x3.setEnabled(true);

        convertAvrageLabel = new JLabel("<html>Ez a convertálás hasznosabb<br>magas felbontású képek esetében.</html>", SwingConstants.CENTER);
        convertSelectionLabel = new JLabel("<html>Ez a convertálás hasznosabb<br>kevés színnel dolgozó kisebb képeknél<br>esetleg pixelartoknál.</html>", SwingConstants.CENTER);

        convertAvrageButton = new ImageGeneratorButton(m, "ÁtlagKonverter", 3, 6);
        convertSelectionButton = new ImageGeneratorButton(m, "KiválasztóKonverter", 3, 6);
        convertAvrageButton.setEnabled(false);

        compressLabel = new JLabel("<html>Tömörítés. Minél balrább, annál korábban tömörít és csak utána választ színeket.</html>", SwingConstants.CENTER);
        int size = Math.min(image.getHeight(), image.getWidth());
        compressSlider = new JSlider(20, size);

        this.setLayout(new BorderLayout());
        topPanel = new JPanel(new GridLayout(1, 3));
        centerPanel = new LevelPanel(null, centerHeight - 40, true);
        bottomPanel = new JPanel(new GridLayout(7, 2));
        leftMultisizedPanel = new JPanel(new GridLayout(1, 2));
        rightMultisizedPanel = new JPanel(new GridLayout(1, 2));

        backButton.setHeightManualy(topHeight);
        backButton.setOrientation(3, 1);
        topPanel.add(backButton);
        generateButton.setHeightManualy(topHeight);
        generateButton.setOrientation(3, 1);
        saveButton.setHeightManualy(topHeight);
        saveButton.setOrientation(3, 1);
        topPanel.add(generateButton);
        topPanel.add(saveButton);
        topPanel.setPreferredSize(new Dimension(m.getWidth(), topHeight));
        topPanel.setMaximumSize(new Dimension(m.getWidth(), topHeight));
        this.add(topPanel, BorderLayout.NORTH);
        centerPanel.setPreferredSize(new Dimension(m.getWidth(), centerHeight));
        centerPanel.setMaximumSize(new Dimension(m.getWidth(), centerHeight));
        this.add(centerPanel, BorderLayout.CENTER);

    }

    public void addImage(BufferedImage im) {
        image = im;
    }

    public void setup(boolean fromMenu) {
        if (fromMenu) {
            converter = false;
            colorNumField.setText("2");
            layerNumField.setText("2");
            sizeSlider.setValue(10);
            colorButton.setState(false);
            changeEditorMenu("&color", 0);
            changeEditorMenu("&layer", 0);
            layerButton.setState(false);
            changeEditorMenu("grid", 0);
            convertSelectionButton.setEnabled(true);
            convertAvrageButton.setEnabled(false);
            leftMultisizedPanel.setVisible(true);
            rightMultisizedPanel.setVisible(true);
        }

        bottomPanel.add(sizeLabel);
        bottomPanel.add(sizeSlider);
        bottomPanel.add(colorLabel);
        colorButton.setHeightManualy(bottomHeight);
        colorButton.setWidthManualy(m.getWidth() / 4);
        colorButton.setOrientation(2, 7);
        colorPanel.add(colorButton);
        colorPanel.add(colorNumField);
        blackWhiteSwitch.setHeightManualy(bottomHeight);
        blackWhiteSwitch.setOrientation(6, 7);
        colorPanel.add(blackWhiteSwitch);
        bottomPanel.add(colorPanel);

        bottomPanel.add(layerLabel);
        layerButton.setHeightManualy(bottomHeight);
        layerButton.setWidthManualy(m.getWidth() / 4);
        layerButton.setOrientation(2, 7);
        layerPanel.add(layerButton);
        layerPanel.add(layerNumField);
        layerNumField.setVisible(false);
        bottomPanel.add(layerPanel);

        leftMultisizedPanel.add(multiLabel);
        leftMultisizedPanel.add(grid1x1);
        bottomPanel.add(leftMultisizedPanel);
        rightMultisizedPanel.add(grid2x2);
        rightMultisizedPanel.add(grid3x3);
        bottomPanel.add(rightMultisizedPanel);
        bottomPanel.add(convertAvrageLabel);
        bottomPanel.add(convertSelectionLabel);
        convertAvrageButton.setHeightManualy(bottomHeight);
        convertAvrageButton.setOrientation(2, 7);
        bottomPanel.add(convertAvrageButton);
        convertSelectionButton.setHeightManualy(bottomHeight);
        convertSelectionButton.setOrientation(2, 7);
        bottomPanel.add(convertSelectionButton);
        bottomPanel.add(compressLabel);
        bottomPanel.add(compressSlider);
        bottomPanel.setPreferredSize(new Dimension(m.getWidth(), bottomHeight));
        bottomPanel.setMaximumSize(new Dimension(m.getWidth(), bottomHeight));
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.repaint();

        setVisible(true);
    }

    public void generate(boolean pixelise) {
        centerPanel.setErrors("");
        int colorNum = 0;
        try {
            if (colorButton.isState()) {
                colorNum = Integer.parseInt(colorNumField.getText());
                if (colorNum < 2) {
                    colorNum = 2;
                    centerPanel.setErrors("Legalább 2 színnek kell lennie!");
                } else if (colorNum > 8) {
                    colorNum = 8;
                    centerPanel.setErrors("Maximum 8 szín lehet!");
                }
            } else {
                colorNum = 2;
            }
        } catch (NumberFormatException e) {
            centerPanel.setErrors("Nem szám a bevitt szín mennyiség!");
        }
        int blackAndWhite = -1;
        if (!colorButton.isState()) {
            blackAndWhite = blackAndWhiteState;
            centerPanel.setBlackAndWhite(true);
        } else {
            centerPanel.setBlackAndWhite(false);
        }

        int layerNum = 1;
        try {
            if (layerButton.isState()) {
                layerNum = Integer.parseInt(layerNumField.getText());
                if (layerNum < 1) {
                    layerNum = 1;
                    centerPanel.setErrors("Legalább 1 rétegnek kell lennie!");
                } else if (layerNum > 10) {
                    layerNum = 10;
                    centerPanel.setErrors("Maximum 10 réteg lehet!");
                }
            } else {
                layerNum = 1;
            }
        } catch (NumberFormatException e) {
            layerNum = 1;
            centerPanel.setErrors("Nem szám a bevitt réteg mennyiség!");
        }
        if (!centerPanel.hasError()) {
            int grided = (int) Math.sqrt((grid == 1 ? 1 : grid * -1));
            ih = new ImageHandler(image, compressSlider.getValue(), sizeSlider.getValue() * grided, colorNum);
            if (((pixelise) ? (converter ? ih.createSelectionImage() : ih.createAvgImage()) : true)) {
                lvl = ih.getImageAsLevel(blackAndWhite, layerNum, 0, grid);
                if (ih.getError() != "") {
                    centerPanel.setErrors(ih.getError());
                } else {
                    centerPanel.addLevel(lvl);
                    centerPanel.repaint();
                    repaint();
                    ih.setError("");
                }
            } else {
                centerPanel.setErrors("Nem lehetséges a generálás! Túl sok a kért szín.");
            }
        }
    }

    public Level getLvl() {
        return lvl;
    }

    public int getLvlSize() {
        return sizeSlider.getValue();
    }

    public boolean isColored() {
        return colorButton.isState();
    }

    public boolean isLayered() {
        return layerButton.isState();
    }

    public int getGrid() {
        return grid;
    }

    private void publishLevel() {
        ih.setBackgroundColor(backColor);
        lvl = ih.getFullLevel();
        m.menuActions("imageEdit");
    }

    public void save() {
        backColor = ih.getFullLevel().getColors().get(centerPanel.getChoosenBackGroundColor());
        if (ih.save()) {
            publishLevel();
        } else {
            saveButton.setText("Színek egyesítése");
            saveButton.repaint();
            centerPanel.setErrors("Vannak színek amik túl hasonlóak!");
        }
    }

    private void colorSum() {
        saveButton.setText("Mentés");
        saveButton.repaint();
        ih.colorSum();
        publishLevel();
    }

    public void changeEditorMenu(String type, int num) {
        if (type == "grid") {
            grid1x1.setEnabled(true);
            grid2x2.setEnabled(true);
            grid3x3.setEnabled(true);
            if (num == 0) {
                grid1x1.setEnabled(false);
                grid = 1;
            } else if (num == 1) {
                grid2x2.setEnabled(false);
                grid = -4;
            } else if (num == 2) {
                grid3x3.setEnabled(false);
                grid = -9;
            }
        } else if (type == "&color") {
            colorButton.repaint();
            if (colorButton.isState()) {
                colorNumField.setVisible(true);
                blackWhiteSwitch.setVisible(false);
            } else {
                colorNumField.setVisible(false);
                blackWhiteSwitch.setVisible(true);
            }
        } else if (type == "&layer") {
            layerButton.repaint();
            if (layerButton.isState()) {
                layerNumField.setVisible(true);

                leftMultisizedPanel.setVisible(false);
                rightMultisizedPanel.setVisible(false);
            } else {
                layerNumField.setVisible(false);

                leftMultisizedPanel.setVisible(true);
                rightMultisizedPanel.setVisible(true);
            }
        } else if (type == "&") {
            convertSelectionButton.setEnabled(converter);
            convertAvrageButton.setEnabled(!converter);
            convertSelectionButton.repaint();
            convertAvrageButton.repaint();
            converter = !converter;
            compressSlider.setValue(50);
            if (converter) {
                compressSlider.setMaximum(200);
            } else {
                compressSlider.setMaximum(Math.max(image.getHeight(), image.getWidth()));
            }
        } else if (type == "#") {
            generate(true);
        } else if (type == "$") {
            if (blackAndWhiteState == 0) {
                blackAndWhiteState = 1;
            } else {
                blackAndWhiteState = 0;
            }
            generate(true);
        } else if (type == "+") {
            save();
        } else if (type == "-") {
            colorSum();
        }
    }

}
