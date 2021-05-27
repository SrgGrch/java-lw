package ai;

import model.AbstractCar;
import ui.Habitat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class BaseAI extends Thread {


    private boolean paused = false;

    int width, height;
    int velocity = 2; //px/s
    private final Habitat context;
    private final boolean running = true;
    ArrayList<AbstractCar> objects;
    HashMap<UUID, JLabel> images;
    HashMap<UUID, Point> objectDestinations = new HashMap<>();

    BaseAI(int width, int height, ArrayList<AbstractCar> objects, HashMap<UUID, JLabel> images, Habitat context, String threadName) {
        this.width = width;
        this.height = height;
        this.objects = objects;
        this.images = images;
        this.context = context;
        this.setName(threadName);
    }

    /**
     * Функция изменения параметров размера панели для AI.
     * Должна вызываться только в момент изменения размера окна программы
     *
     * @param width  ширина панели
     * @param height высота панели
     */
    public void setDim(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void run() {
        while (running) {
            synchronized (this) {
                this.procces();
            }
            context.repaintGamePanel();
            try {
                if (paused) synchronized (this) {
                    this.wait();
                }
                else sleep(45);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopAI() {
        paused = true;
    }

    public void resumeAI() {
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    /**
     * @param velocity скорость в px/s
     */
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public boolean isPaused() {
        return paused;
    }

    double getAngle(AbstractCar car) {
//        Point destination = objectDestinations.get(car.getId());
        Point destination = new Point(0, 0);
        double a = car.getX() - destination.getX();
        double b = car.getY() - destination.getY();
        double distance = Math.sqrt(Math.pow(a, 2)
                + Math.pow(b, 2));
        return Math.acos((a * a + distance * distance - b * b) / (2 * a * distance));
    }

    abstract Point move(AbstractCar car);

    abstract void procces();


    /**
     * @return true - объект находится вне конечной области, false - объект в конечной области
     */
    abstract boolean checkPos(AbstractCar car);

}
