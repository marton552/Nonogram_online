/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online;

import com.mycompany.nonogram_online.level.Level;
import com.mycompany.nonogram_online.server.NonogramFileWriter;
import com.mycompany.nonogram_online.server.Response;
import com.mycompany.nonogram_online.server.Server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author marton552
 */
public class MainFrame extends JPanel {

    private GamePanel gamePanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private Level lvl;
    private int width = 465;
    private int height = 690;

    private Game game;
    private Menu m;

    private JButton prevLayerButton;
    private JButton nextLayerButton;
    private JButton hintButton;

    private JPanel titlePanel;
    private JTextField titleEdit;
    private JLabel titleLabel;
    private JButton giveUpButton;

    private JButton addLayer;
    private JButton[] colorButtons;
    private JButton addColor;
    private JLabel selectedColor;
    private String error;
    private JLabel colorError;
    private JButton deleteColor;
    private int currentColor = 0;
    private JPanel colorPanel;

    private JButton isSolvableButton;
    private JLabel solvableLabel;
    private JButton saveButton;

    private Server server;

    public MainFrame(String title, Menu m, Level lvl, boolean isEditing, boolean isLayered, boolean isColored, int isGrided) {
        this.lvl = lvl;
        this.m = m;

        game = new Game(lvl, isEditing);
        server = new Server();

        titleLabel = new JLabel(lvl.getName(), SwingConstants.CENTER);
        selectedColor = new JLabel("Ez a választott szín", SwingConstants.CENTER);
        colorError = new JLabel("", SwingConstants.CENTER);
        solvableLabel = new JLabel("", SwingConstants.CENTER);

        titleEdit = new JTextField("");
        titleEdit.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                checkSave();
            }

            public void removeUpdate(DocumentEvent e) {
                checkSave();
            }

