import java.util.Arrays;

public class Main {
    public static void main(String[] args){
        QuinticSpline spline = new QuinticSpline(0,0,10,50,30,90);
        System.out.println(spline.getAngle(1));
    }
}
