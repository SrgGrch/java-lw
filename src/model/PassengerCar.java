package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PassengerCar extends AbstractCar {

    public PassengerCar() {
    }

    public PassengerCar(float x, float y, long birthTime) {
        super(x, y, birthTime);
    }

    public PassengerCar(float x, float y, long birthTime, long lifetime) {
        super(x, y, birthTime, lifetime);
    }

}
