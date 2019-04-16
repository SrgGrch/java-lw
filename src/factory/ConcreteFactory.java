package factory;

import model.PassengerCar;
import model.Truck;

// класс, реализующий интерфейс
public class ConcreteFactory implements AbstractFactory {
    // х и у - координаты
    @Override
    public PassengerCar createPassengerCar() {
        return new PassengerCar();
    }

    @Override
    public Truck createTruck() {
        return new Truck();
    }

    // возращает объекта типа model.PassengerCar
    @Override
    public PassengerCar createPassengerCar(float x, float y, float birthTime) {
        return new PassengerCar(x, y, birthTime);
    }

    @Override
    public PassengerCar createPassengerCar(float x, float y, float birthTime, float lifetime) {
        return new PassengerCar(x, y, birthTime, lifetime);
    }

    // возращает объекта типа model.Truck
    @Override
    public Truck createTruck(float x, float y, float birthTime) {
        return new Truck(x, y, birthTime);
    }

    @Override
    public Truck createTruck(float x, float y, float birthTime, float lifetime) {
        return new Truck(x, y, birthTime, lifetime);
    }
}
