package ru.dehasher.halarm;

import ru.dehasher.halarm.managers.DigitFilter;
import ru.dehasher.halarm.managers.KeyHook;
import ru.dehasher.halarm.managers.Methods;
import ru.dehasher.halarm.managers.TaskManager;
import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.stream.Stream;

public class HAlarm {
    private static final TaskManager taskManager = new TaskManager();
    private static boolean success = false;

    public static void main(String[] args) {
        onShutdown();
        JFrame frame  = new JFrame(String.join(" ", HAlarm.class.getSimpleName(), "Будильник"));
        JPanel panel  = new JPanel(new GridLayout(0, 1));

        try {
            run(frame, panel);
            Methods.play();
            KeyHook.block();
        } catch (Throwable t) {
            frame = new JFrame(String.join(" ", HAlarm.class.getSimpleName(), "Ошибка"));
            panel.removeAll();
            panel.add(new JLabel("Произошла ошибка: " + t, SwingConstants.CENTER));
            Stream.of(t.getStackTrace()).forEach(row -> panel.add(new JLabel(row.toString(), SwingConstants.CENTER)));
            Dimension size = frame.getSize();
            size.width += 30;
            size.height += 10;
            frame.setMinimumSize(new Dimension(300, 200));
            frame.setSize(size);
        }
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void onShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!success) Methods.reload();
        }));
    }

    private static void run(JFrame frame, JPanel panel) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowStateListener(e -> frame.setState(JFrame.NORMAL));
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setLocationByPlatform(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                    case KeyEvent.VK_SHIFT:
                    case KeyEvent.VK_DELETE:
                        Methods.reload();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                    case KeyEvent.VK_SHIFT:
                    case KeyEvent.VK_DELETE:
                        Methods.reload();
                }
            }
        });

        Image image = Toolkit.getDefaultToolkit().getImage(HAlarm.class.getResource("/icon.png"));
        frame.setIconImage(image);

        int x = Methods.getRandomInt(3, 9);
        int y = Methods.getRandomInt(3, 9);
        String result = String.valueOf(x * y);
        JLabel label = new JLabel("Реши пример: " + x + " * " + y, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 50));
        panel.add(label);

        JTextField input = new JTextField(5);
        input.setHorizontalAlignment(SwingConstants.CENTER);
        input.setFont(new Font("Arial", Font.PLAIN, 50));

        PlainDocument doc = (PlainDocument) input.getDocument();
        doc.setDocumentFilter(new DigitFilter());

        JButton button = new JButton("Подтвердить!");
        button.setFont(new Font("Arial", Font.PLAIN, 100));
        button.addActionListener(e -> {
            if (input.getText().equals(result)) {
                success = true;
                System.exit(0);
            } else if (!input.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Ответ неверный :)",
                        "Ошибка!",
                        JOptionPane.PLAIN_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "Ты не ввёл ответ :(",
                        "Ошибка!",
                        JOptionPane.PLAIN_MESSAGE
                );
            }
        });
        panel.add(input);
        panel.add(button);
    }

    @SuppressWarnings("unused")
    public static TaskManager getTaskManager() {
        return taskManager;
    }

    public static boolean isSuccess() {
        return success;
    }
}