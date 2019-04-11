import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        double[][] list = {
                {2,3,0},
                {4,8,30},
                {7,2,90},
                {5,10,45},
                {4,1,60},
                {5,5,90}
        };

        Path path = new Path(list);
        PathChart chart = new PathChart(path);

    }
}
