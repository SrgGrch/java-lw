package ui;

import factory.ConcreteFactory;
import model.AbstractCar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

// среда рабочей области
public class Habitat extends JFrame {

    private enum CarType {CAR, TRUCK}

    private int JFwidth, JFheight; // размер рабочей области
    private AbstractCar[] newObjects;
    private ArrayList<AbstractCar> objects; //массив объектов
    private ConcreteFactory factory;
    private float carGenTime, truckGenTime, carProb, truckProb; //N - время генерации объекта, P - вероятность генерации
    private long timeFromStart = 0; //время начала генерации объектов
    private float N1time, N2time; // время последней генерации объекта
    private int PassengerCarNum = 0, TruckNum = 0; // кол-во объектов класса PassengerCarNum, объектов класса TruckNum

    private JPanel gamePanel; // панель, на которой генерируются объекты
    private JButton stopButton;
    private JButton startButton;

    private Timer startTime;  // таймер
    private long firstTime; //время начала запуска таймер
    private boolean firstRun = true; // первый запуск
    private JPanel controlPanel;
    private JPanel rootPanel;
    private PaintPanel paintPanel;
    private JPanel buttonPanel;
    private JCheckBox checkBoxInfo;
    private JLabel timerLabel;
    private JRadioButton showInfo;
    private JRadioButton hideInfo;
    private JPanel RadioGroup;
    private JComboBox carPerComboBox;
    private JSlider carProbSlider;
    private JPanel carPanel;
    private JPanel truckPanel;
    private JComboBox truckPerComboBox;
    private JSlider truckProbSlider;

    // конструктор среды
    public Habitat(int JFwidth, int JFheight, float carGenTime, float truckGenTime, float carProb, float truckProb) {
        this.JFwidth = JFwidth; // ширина
        this.JFheight = JFheight; // выоста
        this.carGenTime = carGenTime; // время генерации каждые N секунд
        this.truckGenTime = truckGenTime;
        this.carProb = carProb; // веротяность генерации
        this.truckProb = truckProb;

        setTitle("Гречишников ЛР2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(JFwidth, JFheight); //размер
        setContentPane(rootPanel); //добавляем панель
        setVisible(true);

        PassengerCarNum = 0; // кол-во легковых машин
        TruckNum = 0; // кол-во грузовых машин
        objects = new ArrayList<>(); // список сгенерированных объектов
        factory = new ConcreteFactory();

        timerLabel.setText("Время: " + "0" + " Легковые: " + PassengerCarNum + " Грузовые: " + TruckNum);
        timerLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));

        gamePanel.setLayout(new BorderLayout());

        paintPanel = new PaintPanel();
        paintPanel.setBackground(Color.white);
        paintPanel.setVisible(true);
        paintPanel.setLayout(null);
        paintPanel.setMinimumSize(gamePanel.getSize());

        gamePanel.add(paintPanel, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();

        showInfo.setSelected(true);
        showInfo.addActionListener(e -> {
            timerLabel.setVisible(true);
            hideInfo.setSelected(false);
        });

        hideInfo.setSelected(false);
        hideInfo.addActionListener(e -> {
            timerLabel.setVisible(false);
            showInfo.setSelected(false);
        });

        startButton.setEnabled(true);
        startButton.addActionListener(e -> startSim());

        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> stopSim());

        Hashtable<Integer, JLabel> sliderLabels = new Hashtable<>();
        sliderLabels.put(0, new JLabel("0"));
        sliderLabels.put(10, new JLabel("1"));

        carProbSlider.setLabelTable(sliderLabels);
        carProbSlider.setMaximum(10);
        carProbSlider.setMinimum(0);
        carProbSlider.setMajorTickSpacing(1);
        carProbSlider.setPaintLabels(true);
        carProbSlider.setValue(5);
        carProbSlider.addChangeListener(e -> setProb(carProbSlider.getValue(), CarType.CAR));

        truckProbSlider.setLabelTable(sliderLabels);
        truckProbSlider.setMaximum(10);
        truckProbSlider.setMinimum(0);
        truckProbSlider.setMajorTickSpacing(1);
        truckProbSlider.setPaintLabels(true);
        truckProbSlider.setValue(5);
        truckProbSlider.addChangeListener(e -> setProb(truckProbSlider.getValue(), CarType.TRUCK));