            public void insertUpdate(DocumentEvent e) {
                checkSave();
            }
        });

        if (isEditing) {
            giveUpButton = new JButton("Vissza");
        } else {
            giveUpButton = new JButton("Feladás");
        }

        giveUpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (giveUpButton.getText() != "Feladás") {
                    Response res;
                    if (lvl.getCreator_name() != "") {
                        res = server.finishLevel(lvl.getName(), lvl.getCreator_name(), m.getUser().getFullUsername());
                        if (res.getStatusCode() == 200) {
                            //do rate level implementation
                        }
                    } else {
                        NonogramFileWriter fw = new NonogramFileWriter("");
                        fw.saveLocalData(m.getUser().getFullUsername(), lvl.getName());
                    }

                }
                m.backToMenu(false);
            }
        });

        colorButtons = new JButton[11];
        colorButtons[0] = new JButton("0");
        colorButtons[0].setBackground(Color.WHITE);
        colorButtons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseColor(0);
            }
        });
        colorButtons[1] = new JButton("1");
        colorButtons[1].setBackground(Color.BLACK);
        colorButtons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseColor(1);
            }
        });

        colorButtons[10] = new JButton("🚩");
        colorButtons[10].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        deleteColor = new JButton("Kiválasztott szín törlése");
        deleteColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteColor();
            }
        });

        isSolvableButton = new JButton("Megoldhatóság check");
        isSolvableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //todo: implementation
                saveButton.setEnabled(true);
                solvableLabel.setText("Megoldható ✓");
                solvableLabel.setForeground(Color.green);
            }
        });

        saveButton = new JButton("Mentés");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveButton.getText() == "Mentés") {
                    titlePanel.removeAll();
                    titlePanel.add(titleEdit);
                    saveButton.setEnabled(false);
                    saveButton.setText("Publikálás");
                    checkSave();
                } else if (saveButton.getText() == "Publikálás") {
                    game.lvl.setName(titleEdit.getText());
                    game.lvl.save(m.getUser());
                    m.backToMenu(false);
                }
            }
        });
        saveButton.setEnabled(false);

        prevLayerButton = new JButton("Előző réteg");
        prevLayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.setLayer(-1);
                setPrevNextVisible();
            }
        });

        nextLayerButton = new JButton("Következő réteg");
        nextLayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.setLayer(1);
                setPrevNextVisible();
            }
        });

        hintButton = new JButton("Segítség 3/3");
        hintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.useRemainingHelp();
                if (game.getRemainingHelp() == 0) {
                    hintButton.setVisible(false);
                }
                hintButton.setText("Segítség " + game.getRemainingHelp() + "/3");
                gamePanel.repaint();
            }
        });

        addLayer = new JButton("Réteg hozzáadása");
        addLayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.addLayer();
                setPrevNextVisible();
            }
        });

        this.setLayout(new BorderLayout());
        gamePanel = new GamePanel(this, game, width, height);
        this.add(gamePanel, BorderLayout.CENTER);

        topPanel = new JPanel(new GridLayout(3, 2));
        bottomPanel = new JPanel(new GridLayout(2, 3));
        colorPanel = new JPanel(new GridLayout(1, 10));
        this.add(topPanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.SOUTH);
        titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(titlePanel);
        topPanel.add(giveUpButton);
        topPanel.add(colorPanel);
        selectedColor.setVisible(false);
        topPanel.add(selectedColor);
        topPanel.add(colorError);
        bottomPanel.add(prevLayerButton);
        bottomPanel.add(nextLayerButton);
        bottomPanel.add(addLayer);
        addLayer.setVisible(false);
        if (isEditing) {
            bottomPanel.add(isSolvableButton);
            bottomPanel.add(solvableLabel);
            bottomPanel.add(saveButton);
            if (isColored) {
                selectedColor.setVisible(true);
                topPanel.add(deleteColor);
                colorPanel.add(colorButtons[0]);
                colorPanel.add(colorButtons[1]);
                colorButtons[10] = new JButton("+");
                colorButtons[10].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addColor();
                    }
                });
                colorPanel.add(colorButtons[10]);
            }
            if (isLayered) {
                addLayer.setVisible(true);
            }
            chooseColor(1);
        } else {
            colorPanel.add(colorButtons[10]);
            bottomPanel.add(hintButton);
        }

        setPrevNextVisible();
        repaint();

        setVisible(true);

    }

    private void checkSave() {
        colorError.setOpaque(true);
        if (titleEdit.getText().length() == 0) {
            colorError.setForeground(Color.white);
            colorError.setBackground(Color.red);
            colorError.setText("A név megadása kötelező!");
            saveButton.setEnabled(false);
        } else if (titleEdit.getText().length() < 4) {
            colorError.setForeground(Color.red);
            colorError.setBackground(Color.WHITE);
            colorError.setText("A név nem lehet rövidebb 4 karakternél!");
            saveButton.setEnabled(false);
        } else if (titleEdit.getText().length() > 20) {
            colorError.setForeground(Color.red);
            colorError.setBackground(Color.WHITE);
            colorError.setText("A név nem lehet hosszabb 20 karakternél!");
            saveButton.setEnabled(false);
        } else if (server.isLevelExist(titleEdit.getText(), m.getUser().getFullUsername()).getStatusCode() == 200) {
            colorError.setForeground(Color.red);
            colorError.setBackground(Color.WHITE);
            colorError.setText("Létezik már ilyen nevű pályád!");
            saveButton.setEnabled(false);
        } else {
            colorError.setForeground(Color.green);
            colorError.setBackground(Color.WHITE);
            colorError.setText("  ✓");
            saveButton.setEnabled(true);
        }
    }

    private void chooseColor(int num) {
        currentColor = num;
        Color color = lvl.getColors().get(num);
        Color invertColor = new Color((255 - color.getRed()) / 255.0f, (255 - color.getGreen()) / 255.0f, (255 - color.getBlue()) / 255.0f, 1);
        selectedColor.setForeground(invertColor);
        selectedColor.setBackground(color);
        selectedColor.setOpaque(true);
        topPanel.revalidate();
        topPanel.repaint();
        game.lvl.setSelectedColor(num);
    }

    private void addColor() {
        JColorChooser c = new JColorChooser();
        Color color = c.showDialog(null, "Válassz színt!", Color.BLACK);
        if (color != null) {
            colorPanel.remove(colorButtons[10]);
            colorButtons[lvl.getColors().size()] = new JButton((lvl.getColors().size()) + "");
            colorButtons[lvl.getColors().size()].setBackground(color);
            colorButtons[lvl.getColors().size()].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chooseColor(Integer.parseInt(((JButton) e.getSource()).getText()));
                }
            });
            colorPanel.add(colorButtons[lvl.getColors().size()]);
            colorPanel.add(colorButtons[10]);
            colorPanel.revalidate();
            colorPanel.repaint();
            lvl.addColor(color);
        }
    }

    private void deleteColor() {
        if (currentColor == 0) {
            error = "Az alap színt nem lehet törölni!";
        } else {
            System.out.println(currentColor);
            Color curr = colorButtons[currentColor].getBackground();
            for (int i = currentColor; i < lvl.getColors().size(); i++) {
                colorButtons[i].setText((i) + "");
                if (i != lvl.getColors().size() - 1) {
                    colorButtons[i].setBackground(game.lvl.getColors().get(i + 1));
                }
            }
            game.lvl.removeColor(curr);
            colorPanel.remove(colorButtons[lvl.getColors().size()]);
            currentColor--;
            chooseColor(currentColor);
            topPanel.revalidate();
            topPanel.repaint();
        }
    }

    private void setPrevNextVisible() {
        if (game.getActualLayer() > 0) {
            prevLayerButton.setVisible(true);
        } else {
            prevLayerButton.setVisible(false);
        }
        if (game.getActualLayer() + 1 < game.getLayerCount()) {
            nextLayerButton.setVisible(true);
        } else {
            nextLayerButton.setVisible(false);
        }
        gamePanel.repaint();
    }

    public void finishLevel() {
        giveUpButton.setText("Befejezés");
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.repaint();
        this.setVisible(true);
    }

}
