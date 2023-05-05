
package com.nonogram_online.buttons;

import com.nonogram_online.Menu;

/**
 *
 * @author marton552
 */
public class SortButton extends BasicButton {

    public SortButton(Menu m, String text, int width, int height) {
        super(m, text, width, height);
    }

    @Override
    protected void setFontSize(){
        fontSize = (int) (height/2.7);
    }
    
    @Override
        public void click() {
        clicked++;
        if (clicked == 3) {
            clicked = 0;
        }
        repaint();
        m.menuActions(text);
    }
}
