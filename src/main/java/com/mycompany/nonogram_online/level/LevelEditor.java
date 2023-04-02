/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.level;

import com.mycompany.nonogram_online.server.NonogramFileWriter;
import com.mycompany.nonogram_online.server.Server;
import com.mycompany.nonogram_online.user.User;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author marton552
 */
public class LevelEditor extends Level {

    public static String[] templateData = {"new_level;",";1;2;rgb(255,255,255);rgb(0,0,0)"};
    
    private int selectedColor = 1;
    private Server server;

    public LevelEditor(ArrayList<String> allData, int size, String creator_name, String created_date, boolean approved) {
        super(allData,creator_name,created_date,approved);
        server = new Server();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void setSelectedColor(int c){
        selectedColor = c;
    }
    
    @Override
    public void clickOnTile(int x, int y, int layer) {
        int posX = (x - matrixStartPosX) / squareSize;
        int posY = (y - matrixStartPosY) / squareSize;
        if (posX >= 0 && posX < hanyszorhany && posY >= 0 && posY < hanyszorhany) {
            matrix.get(layer).addTileBy(posY, posX, selectedColor);
        }
        setMatrixMostLeftNumbers();
        setMatrixMostTopNumbers();
    }
    
    @Override
    public void addLayer(){
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < hanyszorhany*hanyszorhany; i++) {
            data.add(0);
        }
        matrix.add(new LevelMatrix(data, hanyszorhany));
    }
    
    @Override
    public void save(User user){
        String saveData = getName();
        saveData+=";"+hanyszorhany;
        saveData+=";"+matrix.size();
        saveData+=";"+colors.size();
        for (int i = 0; i < colors.size(); i++) {
            saveData+=";rgb("+colors.get(i).getRed()+","+colors.get(i).getGreen()+","+colors.get(i).getBlue()+")";
        }
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < hanyszorhany; j++) {
                for (int k = 0; k < hanyszorhany; k++) {
                    saveData+=";"+matrix.get(i).getTileBy(j, k);
                }
            }
        }
        System.out.println(saveData);
        server.addNewLevel(getName(),user.getFullUsername(),saveData);
        server.finishLevel(getName(),user.getFullUsername(),user.getFullUsername());
        NonogramFileWriter wr = new NonogramFileWriter(saveData);
        wr.writeToFile(getName());
    }
}
