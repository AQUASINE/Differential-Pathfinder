package Project;

public class Robot {
    public double v_r, v_l, curr_v, curr_a;
    private double base, max_v, max_a;
    public Robot(double base, double max_v, double max_a){
        this.base = base;
        this.max_v = max_v;
        this.max_a = max_a;
    }
    public Double[] wheel_v(double v, double w) {
        double v_l = wheel_v_l(v, w);
        double v_r = wheel_v_r(v, w);
        return new Double[] {v_l, v_r};
    }
    public double wheel_v_l(double v, double w){
        return v - w * base / 2;
    }
    public double wheel_v_r(double v, double w){
        return v + w * base / 2;
    }
}
