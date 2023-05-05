
package com.nonogram_online.user;

/**
 *
 * @author marton552
 */
public class MissionIcon {
    private String title;
    private String icon;
    private int needCount;
    private int currentCount;

    public MissionIcon(String title, String icon, int needCount, int currentCount) {
        this.title = title;
        this.icon = icon;
        this.needCount = needCount;
        this.currentCount = currentCount;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public int getNeedCount() {
        return needCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
    
}
