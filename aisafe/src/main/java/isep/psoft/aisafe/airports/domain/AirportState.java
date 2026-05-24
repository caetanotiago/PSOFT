package isep.psoft.aisafe.airports.domain;

// Slide "Anemic Domain Model" (1_Intro_Base_Design_Principles):
// lógica de transição de estado pertence ao domínio, não ao serviço de aplicação.
public enum AirportState {

    OPERATIONAL {
        @Override
        public boolean canTransitionTo(AirportState target) {
            return target == CLOSED || target == UNDER_MAINTENANCE;
        }
    },
    CLOSED {
        @Override
        public boolean canTransitionTo(AirportState target) {
            return target == OPERATIONAL || target == UNDER_MAINTENANCE;
        }
    },
    UNDER_MAINTENANCE {
        @Override
        public boolean canTransitionTo(AirportState target) {
            return target == OPERATIONAL || target == CLOSED;
        }
    };

    public abstract boolean canTransitionTo(AirportState target);
}
