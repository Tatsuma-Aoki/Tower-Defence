package uectd.gameSystem;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FatalError {
    public static void quit(String message) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, message, "エラー", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
