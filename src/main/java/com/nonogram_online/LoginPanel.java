
package com.nonogram_online;

import com.nonogram_online.buttons.BasicButton;
import com.nonogram_online.server.Response;
import com.nonogram_online.server.Server;
import com.nonogram_online.user.User;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author marton552
 */
public class LoginPanel extends JPanel {

    private String mode;

    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel passwordAgainLabel;
    private JLabel errorLabel;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JPasswordField passwordAgainInput;
    private JLabel usercodeLabel;
    private BasicButton usercodeButton;
    private BasicButton backButton;
    private BasicButton okButton;
    private int userCode = 0;

    private JPanel usernamePanel;
    private JPanel passPanel;
    private JPanel passAgainPanel;
    private JPanel buttonsPanel;
    private JPanel usercodePanel;
    private GridLayout edit;

    private Server server;
    private Response response;
    private Timer timer;
    private int time;
    private boolean errorUsername;
    private boolean errorPassword;
    private boolean errorServer;

    private JPanel mainPanel;
    Menu m;

    public LoginPanel(Menu m, String mode) {
        this.m = m;
        this.mode = mode;
        edit = new GridLayout(4, 1);
        edit.setVgap(64);
        this.setLayout(edit);
        server = new Server();
        time = 0;
        mainPanel = this;

        titleLabel = new JLabel(mode, SwingConstants.CENTER);
        usernameLabel = new JLabel("Felhasználónév:", SwingConstants.CENTER);
        passwordLabel = new JLabel("Jelszó:", SwingConstants.CENTER);
        passwordAgainLabel = new JLabel("Jelszó még egyszer:", SwingConstants.CENTER);
        if (mode.equals("Regisztráció")) {
            errorLabel = new JLabel("<html>Disclaimer: Ez egy szakdolgozat projekt,<br>a jelszó semmiféle titkosítást nem alkalmaz,<br>ne adj meg olyan jelszót amit máshol használnál.</html>", SwingConstants.CENTER);
        } else {
            errorLabel = new JLabel("", SwingConstants.CENTER);
        }
        usercodeLabel = new JLabel("Vendég azonosító: ", SwingConstants.CENTER);
        if (mode.equals("Belépés vendégként")) {
            randomUserNumber();
        }

        usernameInput = new JTextField("");
        usernameInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                checkUsername();
                checkPassword();
            }

            public void removeUpdate(DocumentEvent e) {
                checkUsername();
                checkPassword();
            }

