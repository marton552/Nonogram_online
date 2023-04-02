/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nonogram_online.server;

import com.mycompany.nonogram_online.level.Level;
import com.mycompany.nonogram_online.user.MissionIcon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author marton552
 */
public class Server {

    String url = "jdbc:mysql://localhost:3306/nonogram_online";
    String dbUsername = "root";
    String dbPassword = "";

    ArrayList<ArrayList<String>> data = new ArrayList<>();
    Response response;

    public Server() {
        response = new Response(0, "");
        data = new ArrayList<ArrayList<String>>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void lvlUpUser(String username, String usercode,int rank){
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).get(0).equals(username) && data.get(i).get(2).equals(usercode)){
                runQueryNoResponse("UPDATE users SET username = '" + username + "', password = '" + data.get(i).get(1) + "', usercode = '"+usercode+"', rank='"+rank+"' WHERE username ='" + username + "' and usercode = '" + usercode + "' ;");
            }
        }
        closeRequest();
    }
    
    public ArrayList<MissionIcon> getMissionList(int level){
        getMissions(level);
        ArrayList<MissionIcon> res = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            MissionIcon m = new MissionIcon(data.get(i).get(1), data.get(i).get(2), Integer.parseInt(data.get(i).get(3)), 0);
            res.add(m);
        }
        closeRequest();
        return res;
    }

    public Response finishLevel(String lvlName, String fullUsername, String actualUsername) {
        if (isLevelFinishedByUser(lvlName,fullUsername,actualUsername).getStatusCode() == 200) {
            return new Response(409, "This user already completed this map!");
        } else {
            runQueryNoResponse("INSERT INTO completed_maps (level_name, creator_name, player_name, rate) VALUES ('" + lvlName + "','" + fullUsername + "','" + actualUsername + "','1')");
            return new Response(200, "Level complete");
        }
    }
    
    public Response getUserCompletedOnlineMaps(String fullUsername){
        getCompletedLevels();
        int counter = 0;
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).get(2).equals(fullUsername)) counter++;
        }
        closeRequest();
        return new Response(200, counter+"");
    }

    public Response isLevelFinishedByUser(String lvlName, String fullUsername, String actualUsername) {
        getCompletedLevels();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(lvlName) && data.get(i).get(1).equals(fullUsername) && data.get(i).get(2).equals(actualUsername)) {
                closeRequest();
                return new Response(200, "User completed this map");
            }
        }
        closeRequest();
        return new Response(404, "User not completed this map");
    }

    public void deleteLevel(String lvlName, String fullUsername) {
        getLevels("id");
        runQueryNoResponse("DELETE FROM levels WHERE level_name='" + lvlName + "' and creator_name='" + fullUsername + "'");
        closeRequest();
    }

    public void approveLevel(String lvlName, String fullUsername) {
        getLevels("id");
        String id = "";
        String cDate = "";
        String d = "";
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(1).equals(lvlName) && data.get(i).get(2).equals(fullUsername)) {
                id = data.get(i).get(0);
                cDate = data.get(i).get(3);
                d = data.get(i).get(4);
            }
        }
        runQueryNoResponse("UPDATE levels SET id='" + id + "',level_name='" + lvlName + "',creator_name='" + fullUsername + "',created_date='" + cDate + "',data='" + d + "',approved='1' WHERE level_name='" + lvlName + "' and creator_name='" + fullUsername + "'");
        closeRequest();
    }

    public ArrayList<Level> getXLevels(String order, int start, int end) {
        getLevels(order);
        ArrayList<Level> levels = new ArrayList<>();
        for (int i = start; i < end && i < data.size(); i++) {
            levels.add(new Level(new ArrayList<String>(Arrays.asList(data.get(i).get(4).split(";"))), data.get(i).get(2), data.get(i).get(3), data.get(i).get(5).equals("-1") ? false : true));
        }
        closeRequest();
        return levels;
    }

    public Response addNewLevel(String lvlName, String userName, String lvlData) {
        if (isLevelExist(lvlName, userName).getStatusCode() == 200) {
            return new Response(409, "This level already created!");
        } else {
            getLevelCount();
            String id = data.get(0).get(0);
            Date d = new Date(System.currentTimeMillis());
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            runQueryNoResponse("INSERT INTO levels (id, level_name, creator_name, created_date, data, approved) VALUES ('" + id + "', '" + lvlName + "', '" + userName + "', '" + df.format(d) + "', '" + lvlData + "', '-1')");
            closeRequest();
            return new Response(200, "User added succesfully");
        }
    }

    public Response isLevelExist(String lvlName, String fullUsername) {
        getLevels("id");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(1).equals(lvlName) && data.get(i).get(2).equals(fullUsername)) {
                closeRequest();
                return new Response(200, "Level exist");
            }
        }
        response = new Response(404, "Level not exist");
        closeRequest();
        return response;
    }
    
    public Response getUserRank(String name, String usercode){
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(name) && data.get(i).get(2).equals(usercode)) {
                String rank = (data.get(i).get(3));
                closeRequest();
                return new Response(200, rank);
            }
        }
        response = new Response(404, "User not exist");
        closeRequest();
        return response;
    }

    public Response registerGuestUser(String oldName, String newName, String pass, String usercode) {
        if (isRealUserExist(newName).getStatusCode() == 200) {
            return new Response(409, "This username already taken!");
        } else {
            //todo: implement in other tables
            runQueryNoResponse("UPDATE users SET username = '" + newName + "', password = '" + pass + "', usercode = '0000', rank='1' WHERE username ='" + oldName + "' and usercode = '" + usercode + "' ;");
            return new Response(200, "User added succesfully");
        }
    }

    public Response addGuest(String name, int usercode) {
        if (isRealUserExist(name).getStatusCode() == 200) {
            return new Response(409, "This username already taken!");
        } else {
            runQueryNoResponse("INSERT INTO users (username, password, usercode,rank) VALUES ('" + name + "', '0', '" + usercode + "', '0')");
            return new Response(200, "User added succesfully");
        }
    }

    public Response addNewUser(String name, String pass) {
        if (isRealUserExist(name).getStatusCode() == 200) {
            return new Response(409, "This username already taken!");
        } else {
            runQueryNoResponse("INSERT INTO users (username, password, usercode,rank) VALUES ('" + name + "', '" + pass + "', '0000','1')");
            return new Response(200, "User added succesfully");
        }
    }

    public Response isRealUserExist(String name) {
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(name) && data.get(i).get(2).startsWith("0")) {
                closeRequest();
                return new Response(200, "Real user exist");
            }
        }
        response = new Response(404, "User not exist");
        closeRequest();
        return response;
    }

    public Response getRealUserUsercode(String name) {
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(name) && data.get(i).get(2).startsWith("0")) {
                String code = data.get(i).get(2);
                closeRequest();
                return new Response(200, code);
            }
        }
        response = new Response(404, "User not exist");
        closeRequest();
        return response;
    }

    public Response isUserExist(String name) {
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(name)) {
                closeRequest();
                return new Response(200, "User exist");
            }
        }
        response = new Response(404, "User not exist");
        closeRequest();
        return response;
    }

    public Response matchingPass(String name, String pass) {
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(name) && data.get(i).get(2).startsWith("0") && data.get(i).get(1).equals(pass)) {
                closeRequest();
                return new Response(200, "Succesful login");
            }
        }
        response = new Response(403, "Wrong username or password");
        closeRequest();
        return response;
    }

    public Response isUserCodeUsed(int num) {
        String usercode = num + "";
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(2).equals(usercode)) {
                closeRequest();
                return new Response(404, "Usercode is not OK");
            }
        }
        response = new Response(200, "Usercode is OK");
        closeRequest();
        return response;
    }

    private void getMissions(int level) {
        runQuery("select * from missions where level='"+level+"'");
    }
    
    private void getUsers() {
        runQuery("select * from users");
    }

    private void getLevels(String order) {
        runQuery("select * from levels order by " + order);
    }

    private void getLevelCount() {
        runQuery("select count(*) from levels");
    }

    private void getCompletedLevels() {
        runQuery("select * from completed_maps");
    }

    private void runQuery(String sql) {
        try ( Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);  PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                int index = 0;
                while (resultSet.next()) {
                    data.add(new ArrayList<>());
                    for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                        String s = resultSet.getString((i + 1));
                        data.get(index).add(s);
                    }
                    index++;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void runQueryNoResponse(String sql) {
        try ( Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);  PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void closeRequest() {
        data.clear();
        data = new ArrayList<>();
    }
}
