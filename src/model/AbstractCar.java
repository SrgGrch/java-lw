package model;

import java.util.UUID;

// абстрактный класс, реализующий интерфейс
public abstract class AbstractCar implements IBehaviour {

    private float x, y; //координаты
    private float birthTime, lifetime = 5000;
    private final UUID id = UUID.randomUUID();

    // констркутор без параметров
    AbstractCar() {
        x = 0;
        y = 0;
    }

    // констркутор с параметрами, this для доступа к скрытым переменным х и у
    AbstractCar(float x, float y, float birthTime) {
        this.x = x;
        this.y = y;
        this.birthTime = birthTime;
    }

    public AbstractCar(float x, float y, float birthTime, float lifetime) {
        this.x = x;
        this.y = y;
        this.birthTime = birthTime;
        this.lifetime = lifetime;
    }

    public float getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(float birthTime) {
        this.birthTime = birthTime;
    }

    public float getLifetime() {
        return lifetime;
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }

    public UUID getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
