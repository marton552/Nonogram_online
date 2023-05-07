
package com.nonogram_online;

import com.nonogram_online.buttons.BasicRate;
import com.nonogram_online.buttons.FailButton;
import com.nonogram_online.generator.ColorHandler;
import com.nonogram_online.level.Level;
import com.nonogram_online.server.NonogramFileWriter;
import com.nonogram_online.server.Response;
import com.nonogram_online.server.Server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
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
    
    private static final String FINISHSTRING = "Befejezés";

    private GamePanel gamePanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private final Level lvl;
    private final int sWidth = 465;
    private final int sHeight = 690;

    private Game game;
    private final Menu m;

    private final JButton prevLayerButton;
    private final JButton nextLayerButton;
    private JButton hintButton;

    private JPanel titlePanel;
    private JTextField titleEdit;
    private final JLabel titleLabel;
    private JButton giveUpButton;

    private final JButton addLayer;
    private final JButton[] colorButtons;
    private final JLabel selectedColor;
    private final JLabel colorError;
    private final JButton deleteColor;
    private int currentColor = 0;
    private JPanel colorPanel;
    private final BasicRate ratePopUp;

    private final JButton isSolvableButton;
    private JLabel solvableLabel;
    private JButton saveButton;

    private final FailButton failButton;
    private Server server;

    private final boolean isEditing;
    private final boolean isLayered;
    private final boolean isColored;

    private boolean isRating = false;
    private boolean isMultisized;
    private boolean isChoosing;
    private Level placeholder;
    private boolean zeroLayer = false;

    public MainFrame(String title, Menu m, Level lvl, boolean isEditing, boolean isLayered, boolean isColored, int isGrided) {
        this.lvl = lvl;
        this.m = m;
        placeholder = new Level(lvl.getAllData(), lvl.getCreator_name(), lvl.getCreated_date(), lvl.isApproved());

        this.isEditing = isEditing;
        this.isLayered = isLayered;
        this.isColored = isColored;

        isMultisized = lvl.isIsMultisized();
        isChoosing = isMultisized;

        game = new Game(lvl, isEditing);
        server = new Server();
        failButton = new FailButton(m, this, 1, 3);

        titleLabel = new JLabel(lvl.getName(), SwingConstants.CENTER);
        selectedColor = new JLabel("Ez a választott szín", SwingConstants.CENTER);
        colorError = new JLabel("", SwingConstants.CENTER);
        solvableLabel = new JLabel("", SwingConstants.CENTER);

        titleEdit = new JTextField("");
        titleEdit.setForeground(Color.GRAY);
        titleEdit.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (titleEdit.getText().equals("Név megadása")) {
                    titleEdit.setText("");
                    titleEdit.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (titleEdit.getText().isEmpty()) {
                    titleEdit.setForeground(Color.GRAY);
                    titleEdit.setText("Név megadása");
                }
            }
        });

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
        }
        );

        if (isEditing) {
            giveUpButton = new JButton("Vissza");
        } else {
            giveUpButton = new JButton("Feladás");
        }

        ratePopUp = new BasicRate(m, lvl, 0, 1, 4);

        giveUpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (giveUpButton.getText().equals(FINISHSTRING)) {
                    Response res;
                    if (!lvl.getCreator_name().equals("")) {
                        res = server.finishLevel(lvl.getName(), lvl.getCreator_name(), m.getUser().getFullUsername());
                        if (res.equalsStatusCode(200)) {
                            isRating = true;
                            setup();
                        } else {
                            m.backToMenu(true);
                        }
                    } else {
                        if(lvl.getName().length() < 4){
                            server.completeSuperProject(Integer.parseInt(lvl.getName()), m.getUser().getFullUsername());
                        }
                        NonogramFileWriter fw = new NonogramFileWriter("");
                        fw.saveLocalData(m.getUser().getFullUsername(), lvl.getName());
                        m.backToMenu(true);
                    }
                }
                if (giveUpButton.getText().equals(" Vissza ") || giveUpButton.getText().equals(" Befejezés ")) {
                    isChoosing = true;
                    giveUpButton.setText("Feladás");
                    setup();
                } else if (giveUpButton.getText().equals("Vissza") || giveUpButton.getText().equals("Feladás")) {
                    m.backToMenu(false);
                }
            }
        });

        colorButtons = new JButton[11];

        colorButtons[10] = new JButton("🚩");
        colorButtons[10].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseColor(0);
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
                if (game.lvl.isSolvable()) {
                    saveButton.setEnabled(true);
                    solvableLabel.setText("Megoldható ✓");
                    solvableLabel.setForeground(Color.green);
                }
                else{
                    solvableLabel.setText("Nem megoldható!");
                    solvableLabel.setForeground(Color.red);
                }
            }
        });

        saveButton = new JButton("Mentés");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveButton.getText().equals("Mentés")) {
                    titlePanel.removeAll();
                    titlePanel.add(titleEdit);
                    saveButton.setEnabled(false);
                    saveButton.setText("Publikálás");
                    checkSave();
                } else if (saveButton.getText().equals("Publikálás")) {
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

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isMultisized && isChoosing) {
                    chooseMulti(e.getX(), e.getY());
                    repaint();
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

        setup();

    }

    private void setup() {
        this.removeAll();
        this.revalidate();
        this.repaint();
        this.setLayout(new BorderLayout());
        if (!isChoosing) {
            gamePanel = new GamePanel(this, game, sWidth, sHeight);
            if (isRating) {
                this.add(ratePopUp, BorderLayout.CENTER);
            } else {
                this.add(gamePanel, BorderLayout.CENTER);
            }
        }
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
        selectedColor.setVisible(true);
        topPanel.add(selectedColor);
        topPanel.add(colorError);
        bottomPanel.add(prevLayerButton);
        bottomPanel.add(nextLayerButton);
        bottomPanel.add(addLayer);
        addLayer.setVisible(false);
        int index = 0;
        if(!isEditing) index = 1;
        for (int i = index; i < lvl.getColors().size(); i++) {
            colorButtons[i] = new JButton(i + "");
            colorButtons[i].setBackground(lvl.getColors().get(i));
            colorButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chooseColor(Integer.parseInt(((JButton) e.getSource()).getText()));
                    gamePanel.repaint();
                }
            });
            colorPanel.add(colorButtons[i]);
        }
        if (isEditing) {
            bottomPanel.add(isSolvableButton);
            bottomPanel.add(solvableLabel);
            bottomPanel.add(saveButton);
            if (isColored) {
                topPanel.add(deleteColor);
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
        this.repaint();

        setVisible(true);
    }

    private void chooseMulti(int x, int y) {
        int size = (int) Math.sqrt(placeholder.getMatrix().size());
        int pWidth = sWidth / size;
        x = (x / pWidth);
        y = y - (sHeight / 2 - sWidth / 2);
        y = y / pWidth;
        isChoosing = false;
        game.setActualLayer(size * y + x);
        if (size * y + x == 0) {
            game.setActualLayer(1);
            zeroLayer = true;
        }
        gamePanel = new GamePanel(this, game, sWidth, sHeight);
        this.add(gamePanel, BorderLayout.CENTER);
        setPrevNextVisible();
        giveUpButton.setText(" Vissza ");
    }

    private void paintMultisizedMatrix(Graphics g) {
        if (isEditing) {
            String data = lvl.export();
            placeholder = new Level(new ArrayList<String>(Arrays.asList(data.split(";"))), lvl.getCreator_name(), lvl.getCreated_date(), lvl.isApproved());
        }
        if (isChoosing) {
            int size = (int) Math.sqrt(placeholder.getMatrix().size());
            int pWidth = sWidth / size;
            int x = 0;
            int y = 0;
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("levels/questionmark.txt");
            String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
            Level questionmark = new Level(new ArrayList<String>(Arrays.asList(result.split(";"))), "", "", true);
            for (int i = 0; i < placeholder.getMatrix().size(); i++) {
                if (placeholder.isThisPartCompleted(i) || isEditing) {
                    placeholder.setMatrixStartPos(x * pWidth, sHeight / 2 - sWidth / 2 + (y * pWidth));
                    placeholder.setMenuSquareSize(pWidth);
                    placeholder.finishGame();
                    placeholder.drawMatrix(g, i, false, true);
                } else {
                    questionmark.setMatrixStartPos(x * pWidth, sHeight / 2 - sWidth / 2 + (y * pWidth));
                    questionmark.setMenuSquareSize(pWidth);
                    questionmark.finishGame();
                    questionmark.drawMatrix(g, 0, false, true);
                }
                x++;
                if (x == size) {
                    x = 0;
                    y++;
                }
            }
        }
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
        } else if (server.isLevelExist(titleEdit.getText(), m.getUser().getFullUsername()).equalsStatusCode(200)) {
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
        if (num == 0 && !isEditing) {
            selectedColor.setText("Zászló kiválasztva");
        } else {
            selectedColor.setText("Ez a választott szín");
        }
        topPanel.revalidate();
        topPanel.repaint();
        game.lvl.setSelectedColor(num);
    }

    private void addColor() {
        Color color = JColorChooser.showDialog(null, "Válassz színt!", Color.BLACK);
        if(color != null && !ColorHandler.findCloseColors(lvl.getColors(),color).isEmpty()){
            colorError.setText("Ez a szín túl hasonló egy másikhoz!");
        }
        else if (color != null) {
            colorError.setText("");
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
            colorError.setText("Az alap színt nem lehet törölni!");
        } else {
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
        if (isMultisized) {
            prevLayerButton.setVisible(false);
            nextLayerButton.setVisible(false);
        }
        if (!isChoosing) {
            gamePanel.repaint();
        }
    }

    public void finishLevel() {
        giveUpButton.setText(FINISHSTRING);
        this.repaint();
    }

    public void finishLayer() {
        if (isMultisized) {
            giveUpButton.setText(" Befejezés ");
            placeholder.addCompletedPart(game.getActualLayer());
            boolean finish = true;
            for (int i = 0; i < placeholder.getMatrix().size(); i++) {
                if (!placeholder.isThisPartCompleted(i)) {
                    finish = false;
                }
            }
            if (finish) {
                placeholder.finishGame();
                isChoosing = true;
                giveUpButton.setText(FINISHSTRING);
                setup();
            }
        }
        this.repaint();
    }

    void loseGame() {
        this.removeAll();
        this.revalidate();
        this.repaint();
        this.setLayout(new BorderLayout());
        this.add(failButton, BorderLayout.CENTER);
    }

    public void retry() {
        setup();
        game.retry();
        hintButton.setVisible(true);
        hintButton.setText("Segítség 3/3");
    }
    
    public Menu getMenu(){
        return m;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setVisible(true);
        if (isRating) {
            gamePanel.repaint();
            ratePopUp.repaint();
        }
        if (isChoosing) {
            paintMultisizedMatrix(g);
        }
        if (zeroLayer) {
            game.setLayer(-1);
            setPrevNextVisible();
            zeroLayer = false;
        }
    }

}
