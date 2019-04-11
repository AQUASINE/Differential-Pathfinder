import com.google.common.collect.Constraint;

public class TrapezoidalMotionProfile {
    private int m_direction;

    private Constraints m_constraints;
    private State m_initial;
    private State m_goal;

    private double m_endAccel;
    private double m_endFullSpeed;
    private double m_endDecel;

    public class Constraints{
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
    public class State{
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

    public TrapezoidalMotionProfile (State goal, State initial, Constraints constraints){
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
            fullSpeedDist = 0;
        }

        m_endAccel = accelerationTime - cutoffBegin;
        m_endFullSpeed = m_endAccel + fullSpeedDist / m_constraints.max_velocity;
        m_endDecel = m_endFullSpeed + accelerationTime - cutoffEnd;
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
