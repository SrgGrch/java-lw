package core;

import model.AbstractCar;
import model.PassengerCar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureLoader {

    public static BufferedImage load(AbstractCar car){
        BufferedImage img = null;

        if (car instanceof PassengerCar){
            try {
                img = ImageIO.read(new File("passengercar.png"));
            } catch (IOException ex) {
                System.out.println("Image not loaded");
            }
        } else {
            try {
                img = ImageIO.read(new File("truck.png"));
            } catch (IOException ex) {
                System.out.println("Image not loaded");
            }
        }
        return img;

    }

}
