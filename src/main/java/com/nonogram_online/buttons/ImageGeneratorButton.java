
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
        if(text .equals( "Újra generálás")) m.menuActions("#"+text);
        else if(text .equals( "@")) m.menuActions("$"+text);
        else if(text .equals( "Mentés")) m.menuActions("+"+text);
        else if(text .equals( "Színek egyesítése")) m.menuActions("-"+text);
        else m.menuActions("&"+text);
    }
}
