package factory;

import model.PassengerCar;
import model.Truck;

// интерфейс для создания объектов
public interface AbstractFactory {
    PassengerCar createPassengerCar();

    Truck createTruck();

    PassengerCar createPassengerCar(float x, float y); // х и у - координаты

    Truck createTruck(float x, float y);
}
