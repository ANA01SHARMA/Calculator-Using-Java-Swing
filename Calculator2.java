import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator2 implements ActionListener {

    JFrame frame;
    JTextField textField;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[9];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton, negButton;
    JPanel panel;
    private String currentExpression = "0";

    public Calculator2() {
        frame = new JFrame("Neon Glow Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 550);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.black);
        frame.setResizable(false);

        // === DISPLAY ===
        textField = new JTextField();
        textField.setBounds(50, 25, 300, 60);
        textField.setFont(new Font("Consolas", Font.BOLD, 32));
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setBackground(Color.black);
        textField.setForeground(new Color(0, 255, 255));
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2));

        // === BUTTON CREATION ===
        addButton = createNeonButton("+", new Color(0, 200, 255));
        subButton = createNeonButton("-", new Color(0, 200, 255));
        mulButton = createNeonButton("*", new Color(0, 200, 255));
        divButton = createNeonButton("/", new Color(0, 200, 255));
        decButton = createNeonButton(".", new Color(160, 160, 160));
        equButton = createNeonButton("=", new Color(255, 60, 60));
        delButton = createNeonButton("DEL", new Color(100, 100, 100));
        clrButton = createNeonButton("C", new Color(100, 100, 100));
        negButton = createNeonButton("(-)", new Color(100, 100, 100));

        functionButtons = new JButton[]{
            addButton, subButton, mulButton, divButton,
            decButton, equButton, delButton, clrButton, negButton
        };

        for (JButton btn : functionButtons) {
            btn.addActionListener(this);
        }

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createNeonButton(String.valueOf(i), new Color(70, 70, 70));
            numberButtons[i].addActionListener(this);
        }

        // === PANEL ===
        panel = new JPanel();
        panel.setBounds(50, 110, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(Color.black);

        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(addButton);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(divButton);

        // === Bottom Buttons ===
        delButton.setBounds(50, 440, 95, 50);
        clrButton.setBounds(155, 440, 95, 50);
        negButton.setBounds(260, 440, 95, 50);

        frame.add(panel);
        frame.add(textField);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(negButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        textField.setText(currentExpression);
    }

    // 💡 Neon glow effect using custom painting (no overlap)
    private JButton createNeonButton(String text, Color glowColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // soft neon glow halo
                for (int i = 1; i <= 6; i++) {
                    g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 20 - i * 3));
                    g2.setStroke(new BasicStroke(i));
                    g2.drawRoundRect(i, i, getWidth() - 2 * i, getHeight() - 2 * i, 10, 10);
                }

                // text
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(20, 20, 20));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(glowColor.getRed() / 8, glowColor.getGreen() / 8, glowColor.getBlue() / 8));
                button.setForeground(glowColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(20, 20, 20));
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator2::new);
    }

    // === Calculator Logic ===
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (currentExpression.equals("Error")) currentExpression = "0";

        if (command.matches("[0-9.]")) {
            if (currentExpression.equals("0")) currentExpression = command;
            else currentExpression += command;
        } else if (command.matches("[+\\-*/]")) {
            char lastChar = currentExpression.charAt(currentExpression.length() - 1);
            if ("+-*/".indexOf(lastChar) >= 0)
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1) + command;
            else
                currentExpression += command;
        } else if (command.equals("=")) {
            try {
                currentExpression = String.valueOf(evaluate(currentExpression));
            } catch (Exception ex) {
                currentExpression = "Error";
            }
        } else if (command.equals("C")) {
            currentExpression = "0";
        } else if (command.equals("DEL")) {
            if (currentExpression.length() > 1)
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            else
                currentExpression = "0";
        } else if (command.equals("(-)")) {
            if (!currentExpression.equals("0") && !currentExpression.equals("Error")) {
                try {
                    double val = Double.parseDouble(currentExpression);
                    currentExpression = String.valueOf(-val);
                } catch (NumberFormatException ignored) {}
            }
        }

        textField.setText(currentExpression);
    }

    private double evaluate(String expression) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() { ch = (++pos < expression.length()) ? expression.charAt(pos) : -1; }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) { nextChar(); return true; }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}
