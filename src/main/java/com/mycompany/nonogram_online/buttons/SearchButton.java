/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.buttons;

import com.mycompany.nonogram_online.Menu;

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
