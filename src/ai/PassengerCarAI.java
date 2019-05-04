package ai;

import model.AbstractCar;
import model.PassengerCar;
import model.Truck;
import ui.Habitat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class PassengerCarAI extends BaseAI {

    public PassengerCarAI(int width, int height, ArrayList<AbstractCar> objects, HashMap<UUID, JLabel> images, Habitat context) {
        super(width, height, objects, images, context, "CarAI");
    }

    @Override
    double getAngle(AbstractCar car) {
        Point destination = new Point(width, height);
        double a = car.getX() - destination.getX();
        double b = car.getY() - destination.getY();
        double distance = Math.sqrt(Math.pow(a, 2)
                + Math.pow(b, 2));
        return Math.acos((a * a + distance * distance - b * b) / (2 * a * distance));
    }

    @Override
    Point move(AbstractCar car) {
        float newX = car.getX(), newY = car.getY();
        double angle = getAngle(car);

        newX -= velocity * Math.cos(angle);
        newY += velocity * Math.sin(angle);

//        if (objects.contains(car) && images.containsKey(car.getId())) {
//            try {
//                images.get(car.getId()).setLocation(new Point(Math.round(newX), Math.round(newY)));
//                objects.get(objects.indexOf(car)).setPosition(new Point(Math.round(newX), Math.round(newY)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return new Point(Math.round(newX), Math.round(newY));
    }

    @Override
    synchronized void procces() {
        if (objects.size() != 0) {
            Iterator<AbstractCar> iterator = objects.iterator();
            try {
                for (AbstractCar car = iterator.next(); iterator.hasNext(); car = iterator.next()) {
                    if (car instanceof PassengerCar && checkPos(car)) {
                        Point p = move(car);
                        car.setPosition(p);
                        images.get(car.getId()).setLocation(p);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    boolean checkPos(AbstractCar car) {
        return true; //car.getX() > height/2 && car.getY() > width/2;
    }
}
