/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author marton552
 */
public class Line {
    private ArrayList<Integer> groups;
    private int gn;
    private int[] restLength;
    private int length;
    public List<Integer> cells;
    private int[] sure;
    private int[] cur;
    private int[] ansLine;
    private int realFound;

    public Line(ArrayList<Integer> groups) {
        this.groups = groups;
        this.gn = groups.size();
        cells = new ArrayList<>();
        
        if (this.gn > 0) {
            this.restLength = zero1D(this.gn);
            groups.set(this.gn - 1, groups.get(this.gn - 1));
            for (int i = this.gn - 2; i >= 0; i--) {
                this.restLength[i] = groups.get(i) + 1 + this.restLength[i + 1];
            }
        }
    }
    
    public static int[] zero1D(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 0;
        }
        return arr;
    }
    
    public void setCells(Object[] cells) {
        this.length = cells.length;
        for (int i = 0; i < cells.length; i++) {
            this.cells.add((int)cells[i]);
        }

        this.sure = zero1D(this.length);
        for (int i = 0; i < this.length; i++) {
            if (this.cells.get(i) != 0) {
                this.sure[i] = 1;
            }
        }

        this.cur = zero1D(this.length);
        this.ansLine = zero1D(this.length);
    }
    
    public void setCells(int[] cells) {
        this.length = cells.length;
        for (int i = 0; i < cells.length; i++) {
            this.cells.add((int)cells[i]);
        }

        this.sure = zero1D(this.length);
        for (int i = 0; i < this.length; i++) {
            if (this.cells.get(i) != 0) {
                this.sure[i] = 1;
            }
        }

        this.cur = zero1D(this.length);
        this.ansLine = zero1D(this.length);
    }

    private void checkFinal(int pos) {       
        for (int i = pos; i < this.length; i++) {
            if (this.cells.get(i) == 1) {
                return;
            }
        }

        for (int i = 0; i < this.length; i++) {
            if (this.ansLine[i] == 0) {
                this.ansLine[i] = this.cur[i];
            } else if (this.ansLine[i] != this.cur[i]) {
                this.ansLine[i] = 2;
                this.cells.set(i, 0);
                this.sure[i] = 1;
            }
        }

        this.realFound++;
    }

    private void rec(int g, int pos) {
        if (this.realFound > 0) {
            return;
        }

        if (pos + this.restLength[g] > this.length) {
            return;
        }

        boolean ok = true;
        for (int i = pos; i < pos + this.groups.get(g); i++) {
            if (this.cells.get(i) == -1) {
                ok = false;
                break;
            }
            this.cur[i] = 1;
        }

        if (pos + this.groups.get(g) < this.length && this.cells.get(pos + this.groups.get(g)) == 1) {
            ok = false;
        }

        if (ok) {
            if (g == this.gn - 1) {
                checkFinal(pos + this.groups.get(g));
            } else {
                for (int i = pos + this.groups.get(g) + 1; i < this.length; ++i) {
                    rec(g + 1, i);
                    if (this.cells.get(i) == 1) {
                        break;
                    }
                }
            }
        }

        for (int i = pos; i < pos + this.groups.get(g); i++) {
            this.cur[i] = 0;
        }
    }
    
    public boolean isFeasible() {
    if (this.gn == 0) {
        for (int i = 0; i < this.length; ++i) {
            if (this.cells.get(i) == 1) {
                return false;
            }
        }
        return true;
    }

    this.realFound = 0;
    for (int i = 0; i < this.length; ++i) {
        // System.out.println("TRYING FROM " + i + "...");
        this.rec(0, i);
        if (this.cells.get(i) == 1) {
            break;
        }
    }
    return (this.realFound != 0);
}

public boolean isModificationFeasible(int pos, int val) {
    if (this.ansLine[pos] == 2 || this.ansLine[pos] == val) {
        return true;
    }
    int tmp = this.cells.get(pos);
    this.cells.set(pos,val);
    boolean ans = this.isFeasible();
    this.cells.set(pos,tmp);
    return ans;
}

public boolean solve() {
    if (!this.isFeasible()) {
        return false;
    }
    for (int i = 0; i < this.length; ++i) {
        System.out.println("Checking " + i + "...");
        if (this.sure[i] == 1) {
            continue;
        }
        if (!this.isModificationFeasible(i, 1)) {
            this.cells.set(i,-1);
        } else if (!this.isModificationFeasible(i, -1)) {
            this.cells.set(i,1);
        } else {
            this.cells.set(i,0);
        }
        this.sure[i] = 1;
    }
    return true;
}
}