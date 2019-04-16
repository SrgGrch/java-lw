package ui;// панель, на которой генерируются объекты


import model.AbstractCar;
import model.PassengerCar;
import model.Truck;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PaintPanel extends JPanel {

    private ArrayList<AbstractCar> objects; // список объектов

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    //загрузка объектов в массив
    void paintPanelUpdate(ArrayList<AbstractCar> o) {
        objects = o;
    }

    // прорисовка объектов
    void paintCar(Graphics g) {
        try {
            //проходим по каждому объекту в списке
            for (AbstractCar car : this.objects) {

                //проверяем его принадлежность к классу model.PassengerCar
                if (car instanceof PassengerCar) {
                    //риусем объект с координатами х и у, размером 100х50
                    g.drawImage(car.GetImage(), (int) car.getX(), (int) car.getY(), 100, 50, null);
                }

                //проверяем его принадлежность к классу model.Truck
                if (car instanceof Truck) {
                    //риусем объект с координатами х и у, размером 115х50
                    g.drawImage(car.GetImage(), (int) car.getX(), (int) car.getY(), 115, 50, null);
                }
            }
        } catch (Exception ignored) {
        }
    }
}