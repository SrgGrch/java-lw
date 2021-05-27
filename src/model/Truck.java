package model;

import java.util.UUID;

public class Truck extends AbstractCar {

    public Truck() {
    }


    public Truck(float x, float y, long birthTime) {
        super(x, y, birthTime);
    }

    public Truck(float x, float y, long birthTime, long lifetime) {
        super(x, y, birthTime, lifetime);
    }

    public Truck(UUID id, float x, float y, long birthTime, long lifetime) {
        super(id, x, y, birthTime, lifetime);
    }


}
