package model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendCarsResponse {
    public static final int TYPE = 0;

    private final List<AbstractCar> cars;

    public SendCarsResponse(List<AbstractCar> cars) {
        this.cars = cars;
    }

    public SendCarsResponse(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        final List<AbstractCar> cars = new ArrayList<>();

        int size = buffer.getInt();
        for (int i = 0; i < size; i++) {
            byte[] carBytes = new byte[AbstractCar.SIZE];
            buffer.get(carBytes);
            cars.add(AbstractCar.fromBytes(carBytes));
        }

        this.cars = cars;
    }


    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + Integer.BYTES + cars.size() * AbstractCar.SIZE);
        buffer.putInt(TYPE);
        buffer.putInt(cars.size());
        cars.forEach(car -> buffer.put(car.toBytes()));

        return buffer.array();
    }

    public List<AbstractCar> getCars() {
        return cars;
    }
}
