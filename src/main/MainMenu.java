package main;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Loja2D");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("LOJA 2D", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        gbc.gridy = 0;
        panel.add(title, gbc);

        JButton newGame = new JButton("New Game");
        styleButton(newGame);
        gbc.gridy = 1;
        panel.add(newGame, gbc);

        JButton loadGame = new JButton("Load Game");
        styleButton(loadGame);
        gbc.gridy = 2;
        panel.add(loadGame, gbc);

        add(panel);

        newGame.addActionListener(e -> {
            dispose();
            Main.startGame(false);
        });

        loadGame.addActionListener(e -> {
            dispose();
            Main.startGame(true);
        });

        setVisible(true);
    }

    private void styleButton(JButton b) {
        b.setPreferredSize(new Dimension(200, 40));
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setFocusPainted(false);
    }
}
