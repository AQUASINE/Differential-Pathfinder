package Project;

import com.google.common.collect.Constraint;

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
        public double max_velocity;
        @SuppressWarnings("MemberName")
        public double max_acceleration;

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
    }


    public TrapezoidalMotionProfile (State initial, State goal, Constraints constraints){
        m_direction = shouldFlipAccel(initial, goal, constraints) ? 1 : -1;
        m_constraints = constraints;
        m_initial = direction(initial);
        m_goal = direction(goal);
        double cutoffBegin = m_initial.velocity / m_constraints.max_acceleration;
        double cutoffDistBegin = m_initial.velocity;
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

    public TrapezoidalMotionProfile(double start_velocity, double start_position, double end_velocity, double end_position, double max_acceleration, double max_velocity){
        this(new State(start_position,start_velocity), new State(end_position, end_velocity), new Constraints(max_velocity, max_acceleration));
    }

    public TrapezoidalMotionProfile(double end_velocity, double end_position, double max_acceleration, double max_velocity){
        this(new State(end_position, end_velocity), new Constraints(max_velocity, max_acceleration));
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
        double endDeccel = m_endDecel - endAccel - endFullSpeed;
        endDeccel = Math.max(endDeccel, 0);

        double acceleration = m_constraints.max_acceleration;
        double decceleration = -m_constraints.max_acceleration;

        double distToTarget = Math.abs(target - position);

        if (distToTarget < 1e-6) {
            return 0;
        }

        double accelDist = velocity * endAccel + 0.5 * acceleration * endAccel * endAccel;

        double deccelVelocity;
        if (endAccel > 0) {
            deccelVelocity = Math.sqrt(Math.abs(velocity * velocity + 2 * acceleration * accelDist));
        } else {
            deccelVelocity = velocity;
        }

        double deccelDist = deccelVelocity * endDeccel + 0.5 * decceleration * endDeccel * endDeccel;

        deccelDist = Math.max(deccelDist, 0);

        double fullSpeedDist = m_constraints.max_velocity * endFullSpeed;

        if (accelDist > distToTarget) {
            accelDist = distToTarget;
            fullSpeedDist = 0;
            deccelDist = 0;
        } else if (accelDist + fullSpeedDist > distToTarget) {
            fullSpeedDist = distToTarget - accelDist;
            deccelDist = 0;
        } else {
            deccelDist = distToTarget - fullSpeedDist - accelDist;
        }

        double accelTime = (-velocity + Math.sqrt(Math.abs(velocity * velocity + 2 * acceleration
                * accelDist))) / acceleration;

        double deccelTime = (-deccelVelocity + Math.sqrt(Math.abs(deccelVelocity * deccelVelocity
                + 2 * decceleration * deccelDist))) / decceleration;

        double fullSpeedTime = fullSpeedDist / m_constraints.max_velocity;

        return accelTime + fullSpeedTime + deccelTime;
    }

    @SuppressWarnings("ParameterName")
    public State calculate(double t) {
        State result = m_initial;

        if (t < m_endAccel) {
            result.velocity += t * m_constraints.max_acceleration;
            result.position += (m_initial.velocity + t * m_constraints.max_acceleration / 2.0) * t;
        } else if (t < m_endFullSpeed) {
            result.velocity = m_constraints.max_velocity;
            result.position += (m_initial.velocity + m_endAccel * m_constraints.max_acceleration
                    / 2.0) * m_endAccel + m_constraints.max_velocity * (t - m_endAccel);
        } else if (t <= m_endDecel) {
            result.velocity = m_goal.velocity + (m_endDecel - t) * m_constraints.max_acceleration;
            double timeLeft = m_endDecel - t;
            result.position = m_goal.position - (m_goal.velocity + timeLeft
                    * m_constraints.max_acceleration / 2.0) * timeLeft;
        } else {
            result = m_goal;
        }

        return direction(result);
    }
    private boolean shouldFlipAccel(State initial, State goal, Constraints constraints){
        double velocityChange = goal.velocity - initial.velocity;
        double positionChange = goal.position - initial.position;
        double t = Math.abs(velocityChange) / constraints.max_acceleration;
        return t * (velocityChange / 2 + initial.velocity) > positionChange;
    }

    public State direction(State state){
        state.position *= m_direction;
        state.velocity *= m_direction;
        return state;
    }
}
