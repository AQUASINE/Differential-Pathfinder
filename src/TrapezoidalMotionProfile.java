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
        this.max_velocity = max_velocity;
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
