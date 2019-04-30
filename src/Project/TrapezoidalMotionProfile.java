package Project;
//credit to Tyler from WPILib for help with this
public class TrapezoidalMotionProfile {
    private int m_direction;

    private Constraints m_constraints;
    private State m_initial;
    private State m_goal;

    private double m_endAccel;
    private double m_endFullSpeed;
    private double m_endDecel;

    public static class Constraints{
        @SuppressWarnings("MemberName")
        double max_velocity;
        @SuppressWarnings("MemberName")
        double max_acceleration;

        public Constraints(){
        }

        public Constraints(double max_velocity, double max_acceleration){
            //velocity: m/s
        this.max_velocity = max_velocity;
            //acceleration: m/s^2
        this.max_acceleration = max_acceleration;
        }
    }
    public static class State{
        @SuppressWarnings("MemberName")
        public double position;
        @SuppressWarnings("MemberName")
        public double velocity;

        public State(){
        }

        public State(double position, double velocity){

            this.position = position;
            this.velocity = velocity;

        }
        State(State state){
            this.position = state.position;
            this.velocity = state.velocity;
        }
    }


    public TrapezoidalMotionProfile (State initial, State goal, Constraints constraints){
        Debug.print("constructor", 5);
        m_direction = shouldFlipAccel(initial, goal, constraints) ? -1 : 1;
        m_constraints = constraints;
        m_initial = direction(initial);
        Debug.print("m_initial", 5);
        m_goal = direction(goal);
        Debug.print("m_goal", 5);
            //seconds
        double cutoffBegin = m_initial.velocity / m_constraints.max_acceleration;
            //m/s
        double cutoffDistBegin = cutoffBegin * cutoffBegin * m_initial.velocity;
            //seconds
        double cutoffEnd = m_goal.velocity / m_constraints.max_acceleration;
        double cutoffDistEnd = cutoffEnd * cutoffEnd * m_constraints.max_acceleration;

        double fullTrapezoidDist = cutoffDistBegin + (m_goal.position - m_initial.position) + cutoffDistEnd;

        double accelerationTime = m_constraints.max_velocity / m_constraints.max_acceleration;
        double fullSpeedDist = fullTrapezoidDist - accelerationTime * accelerationTime * m_constraints.max_acceleration;

        if (fullSpeedDist < 0) {
            accelerationTime = Math.sqrt(fullTrapezoidDist / m_constraints.max_acceleration);
            Debug.print("New acceleration time: " + accelerationTime, 4);
            fullSpeedDist = 0;
        }

        m_endAccel = accelerationTime - cutoffBegin;
        m_endFullSpeed = m_endAccel + fullSpeedDist / m_constraints.max_velocity;
        m_endDecel = m_endFullSpeed + accelerationTime - cutoffEnd;
        Debug.print("m_endDecel: " + m_endDecel, 4);
        Debug.print("accelerationTime: " + accelerationTime, 4);
        Debug.print("cutoffBegin: " + cutoffBegin, 4);
        Debug.print("fullSpeedDist: " + fullSpeedDist, 4);
        Debug.print("fullTrapezoidDist: " + fullTrapezoidDist, 4);
        Debug.print(" m_constraints.max_acceleration: " + m_constraints.max_acceleration, 4);
    }

    public TrapezoidalMotionProfile(State goal, Constraints constraints) {
        this(new State(0,0), goal, constraints);
    }
/*
    TODO: FIX THIS
    public TrapezoidalMotionProfile(double start_velocity, double start_position, double end_velocity, double end_position, double max_acceleration, double max_velocity){
        this(new State(start_position, start_velocity), new State(end_position, end_velocity), new Constraints(max_velocity, max_acceleration));
    }

 */

    public TrapezoidalMotionProfile(double start_position, double end_position, double max_acceleration, double max_velocity){
        this(new State(end_position, 0), new Constraints(max_velocity, max_acceleration));
    }

