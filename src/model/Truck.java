package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Truck extends AbstractCar {

    public Truck() {
    }


    public Truck(float x, float y, long birthTime) {
        super(x, y, birthTime);
    }

    public Truck(float x, float y, long birthTime, long lifetime) {
        super(x, y, birthTime, lifetime);
    }


}
