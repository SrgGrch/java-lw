package ui;

import factory.ConcreteFactory;
import model.AbstractCar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// среда рабочей области
public class Habitat extends JFrame {

    private int JFwidth, JFheight; // размер рабочей области
    private ArrayList<AbstractCar> objects; //массив объектов
    private ConcreteFactory factory;
    private float N1, N2, P1, P2; //N - время генерации объекта, P - вероятность генерации
    private long timeFromStart = 0; //время начала генерации объектов
    private float N1time, N2time; // время последней генерации объекта
    private int PassengerCarNum = 0, TruckNum = 0; // кол-во объектов класса PassengerCarNum, объектов класса TruckNum

    private JLabel timerLabel; //отображение времени
    private JPanel gamePanel; // панель, на которой генерируются объекты
    private JButton stopButton;
    private JButton startButton;

    private Timer startTime;  // таймер
    private long firstTime; //время начала запуска таймер
    private boolean firstRun = true; // первый запуск
    private JPanel controlPanel;
    private JPanel rootPanel;
    private JPanel buttonPanel;

    // конструктор среды
    public Habitat(int JFwidth, int JFheight, float N1, float N2, float P1, float P2) {
        this.JFwidth = JFwidth; // ширина
        this.JFheight = JFheight; // выоста
        this.N1 = N1; // время генерации каждые N секунд
        this.N2 = N2;
        this.P1 = P1; // веротяность генерации
        this.P2 = P2;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setTitle("Гречишников ЛР2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(JFwidth, JFheight); //размер
        setContentPane(rootPanel); //добавляем панель
        setVisible(true);
        this.setLayout(null); //абсолютное позиционирование (точное положение можно задать)

        PassengerCarNum = 0; // кол-во легковых машин
        TruckNum = 0; // кол-во грузовых машин
        objects = new ArrayList<AbstractCar>(); // список сгенерированных объектов
        factory = new ConcreteFactory();

        timerLabel = new JLabel(); //строка таймера
        timerLabel.setBounds(10, 2, 700, 20); // положение таймера
        timerLabel.setText("Время: " + " Легковые машины: " + PassengerCarNum + " Грузовые машины: " + TruckNum);
        timerLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));

        // панель, на которой рисуются объекты
        //gamePanel = new PaintPanel();
        gamePanel.setLayout(null); //абсолютное позиционирование (точное положение можно задать)
        //gamePanel.setSize(JFwidth - 100, JFheight - 100); // размеры
        //gamePanel.setLocation(40, 40); // положение
        gamePanel.setBackground(Color.white);

        //startButton = new JButton();
        startButton.setEnabled(true);
//        startButton.setBounds(450, 5, 150, 20);
        //startButton.setText("Start");
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            startButton.setFocusable(false);
            stopButton.setFocusable(true);
            if (startTime != null)
                return;
            objects.clear(); // очищаем список объектов
            gamePanel.removeAll(); // очищаем панель рисования объектов

            startTime = new Timer(); // создаем таймер
            showPanel();
            startTime.schedule(new TimerTask() { //запуск таймера
                @Override
                public void run() { //метод для таймера
                    if (firstRun) {
                        firstTime = System.currentTimeMillis(); //начало симуляции
                        firstRun = false;
                    }

                    long currentTime = System.currentTimeMillis();
                    long ticktack = (currentTime - firstTime); // время прошедшее с начала симуляции

                    timerLabel.setText("Время: " + ticktack / 1000.f + " Легковые машины: " + PassengerCarNum + " Грузовые машины: " + TruckNum);

                    Update(ticktack); // обновляем таймер
                }
            }, 0, 100); // каждую 0,1 секунду запускается update
        });

        //stopButton = new JButton();
        stopButton.setEnabled(false);
        //stopButton.setBounds(600, 5, 150, 20);
        //stopButton.setText("Stop");
        stopButton.addActionListener(e -> {
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            stopButton.setFocusable(false);
            startButton.setFocusable(true);

            if (startTime == null)
                return;

            startTime.cancel(); //останавливаем таймер
            startTime = null;
            objects.clear(); // очищаем список объектов
            PassengerCarNum = 0;
            TruckNum = 0;
            N1time = 0;
            N2time = 0;
            firstRun = true;
            gamePanel.removeAll();

        });


