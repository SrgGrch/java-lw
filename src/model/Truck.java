package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Truck extends AbstractCar {
    private BufferedImage img;

    public Truck() {
    }


    public Truck(float x, float y, float birthTime) {
        super(x, y, birthTime);

        try {
            img = ImageIO.read(new File("truck.png"));
        } catch (IOException ex) {
            System.out.println("Image not loaded");
        }
    }

    public Truck(float x, float y, float birthTime, float lifetime) {
        super(x, y, birthTime, lifetime);

        try {
            img = ImageIO.read(new File("truck.png"));
        } catch (IOException ex) {
            System.out.println("Image not loaded");
        }
    }

    @Override
    public BufferedImage getImage() {
        return img;
    }

}
