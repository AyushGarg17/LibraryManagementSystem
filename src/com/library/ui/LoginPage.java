package gui;

import model.Admin;
import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("Library Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Admin admin = new Admin();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 250));

        JLabel title = new JLabel("Admin Login");
        title.setBounds(140, 20, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JTextField user = new JTextField();
        user.setBounds(100, 80, 200, 30);
        user.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField pass = new JPasswordField();
        pass.setBounds(100, 130, 200, 30);
        pass.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton login = new JButton("Login");
        login.setBounds(140, 180, 120, 35);
        login.setBackground(new Color(0, 120, 215));
        login.setForeground(Color.WHITE);

        login.addActionListener(e -> {
            if (admin.login(user.getText(), new String(pass.getPassword()))) {
                new Dashboard().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        });

        panel.add(title);
        panel.add(user);
        panel.add(pass);
        panel.add(login);

        add(panel);
    }
}
