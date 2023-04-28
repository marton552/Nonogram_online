/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.level;

import com.mycompany.nonogram_online.buttons.BasicButton;
import com.mycompany.nonogram_online.buttons.SwitchButton;
import com.mycompany.nonogram_online.Menu;
import com.mycompany.nonogram_online.buttons.ImageGeneratorButton;
import com.mycompany.nonogram_online.generator.ImageHandler;
import java.awt.BorderLayout;
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

    private JLabel sizeLabel;
    private JSlider sizeSlider;
    private JPanel colorPanel;
    private JTextField colorNumField;
    private JLabel colorLabel;
    private SwitchButton colorButton;
    private ImageGeneratorButton blackWhiteSwitch;
    private JLabel layerLabel;
    private SwitchButton layerButton;
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

    private String grid = "1x1";
    private boolean converter = true;

    private int topHeight = 50;
    private int bottomHeight = 350;
    private int centerHeight = 0;
    private int blackAndWhiteState = 0;

    private Level lvl;

    public ImageToLevel(Menu m, BufferedImage image) {
        this.m = m;
        this.image = image;
        lvl = null;

        centerHeight = m.getHeight() - topHeight - bottomHeight;

        backButton = new BasicButton(m, "Vissza", 3, 6);
        generateButton = new ImageGeneratorButton(m, "Újra generálás", 3, 6);

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
        blackWhiteSwitch = new ImageGeneratorButton(m, "FcsF", 4, 7);

        layerLabel = new JLabel("Legyen többrétegű:", SwingConstants.CENTER);
        layerButton = new SwitchButton(m, "&layer", 3, 6);

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
        convertSelectionButton.setEnabled(false);

        compressLabel = new JLabel("<html>Tömörítés. Minél balrább, annál korábban tömörít és csak utána választ színeket.</html>", SwingConstants.CENTER);
        int size = Math.min(image.getHeight(), image.getWidth());
        compressSlider = new JSlider(20, size);

        this.setLayout(new BorderLayout());
        topPanel = new JPanel(new GridLayout(1, 2));
        centerPanel = new LevelPanel(null, centerHeight - 40);
        bottomPanel = new JPanel(new GridLayout(7, 2));
        leftMultisizedPanel = new JPanel(new GridLayout(1, 2));
        rightMultisizedPanel = new JPanel(new GridLayout(1, 2));

        backButton.setHeightManualy(topHeight);
        backButton.setOrientation(2, 1);
        topPanel.add(backButton);
        generateButton.setHeightManualy(topHeight);
        generateButton.setOrientation(2, 1);
        topPanel.add(generateButton);
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

    public void setup() {
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
        bottomPanel.add(layerButton);
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

    public void generate() {
        int colorNum = 0;
        try {
            if (colorButton.isState()) {
                colorNum = Integer.parseInt(colorNumField.getText());
            } else {
                colorNum = 2;
            }
        } catch (NumberFormatException e) {
            colorNum = 2;
        }
        int blackAndWhite = -1;
        if (!colorButton.isState()) {
            blackAndWhite = blackAndWhiteState;
        }

        ImageHandler ih = new ImageHandler(image, sizeSlider.getValue(), colorNum);
        if (ih.createImage()) {
            lvl = ih.getImageAsLevel(blackAndWhite);
            centerPanel.addLevel(lvl);
            centerPanel.repaint();
            repaint();
        }
    }

    public void changeEditorMenu(String type, int num) {
        if (type == "grid") {
            grid1x1.setEnabled(true);
            grid2x2.setEnabled(true);
            grid3x3.setEnabled(true);
            if (num == 0) {
                grid1x1.setEnabled(false);
                grid = "1x1";
            } else if (num == 1) {
                grid2x2.setEnabled(false);
                grid = "2x2";
            } else if (num == 2) {
                grid3x3.setEnabled(false);
                grid = "3x3";
            }
        } else if (type == "&color") {
            if (colorButton.isState()) {
                layerButton.setVisible(false);
                leftMultisizedPanel.setVisible(true);
                rightMultisizedPanel.setVisible(true);
                colorNumField.setVisible(true);
                blackWhiteSwitch.setVisible(false);
            } else {
                layerButton.setVisible(true);
                colorNumField.setVisible(false);
                blackWhiteSwitch.setVisible(true);
            }
        } else if (type == "&layer") {
            if (layerButton.isState()) {
                colorPanel.setVisible(false);

                leftMultisizedPanel.setVisible(false);
                rightMultisizedPanel.setVisible(false);
            } else {
                colorPanel.setVisible(true);

                leftMultisizedPanel.setVisible(true);
                rightMultisizedPanel.setVisible(true);
            }
        } else if (type == "&") {
            convertSelectionButton.setEnabled(converter);
            convertAvrageButton.setEnabled(!converter);
            convertSelectionButton.repaint();
            convertAvrageButton.repaint();
            converter = !converter;
        } else if (type == "#") {
            generate();
        } else if (type == "$") {
            if (blackAndWhiteState == 0) {
                blackAndWhiteState = 1;
            } else {
                blackAndWhiteState = 0;
            }
            generate();
        }
    }

}
