package Project;

public class SimulatedRobot {

    public MotionDataManager feedforward, output;
    private static double dt = Constants.kTimeStep;
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
            robotAngularV = feedforward.getAngularV(i);
            //robotV = feedforward.getV(i);
            calculateVelocities();
            move();
            System.out.println(feedforward.getAngle(i) + " " + robotAngle);
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
        //robotAngularV = Math.toDegrees((robotRV - robotLV) / baseWidth); //deg/s TODO: Something wrong here
        robotV = (robotRV + robotLV) / 2; //m/s
    }
    private void move(){
        //TODO: Something wrong here too
        robotAngle += robotAngularV * dt; //deg/s * s
        robotX += robotV * Math.cos(Math.toRadians(robotAngle)) * dt; //m/s * s
        robotY += robotV * Math.sin(Math.toRadians(robotAngle)) * dt; //m/s * s
    }
}
