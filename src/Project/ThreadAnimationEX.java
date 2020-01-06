package Project;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class ThreadAnimationEX extends JFrame {

    public ThreadAnimationEX(DrawableRobot[] robots, double[][] points) {
        initUI(robots, points);
    }

    private void initUI(DrawableRobot[] robots, double[][] points) {
        add(new Board(robots, points));
        setResizable(false);
        pack();

        setTitle("Robot Driving Simulation");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void start(DrawableRobot[] robots, double[][] points){
        EventQueue.invokeLater(() -> {
            JFrame ex = new ThreadAnimationEX(robots, points);
            ex.setVisible(true);
        });
    }
}