/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package java_isp121.cozy_house;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Maria
 */
public class Cozy_house {

    public static void main(String[] args) {
         JFrame frame = new JFrame("Уютный дом");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        // Создание панели логина
        JPanel loginContainer = new JPanel();
        loginContainer.setBackground(new Color(215, 245, 255));
        loginContainer.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        loginContainer.setLayout(new BoxLayout(loginContainer, BoxLayout.Y_AXIS));
        loginContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Установка шрифта
        Font font = new Font("Open Sans", Font.PLAIN, 14);
        Font boldFont = new Font("Open Sans", Font.BOLD, 14);

        // Заголовок
        JLabel header = new JLabel("Авторизация");
        header.setFont(new Font("Open Sans", Font.BOLD, 18));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Поле ввода почты
        JLabel login = new JLabel("Логин");
        JTextField emailField = new JTextField("Email");
        emailField.setFont(font);
        emailField.setForeground(Color.GRAY);
        emailField.setMaximumSize(new Dimension(300, 40));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Email")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Email");
                    emailField.setForeground(Color.GRAY);
                }
            }
        });

        // Поле ввода Пароль
         JLabel password = new JLabel("Пароль");
        JPasswordField passwordField = new JPasswordField("Пароль");
        passwordField.setFont(font);
        passwordField.setForeground(Color.GRAY);
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals("Пароль")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('*');
                    passwordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Пароль");
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        // Кнопка Войти
        JButton loginButton = new JButton("Войти");
        loginButton.setFont(boldFont);
        loginButton.setBackground(Color.GREEN);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        loginButton.setMaximumSize(new Dimension(300, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Действие при нажатии на кнопку
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    login(frame, emailField.getText(), new String(passwordField.getPassword()));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // Добавление компонентов
        loginContainer.add(header);
        loginContainer.add(login);
        loginContainer.add(password);
        loginContainer.add(emailField);
        loginContainer.add(passwordField);
        loginContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Отступ между полем ввода и кнопкой
        loginContainer.add(loginButton);
        loginContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Отступ между кнопкой и ссылкой

        // Настройка размещения контейнера логина в главном окне
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        frame.add(loginContainer, gbc);

        // Настройка фона главного окна
        frame.getContentPane().setBackground(new Color(215, 245, 255));

        // Отображение окна
        frame.setVisible(true);
    }

    private static void login(JFrame frame, String email, String password) throws SQLException {
        try (Connection connection = DataBase.getConnection()) {
            String sql = "SELECT * FROM client WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                frame.dispose();
                new MainFrame().setVisible(true); // Переход в новое окно после успешного входа
            } else {
                JOptionPane.showMessageDialog(null, "Неверный логин или пароль.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
}
