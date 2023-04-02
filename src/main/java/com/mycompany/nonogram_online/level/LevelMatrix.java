/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marton552
 */
public class LevelMatrix {

    private ArrayList<Point> rowsColls;
    private ArrayList<ArrayList<Point>> leftNumbers;
    private ArrayList<ArrayList<Point>> topNumbers;
    private int hanyszorhany;

    public LevelMatrix(ArrayList<Integer> data, int hanyszorhany) {
        rowsColls = new ArrayList<Point>();
        for (int i = 0; i < data.size(); i++) {
            rowsColls.add(new Point(-1, data.get(i),false));
        }
        this.hanyszorhany = hanyszorhany;
        setLeftNumbers();
        setTopNumbers();
    }

    private void setLeftNumbers() {
        leftNumbers = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < hanyszorhany; i++) {
            leftNumbers.add(new ArrayList<>());
            int db = 0;
            int color = -1;
            boolean solved = false;
            for (int k = 0; k < hanyszorhany; k++) {
                if(db == 0 && color == -1 && getTileBy(i, k) != 0){
                    color = getTileBy(i, k);
                    db = 1;
                    solved = getTileByIsDone(i,k);
                }
                else if(db > 0 && color == getTileBy(i, k)){
                    db++;
                    solved = solved ? getTileByIsDone(i,k) : solved;
                }
                else if(db > 0 && color != getTileBy(i, k) && getTileBy(i, k) != 0){
                    leftNumbers.get(i).add(new Point(db,color,solved));
                    color = getTileBy(i, k);
                    db = 1;
                    solved = getTileByIsDone(i,k);
                }
                else if(db > 0 && color != getTileBy(i, k) && getTileBy(i, k) == 0){
                    leftNumbers.get(i).add(new Point(db,color,solved));
                    color = -1;
                    db = 0;
                    solved = getTileByIsDone(i,k);
                }
            }
            if(db > 0) leftNumbers.get(i).add(new Point(db,color,solved));
        }
    }

    private void setTopNumbers() {
        topNumbers = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < hanyszorhany; i++) {
            topNumbers.add(new ArrayList<>());
            int db = 0;
            int color = -1;
            boolean solved = false;
            for (int k = 0; k < hanyszorhany; k++) {
                if(db == 0 && color == -1 && getTileBy(k, i) != 0){
                    color = getTileBy(k, i);
                    db = 1;
                    solved = getTileByIsDone(k,i);
                }
                else if(db > 0 && color == getTileBy(k, i)){
                    db++;
                    solved = solved ? getTileByIsDone(k,i) : solved;
                }
                else if(db > 0 && color != getTileBy(k, i) && getTileBy(k, i) != 0){
                    topNumbers.get(i).add(new Point(db,color,solved));
                    color = getTileBy(k, i);
                    db = 1;
                    solved = getTileByIsDone(k,i);
                }
                else if(db > 0 && color != getTileBy(k, i) && getTileBy(k, i) == 0){
                    topNumbers.get(i).add(new Point(db,color,solved));
                    color = -1;
                    db = 0;
                    solved = getTileByIsDone(k,i);
                }
            }
            if(db > 0) topNumbers.get(i).add(new Point(db,color,solved));
        }
    }
    
    public int mostTopNumber(){
        int res = 0;
        for (int i = 0; i < topNumbers.size(); i++) {
            res = topNumbers.get(i).size() > res ? topNumbers.get(i).size() : res;
        }
        return res;
    }
    
    public int mostLeftNumber(){
        int res = 0;
        for (int i = 0; i < leftNumbers.size(); i++) {
            res = leftNumbers.get(i).size() > res ? leftNumbers.get(i).size() : res;
        }
        return res;
    }
    
    //FORDÍTVA MŰKÖDIK!
    public void setTileBy(int colls,int rows){
        rowsColls.get(rows * hanyszorhany + colls).clickOn();
        setLeftNumbers();
        setTopNumbers();
    }
    
    public void clearTileBy(int rows, int colls){
        rowsColls.get(rows * hanyszorhany + colls).unClick();
        setLeftNumbers();
        setTopNumbers();
    }
    
    public void resetTileBy(int rows, int colls){
        rowsColls.get(rows * hanyszorhany + colls).resetColor();
        setLeftNumbers();
        setTopNumbers();
    }
    
    public void addTileBy(int rows,int colls, int color){
        rowsColls.get(rows * hanyszorhany + colls).add(color);
        setLeftNumbers();
        setTopNumbers();
    }

    public int getTileBy(int rows, int colls) {
        return rowsColls.get(rows * hanyszorhany + colls).getColor();
    }
    
    public boolean getTileByIsDone(int rows, int colls) {
        return rowsColls.get(rows * hanyszorhany + colls).isSolved();
    }
    
    public boolean getTileByIsFailed(int rows, int colls) {
        return rowsColls.get(rows * hanyszorhany + colls).isFailed();
    }

    public ArrayList<ArrayList<Point>> getLeftNumbers() {
        return leftNumbers;
    }

    public ArrayList<ArrayList<Point>> getTopNumbers() {
        return topNumbers;
    }

}
