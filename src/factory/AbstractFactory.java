package factory;

import model.PassengerCar;
import model.Truck;

// интерфейс для создания объектов
public interface AbstractFactory {
    PassengerCar createPassengerCar();

    PassengerCar createPassengerCar(float x, float y, float birthTime); // х и у - координаты

    PassengerCar createPassengerCar(float x, float y, float birthTime, float lifetime); // х и у - координаты

    Truck createTruck();

    Truck createTruck(float x, float y, float birthTime);

    Truck createTruck(float x, float y,  float birthTime, float lifetime);
}
