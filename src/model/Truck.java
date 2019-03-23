package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// класс, наследующий абстрактный класс
public class Truck extends AbstractCar {
    private static BufferedImage img; // static - для всех

    public Truck() {
    }// констркутор без параметров

    //констрктуор с параметрами
    public Truck(float x, float y) {
        super(x, y);// вызываем конструктор родителя

        //загрузка изображения в img
        try {
            img = ImageIO.read(new File("truck.png"));
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