            public void insertUpdate(DocumentEvent e) {
                checkUsername();
                checkPassword();
            }
        });

        passwordInput = new JPasswordField("");
        passwordInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                checkPassword();
                checkUsername();
            }

            public void removeUpdate(DocumentEvent e) {
                checkPassword();
                checkUsername();
            }

            public void insertUpdate(DocumentEvent e) {
                checkPassword();
                checkUsername();
            }
        });

        passwordAgainInput = new JPasswordField("");
        passwordAgainInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                checkPassword();
                checkUsername();
            }

            public void removeUpdate(DocumentEvent e) {
                checkPassword();
                checkUsername();
            }

            public void insertUpdate(DocumentEvent e) {
                checkPassword();
                checkUsername();
            }
        });

        usercodeButton = new BasicButton(m, "Új kód generálása", 1, 4);
        backButton = new BasicButton(m, " Vissza", 1, 4);
        okButton = new BasicButton(m, mode, 1, 4);
        okButton.setEnabled(false);
        setupButtons(5);
        
        timer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (time > 0) {
                    String stringPart = "";
                    if (mode.equals(" Regisztráció") || mode.equals(" Vendég regisztrálása")) {
                        stringPart = "Felhasználó sikeresen létrehozva!<br>";
                    }
                    if (time == 4) {
                        errorLabel.setText("<html>" + stringPart + "<div style='text-align:center'>Bejelentkezés </div></html>");
                    } else {
                        String s = "<html>" + stringPart + "<div style='text-align:center'>Bejelentkezés ";
                        for (int i = 4 - time; i > 0; i--) {
                            s += ". ";
                        }
                        s += "</div></html>";
                        errorLabel.setText(s);
                    }

                    mainPanel.revalidate();
                    mainPanel.repaint();
                    time--;
                } else if (time == 0) {
                    time--;
                    timer.stop();
                    Response rank = server.getUserRank(usernameInput.getText(), User.fullCode(userCode));
                    Response role = server.getUserRole(usernameInput.getText(), User.fullCode(userCode));
                    m.setUser(usernameInput.getText(), User.fullCode(userCode),(rank.getStatusCode() == 200 ? Integer.parseInt(rank.getMessage()) : 0),(role.equalsStatusCode(200) ? role.getMessage() : "user"));
                    m.login();
                }
            }
        });

        errorServer = false;

        if (mode.equals(" Regisztráció")) {
            setupRegistry();
        } else if (mode.equals(" Bejelentkezés")) {
            setupLogin();
        } else if (mode.equals(" Belépés vendégként")) {
            setupGuest();
        } else if (mode.equals(" Vendég regisztrálása")) {
            setupRegistryGuest();
        }

        repaint();
        setVisible(true);
    }
    
    public void setupButtons(int height){
        this.add(titleLabel);
        usernamePanel = new JPanel(new GridLayout(1, 2));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameInput);
        passPanel = new JPanel(new GridLayout(1, 2));
        passPanel.add(passwordLabel);
        passPanel.add(passwordInput);
        passAgainPanel = new JPanel(new GridLayout(1, 2));
        passAgainPanel.add(passwordAgainLabel);
        passAgainPanel.add(passwordAgainInput);
        usercodePanel = new JPanel(new GridLayout(1, 2));
        usercodePanel.add(usercodeLabel);
        usercodeButton.setHeight(64);
        usercodeButton.setOrientation(2, height);
        usercodePanel.add(usercodeButton);
        buttonsPanel = new JPanel(new GridLayout(1, 2));
        backButton.setHeight(64);
        backButton.setOrientation(2, height);
        buttonsPanel.add(backButton);
        okButton.setOrientation(2, height);
        okButton.setHeight(64);
        buttonsPanel.add(okButton);
    }

    public void menuActions(String text) {
        if (text.equals("Új kód generálása")) {
            randomUserNumber();
        }
        else if (mode.equals(" Regisztráció") && mode.equals(text)) {
            response = server.isRealUserExist(usernameInput.getText());
            if (response.equalsStatusCode(404)) {
                response = server.addNewUser(usernameInput.getText(), new String(passwordInput.getPassword()));
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                mainPanel.setLayout(new BorderLayout());
                errorLabel.setText("Felhasználó sikeresen létrehozva!");
                mainPanel.add(errorLabel, BorderLayout.CENTER);
                time = 4;
                timer.start();
            } else {
                errorServer = true;
                ableToLogin();
            }
        } else if (mode.equals(" Bejelentkezés") && mode.equals(text)) {
            response = server.isRealUserExist(usernameInput.getText());
            if (response.equalsStatusCode(404)) {
                errorServer = true;
                ableToLogin();
            } else {
                response = server.matchingPass(usernameInput.getText(), new String(passwordInput.getPassword()));
                if (response.equalsStatusCode(403)) {
                    errorServer = true;
                    ableToLogin();
                } else {
                    userCode = Integer.parseInt(server.getRealUserUsercode(usernameInput.getText()).getMessage());
                    mainPanel.removeAll();
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    mainPanel.setLayout(new BorderLayout());
                    errorLabel.setText("Sikeres bejelentkezés!");
                    mainPanel.add(errorLabel, BorderLayout.CENTER);
                    time = 4;
                    timer.start();
                }
            }
        } else if (mode.equals(" Belépés vendégként") && mode.equals(text)) {
            response = server.addGuest(usernameInput.getText(), userCode);
            if (response.equalsStatusCode(409)) {
                errorServer = true;
                ableToLogin();
            } else {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                mainPanel.setLayout(new BorderLayout());
                errorLabel.setText("Sikeres bejelentkezés!");
                mainPanel.add(errorLabel, BorderLayout.CENTER);
                time = 4;
                timer.start();

            }
        } else if (mode.equals(" Vendég regisztrálása") && mode.equals(text)) {
            response = server.isRealUserExist(usernameInput.getText());
            if (response.equalsStatusCode(404)) {
                response = server.registerGuestUser(m.getUser().getUsername(), usernameInput.getText(), new String(passwordInput.getPassword()), m.getUser().getUsercode());
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                mainPanel.setLayout(new BorderLayout());
                errorLabel.setText("Felhasználó sikeresen regisztrálva!");
                mainPanel.add(errorLabel, BorderLayout.CENTER);
                time = 4;
                timer.start();
            } else {
                errorServer = true;
                ableToLogin();
            }
        }
    }

    private void randomUserNumber() {
        Random r = new Random();
        int low = 1000;
        int high = 10000;
        response = new Response(404, "");
        int result = 0;
        while (response.equalsStatusCode(404)) {
            result = r.nextInt(high - low) + low;
            response = server.isUserCodeUsed(result);
        }
        userCode = result;
        usercodeLabel.setText("Vendég azonosító: " + result + "     ");
    }

    private void checkPassword() {
        if (mode.equals(" Regisztráció") || mode.equals(" Vendég regisztrálása")) {
            errorPassword = true;
            if (new String(passwordInput.getPassword()).length() < 4) {
                errorLabel.setText("A jelszónak legalább 4 karakterből kell állnia!");
            } else if (!new String(passwordInput.getPassword()).matches(".*\\d.*")) {
                errorLabel.setText("A jelszónak legalább 1 számot tartalmaznia kell!");
            } else if (new String(passwordInput.getPassword()).equals(new String(passwordInput.getPassword()).toLowerCase())) {
                errorLabel.setText("A jelszónak legalább 1 nagybetűt tartalmaznia kell!");
            } else if (!new String(passwordInput.getPassword()).equals(new String(passwordAgainInput.getPassword()))) {
                errorLabel.setText("A megadott jelszavaknak egyezniük kell!");
            } else {
                errorPassword = false;
            }
        } else {
            errorPassword = false;
        }
        ableToLogin();
    }

    private void checkUsername() {
        if (mode.equals(" Regisztráció") || mode.equals(" Belépés vendégként") || mode.equals(" Vendég regisztrálása")) {
            errorUsername = true;
            if (usernameInput.getText().length() < 4) {
                errorLabel.setText("A felhasználónévnek legalább 4 karakterből kell állnia!");
            } else if (usernameInput.getText().length() > 20) {
                errorLabel.setText("A felhasználónév legfeljebb 20 karakterből állhat!");
            } else {
                errorUsername = false;
            }
        } else {
            errorUsername = false;
        }
        ableToLogin();
    }

    private void ableToLogin() {
        if (errorUsername || errorPassword) {
            okButton.setEnabled(false);
        } else if (errorServer) {
            errorLabel.setText(response.getMessage());
        } else {
            errorLabel.setText("");
            okButton.setEnabled(true);
        }
    }

    private void setupRegistry() {
        setupButtons(6);
        edit = new GridLayout(6, 1);
        edit.setVgap(64);
        this.setLayout(edit);
        this.add(usernamePanel);
        this.add(passPanel);
        this.add(passAgainPanel);
        this.add(errorLabel);
        this.add(buttonsPanel);
        errorUsername = true;
        errorPassword = true;
    }

    private void setupLogin() {
        setupButtons(5);
        edit = new GridLayout(5, 1);
        edit.setVgap(64);
        this.setLayout(edit);
        this.add(usernamePanel);
        this.add(passPanel);
        this.add(errorLabel);
        this.add(buttonsPanel);
        errorUsername = false;
        errorPassword = false;
    }

    private void setupGuest() {
        setupButtons(5);
        edit = new GridLayout(5, 1);
        edit.setVgap(64);
        this.setLayout(edit);
        this.add(usernamePanel);
        this.add(usercodePanel);
        this.add(errorLabel);
        this.add(buttonsPanel);
        errorUsername = true;
        errorPassword = false;
        randomUserNumber();
    }

    private void setupRegistryGuest() {
        setupButtons(6);
        edit = new GridLayout(6, 1);
        edit.setVgap(64);
        this.setLayout(edit);
        usernameInput.setText(m.getUser().getFullUsername());
        this.add(usernamePanel);
        this.add(passPanel);
        this.add(passAgainPanel);
        this.add(errorLabel);
        this.add(buttonsPanel);
        errorUsername = true;
        errorPassword = true;
    }

}
