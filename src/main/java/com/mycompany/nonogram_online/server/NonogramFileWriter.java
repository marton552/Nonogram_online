/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
