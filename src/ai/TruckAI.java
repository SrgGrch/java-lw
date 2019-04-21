package ai;

import model.AbstractCar;
import model.Truck;
import ui.Habitat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class TruckAI extends BaseAI {

    public TruckAI(int width, int height, ArrayList<AbstractCar> objects, HashMap<UUID, JLabel> images, Habitat context) {
        super(width, height, objects, images, context, "TruckAI");
    }

    @Override
    void move(AbstractCar car) {
        float newX = car.getX(), newY = car.getY();
        double angle = getAngle(car);

        newX -= velocity * Math.cos(angle);
        newY -= velocity * Math.sin(angle);

        images.get(car.getId()).setLocation(new Point(Math.round(newX), Math.round(newY)));
        objects.get(objects.indexOf(car)).setPosition(new Point(Math.round(newX), Math.round(newY)));
    }

    @Override
    public void procces() {
        if (objects.size() != 0) {
            Iterator<AbstractCar> iterator = objects.iterator();
            for (AbstractCar car = iterator.next(); iterator.hasNext(); car = iterator.next()) {
                if (car instanceof Truck && checkPos(car)) move(car);
            }
        }

//        for (AbstractCar car: objects) {
//            if (car instanceof Truck && checkPos(car)) move(car);
//        }
    }

    @Override
    boolean checkPos(AbstractCar car) {
        return true; //car.getX() > height/2 && car.getY() > width/2;
    }
}
