
package com.nonogram_online.server;

import com.nonogram_online.level.Level;
import com.nonogram_online.user.MissionIcon;
import com.nonogram_online.user.User;
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

/**
 *
 * @author marton552
 */
public class Server {
    
    private static final String USERHASTAGSTRING = "#0000";

    private static final String URL = "jdbc:mysql://localhost:3306/nonogram_online";
    private static final String DBUSERNAME = "root";
    private static final String DBPASSWORD = "";

    ArrayList<ArrayList<String>> data;

    public Server() {
        data = new ArrayList<ArrayList<String>>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException ex) {
            System.out.println("com.nonogram_online.server.Server.<init>()");
        }
    }
    
    public void promoteUser(String username, String usercode) {
        runQueryNoResponse("UPDATE `users` SET `role`='mod' WHERE username ='"+username+"' and usercode='"+usercode+"'");
    }
    
    public void deleteUser(String username, String usercode) {
        runQueryNoResponse("DELETE FROM users WHERE username ='"+username+"' and usercode='"+usercode+"'");
    }

    public void refreshMissions(ArrayList<String> missions) {
        runQueryNoResponse("DELETE FROM missions");
        for (int i = 0; i < missions.size(); i++) {
            runQueryNoResponse("INSERT INTO missions (level, title, icon, need) VALUES ('" + missions.get(i).split(";")[0] + "','" + missions.get(i).split(";")[1] + "','" + missions.get(i).split(";")[2] + "','" + missions.get(i).split(";")[3] + "')");
        }
    }

    public void lvlUpUser(String username, String usercode, int rank) {
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(username) && data.get(i).get(2).equals(usercode)) {
                runQueryNoResponse("UPDATE users SET username = '" + username + "', password = '" + data.get(i).get(1) + "', usercode = '" + usercode + "', rank='" + rank + "', role='" + data.get(i).get(4) + "' WHERE username ='" + username + "' and usercode = '" + usercode + "' ;");
            }
        }
        closeRequest();
    }

    public ArrayList<MissionIcon> getMissionList(int level) {
        getMissions(level);
        ArrayList<MissionIcon> res = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            MissionIcon m = new MissionIcon(data.get(i).get(1), data.get(i).get(2), Integer.parseInt(data.get(i).get(3)), 0);
            res.add(m);
        }
        closeRequest();
        return res;
    }

    public double getAvgRateOfLevel(String lvlName, String fullUsername) {
        getCompletedLevels();
        double sum = 0;
        double db = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(lvlName) && data.get(i).get(1).equals(fullUsername)) {
                double val = Double.parseDouble(data.get(i).get(3).toString());
                sum += val;
                if (val != 0) {
                    db++;
                }
            }
        }
        closeRequest();
        if (sum == 0) {
            return 0;
        } else {
            return sum / db;
        }
    }

    public void rateLevel(String lvlName, String fullUsername, String actualUsername, double rate) {
        getCompletedLevels();
        runQueryNoResponse("UPDATE completed_maps SET level_name='" + lvlName + "',creator_name='" + fullUsername + "',player_name='" + actualUsername + "',rate='" + rate + "' WHERE level_name='" + lvlName + "' and creator_name='" + fullUsername + "' and player_name='" + actualUsername + "'");
        closeRequest();
    }

    public Response finishLevel(String lvlName, String fullUsername, String actualUsername) {
        if (isLevelFinishedByUser(lvlName, fullUsername, actualUsername).equalsStatusCode(200)) {
            return new Response(409, "This user already completed this map!");
        } else {
            runQueryNoResponse("INSERT INTO completed_maps (level_name, creator_name, player_name, rate) VALUES ('" + lvlName + "','" + fullUsername + "','" + actualUsername + "','0')");
            return new Response(200, "Level complete");
        }
    }

    public Response getUserRatedOnlineMaps(String fullUsername) {
        getCompletedLevels();
        int counter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(1).equals(fullUsername) && !data.get(i).get(3).equals("0")) {
                counter++;
            }
        }
        closeRequest();
        return new Response(200, counter + "");
    }

    public Response getUserApprovedOnlineMaps(String fullUsername) {
        getLevels("id");
        int counter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(2).equals(fullUsername) && data.get(i).get(5).equals("1")) {
                counter++;
            }
        }
        closeRequest();
        return new Response(200, counter + "");
    }
    
    public Response getUserCreatedOnlineMaps(String fullUsername) {
        getLevels("id");
        int counter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(2).equals(fullUsername)) {
                counter++;
            }
        }
        closeRequest();
        return new Response(200, counter + "");
    }

    public Response getUserCompletedOnlineMaps(String fullUsername) {
        getCompletedLevels();
        int counter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(2).equals(fullUsername)) {
                counter++;
            }
        }
        closeRequest();
        return new Response(200, counter + "");
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

    public ArrayList<Level> getXLevels(SortResponse order, SearchResponse search, int start, int end) {
        getLevels(order, search);
        ArrayList<Level> levels = new ArrayList<>();
        for (int i = start; i < end && i < data.size(); i++) {
            levels.add(new Level(new ArrayList<>(Arrays.asList(data.get(i).get(4).split(";"))), data.get(i).get(2), data.get(i).get(3), !data.get(i).get(5).equals("-1")));
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
        Response response = new Response(404, "Level not exist");
        closeRequest();
        return response;
    }
    
    public ArrayList<User> getXUser(SortResponse order, SearchResponse search, int start, int end) {
        getUsers();
        ArrayList<User> users = new ArrayList<>();
        for (int i = start; i < end && i < data.size(); i++) {
            users.add(new User(data.get(i).get(0),data.get(i).get(2), Integer.parseInt(data.get(i).get(3)), data.get(i).get(4)));
        }
        closeRequest();
        return users;
    }

    public Response getUserRank(String name, String usercode) {
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(name) && data.get(i).get(2).equals(usercode)) {
                String rank = (data.get(i).get(3));
                closeRequest();
                return new Response(200, rank);
            }
        }
        Response response = new Response(404, "User not exist");
        closeRequest();
        return response;
    }

    public void deleteGuestUsers() {
        runQueryNoResponse("DELETE FROM users WHERE rank='0'");
    }

    public Response getUserRole(String username, String usercode) {
        getUsers();
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).get(0).equals(username) && data.get(i).get(2).equals(usercode)){
                Response res = new Response(200, data.get(i).get(4)+"");
                closeRequest();
                return res;
            }
        }
        closeRequest();
        return new Response(404, "User not found");
    }

    public Response registerGuestUser(String oldName, String newName, String pass, String usercode) {
        if (isRealUserExist(newName).equalsStatusCode(200)) {
            return new Response(409, "This username already taken!");
        } else {
            runQueryNoResponse("UPDATE completed_maps SET creator_name='"+(newName+USERHASTAGSTRING)+"' WHERE creator_name='"+(oldName+"#"+usercode)+"'");
            runQueryNoResponse("UPDATE completed_maps SET player_name='"+(newName+USERHASTAGSTRING)+"' WHERE player_name='"+(oldName+"#"+usercode)+"'");
            runQueryNoResponse("UPDATE levels SET creator_name='"+(newName+USERHASTAGSTRING)+"' WHERE creator_name='"+(oldName+"#"+usercode)+"'");
            runQueryNoResponse("UPDATE superproject SET completed_by='"+(newName+USERHASTAGSTRING)+"' WHERE completed_by='"+(oldName+"#"+usercode)+"'");
            runQueryNoResponse("UPDATE users SET username = '" + newName + "', password = '" + pass + "', usercode = '0000', rank='1', role='user' WHERE username ='" + oldName + "' and usercode = '" + usercode + "' ;");
            return new Response(200, "User added succesfully");
        }
    }

    public Response addGuest(String name, int usercode) {
        if (isRealUserExist(name).equalsStatusCode(200)) {
            return new Response(409, "This username already taken!");
        } else {
            runQueryNoResponse("INSERT INTO users (username, password, usercode,rank,role) VALUES ('" + name + "', '0', '" + usercode + "', '0','guest')");
            return new Response(200, "User added succesfully");
        }
    }

    public Response addNewUser(String name, String pass) {
        if (isRealUserExist(name).equalsStatusCode(200)) {
            return new Response(409, "This username already taken!");
        } else {
            runQueryNoResponse("INSERT INTO users (username, password, usercode,rank,role) VALUES ('" + name + "', '" + pass + "', '0000','1','user')");
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
        Response response = new Response(404, "User not exist");
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
        Response response = new Response(404, "User not exist");
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
        Response response = new Response(404, "User not exist");
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
        Response response = new Response(403, "Wrong username or password");
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
        Response response = new Response(200, "Usercode is OK");
        closeRequest();
        return response;
    }
    
    public Response getSPTop(int top){
        getSPTopList();
        Response response;
        if(data.size() > top) response = new Response(Integer.parseInt(data.get(top).get(1)), data.get(top).get(0));
        else response = new Response(404, "Not found");
        closeRequest();
        return response;
    }
    
    public void completeSuperProject(int index, String username){
        runQueryNoResponse("UPDATE `superproject` SET completed_by='"+username+"' WHERE id='"+index+"'");
    }
    
    public Response getSuperProjectisDone(int index){
        getSP();
        Response response = new Response(200, data.get(index).get(3));
        closeRequest();
        return response;
    }
    
    public Response getSuperProject(int index){
        getSP();
        Response response = new Response(200, data.get(index).get(2));
        closeRequest();
        return response;
    }
    
    public void refreshSuperProject(ArrayList<String> sps) {
        runQueryNoResponse("DELETE FROM superproject");
        Date d = new Date(System.currentTimeMillis());
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        for (int i = 0; i < sps.size(); i++) {
            int id = i+1;
            System.out.println("INSERT INTO superproject(id, type, data, completed_by) VALUES ('"+(id)+"', '"+df.format(d)+"','"+sps.get(i)+"','no')");
            runQueryNoResponse("INSERT INTO superproject(id, type, data, completed_by) VALUES ('"+(id)+"', '"+df.format(d)+"','"+sps.get(i)+"','no')");
        }
    }

    private void getSPTopList() {
        runQuery("SELECT completed_by, COUNT(*) AS count FROM superproject WHERE completed_by <> 'no' GROUP BY completed_by ORDER BY count DESC");
    }
    
    private void getSP() {
        runQuery("select * from superproject");
    }
    
    private void getMissions(int level) {
        runQuery("select * from missions where level='" + level + "'");
    }

    private void getUsers() {
        runQuery("select * from users");
    }

    private void getLevels(String order) {
        runQuery("select * from levels order by " + order);
    }

    private void getLevels(SortResponse order, SearchResponse search) {
        runQuery("SELECT levels.id, levels.level_name, levels.creator_name, levels.created_date, levels.data, levels.approved, SUM(completed_maps.rate)/COUNT(NULLIF(completed_maps.rate,0)) AS 'rate' FROM levels INNER JOIN completed_maps ON levels.level_name = completed_maps.level_name and levels.creator_name = completed_maps.creator_name " + search.toString() + " GROUP BY levels.level_name, levels.creator_name, levels.created_date order by " + order.toString());
    }

    private void getLevelCount() {
        runQuery("select count(*) from levels");
    }

    private void getCompletedLevels() {
        runQuery("select * from completed_maps");
    }

    private void runQuery(String sql) {
        try ( Connection connection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);  PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
        try ( Connection connection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);  PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