        String[] comboItems = {
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10"
        };
        for (String i : comboItems) carPerComboBox.addItem(i);
        carPerComboBox.setSelectedIndex(Math.round(carGenTime-1));
        carPerComboBox.addItemListener(e -> setGenTime(carPerComboBox.getSelectedIndex() + 1, CarType.CAR));

        for (String i : comboItems) truckPerComboBox.addItem(i);
        truckPerComboBox.setSelectedIndex(Math.round(truckGenTime-1));
        truckPerComboBox.addItemListener(e -> setGenTime(truckPerComboBox.getSelectedIndex() + 1, CarType.TRUCK));

        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            // клавиша нажата
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) { //нажата B
                    startSim();
                }
                if (e.getKeyCode() == KeyEvent.VK_E) //нажата E, симуляция завершена
                {
                    stopSim();
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

        menuInit();

        revalidate();
        repaint();
        showPanel();
    }

    private void setGenTime(int selectedIndex, CarType carType) {
        if (CarType.CAR == carType) carGenTime = selectedIndex;
        else truckGenTime = selectedIndex;
    }

    private void setProb(int value, CarType carType) {
        if (CarType.CAR == carType) carProb = ((float) value) / 10;
        else System.out.println(truckProb = ((float) value) / 10);
    }

    private void menuInit() {
        JMenuBar menuBar = new JMenuBar();
        JMenu simMenu = new JMenu("Simulation");

        JMenuItem startSimItem = new JMenuItem("Start simulation");
        startSimItem.addActionListener(e -> startSim());

        JMenuItem stopSimItem = new JMenuItem("Stop simulation");
        stopSimItem.addActionListener(e -> stopSim());

        simMenu.add(startSimItem);
        simMenu.add(stopSimItem);
        menuBar.add(simMenu);

        setJMenuBar(menuBar);

        init();
    }

    private int showDialog() {
        return JOptionPane.showConfirmDialog(this,
                "Легковые машины: " + PassengerCarNum + "\n" +
                        "Грузовые машины: " + TruckNum,
                "Статистика",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Функция запуска симуляции
     */
    private void startSim() {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        startButton.setFocusable(false);
        stopButton.setFocusable(true);
        if (startTime != null)
            return;
        objects.clear(); // очищаем список объектов
        paintPanel.removeAll(); // очищаем панель рисования объектов

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
                long simTime = (currentTime - firstTime); // время прошедшее с начала симуляции

                timerLabel.setText("Время: " + Math.round(simTime / 1000.f) + " Легковые: " + PassengerCarNum + " Грузовые: " + TruckNum);

                Update(simTime); // обновляем таймер
            }
        }, 0, 100); // каждую 0,1 секунду запускается update
    }

    /**
     * Функция остановки симуляции
     */

    private void stopSim() {
        if (showDialog() == 0) {
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
            paintPanel.removeAll();
        }
    }

    //инициализация среды
    public void init() {
        //добавляем слушателя изменений на главном окне
        //обработчик клавиш


    }

    // обеновление таймера
    private void Update(long timeFromStart) {
        //генерация легковой машины
        if (timeFromStart > N1time + carGenTime * 1000) {
            N1time += carGenTime * 1000;
            if ((float) Math.random() <= carProb) {
                PassengerCarNum++; //увеличиваем счетчик
                objects.add(factory.createPassengerCar((float) (Math.random() * paintPanel.getWidth() - 100), (float) (Math.random() * paintPanel.getHeight() - 25)));
            }
        }
        //генерация грузовой машины
        if (timeFromStart > N2time + truckGenTime * 1000) {
            N2time += truckGenTime * 1000;

            if ((float) Math.random() <= truckProb) {
                TruckNum++;//увеличиваем счетчик
                objects.add(factory.createTruck((float) (Math.random() * paintPanel.getWidth() - 115), (float) (Math.random() * paintPanel.getHeight() - 25)));
            }
        }
        paintPanel.paintPanelUpdate(objects); //загружает объекты в массив
        paintPanel.paintCar(paintPanel.getGraphics()); // прорисовка объектов
        paintPanel.revalidate();
    }

    /**
     * Устанавливает Graphics в paint
     */

    private void showPanel() {
        paintPanel.paintComponent(paintPanel.getGraphics());
    }

}










