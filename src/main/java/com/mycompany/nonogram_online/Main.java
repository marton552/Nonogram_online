/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online;

import com.mycompany.nonogram_online.server.Response;
import com.mycompany.nonogram_online.server.Server;

/**
 *
 * @author marton552
 */
public class Main {
    public static void main(String[] args) {
        String title = "Nonogram online";
        Server server = new Server();
        Response res = server.isRealUserExist("proba");
        Menu game = new Menu(title);
    }
}