//        myFrame.add(gamePanel); // добавляем на главное окно панель
//        myFrame.add(timerLabel); // добавляем на главное окно таймер
//        myFrame.add(startButton); // добавляем на главное окно таймер
//        myFrame.add(stopButton); // добавляем на главное окно таймер
//        myFrame.repaint();
        showPanel();
    }

    //инициализация среды
    public void Init() {
        //добавляем слушателя изменений на главном окне
        //обработчик клавиш
        this.addKeyListener(new KeyAdapter() {
            // клавиша нажата
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) { //нажата B
                    if (startTime != null)
                        return;
                    stopButton.setEnabled(true);
                    startButton.setEnabled(false);
                    objects.clear(); // очищаем список объектов
                    gamePanel.removeAll(); // очищаем панель рисования объектов

                    startTime = new Timer(); // создаем таймер
                    showPanel();
                    startTime.schedule(new TimerTask() { //запуск таймера
                        @Override
                        public void run() { //метод для таймера
                            if (firstRun) {
                                firstTime = System.currentTimeMillis(); //начало симуляции
                                firstRun = false;
                            }

                            long currentTime = System.currentTimeMillis();
                            long ticktack = (currentTime - firstTime); // время прошедшее с начала симуляции

                            timerLabel.setText("Время: " + ticktack / 1000.f + " Легковые машины: " + PassengerCarNum + " Грузовые машины: " + TruckNum);

                            Update(ticktack); // обновляем таймер
                        }
                    }, 0, 100); // каждую 0,1 секунду запускается update

                }
                if (e.getKeyCode() == KeyEvent.VK_E) //нажата E, симуляция завершена
                {
                    if (startTime == null)
                        return;
                    stopButton.setEnabled(false);
                    startButton.setEnabled(true);
                    startTime.cancel(); //останавливаем таймер
                    startTime = null;
                    objects.clear(); // очищаем список объектов
                    PassengerCarNum = 0;
                    TruckNum = 0;
                    N1time = 0;
                    N2time = 0;
                    firstRun = true;
                    gamePanel.removeAll();

                }
                if (e.getKeyCode() == KeyEvent.VK_T) {  //нажата T, показать/скрыть статистику

                    if (timerLabel.isVisible()) {
                        timerLabel.setVisible(false);
                    } else {
                        timerLabel.setVisible(true);
                    }
                }
            }

        });
    }

    // обеновление таймера
    private void Update(long timeFromStart) {
        //генерация легковой машины
        if (timeFromStart > N1time + N1 * 1000) {
            N1time += N1 * 1000;
            if ((float) Math.random() <= P1) {
                PassengerCarNum++; //увеличиваем счетчик
                objects.add(factory.createPassengerCar((float) (Math.random() * gamePanel.getWidth() - 25), (float) (Math.random() * gamePanel.getHeight() - 25)));
            }
        }
        //генерация грузовой машины
        if (timeFromStart > N2time + N2 * 1000) {
            N2time += N2 * 1000;

            if ((float) Math.random() <= P2) {
                TruckNum++;//увеличиваем счетчик
                objects.add(factory.createTruck((float) (Math.random() * gamePanel.getWidth() - 25), (float) (Math.random() * gamePanel.getHeight() - 25)));
            }
        }
        ObjectPainter.PaintPanelUpdate(objects);
        ObjectPainter.paintCar(gamePanel.getGraphics());
        gamePanel.revalidate();
    }

    private void showPanel() {
        gamePanel.paintComponents(gamePanel.getGraphics()); //отображает компоненты контейнера
    }

}
