package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// класс, наследующий абстрактный класс
public class PassengerCar extends AbstractCar {

    private static BufferedImage img; // static - для всех

    public PassengerCar() {
    } // констркутор без параметров

    //констрктуор с параметрами
    public PassengerCar(float x, float y, float birthTime) {
        super(x, y, birthTime); // вызываем конструктор родителя

        //загрузка изображения в img
        try {
            img = ImageIO.read(new File("passengercar.png"));
        } catch (IOException ex) {
            System.out.println("Image not loaded");
        }
    }

    public PassengerCar(float x, float y, float birthTime, float lifetime) {
        super(x, y, birthTime, lifetime); // вызываем конструктор родителя

        //загрузка изображения в img
        try {
            img = ImageIO.read(new File("passengercar.png"));
        } catch (IOException ex) {
            System.out.println("Image not loaded");
        }
    }

    //реализуем методы интерфейса
    @Override
    public Image GetImage() {
        return img;
    }

}
