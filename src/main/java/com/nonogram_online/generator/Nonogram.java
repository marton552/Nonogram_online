
package com.nonogram_online.generator;

import java.util.ArrayList;

public class Nonogram {

    private final int width;
    private final int height;
    private final int[][] matrix;
    private final ArrayList<Line> rows;
    private final ArrayList<Line> columns;
    private boolean changed;

    public Nonogram(ArrayList<ArrayList<Integer>> groupsHor, ArrayList<ArrayList<Integer>> groupsVert) {
        this.width = groupsVert.size();
        this.height = groupsHor.size();
        this.matrix = zero2D(this.height, this.width);
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();

        for (int i = 0; i < this.height; i++) {
            this.rows.add(new Line(groupsHor.get(i)));
        }
        for (int i = 0; i < this.width; i++) {
            this.columns.add(new Line(groupsVert.get(i)));
        }
    }

    public static int[][] zero2D(int rows, int cols) {
        int[][] array = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = 0;
            }
        }
        return array;
    }

    public ArrayList<Integer> getColumn(int j) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < this.height; i++) {
            ans.add(this.matrix[i][j]);
        }
        return ans;
    }

    public void updateMatrix(int x, int y, int value) {
        if (this.matrix[x][y] == 0 && value != 0) {
            this.matrix[x][y] = value;
            this.changed = true;
        }
    }

    public boolean isComplete() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.matrix[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean solve() {
        do {
            this.changed = false;
            for (int i = 0; i < this.height; i++) {
                this.rows.get(i).setCells(this.matrix[i]);
                if (!this.rows.get(i).solve()) {
                    return false;
                }
                for (int j = 0; j < this.width; j++) {
                    this.updateMatrix(i, j, this.rows.get(i).cells.get(j));
                }
            }

            for (int i = 0; i < this.width; i++) {
                ArrayList<Integer> res = this.getColumn(i);
                Object[] obj = res.toArray();
                this.columns.get(i).setCells(obj);
                if (!this.columns.get(i).solve()) {
                    return false;
                }
                for (int j = 0; j < this.height; j++) {
                    this.updateMatrix(j, i, this.columns.get(i).cells.get(j));
                }
            }
        } while (this.changed);
        return true;
    }

    public boolean solveAndCheck() {
        try {
            if (!this.solve()) {
                return false;
            }

            if (!this.isComplete()) {
                return false;
            } else {
                return true;
            }
        }catch(IndexOutOfBoundsException e){
            return true;
        }
    }
}
