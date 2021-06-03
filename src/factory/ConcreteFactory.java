package factory;

import model.PassengerCar;
import model.Truck;

// класс, реализующий интерфейс
public class ConcreteFactory implements AbstractFactory {

    @Override
    public PassengerCar createPassengerCar() {
        return new PassengerCar();
    }

    @Override
    public Truck createTruck() {
        return new Truck();
    }


    @Override
    public PassengerCar createPassengerCar(float x, float y, long birthTime) {
        return new PassengerCar(x, y, birthTime);
    }

    @Override
    public PassengerCar createPassengerCar(float x, float y, long birthTime, long lifetime) {
        System.out.println("x: " + x + "y: " + y);
        return new PassengerCar(x, y, birthTime, lifetime);
    }


    @Override
    public Truck createTruck(float x, float y, long birthTime) {
        return new Truck(x, y, birthTime);
    }

    @Override
    public Truck createTruck(float x, float y, long birthTime, long lifetime) {
        return new Truck(x, y, birthTime, lifetime);
    }
}
