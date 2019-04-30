package Project;

import sun.java2d.windows.GDIRenderer;

import java.awt.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static Project.Main.*;

//based on JPanel sample
public class Board extends JPanel implements Runnable {

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 400;
    private final int INITIAL_X = -40;
    private final int INITIAL_Y = -40;
    private final int DELAY = (int)(1000 * Constants.kSampleRate);
    private final int PADDING = 140;
    private final int SIZE_MULTIPLIER = 15;
    private Thread animator;
    private int x, y, angle;
    private double w_r, w_l;
    private int t = 0;
    private double base_width = 1;
    public Board() {

        initBoard();
    }


    private void initBoard() {

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        x = INITIAL_X;
        y = INITIAL_Y;
        angle = 0;
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
        double squareWidth = SIZE_MULTIPLIER*base_width;
        double WHEELVMULT = SIZE_MULTIPLIER/4;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(40,40,80));
        for(int i = 0; i < m_dataX.size()-1; i++){
            g2.drawLine((int)Math.round(m_dataX.get(i)*SIZE_MULTIPLIER)+PADDING,B_HEIGHT-(int)Math.round(m_dataY.get(i)*SIZE_MULTIPLIER+PADDING),(int)Math.round(m_dataX.get(i+1)*SIZE_MULTIPLIER)+PADDING,B_HEIGHT-(int)Math.round(m_dataY.get(i+1)*SIZE_MULTIPLIER+PADDING));
        }
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(0,80,80));
        for(int r = 0; r <= list.length-1; r++){
            g2.drawLine((int)Math.round(list[r][0]*SIZE_MULTIPLIER+PADDING),B_HEIGHT-(int)Math.round(list[r][1]*SIZE_MULTIPLIER+PADDING),(int)Math.round(list[r][0]*SIZE_MULTIPLIER+PADDING),B_HEIGHT-(int)Math.round(list[r][1]*SIZE_MULTIPLIER+PADDING));
        }
        g2.setStroke(new BasicStroke(4));
        g2.setColor(new Color(0,255,255));
        g2.drawLine((int)(Math.cos(Math.toRadians(angle+45))*squareWidth*Math.sqrt(2)+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+45))*squareWidth*Math.sqrt(2)+y),
                (int)(Math.cos(Math.toRadians(angle+135))*squareWidth*Math.sqrt(2)+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+135))*squareWidth*Math.sqrt(2)+y));
        g2.drawLine((int)(Math.cos(Math.toRadians(angle+135))*squareWidth*Math.sqrt(2)+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+135))*squareWidth*Math.sqrt(2)+y),
                (int)(Math.cos(Math.toRadians(angle+225))*squareWidth*Math.sqrt(2)+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+225))*squareWidth*Math.sqrt(2)+y));
        g2.drawLine((int)(Math.cos(Math.toRadians(angle+225))*squareWidth*Math.sqrt(2)+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+225))*squareWidth*Math.sqrt(2)+y),
                (int)(Math.cos(Math.toRadians(angle+315))*squareWidth*Math.sqrt(2)+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+315))*squareWidth*Math.sqrt(2)+y));
        g2.drawLine((int)(Math.cos(Math.toRadians(angle+315))*squareWidth*Math.sqrt(2)+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+315))*squareWidth*Math.sqrt(2)+y),
                (int)(Math.cos(Math.toRadians(angle+45))*squareWidth*Math.sqrt(2)+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+45))*squareWidth*Math.sqrt(2)+y));
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(255,0,0));
        g2.drawLine((int)(Math.cos(Math.toRadians(angle+90))*squareWidth+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+90))*squareWidth+y),
                (int)(Math.cos(Math.toRadians(angle+90))*squareWidth+x+w_l*WHEELVMULT*Math.cos(Math.toRadians(angle))),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+90))*squareWidth+y+w_l*WHEELVMULT*Math.sin(Math.toRadians(angle))));
        g2.drawLine((int)(Math.cos(Math.toRadians(angle+270))*squareWidth+x),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+270))*squareWidth+y),
                (int)(Math.cos(Math.toRadians(angle+270))*squareWidth+x+w_r*WHEELVMULT*Math.cos(Math.toRadians(angle))),
                B_HEIGHT-(int)(Math.sin(Math.toRadians(angle+270))*squareWidth+y+w_r*WHEELVMULT*Math.sin(Math.toRadians(angle))));
        Toolkit.getDefaultToolkit().sync();
    }

    private void cycle() {

        if (t == m_dataT.size()) t = 0;
        w_r = m_dataWheel_r.get(t);
        w_l = m_dataWheel_l.get(t);
        x = (int)Math.round((m_dataX.get(t)+m_dataX.get(0))*SIZE_MULTIPLIER+PADDING);
        y = (int)Math.round((m_dataY.get(t)+m_dataY.get(0))*SIZE_MULTIPLIER+PADDING);
        angle = (int)(Math.round(m_dataAngle.get(t)));
        t++;
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            cycle();
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