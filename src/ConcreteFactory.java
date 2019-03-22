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

    // возращает объекта типа PassengerCar
    @Override
    public PassengerCar createPassengerCar(float x, float y) {
        return new PassengerCar(x, y);
    }

    // возращает объекта типа Truck
    @Override
    public Truck createTruck(float x, float y) {
        return new Truck(x, y);
    }
}
