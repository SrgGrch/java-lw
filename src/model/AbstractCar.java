package model;

// абстрактный класс, реализующий интерфейс
public abstract class AbstractCar implements IBehaviour {

    private float x, y; //координаты

    // констркутор без параметров
    AbstractCar() {
        x = 0;
        y = 0;
    }

    // констркутор с параметрами, this для доступа к скрытым переменным х и у
    AbstractCar(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // методы получения координат
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
