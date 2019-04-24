package ai;

import model.AbstractCar;
import model.Coords;
import ui.Habitat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class BaseAI extends Thread {

    int width, height;
    int velocity = 5; //px/s
    Habitat context;
    boolean running = true;
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
            procces();
            context.repaintGamePanel();

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param velocity скорость в px/s
     */
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void updateObjectList(ArrayList<AbstractCar> objects) {
        this.objects = objects;
    }

    public void updateObjectList(ArrayList<AbstractCar> objects, HashMap<UUID, JLabel> images) {
        this.objects = objects;
        this.images = images;
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

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @return true - объект находится вне конечной области, false - объект в конечной области
     */
    abstract boolean checkPos(AbstractCar car);

}
