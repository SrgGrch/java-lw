// панель, на которой генерируются объекты


import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class PaintPanel extends JPanel {
    private ArrayList<AbstractCar> objects1; // список объектов

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    //загрузка объектов в массив
    void PaintPanelUpdate(ArrayList<AbstractCar> o) {
        objects1 = o;
    }

    // прорисовка объектов
    void paintCar(Graphics g) {
        try {
            //проходим по каждому объекту в списке
            for (AbstractCar car : this.objects1) {

                //проверяем его принадлежность к классу PassengerCar
                if (car instanceof PassengerCar) {
                    //риусем объект с координатами х и у, размером 100х50
                    g.drawImage(car.GetImage(), (int) car.getX(), (int) car.getY(), 100, 50, (ImageObserver) null);
                }

                //проверяем его принадлежность к классу Truck
                if (car instanceof Truck) {
                    //риусем объект с координатами х и у, размером 115х50
                    g.drawImage(car.GetImage(), (int) car.getX(), (int) car.getY(), 115, 50, (ImageObserver) null);
                }
            }
        } catch (Exception e) {
        }
    }
}