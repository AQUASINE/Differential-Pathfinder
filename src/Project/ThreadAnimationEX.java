package Project;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class ThreadAnimationEX extends JFrame {

    public ThreadAnimationEX() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setResizable(false);
        pack();

        setTitle("AP Task: Robot Driving Simulator");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new ThreadAnimationEX();
            ex.setVisible(true);
        });
    }
    public static void start(){
        EventQueue.invokeLater(() -> {
            JFrame ex = new ThreadAnimationEX();
            ex.setVisible(true);
        });
    }
}