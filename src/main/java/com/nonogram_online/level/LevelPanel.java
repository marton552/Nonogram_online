
package com.nonogram_online.level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author marton552
 */
public class LevelPanel extends JPanel {
    
    private Level lvl;
    private final int widthHeight;
    private int choosenLayer = 0;
    private String error = "";
    private int choosenBackGroundColor = 0;
    private boolean blackAndWhite;

    public LevelPanel(Level lvl, int widthHeight, boolean blackAndWhite) {
        this.lvl = lvl;
        this.widthHeight = widthHeight;
        this.blackAndWhite = blackAndWhite;

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                click(e);
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

    public void setBlackAndWhite(boolean blackAndWhite) {
        this.blackAndWhite = blackAndWhite;
    }

    public int getChoosenBackGroundColor() {
        if(blackAndWhite) return 0;
        else return choosenBackGroundColor+2;
    }

    private void click(MouseEvent e) {
        if (e.getX() < 100) {
            int y = e.getY();
            y = y / 30;
            if (lvl.getMatrix().size() > y) {
                choosenLayer = y;
                repaint();
            }
        } else {
            if (e.getX() > 390 && e.getX() < 435) {
                int c = 0;
                if (e.getY() >= 70 && e.getY() <= 90) {
                    c = 1;
                } else if (e.getY() >= 95 && e.getY() <= 115) {
                    c = 2;
                } else if (e.getY() >= 120 && e.getY() <= 140) {
                    c = 3;
                } else if (e.getY() >= 145 && e.getY() <= 165) {
                    c = 4;
                } else if (e.getY() >= 170 && e.getY() <= 190) {
                    c = 5;
                } else if (e.getY() >= 195 && e.getY() <= 215) {
                    c = 6;
                } else if (e.getY() >= 220 && e.getY() <= 240) {
                    c = 7;
                } else if (e.getY() >= 245 && e.getY() <= 265) {
                    c = 8;
                } else if (e.getY() >= 270 && e.getY() <= 290) {
                    c = 9;
                }
                if (lvl.getColors().size() - 2 > c) {
                    choosenBackGroundColor = c;
                    repaint();
                }
            }
        }
    }

    public void addLevel(Level lvl) {
        this.lvl = lvl;
    }

    public void setErrors(String error) {
        this.error = error;
        repaint();
    }
    
    public boolean hasError(){
        return !"".equals(this.error);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (lvl != null) {
            if (!lvl.isIsMultisized()) {
                if(choosenLayer > lvl.getMatrix().size()) choosenLayer = 0;
                lvl.setMatrixStartPos(100, 0);
                lvl.setMenuSquareSize(widthHeight - 20);
                lvl.finishGame();
                lvl.drawMatrix(g, choosenLayer, false, true);
            } else {
                int index = 0;
                int height = (widthHeight - 20) / (int) Math.sqrt(lvl.getMatrix().size());
                for (int i = 0; i < Math.sqrt(lvl.getMatrix().size()); i++) {
                    for (int j = 0; j < Math.sqrt(lvl.getMatrix().size()); j++) {
                        lvl.setMatrixStartPos(100 + (height + 5) * i, (height + 5) * j);
                        lvl.setMenuSquareSize(height);
                        lvl.finishGame();
                        lvl.drawMatrix(g, index, false, true);
                        index++;
                    }
                }
            }
            if (lvl.getMatrix().size() > 1 && !lvl.isIsMultisized()) {
                for (int i = 0; i < lvl.getMatrix().size(); i++) {
                    Font font = new Font("TimesRoman", Font.PLAIN, (20));
                    g.setFont(font);
                    if (choosenLayer == i) {
                        g.drawString("Réteg " + (i + 1) + " <--", 0, 20 + (i * 30));
                    } else {
                        g.drawString("Réteg " + (i + 1), 0, 20 + (i * 30));
                    }
                }
            }
            if (!blackAndWhite) {
                Font font = new Font("TimesRoman", Font.PLAIN, (20));
                g.setFont(font);
                g.drawString("Háttér: ", 99 + widthHeight + 5, 30);
                for (int i = 2; i < lvl.getColors().size(); i++) {
                    Color color = lvl.getColors().get(i);
                    Color invertColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                    g.setColor(invertColor);
                    g.fillRect(99 + widthHeight + 10, 19 + ((i - 1) * 25), 42, 22);
                    g.setColor(color);
                    g.fillRect(100 + widthHeight + 10, 20 + ((i - 1) * 25), 40, 20);
                    if (i - 2 == choosenBackGroundColor) {
                        g.setColor(Color.BLACK);
                        g.drawString("<--", 100 + widthHeight + 52, 40 + ((i - 1) * 25));
                    }
                }
            }
            if (!"".equals(error)) {
                Font font = new Font("TimesRoman", Font.PLAIN, (20));
                g.setFont(font);
                g.setColor(Color.RED);
                g.drawString(error,0,widthHeight-10);
            }
        }
    }
}
