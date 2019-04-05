package ui;

import factory.ConcreteFactory;
import model.AbstractCar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;
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

    private String[] comboItems = {
            "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10"
    };

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
    private JComboBox perComboBox;
    private JSlider probSlider;

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
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

        probSlider.setLabelTable(sliderLabels);
        probSlider.setMaximum(10);
        probSlider.setMinimum(0);
        probSlider.setMajorTickSpacing(1);
        probSlider.setPaintLabels(true);
        probSlider.setValue(5);
        probSlider.addChangeListener(e -> setProb(probSlider.getValue()));

        for (String i:comboItems) {
            perComboBox.addItem(i);
        }
        perComboBox.setSelectedIndex(0);
        perComboBox.addItemListener(e -> setGenTime(perComboBox.getSelectedIndex()));

        menuInit();

        revalidate();
        repaint();
        showPanel();
    }

    private void setGenTime(int selectedIndex) {
        N1 = N2 = selectedIndex;
    }

    private void setProb(int value) {
        P1 = P2 = ((float) value)/10;
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
    }

    private int showDialog() {
        return JOptionPane.showConfirmDialog(this,
                "Легковые машины: " + PassengerCarNum + "\n" +
                        "Грузовые машины: " + TruckNum,
                "Статистика",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
    }

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
    public void Init() {
        //добавляем слушателя изменений на главном окне
        //обработчик клавиш
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
    }

    // обеновление таймера
    private void Update(long timeFromStart) {
        //генерация легковой машины
        if (timeFromStart > N1time + N1 * 1000) {
            N1time += N1 * 1000;
            if ((float) Math.random() <= P1) {
                PassengerCarNum++; //увеличиваем счетчик
                objects.add(factory.createPassengerCar((float) (Math.random() * paintPanel.getWidth() - 100), (float) (Math.random() * paintPanel.getHeight() - 25)));
            }
        }
        //генерация грузовой машины
        if (timeFromStart > N2time + N2 * 1000) {
            N2time += N2 * 1000;

            if ((float) Math.random() <= P2) {
                TruckNum++;//увеличиваем счетчик
                objects.add(factory.createTruck((float) (Math.random() * paintPanel.getWidth() - 115), (float) (Math.random() * paintPanel.getHeight() - 25)));
            }
        }
        paintPanel.paintPanelUpdate(objects); //загружает объекты в массив
        paintPanel.paintCar(paintPanel.getGraphics()); // прорисовка объектов
        paintPanel.revalidate();
    }

    /**
     * Устанавливает Graphics в paintPanel
     */

    private void showPanel() {
        paintPanel.paintComponent(paintPanel.getGraphics());
    }

}
