
package com.nonogram_online.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marton552
 */
public class NonogramFileWriter {

    private String data;

    public NonogramFileWriter(String data) {
        this.data = data;
    }

    public void saveLocalData(String user, String lvlName) {
        try {
            Writer output;
            output = new BufferedWriter(new FileWriter("src/main/resources/levels/saved_data.txt", true));
            try {
                output.append("\n"+user + ";" + lvlName);
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(NonogramFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(NonogramFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeToFile(String name) {
        try {
            File myObj = new File("src/main/resources/levels/created/" + name + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                FileWriter myWriter = new FileWriter("src/main/resources/levels/created/" + name + ".txt");
                myWriter.write(data);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
