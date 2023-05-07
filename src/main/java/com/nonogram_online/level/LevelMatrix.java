
package com.nonogram_online.level;

import java.util.ArrayList;

/**
 *
 * @author marton552
 */
public class LevelMatrix {

    private final ArrayList<Point> rowsColls;
    private ArrayList<ArrayList<Point>> leftNumbers;
    private ArrayList<ArrayList<Point>> topNumbers;
    private ArrayList<ArrayList<Point>> leftColorlessNumbers;
    private ArrayList<ArrayList<Point>> topColorlessNumbers;
    private final int hanyszorhany;

    public LevelMatrix(ArrayList<Integer> data, int hanyszorhany) {
        rowsColls = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            rowsColls.add(new Point(-1, data.get(i), false));
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
                if (db == 0 && color == -1 && getTileBy(i, k) != 0) {
                    color = getTileBy(i, k);
                    db = 1;
                    solved = getTileByIsDone(i, k);
                } else if (db > 0 && color == getTileBy(i, k)) {
                    db++;
                    solved = solved && getTileByIsDone(i, k);
                } else if (db > 0 && color != getTileBy(i, k) && getTileBy(i, k) != 0) {
                    leftNumbers.get(i).add(new Point(db, color, solved));
                    color = getTileBy(i, k);
                    db = 1;
                    solved = getTileByIsDone(i, k);
                } else if (db > 0 && color != getTileBy(i, k) && getTileBy(i, k) == 0) {
                    leftNumbers.get(i).add(new Point(db, color, solved));
                    color = -1;
                    db = 0;
                    solved = getTileByIsDone(i, k);
                }
            }
            if (db > 0) {
                leftNumbers.get(i).add(new Point(db, color, solved));
            }
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
                if (db == 0 && color == -1 && getTileBy(k, i) != 0) {
                    color = getTileBy(k, i);
                    db = 1;
                    solved = getTileByIsDone(k, i);
                } else if (db > 0 && color == getTileBy(k, i)) {
                    db++;
                    solved = solved && getTileByIsDone(k, i);
                } else if (db > 0 && color != getTileBy(k, i) && getTileBy(k, i) != 0) {
                    topNumbers.get(i).add(new Point(db, color, solved));
                    color = getTileBy(k, i);
                    db = 1;
                    solved = getTileByIsDone(k, i);
                } else if (db > 0 && color != getTileBy(k, i) && getTileBy(k, i) == 0) {
                    topNumbers.get(i).add(new Point(db, color, solved));
                    color = -1;
                    db = 0;
                    solved = getTileByIsDone(k, i);
                }
            }
            if (db > 0) {
                topNumbers.get(i).add(new Point(db, color, solved));
            }
        }
    }

    private void setLeftColorlessNumbers() {
        leftColorlessNumbers = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < hanyszorhany; i++) {
            leftColorlessNumbers.add(new ArrayList<>());
            int db = 0;
            int color = -1;
            boolean solved = false;
            for (int k = 0; k < hanyszorhany; k++) {
                if (db == 0 && color == -1 && getTileBy(i, k) != 0) {
                    color = 1;
                    db = 1;
                    solved = getTileByIsDone(i, k);
                } else if (db > 0 && getTileBy(i, k) != 0) {
                    db++;
                    solved = solved && getTileByIsDone(i, k);
                } else if (db > 0 && color != getTileBy(i, k) && getTileBy(i, k) == 0) {
                    leftColorlessNumbers.get(i).add(new Point(db, color, solved));
                    color = -1;
                    db = 0;
                    solved = getTileByIsDone(i, k);
                }
            }
            if (db > 0) {
                leftColorlessNumbers.get(i).add(new Point(db, color, solved));
            }
        }
    }
    
    private void setTopColorlessNumbers() {
        topColorlessNumbers = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < hanyszorhany; i++) {
            topColorlessNumbers.add(new ArrayList<>());
            int db = 0;
            int color = -1;
            boolean solved = false;
            for (int k = 0; k < hanyszorhany; k++) {
                if (db == 0 && color == -1 && getTileBy(k, i) != 0) {
                    color = 1;
                    db = 1;
                    solved = getTileByIsDone(k, i);
                } else if (db > 0 && getTileBy(k, i) != 0) {
                    db++;
                    solved = solved && getTileByIsDone(k, i);
                }  else if (db > 0 && color != getTileBy(k, i) && getTileBy(k, i) == 0) {
                    topColorlessNumbers.get(i).add(new Point(db, color, solved));
                    color = -1;
                    db = 0;
                    solved = getTileByIsDone(k, i);
                }
            }
            if (db > 0) {
                topColorlessNumbers.get(i).add(new Point(db, color, solved));
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getLeftOnlyNumbers() {
        setLeftColorlessNumbers();
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        for (int i = 0; i < leftColorlessNumbers.size(); i++) {
            ArrayList<Integer> x = new ArrayList<>();
            int num = 0;
            for (int j = 0; j < leftColorlessNumbers.get(i).size(); j++) {
                num++;
                x.add(leftColorlessNumbers.get(i).get(j).getNum());
            }
            if (num == 0) {
                x.add(0);
            }
            res.add(x);
        }
        return res;
    }

    public ArrayList<ArrayList<Integer>> getTopOnlyNumbers() {
        setTopColorlessNumbers();
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        for (int i = 0; i < topColorlessNumbers.size(); i++) {
            ArrayList<Integer> x = new ArrayList<>();
            int num = 0;
            for (int j = 0; j < topColorlessNumbers.get(i).size(); j++) {
                num++;
                x.add(topColorlessNumbers.get(i).get(j).getNum());
            }
            if (num == 0) {
                x.add(0);
            }
            res.add(x);
        }
        return res;
    }

    public boolean isLayerEmpty(int backColor) {
        for (Point rowsColl : rowsColls) {
            if (rowsColl.getColor() != backColor) {
                return false;
            }
        }
        return true;
    }

    public int mostTopNumber() {
        int res = 0;
        for (int i = 0; i < topNumbers.size(); i++) {
            res = Math.max(topNumbers.get(i).size(), res);
        }
        return res;
    }

    public int mostLeftNumber() {
        int res = 0;
        for (int i = 0; i < leftNumbers.size(); i++) {
            res = Math.max(leftNumbers.get(i).size(), res);
        }
        return res;
    }

    //FORDÍTVA MŰKÖDIK!
    public void setTileBy(int colls, int rows, int color) {
        rowsColls.get(rows * hanyszorhany + colls).clickOn(color);
        setLeftNumbers();
        setTopNumbers();
    }

    public void clearTileBy(int rows, int colls) {
        rowsColls.get(rows * hanyszorhany + colls).unClick();
        setLeftNumbers();
        setTopNumbers();
    }

    public void resetTileBy(int rows, int colls) {
        rowsColls.get(rows * hanyszorhany + colls).resetColor();
        setLeftNumbers();
        setTopNumbers();
    }

    public void addTileBy(int rows, int colls, int color) {
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

    public boolean getTileByIsFlagged(int rows, int colls) {
        return rowsColls.get(rows * hanyszorhany + colls).isFlagged();
    }

    public ArrayList<ArrayList<Point>> getLeftNumbers() {
        return leftNumbers;
    }

    public ArrayList<ArrayList<Point>> getTopNumbers() {
        return topNumbers;
    }

}
