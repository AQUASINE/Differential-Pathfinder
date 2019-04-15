package Project;

public class QuinticSpline {
    private final double kIntegralRate = 0.01;
    private double x0, x1, dx0, dx1, ddx0, ddx1, y0, y1, dy0, dy1, ddy0, ddy1;
    private double ax, bx, cx, ex, fx, gx, ay, by, cy, ey, fy, gy;
    public QuinticSpline(double[] list0, double[] list1){
        this(list0[0],list0[1],list1[0],list1[1],list0[2],list1[2]);
    }
    public QuinticSpline(double x0, double y0, double x1, double y1, double deg0, double deg1){
        this.x0 = x0;
        this.x1 = x1;
        this.dx0 = Math.sin(Math.toRadians(deg0));
        this.dx1 = Math.sin(Math.toRadians(deg1));
        this.ddx0 = 0;
        this.ddx1 = 0;
        this.y0 = y0;
        this.y1 = y1;
        this.dy0 = Math.cos(Math.toRadians(deg0));
        this.dy1 = Math.cos(Math.toRadians(deg1));
        this.ddy0 = 0;
        this.ddy1 = 0;
        double m_multiplier = 1 + Math.sqrt(Math.pow(this.x0-this.x1,2)+Math.pow(this.y0-this.y1,2));
        this.dx0 *= m_multiplier;
        this.dx1 *= m_multiplier;
        this.dy0 *= m_multiplier;
        this.dy1 *= m_multiplier;
        calculate_coefficients();
    }
    private void calculate_coefficients(){
        ax = -6*x0 + -3*dx0 + -0.5*ddx0 + 0.5* ddx1 + -3*dx1 + 6*x1;
        bx = 15*x0 + 8*dx0 + 1.5*ddx0 + -1*ddx1 + 7*dx1 + -15*x1;
        cx = -10*x0 + -6*dx0 + -1.5*ddx0 + 0.5*ddx1 + -4*dx1 + 10*x1;
        ex = 0.5*ddx0;
        fx = dx0;
        gx = x0;
        ay = -6*y0 + -3*dy0 + -0.5*ddy0 + 0.5* ddy1 + -3*dy1 + 6*y1;
        by = 15*y0 + 8*dy0 + 1.5*ddy0 + -1*ddy1 + 7*dy1 + -15*y1;
        cy = -10*y0 + -6*dy0 + -1.5*ddy0 + 0.5*ddy1 + -4*dy1 + 10*y1;
        ey = 0.5*ddy0;
        fy = dy0;
        gy = y0;
    }
    public double dx(double t){ return 5*ax*t*t*t*t + 4*bx*t*t*t + 3*cx*t*t + 2*ex*t + fx; }
    public double dy(double t){ return 5*ay*t*t*t*t + 4*by*t*t*t + 3*cy*t*t + 2*ey*t + fy; }
    public double ddx(double t){ return 20*ax*t*t*t + 12*bx*t*t + 6*cx*t + 2*ex; }
    public double ddy(double t){ return 20*ay*t*t*t + 12*by*t*t + 6*cy*t + 2*ey; }
    public double dddx(double t){ return 60*ax*t*t + 24*bx*t + 6*cx; }
    public double dddy(double t){ return 60*ay*t*t + 24*by*t + 6*cy; }
    public double dPos(double t){ return Math.sqrt(Math.pow(dx(t),2) + Math.pow(dy(t),2)); }
    public double getY(double t){
        return ay*t*t*t*t*t + by*t*t*t*t + cy*t*t*t + ey*t*t + fy*t + gy;
    }
    public double getX(double t){
        return ax*t*t*t*t*t + bx*t*t*t*t + cx*t*t*t + ex*t*t + fx*t + gx;
    }
    public double getAngle(double t){
        return Math.toDegrees(Math.atan2(dy(t),dx(t)));
    }
    public double arc_length(){
        double curr_arc = 0;
        double prev_length = dPos(0);
        double curr_length;
        for(double r = 0; r <= 1; r+= kIntegralRate){
            curr_length = dPos(r);
            curr_arc += (curr_length+prev_length)/2*kIntegralRate;
            prev_length = curr_length;
        }
        Debug.print("Arc Length: " + curr_arc, 1);
        return curr_arc;
    }
}
