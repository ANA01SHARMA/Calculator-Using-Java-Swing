import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator1 implements ActionListener {

    JFrame frame;
    JTextField textField;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[9];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton, negButton;
    JPanel panel;
    String currentExpression = "0";

    public Calculator1() {
        // === FRAME ===
        frame = new JFrame("Neon Glow Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 640);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.black);
        frame.setResizable(false);

        // === DISPLAY ===
        textField = new JTextField();
        textField.setBounds(35, 30, 340, 70);
        textField.setFont(new Font("Consolas", Font.BOLD, 34));
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setBackground(new Color(20, 20, 20, 220));
        textField.setForeground(new Color(0, 255, 255));
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2, true));

        // === FUNCTION BUTTONS ===
        addButton = createNeonButton("+", new Color(0, 255, 255));
        subButton = createNeonButton("-", new Color(0, 255, 255));
        mulButton = createNeonButton("*", new Color(0, 255, 255));
        divButton = createNeonButton("/", new Color(0, 255, 255));
        decButton = createNeonButton(".", new Color(180, 180, 180));
        equButton = createNeonButton("=", new Color(255, 60, 60));
        delButton = createNeonButton("DEL", new Color(255, 165, 0));
        clrButton = createNeonButton("C", new Color(255, 215, 0));
        negButton = createNeonButton("(-)", new Color(150, 150, 150));

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;
        functionButtons[8] = negButton;

        for (JButton btn : functionButtons) btn.addActionListener(this);

        // === NUMBER BUTTONS ===
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createNeonButton(String.valueOf(i), new Color(60, 60, 60));
            numberButtons[i].setForeground(Color.WHITE);
            numberButtons[i].addActionListener(this);
        }

        // === MAIN BUTTON PANEL ===
        panel = new JPanel();
        panel.setBounds(35, 120, 340, 305);
        panel.setLayout(new GridLayout(4, 4, 12, 12));
        panel.setOpaque(false);

        // === GRID ARRANGEMENT ===
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

        // === BOTTOM ROW ===
        delButton.setBounds(35, 465, 100, 50);
        clrButton.setBounds(160, 465, 100, 50);
        negButton.setBounds(285, 465, 90, 50);

        // === ADD COMPONENTS ===
        frame.add(textField);
        frame.add(panel);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(negButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // === Neon Button Creator ===
    private JButton createNeonButton(String text, Color borderColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(25, 25, 25, 220));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverColor = borderColor.brighter();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(borderColor);
                button.setForeground(Color.BLACK);
                button.setBorder(BorderFactory.createLineBorder(hoverColor, 3, true));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(25, 25, 25, 220));
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));
            }
        });

        return button;
    }

    // === ACTION HANDLER ===
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
            else currentExpression += command;
        } else if (command.equals("=")) {
            try {
                double result = eval(currentExpression);
                if (Double.isNaN(result) || Double.isInfinite(result))
                    currentExpression = "Error";
                else {
                    currentExpression = String.valueOf(result);
                    if (currentExpression.endsWith(".0"))
                        currentExpression = currentExpression.replace(".0", "");
                }
            } catch (Exception ex) {
                currentExpression = "Error";
            }
        } else if (command.equals("C")) {
            currentExpression = "0";
        } else if (command.equals("DEL")) {
            if (currentExpression.length() > 1)
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            else currentExpression = "0";
        } else if (command.equals("(-)")) {
            try {
                double val = Double.parseDouble(currentExpression);
                currentExpression = String.valueOf(-val);
            } catch (NumberFormatException ignored) {}
        }

        textField.setText(currentExpression);
    }

    // === Expression Evaluator ===
    private double eval(String expression) {
        try {
            expression = expression.replaceAll("[^0-9.*/+\\-]", "");
            java.util.Stack<Double> numbers = new java.util.Stack<>();
            java.util.Stack<Character> ops = new java.util.Stack<>();

            for (int i = 0; i < expression.length(); i++) {
                char ch = expression.charAt(i);

                if (Character.isDigit(ch) || ch == '.') {
                    StringBuilder sb = new StringBuilder();
                    while (i < expression.length() &&
                           (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        sb.append(expression.charAt(i++));
                    }
                    numbers.push(Double.parseDouble(sb.toString()));
                    i--;
                } else if ("+-*/".indexOf(ch) >= 0) {
                    while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                        numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
                    }
                    ops.push(ch);
                }
            }

            while (!ops.isEmpty()) {
                numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
            }

            return numbers.pop();
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private int precedence(char op) {
        return (op == '+' || op == '-') ? 1 : (op == '*' || op == '/') ? 2 : 0;
    }

    private double applyOp(char op, double b, double a) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> b == 0 ? Double.NaN : a / b;
            default -> 0;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator1::new);
    }
}
