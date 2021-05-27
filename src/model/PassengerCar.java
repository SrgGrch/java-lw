package model;

import java.util.UUID;

public class PassengerCar extends AbstractCar {

    public PassengerCar() {
    }

    public PassengerCar(float x, float y, long birthTime) {
        super(x, y, birthTime);
    }

    public PassengerCar(float x, float y, long birthTime, long lifetime) {
        super(x, y, birthTime, lifetime);
    }

    public PassengerCar(UUID id, float x, float y, long birthTime, long lifetime) {
        super(id, x, y, birthTime, lifetime);
    }

}
