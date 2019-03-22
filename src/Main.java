import javax.swing.*;

//Первоначальный класс
public class Main extends JFrame {

    //констркутор окна, width - ширина, height - высота
    Main(int width, int height) {
        super("Гречишников. ЛР 1"); //заголовок
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height); //размер
        setContentPane(new JPanel()); //добавляем панель
        setVisible(true);
    }

    // обязательный метод
    public static void main(String[] args) {
        // создание среды рабочей области с размерами 800х600,
        // с генерацией легковых машин каждую 1 секунду, грузовых - каждые 2 секунды
        // с вероятностью генерации 50%
        Habitat habitat = new Habitat(800, 600, 1, 2, 0.5f, 0.5f);
        habitat.Init();//инициализация среды

    }
}