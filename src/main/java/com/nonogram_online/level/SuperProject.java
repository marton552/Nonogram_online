
package com.nonogram_online.level;

import com.nonogram_online.Menu;
import com.nonogram_online.server.Response;
import com.nonogram_online.server.Server;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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

/**
 *
 * @author marton552
 */
public class SuperProject extends JPanel {

    private Menu m;
    private ArrayList<Level> sp;
    private ArrayList<String> toplist;
    private ArrayList<Integer> topnum;
    private Level placeholder;
    private int widthHeight;
    private Server server;

    public SuperProject(Menu m) {
        this.m = m;
        server = new Server();
        widthHeight = this.m.getWidth();
        sp = new ArrayList<>();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("levels/questionmark.txt");
        String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        placeholder = new Level(new ArrayList<String>(Arrays.asList(result.split(";"))), "", "", true);

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

    public void setup() {
        for (int i = 0; i < 100; i++) {
            Level newLevel = new Level(new ArrayList<String>(Arrays.asList(server.getSuperProject(i).getMessage().split(";"))), "", "", true);
            sp.add(newLevel);
        }
        toplist = new ArrayList<>();
        topnum = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Response response = server.getSPTop(i);
            if (response.getStatusCode() != 404) {
                toplist.add(response.getMessage());
                topnum.add(response.getStatusCode());
            }
        }
    }

    private void click(MouseEvent e) {
        System.out.println(e.getY() +" és "+(e.getY()> m.getHeight() - 140));
        if (e.getY() < widthHeight) {
            int x = e.getX() / ((widthHeight - 10) / 10);
            int y = e.getY() / ((widthHeight - 10) / 10);
            int num = y * 10 + x;
            if (server.getSuperProjectisDone(num).getMessage().equals("no")) {
                m.playSuperProject(sp.get(num));
            }
        }
        else if(e.getY()> m.getHeight() - 140){
            m.backToMenu(true);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setup();
        int index = 0;
        int height = (widthHeight - 10) / 10;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boolean isDone = !server.getSuperProjectisDone(index).getMessage().equals("no");
                if (isDone) {
                    sp.get(index).setMatrixStartPos((height) * j, (height) * i);
                    sp.get(index).setMenuSquareSize(height);
                    sp.get(index).finishGame();
                    sp.get(index).drawSPMatrix(g, 20);
                    index++;
                } else {
                    placeholder.setMatrixStartPos((height) * j, (height) * i);
                    placeholder.setMenuSquareSize(height);
                    placeholder.finishGame();
                    placeholder.drawSPMatrix(g, 10);
                    index++;
                }
            }
        }

        g.setColor(Color.gray);
        g.drawImage(new ImageIcon(this.getClass().getResource("/images/button.png")).getImage(), 0, m.getHeight() - 140, widthHeight, 100, null);

        FontRenderContext frc = new FontRenderContext(null, true, true);

        Font font = new Font("TimesRoman", Font.PLAIN, (int) (widthHeight / 12));
        Rectangle2D r2D = font.getStringBounds("Vissza", frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rX = (int) Math.round(r2D.getX());

        int a = (widthHeight / 2) - (rWidth / 2) - rX;

        g.setFont(font);
        g.drawString("Vissza", a, m.getHeight() - 80);

        font = new Font("TimesRoman", Font.PLAIN, (int) (widthHeight / 20));
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("Legtöbbet teljesítők:", 0, widthHeight + 10);
        for (int i = 0; i < toplist.size(); i++) {
            g.drawString((i+1)+". "+toplist.get(i) + ":  " + topnum.get(i)+"db", 0, widthHeight + 5 + (30 * (i + 1)));
        }
    }
}