    @SuppressWarnings("ParameterName")
    public boolean isFinished(double t) {
        return t >= totalTime();
    }
    public double totalTime() {
        return m_endDecel;
    }
    public double timeLeftUntil(double target) {
        double position = m_initial.position * m_direction;
        double velocity = m_initial.velocity * m_direction;

        double endAccel = m_endAccel * m_direction;
        double endFullSpeed = m_endFullSpeed * m_direction - endAccel;

        if (target < position) {
            endAccel = -endAccel;
            endFullSpeed = -endFullSpeed;
            velocity = -velocity;
        }

        endAccel = Math.max(endAccel, 0);
        endFullSpeed = Math.max(endFullSpeed, 0);

        double acceleration = m_constraints.max_acceleration;
        double deceleration = -m_constraints.max_acceleration;

        double distToTarget = Math.abs(target - position);

        if (distToTarget < 1e-6) {
            return 0;
        }

        double accelDist = velocity * endAccel + 0.5 * acceleration * endAccel * endAccel;

        double decelVelocity;
        if (endAccel > 0) {
            decelVelocity = Math.sqrt(Math.abs(velocity * velocity + 2 * acceleration * accelDist));
        } else {
            decelVelocity = velocity;
        }

        double decelDist;
        double fullSpeedDist = m_constraints.max_velocity * endFullSpeed;

        if (accelDist > distToTarget) {
            accelDist = distToTarget;
            fullSpeedDist = 0;
            decelDist = 0;
        } else if (accelDist + fullSpeedDist > distToTarget) {
            fullSpeedDist = distToTarget - accelDist;
            decelDist = 0;
        } else {
            decelDist = distToTarget - fullSpeedDist - accelDist;
        }

        double accelTime = (-velocity + Math.sqrt(Math.abs(velocity * velocity + 2 * acceleration * accelDist))) / acceleration;
        double decelTime = (-decelVelocity + Math.sqrt(Math.abs(decelVelocity * decelVelocity + 2 * deceleration * decelDist))) / deceleration;
        double fullSpeedTime = fullSpeedDist / m_constraints.max_velocity;

        return accelTime + fullSpeedTime + decelTime;
    }

    @SuppressWarnings("ParameterName")
    public State calculate(double t) {
        State result = new State(m_initial);
        double timeLeft = m_endDecel - t;
        Debug.print("m_initial: " + this.m_initial.velocity, 3);
        if (t < m_endAccel) {
            //calculate accel State
            result.velocity = m_initial.velocity + t * m_constraints.max_acceleration;
            result.position = (m_initial.velocity + t * m_constraints.max_acceleration / 2.0) * t;

        } else if (t < m_endFullSpeed) {
            //calculate full speed State
            result.velocity = m_constraints.max_velocity;
            result.position = (m_initial.velocity + m_endAccel * m_constraints.max_acceleration
                    / 2.0) * m_endAccel + m_constraints.max_velocity * (t - m_endAccel);
        } else if (t <= m_endDecel) {
            //calculate decel State
            result.velocity = m_goal.velocity + timeLeft * m_constraints.max_acceleration;
            result.position = m_goal.position - (m_goal.velocity + timeLeft
                    * m_constraints.max_acceleration / 2.0) * timeLeft;
           /* result.position = (m_initial.velocity + m_endAccel * m_constraints.max_acceleration
                    / 2.0) * m_endAccel + (m_constraints.max_velocity * (m_endFullSpeed - m_endAccel))
                    + (m_constraints.max_velocity - m_constraints.max_acceleration / 2.0 * timeLeft) * timeLeft;

            */
        } else {
            result = m_goal;
        }

        return direction(result);
    }

    private boolean shouldFlipAccel(State initial, State goal, Constraints constraints){
        double velocityChange = goal.velocity - initial.velocity;
        double positionChange = goal.position - initial.position;
        double t = Math.abs(velocityChange) / constraints.max_acceleration;
        try {
            Debug.print("m_initial: " + m_initial.velocity, 5);
        }
        catch (NullPointerException e){
//            e.printStackTrace();
        }
        return t * (velocityChange / 2 + initial.velocity) > positionChange;

    }

    public State direction(State state){
        state.position *= m_direction;
        state.velocity *= m_direction;
        return state;
    }
}
