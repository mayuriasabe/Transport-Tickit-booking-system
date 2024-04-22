
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Map<String, String> userMap;

    public LoginForm() {
        super("Login/Signup Page");
        userMap = new HashMap<>();
        createUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Transport Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        panel.add(titleLabel);
        panel.add(new JLabel()); // Placeholder
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(signupButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (isValidLogin(username, password)) {
                    openBookingApp();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (userMap.containsKey(username)) {
                    JOptionPane.showMessageDialog(LoginForm.this, "Username already exists!", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    userMap.put(username, password);
                    JOptionPane.showMessageDialog(LoginForm.this, "Signup successful!", "Signup Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private boolean isValidLogin(String username, String password) {
        // Check if username exists and password matches
        return userMap.containsKey(username) && userMap.get(username).equals(password);
    }

    private void openBookingApp() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new BookingApp().setVisible(true);
                dispose(); // Close the login/signup page after opening BookingApp
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new LoginForm().setVisible(true);
            }
        });
    }
}

