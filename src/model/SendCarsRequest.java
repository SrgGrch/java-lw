package model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendCarsRequest {
    private final List<AbstractCar> cars;
    private final UUID receiver;

    public SendCarsRequest(List<AbstractCar> cars, UUID receiver) {
        this.cars = cars;
        this.receiver = receiver;
    }

    public SendCarsRequest(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        final List<AbstractCar> cars = new ArrayList<>();

        this.receiver = new UUID(buffer.getLong(), buffer.getLong());

        int size = buffer.getInt();
        for (int i = 0; i < size; i++) {
            byte[] carBytes = new byte[AbstractCar.SIZE];
            buffer.get(carBytes);
            cars.add(AbstractCar.fromBytes(carBytes));
        }

        this.cars = cars;
    }


    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * 2 + Integer.BYTES + cars.size() * AbstractCar.SIZE);
        buffer.putLong(receiver.getMostSignificantBits());
        buffer.putLong(receiver.getLeastSignificantBits());
        buffer.putInt(cars.size());
        cars.forEach(car -> buffer.put(car.toBytes()));

        return buffer.array();
    }

    public List<AbstractCar> getCars() {
        return cars;
    }

    public UUID getReceiver() {
        return receiver;
    }
}
