package model;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

// абстрактный класс, реализующий интерфейс
public abstract class AbstractCar implements IBehaviour, Serializable {

    private float x, y; //координаты
    private int newX, newY;
    private long birthTime, lifetime = 5000;
    private UUID id = UUID.randomUUID();

    // констркутор без параметров
    AbstractCar() {
        x = 0;
        y = 0;
    }

    // констркутор с параметрами, this для доступа к скрытым переменным х и у
    AbstractCar(float x, float y, long birthTime) {
        this.x = x;
        this.y = y;
        this.birthTime = birthTime;
    }

    public AbstractCar(float x, float y, long birthTime, long lifetime) {
        this.x = x;
        this.y = y;
        this.birthTime = birthTime;
        this.lifetime = lifetime;

    }

    public AbstractCar(UUID id, float x, float y, long birthTime, long lifetime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.birthTime = birthTime;
        this.lifetime = lifetime;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(long birthTime) {
        this.birthTime = birthTime;
    }

    public long getLifetime() {
        return lifetime;
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }

    public void setPosition(Point p) {
        x = p.x;
        y = p.y;
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
