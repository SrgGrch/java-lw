package ai;

import model.AbstractCar;
import ui.Habitat;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PassengerCarAI extends BaseAI {

    public PassengerCarAI(int width, int height, ArrayList<AbstractCar> objects, HashMap<UUID, JLabel> images, Habitat context) {
        super(width, height, objects, images, context);
    }

    @Override
    void move(AbstractCar car) {

    }

    @Override
    void procces() {

    }

    @Override
    boolean checkPos(AbstractCar car) {

        return false;
    }
}
