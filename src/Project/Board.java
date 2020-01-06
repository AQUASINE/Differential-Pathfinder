package Project;

import java.awt.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//based on JPanel sample
public class Board extends JPanel implements Runnable {

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 400;
    private final int DELAY = (int)(1000 * Constants.kSampleRate);
    private final int PADDING = 140;
    private final int SIZE_MULTIPLIER = 15;
    private Thread animator;
    private int t = 0;
    private double base_width = 1;
    private DrawableRobot[] robots;
    private double[][] points;
    private double squareWidth;
    private double WHEELVMULT;

    public Board(DrawableRobot[] robots, double[][] points) {
        this.robots = robots;
        this.points = points;
        initBoard();
    }


    private void initBoard() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void setBaseWidth(double baseWidth){
        this.base_width = baseWidth;
    }

    private void draw(Graphics g) {
        squareWidth = SIZE_MULTIPLIER*base_width;
        WHEELVMULT = SIZE_MULTIPLIER/4;
        Graphics2D g2 = (Graphics2D) g;
        for(DrawableRobot r : robots) {
            drawPath(g2, r);
        }
        drawPoints(g2);
        for(DrawableRobot r : robots) {
            drawRobotOnPath(g2, r);
            drawWheelVectors(g2, r);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawPath(Graphics2D g2, DrawableRobot robot) {
        g2.setStroke(new BasicStroke(1));
        g2.setColor(robot.getPathColor());
        for(int i = 0; i < robot.data.size()-1; i++){
            g2.drawLine((int)Math.round(robot.data.getX(i)*SIZE_MULTIPLIER)+PADDING,
                    B_HEIGHT-(int)Math.round(robot.data.getY(i)*SIZE_MULTIPLIER+PADDING),
                    (int)Math.round(robot.data.getX(i+1)*SIZE_MULTIPLIER)+PADDING,
                    B_HEIGHT-(int)Math.round(robot.data.getY(i+1)*SIZE_MULTIPLIER+PADDING));
        }
    }

    private void drawPoints(Graphics2D g2) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(0,80,80));
        for(int r = 0; r <= points.length-1; r++){
            g2.drawLine((int)Math.round(points[r][0]*SIZE_MULTIPLIER+PADDING),
                    B_HEIGHT-(int)Math.round(points[r][1]*SIZE_MULTIPLIER+PADDING),
                    (int)Math.round(points[r][0]*SIZE_MULTIPLIER+PADDING),
                    B_HEIGHT-(int)Math.round(points[r][1]*SIZE_MULTIPLIER+PADDING));
        }
    }

    private void drawRobotOnPath(Graphics2D g2, DrawableRobot robot){
        g2.setStroke(new BasicStroke(4));
        g2.setColor(robot.getRobotColor());
        g2.drawLine((int)(Math.cos(Math.toRadians(robot.angle+45))*squareWidth*Math.sqrt(2)+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+45))*squareWidth*Math.sqrt(2)+robot.y*SIZE_MULTIPLIER+PADDING),
                (int)(Math.cos(Math.toRadians(robot.angle+135))*squareWidth*Math.sqrt(2)+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+135))*squareWidth*Math.sqrt(2)+robot.y*SIZE_MULTIPLIER+PADDING));
        g2.drawLine((int)(Math.cos(Math.toRadians(robot.angle+135))*squareWidth*Math.sqrt(2)+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+135))*squareWidth*Math.sqrt(2)+robot.y*SIZE_MULTIPLIER+PADDING),
                (int)(Math.cos(Math.toRadians(robot.angle+225))*squareWidth*Math.sqrt(2)+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+225))*squareWidth*Math.sqrt(2)+robot.y*SIZE_MULTIPLIER+PADDING));
        g2.drawLine((int)(Math.cos(Math.toRadians(robot.angle+225))*squareWidth*Math.sqrt(2)+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+225))*squareWidth*Math.sqrt(2)+robot.y*SIZE_MULTIPLIER+PADDING),
                (int)(Math.cos(Math.toRadians(robot.angle+315))*squareWidth*Math.sqrt(2)+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+315))*squareWidth*Math.sqrt(2)+robot.y*SIZE_MULTIPLIER+PADDING));
        g2.drawLine((int)(Math.cos(Math.toRadians(robot.angle+315))*squareWidth*Math.sqrt(2)+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+315))*squareWidth*Math.sqrt(2)+robot.y*SIZE_MULTIPLIER+PADDING),
                (int)(Math.cos(Math.toRadians(robot.angle+45))*squareWidth*Math.sqrt(2)+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+45))*squareWidth*Math.sqrt(2)+robot.y*SIZE_MULTIPLIER+PADDING));
    }

    private void drawWheelVectors(Graphics2D g2, DrawableRobot robot) {
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(255,0,0));
        g2.drawLine((int)(Math.cos(Math.toRadians(robot.angle+90))*squareWidth+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+90))*squareWidth+robot.y*SIZE_MULTIPLIER+PADDING),
                (int)(Math.cos(Math.toRadians(robot.angle+90))*squareWidth+robot.x*SIZE_MULTIPLIER+PADDING+robot.w_l*WHEELVMULT*Math.cos(Math.toRadians(robot.angle))),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+90))*squareWidth+robot.y*SIZE_MULTIPLIER+PADDING+robot.w_l*WHEELVMULT*Math.sin(Math.toRadians(robot.angle))));
        g2.drawLine((int)(Math.cos(Math.toRadians(robot.angle+270))*squareWidth+robot.x*SIZE_MULTIPLIER+PADDING),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+270))*squareWidth+robot.y*SIZE_MULTIPLIER+PADDING),
                (int)(Math.cos(Math.toRadians(robot.angle+270))*squareWidth+robot.x*SIZE_MULTIPLIER+PADDING+robot.w_r*WHEELVMULT*Math.cos(Math.toRadians(robot.angle))),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(robot.angle+270))*squareWidth+robot.y*SIZE_MULTIPLIER+PADDING+robot.w_r*WHEELVMULT*Math.sin(Math.toRadians(robot.angle))));
    }
    
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            for(DrawableRobot r : robots) {
                r.cycle();
            }
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) sleep = 2;

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {

                String msg = String.format("Thread interrupted: %s", e.getMessage());

                JOptionPane.showMessageDialog(this, msg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}