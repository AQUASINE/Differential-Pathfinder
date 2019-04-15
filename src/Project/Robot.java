package Project;

public class Robot {
    public double v_r, v_l, curr_v, curr_a;
    private double base, max_v, max_a;
    public Robot(double base, double max_v, double max_a){
        this.base = base;
        this.max_v = max_v;
        this.max_a = max_a;
    }
    public void rotate(double angle0, double angle1){
        if (angle1>angle0){
            v_r = curr_v;
            v_l = curr_v-base*(angle1-angle0);
            curr_v = 0.5*(v_r+v_l);
        }
        else if(angle0<angle1){
            v_l = curr_v;
            v_r = v_r-base*(angle1-angle0);
        }
        else{
            v_r = curr_v;
            v_l = curr_v;
        }
    }
}
