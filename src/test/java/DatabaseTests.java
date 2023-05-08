import com.nonogram_online.server.Response;
import com.nonogram_online.server.Server;
import java.util.Date;
import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class DatabaseTests {
    
    Server server;
    String username;
    String usernameWithCode;
    String password;
    String defaultLevel = "defaultlevel;5;1;2;rgb(255,255,255);rgb(0,0,0);0;0;0;0;0;0;0;0;0;0;0;0;1;0;0;0;0;0;0;0;0;0;0;0;0";
    String testLevel = "testLevel";
    String testUser = "testUser#0000";
    
    @BeforeEach
    void init(){
        server = new Server();
        Date date = new Date();
        long msec = date.getTime();
        username = "t"+msec;
        usernameWithCode = username+"#0000";
        password = "asd1A";
    }
    
    @BeforeAll
    void createLevel(){
        server = new Server();
        server.addNewLevel(testLevel, testUser, defaultLevel);
    }

    @Test
    @DisplayName("Test: A new player regiter to the game.")
    public void testRegistryNewPlayer() {
        Response res = server.isUserExist(username);
        assertEquals(res.getStatusCode(), 404);
        res = server.addNewUser(username, password);
        assertEquals(res.getStatusCode(), 200);
    }
    
    @Test
    @DisplayName("Test: A new player regiter with a used name.")
    public void testRegistryUsedPlayer() {
        Response res = server.addNewUser("helloTest", password);
        res = server.addNewUser("helloTest", password);
        assertEquals(res.getStatusCode(), 409);
    }
    
    @Test
    @DisplayName("Test: Create a profile and login with wrong username-password and with the correct one.")
    public void testLogin() {
        Response res = server.addNewUser(username, password);
        assertEquals(res.getStatusCode(), 200);
        res = server.isRealUserExist("a");
        assertEquals(res.getStatusCode(), 404);
        res = server.matchingPass(username, "a");
        assertEquals(res.getStatusCode(), 403);
        res = server.matchingPass(username, password);
        assertEquals(res.getStatusCode(), 200);
    }
    
    @Test
    @DisplayName("Test: Create a guest profile and register it.")
    public void testGuest() {
        Random r = new Random();
        int low = 1000;
        int high = 10000;
        Response res = new Response(404, "");
        int result = 0;
        while (res.equalsStatusCode(404)) {
            result = r.nextInt(high - low) + low;
            res = server.isUserCodeUsed(result);
        }
        res = server.addGuest((username+"guest"), result);
        assertEquals(res.getStatusCode(), 200);
        res = server.registerGuestUser((username+"guest"), username, password, result+"");
        assertEquals(res.getStatusCode(), 200);
    }
    
    @Test
    @DisplayName("Test: Promote a new player to mod")
    public void testPromotePlayerMod() {
        Response res = server.addNewUser(username, password);
        assertEquals(res.getStatusCode(), 200);
        res = server.getUserRole(username, "0000");
        assertEquals(res.getMessage(), "user");
        server.promoteUser(username, "0000");
        res = server.getUserRole(username, "0000");
        assertEquals(res.getMessage(), "mod");
    }
    
    @Test
    @DisplayName("Test: A new player level up")
    public void testLevelUpPlayer() {
        Response res = server.addNewUser(username, password);
        assertEquals(res.getStatusCode(), 200);
        res = server.getUserRank(username, "0000");
        assertEquals(res.getMessage(), "1");
        server.lvlUpUser(username, "0000", 2);
        res = server.getUserRank(username, "0000");
        assertEquals(res.getMessage(), "2");
    }
    
    @Test
    @DisplayName("Test: A new player complete a level")
    public void testCompleteLevel() {
        Response res = server.addNewUser(username, password);
        assertEquals(res.getStatusCode(), 200);
        res = server.isLevelExist(testLevel, testUser);
        assertEquals(res.getStatusCode(), 200);
        res = server.isLevelFinishedByUser(testLevel, testUser, usernameWithCode);
        assertEquals(res.getStatusCode(), 404);
        server.finishLevel(testLevel, testUser, usernameWithCode);
        res = server.isLevelFinishedByUser(testLevel, testUser, usernameWithCode);
        assertEquals(res.getStatusCode(), 200);
        res = server.getUserCompletedOnlineMaps(usernameWithCode);
        assertEquals(res.getMessage(), "1");
    }
    
    @Test
    @DisplayName("Test: A new player rate a level")
    public void testRateLevel() {
        Response res = server.addNewUser(username, password);
        assertEquals(res.getStatusCode(), 200);
        server.finishLevel(testLevel, testUser, usernameWithCode);
        server.rateLevel(testLevel,testUser,usernameWithCode,5);
        double avg = server.getAvgRateOfLevel(testLevel,testUser);
        assertEquals(avg, 5);
    }
    
    @Test
    @DisplayName("Test: Get a player created levesls number")
    public void testCreatedLevelNum() {
        Response res = server.getUserCreatedOnlineMaps(testUser);
        assertEquals(res.getMessage(), "1");
    }
    
    @Test
    @DisplayName("Test: A new player regiter and delete the profile")
    public void testRegistryAndDelete() {
        Response res = server.isUserExist(username);
        assertEquals(res.getStatusCode(), 404);
        res = server.addNewUser(username, password);
        assertEquals(res.getStatusCode(), 200);
        server.deleteUser(username, "0000");
        res = server.isUserExist(username);
        assertEquals(res.getStatusCode(), 404);
    }
}