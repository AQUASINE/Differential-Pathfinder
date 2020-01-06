package Project;

public class FeedforwardPhysicsGen {
    public double v_r, v_l, curr_v, curr_a;
    private double base, max_v, max_a;
    public FeedforwardPhysicsGen(double base, double max_v, double max_a){
        this.base = base;
        this.max_v = max_v;
        this.max_a = max_a;
    }
    public Double[] wheel_v(double v, double w) {
        double v_l = wheelLV(v, w);
        double v_r = wheelRV(v, w);
        return new Double[] {v_l, v_r};
    }
    public double wheelLV(MotionDataManager data, int i){
        double v = data.getV(i);
        double w = data.getAngularV(i)*Constants.kTimeStep;
        return wheelLV(v,w);
    }
    public double wheelRV(MotionDataManager data, int i){
        double v = data.getV(i);
        double w = data.getAngularV(i)*Constants.kTimeStep;
        return wheelRV(v,w);
    }
    public double wheelLV(double v, double w){
        return v - w * base / 2;
    }
    public double wheelRV(double v, double w){
        return v + w * base / 2;
    }
}
