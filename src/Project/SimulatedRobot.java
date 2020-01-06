package Project;

public class SimulatedRobot {
    public MotionDataManager feedforward, output;
    private static double dt = Constants.kSampleRate;
    private double robotX, robotY, robotV, robotLV, robotRV, robotAngle, robotAngularV;
    private double motorMaxV, baseWidth;

    public SimulatedRobot(MotionDataManager feedforward, double motorMaxV, double baseWidth){
        this.feedforward = feedforward;
        this.motorMaxV = motorMaxV;
        this.baseWidth = baseWidth;
        robotX = feedforward.getX(0);
        robotY = feedforward.getY(0);
        robotAngle = feedforward.getAngle(0);
        output = new MotionDataManager();

        calculatePath();
    }

    private void calculatePath() {
        for(int i = 0; i < feedforward.size(); i++){
            setLeft(feedforward.getLWheelV(i));
            setRight(feedforward.getRWheelV(i));
            calculateVelocities();
            move();
            output.addX(robotX);
            output.addY(robotY);
            output.addV(robotV);
            output.addAngle(robotAngle);
            output.addAngularV(robotAngularV);
            output.addRWheelV(robotRV);
            output.addLWheelV(robotLV);
            output.addT(dt * i);
        }
    }

    private void setLeft(double v) {
        /*
        if(v > motorMaxV){
            v = motorMaxV;
        }

         */
        robotLV = v;
    }

    private void setRight(double v) {
        /*
        if(v > motorMaxV){
            v = motorMaxV;
        }

         */
        robotRV = v;
    }

    private void calculateVelocities() {
        robotAngularV = (robotRV - robotLV) / baseWidth;
        robotV = (robotRV + robotLV) / 2;
    }
    private void move(){
        robotAngle += Math.toDegrees(robotAngularV * dt);
        robotX += robotV * Math.cos(Math.toRadians(robotAngle)) * dt;
        robotY += robotV * Math.sin(Math.toRadians(robotAngle)) * dt;
    }
}
