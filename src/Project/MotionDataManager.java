package Project;

import java.util.ArrayList;

public class MotionDataManager {
    private static final int cX = 0;
    private static final int cY = 1;
    private static final int cV = 2;
    private static final int cAngle = 3;
    private static final int cAngleV = 4;
    private static final int cLV = 5;
    private static final int cRV = 6;
    private static final int cT = 7;

    private ArrayList<ArrayList<Double>> data;

    public MotionDataManager() {
        int dataColumns = 8;
        data = new ArrayList<>();
        for(int i = 0; i < dataColumns; i++){
            data.add(new ArrayList<>());
        }
    }

    public Double getX(int i){
        return data.get(cX).get(i);
    }
    public Double getY(int i){
        return data.get(cY).get(i);
    }
    public Double getV(int i){
        return data.get(cV).get(i);
    }
    public Double getAngle(int i){
        return data.get(cAngle).get(i);
    }
    public Double getAngularV(int i){
        return data.get(cAngleV).get(i);
    }
    public Double getLWheelV(int i){
        return data.get(cLV).get(i);
    }
    public Double getRWheelV(int i){
        return data.get(cRV).get(i);
    }

    public void addX(double i){
        data.get(cX).add(i);
    }
    public void addY(double i){
        data.get(cY).add(i);
    }
    public void addV(double i){
        data.get(cV).add(i);
    }
    public void addAngle(double i){
        data.get(cAngle).add(i);
    }
    public void addAngularV(double i){
        data.get(cAngleV).add(i);
    }
    public void addLWheelV(double i){
        data.get(cLV).add(i);
    }
    public void addRWheelV(double i){
        data.get(cRV).add(i);
    }
    public void addT(double i){
        data.get(cT).add(i);
    }

    public int size(){
        int largestSize = 0;
        for(ArrayList a : data){
            if (a.size() > largestSize) largestSize = a.size();
        }
        return largestSize;
    }
}
