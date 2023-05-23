
package com.nonogram_online.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author marton552
 */
public class NonogramFileWriter {

    private final String data;

    public NonogramFileWriter(String data) {
        this.data = data;
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
