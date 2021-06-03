import ui.Habitat;

import javax.swing.*;
import java.io.IOException;

//Первоначальный класс
public class Main {

    // обязательный метод
    public static void main(String[] args) {
        // создание среды рабочей области с размерами 800х600,
        // с генерацией легковых машин каждую 1 секунду, грузовых - каждые 2 секунды
        // с вероятностью генерации 50%
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        try {
            Habitat habitat = new Habitat(800, 600, 1, 2, 0.5f, 0.5f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //habitat.Init();//инициализация среды

    }
}