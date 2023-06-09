
package com.nonogram_online.buttons;

import com.nonogram_online.Menu;

/**
 *
 * @author marton552
 */
public class SearchButton extends BasicButton{

    public SearchButton(Menu m, String text, int width, int height) {
        super(m, text, width, height);
    }
    
        @Override
    protected void setFontSize(){
        fontSize = (int) (height/2.7);
    }
    
    @Override
        public void click() {
        clicked+=10;
        if (clicked == 20) {
            clicked = 0;
        }
        repaint();
        m.menuActions(text);
    }
}
