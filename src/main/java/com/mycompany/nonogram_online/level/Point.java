/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.level;

/**
 *
 * @author marton552
 */
public class Point {
    private int num;
    private int color;
    private boolean solved;
    private boolean failed;

    public Point(int num, int color, boolean solved) {
        this.num = num;
        this.color = color;
        this.solved = solved;
        this.failed = false;
    }

    public int getNum() {
        return num;
    }

    public int getColor() {
        return color;
    }

    public boolean isSolved() {
        return solved;
    }
    
    @Override
    public String toString() {
    return num+","+color;
  }
    
    public void clickOn(){
        this.solved = true;
        if(color == 0) failed = true;
    }
    
    public void add(int color){
        this.color = color;
        this.solved = true;
    }
    
    public void unClick(){
        this.solved = false;
    }
    
    public void resetColor(){
        this.color = 0;
    }

    public boolean isFailed() {
        return failed;
    }
    
}
