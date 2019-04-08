import java.util.ArrayList;

public class Path {
    public double[][] list;
    ArrayList<QuinticSpline> path_splines;
    ArrayList<Double>[] velocity_profile;
    double total_length = 0;
    double accel_time, accel_dist, decel_time, decel_dist, max_v_dist, max_v_time, total_time;


    public Path(double[][] list){
        path_splines = new ArrayList<>();
        this.list = list;
        Debug.print("Max Index: " + (list.length-1), 2);
        for (int i = 0; i <= list.length-2; i++){
            path_splines.add(new QuinticSpline(list[i], list[i+1]));
            total_length += path_splines.get(i).arc_length();
        }
        Debug.print("Total Length: " + total_length + " meters", 2);
        velocity_profile = generate_motion_profile(Constants.kMax_accel, Constants.kMax_v, Constants.kMax_decel);
        Debug.graph(velocity_profile[0],velocity_profile[1],2);
    }

    public ArrayList<Double>[] generate_motion_profile(double max_accel, double max_v, double max_decel){
        ArrayList<Double> profile = new ArrayList<>();
        ArrayList<Double> time = new ArrayList<>();

        accel_time = max_v/(max_accel);
        accel_dist = 0.5*accel_time*accel_time*max_accel;

        decel_time = -max_v/(max_decel);
        decel_dist = 0.5*decel_time*decel_time*-max_decel;

        if (total_length-(accel_dist + decel_dist) < 0){
            accel_dist = total_length/2;
            decel_dist = total_length/2;
            accel_time = Math.sqrt(2*(accel_dist)/max_accel);
            decel_time = Math.sqrt(2*(decel_dist)/-max_decel);
        }
        max_v_dist = total_length-(accel_dist + decel_dist);
        max_v_time = max_v_dist/max_v;

        total_time = accel_time+max_v_time+decel_time;

        Debug.print("\n", 3);
        Debug.print("Accel dist: " + accel_dist, 3);
        Debug.print("Accel time: " + accel_time, 3);
        Debug.print("\n", 3);
        Debug.print("Max v dist: " + max_v_dist, 3);
        Debug.print("Max v time: " + max_v_time, 3);
        Debug.print("\n", 3);
        Debug.print("Decel dist: " + decel_dist, 3);
        Debug.print("Decel time: " + decel_time, 3);
        Debug.print("\n", 3);

        double current_v = 0;
        for(double t = 0; t<total_time; t+=Constants.kTimeStep){
            if(t<accel_time){
                current_v += max_accel*Constants.kTimeStep;
                profile.add(current_v);
            }
            else if(t<accel_time+max_v_time){
                profile.add(current_v);
            }
            else if(t<total_time){
                current_v += max_decel*Constants.kTimeStep;
                profile.add(current_v);
            }
            time.add(t);
        }
        Debug.print("Seconds: " + total_time, 2);
        return new ArrayList[] {time,profile};
    }
    public double getX(double t){
         int i = 0;
        if (t>1){
            i = (int)t;
            t -= i;
        }
        try {
            return path_splines.get(i).getX(t);
        }
        catch (IndexOutOfBoundsException e)
        {
            return -1;
        }
    }
    public double getY(double t){
        int i = 0;
        if (t>1){
            i = (int)t;
            t -= i;
        }
        try {
            return path_splines.get(i).getY(t);
        }
        catch (IndexOutOfBoundsException e)
        {
            return -1;
        }
    }
}
