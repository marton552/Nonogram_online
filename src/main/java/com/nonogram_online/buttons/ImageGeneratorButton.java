/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online.buttons;

import com.nonogram_online.Menu;

/**
 *
 * @author marton552
 */
public class ImageGeneratorButton extends BasicButton{
    
    

    public ImageGeneratorButton(Menu m, String text, int width, int height) {
        super(m, text, width, height);
    }
    
    @Override
    public void click() {
        if(text == "Újra generálás") m.menuActions("#"+text);
        else if(text == "@") m.menuActions("$"+text);
        else if(text == "Mentés") m.menuActions("+"+text);
        else if(text == "Színek egyesítése") m.menuActions("-"+text);
        else m.menuActions("&"+text);
    }
}
