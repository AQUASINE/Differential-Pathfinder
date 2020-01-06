package Project;


import java.awt.*;

public class DrawableRobot {
    MotionDataManager data;
    private Color pathColor, robotColor;
    double w_r, w_l, x, y, angle;
    int t = 0;

    public DrawableRobot(MotionDataManager data){
        this.data = data;
        pathColor = new Color(40,40,80);
        robotColor = new Color(0,255,255, 100);
        loadData();
    }

    private void loadData() {
        w_r = data.getRWheelV(t);
        w_l = data.getLWheelV(t);
        x = data.getX(t);
        y = data.getY(t);
        angle = data.getAngle(t);
    }
    public void cycle() {
        if (t == data.size()) t = 0;
        loadData();
        t++;
    }

    public void setPathColor(Color pathColor){
        this.pathColor = pathColor;
    }

    public Color getPathColor(){
        return pathColor;
    }

    public void setRobotColor(Color robotColor){
        this.robotColor = robotColor;
    }

    public Color getRobotColor(){
        return robotColor;
    }
}
