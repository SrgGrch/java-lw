package ui;

import ai.PassengerCarAI;
import ai.TruckAI;
import core.PictureLoader;
import factory.ConcreteFactory;
import model.AbstractCar;
import model.Truck;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Timer;
import java.util.*;

// среда рабочей области
public class Habitat extends JFrame {

    private enum CarType {CAR, TRUCK}

    private ArrayList<AbstractCar> objects; //массив объектов
    private HashMap<UUID, JLabel> images = new HashMap<>();
    private TreeSet<UUID> uuidTree = new TreeSet<>();
    private HashMap<UUID, Long> birthTimeMap = new HashMap<>();

    long carLifeTime = 5000, truckLifeTime = 5000;

    private final TruckAI truckAI;
    private final PassengerCarAI passengerCarAI;

    Properties properties = new Properties();

    private ConcreteFactory factory;
    private float carGenTime, truckGenTime, carProb, truckProb; //N - время генерации объекта, P - вероятность генерации
    private long timeFromStart = 0; //время начала генерации объектов
    private float carTime, truckTime; // время последней генерации объекта
    private int PassengerCarNum, TruckNum; // кол-во объектов класса PassengerCarNum, объектов класса TruckNum

    long simTime;

    private Timer simTimer;
    private long firstTime;
    private boolean firstRun = true;

    private JPanel gamePanel;
    private JButton stopButton;
    private JButton startButton;
    private JPanel controlPanel;
    private JPanel rootPanel;
    private JPanel buttonPanel;
    private JCheckBox checkBoxInfo;
    private JLabel timerLabel;
    private JRadioButton showInfo;
    private JRadioButton hideInfo;
    private JComboBox<String> carPerComboBox;
    private JSlider carProbSlider;
    private JPanel carPanel;
    private JPanel truckPanel;
    private JComboBox<String> truckPerComboBox;
    private JSlider truckProbSlider;
    private JSpinner carSpinner;
    private JSpinner truckSpinner;
    private JButton statButton;
    private JCheckBox carAICheckBox;
    private JCheckBox truckAICheckBox;
    private JCheckBox priorCheckBox;
    private JPanel radioGroup;
    private ButtonGroup buttonGroup;

    // конструктор среды
    public Habitat(int JFwidth, int JFheight, float carGenTimeIn, float truckGenTimeIn, float carProbIn, float truckProbIn) {
        this.carGenTime = carGenTimeIn; // время генерации каждые N секунд
        this.truckGenTime = truckGenTimeIn;
        this.carProb = carProbIn; // веротяность генерации
        this.truckProb = truckProbIn;

        readProps();

        setTitle("Гречишников ЛР5");
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

        gamePanel.setLayout(null);
        gamePanel.setBackground(Color.white);
        gamePanel.revalidate();
        gamePanel.repaint();

        showInfo.setSelected(true);
        showInfo.addActionListener(e -> timerLabel.setVisible(true));

        hideInfo.setSelected(false);
        hideInfo.addActionListener(e -> timerLabel.setVisible(false));

        startButton.setEnabled(true);
        startButton.addActionListener(e -> startSim());

        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> stopSim());

        buttonGroup = new ButtonGroup();
        buttonGroup.add(showInfo);
        buttonGroup.add(hideInfo);

        Hashtable<Integer, JLabel> sliderLabels = new Hashtable<>();
        sliderLabels.put(0, new JLabel("0"));
        sliderLabels.put(10, new JLabel("1"));

        JFormattedTextField txtCar = ((JSpinner.NumberEditor) carSpinner.getEditor()).getTextField();
        ((NumberFormatter) txtCar.getFormatter()).setAllowsInvalid(false);
        carSpinner.setValue(carLifeTime / 1000);
        carSpinner.addChangeListener(e -> {
            if ((Integer) carSpinner.getValue() < 0) carSpinner.setValue(0);
            carLifeTime = (Integer) carSpinner.getValue() * 1000;
        });

        JFormattedTextField txtTruck = ((JSpinner.NumberEditor) truckSpinner.getEditor()).getTextField();
        ((NumberFormatter) txtTruck.getFormatter()).setAllowsInvalid(false);
        truckSpinner.setValue(truckLifeTime / 1000);
        truckSpinner.addChangeListener(e -> {
            if ((Integer) truckSpinner.getValue() < 0) truckSpinner.setValue(0);
            truckLifeTime = (Integer) truckSpinner.getValue() * 1000;
        });

        carProbSlider.setLabelTable(sliderLabels);
        carProbSlider.setMaximum(10);
        carProbSlider.setMinimum(0);
        carProbSlider.setMajorTickSpacing(1);
        carProbSlider.setPaintLabels(true);
        carProbSlider.setValue(Math.round(truckProb * 10));
        carProbSlider.addChangeListener(e -> setProb(carProbSlider.getValue(), CarType.CAR));

        statButton.addActionListener(e -> {
            StatDialog dialog = new StatDialog("Please select an item in the list: ", objects);
            dialog.setOnOk(event -> System.out.println("Chosen item: " + dialog.getSelectedItem()));
            dialog.show();
        });

        truckProbSlider.setLabelTable(sliderLabels);
        truckProbSlider.setMaximum(10);
        truckProbSlider.setMinimum(0);
        truckProbSlider.setMajorTickSpacing(1);
        truckProbSlider.setPaintLabels(true);
        truckProbSlider.setValue(Math.round(truckProb * 10));
        truckProbSlider.addChangeListener(e -> setProb(truckProbSlider.getValue(), CarType.TRUCK));

        String[] comboItems = {
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10"
        };

        for (String i : comboItems) carPerComboBox.addItem(i);
        carPerComboBox.setSelectedIndex(Math.round(carGenTime - 1));
        carPerComboBox.addItemListener(e -> setGenTime(carPerComboBox.getSelectedIndex() + 1, CarType.CAR));

        for (String i : comboItems) truckPerComboBox.addItem(i);
        truckPerComboBox.setSelectedIndex(Math.round(truckGenTime - 1));
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

        truckAI = new TruckAI(gamePanel.getWidth(), gamePanel.getHeight(), objects, images, this);
        passengerCarAI = new PassengerCarAI(gamePanel.getWidth(), gamePanel.getHeight(), objects, images, this);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                properties.put("carProb", Float.toString(carProb));
                properties.put("truckProb", Float.toString(truckProb));
                properties.put("carGenTime", Float.toString(carGenTime));
                properties.put("truckGenTime", Float.toString(truckGenTime));
                properties.put("carLifeTime", Long.toString(carLifeTime));
                properties.put("truckLifeTime", Long.toString(truckLifeTime));
                properties.put("carAi", Boolean.toString(carAICheckBox.isSelected()));
                properties.put("truckAi", Boolean.toString(truckAICheckBox.isSelected()));
//                properties.put("simTime", radioGroup.)
                try {
                    properties.store(new FileOutputStream("config.prop"), " ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        menuInit();

        revalidate();
        repaint();
    }

    private void readProps() {

        try {
            properties.load(new FileInputStream("config.prop"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        carProb = Float.parseFloat((String) properties.get("carProb"));
        truckProb = Float.parseFloat((String) properties.get("truckProb"));
        carGenTime = Float.parseFloat((String) properties.get("carGenTime"));
        truckGenTime = Float.parseFloat((String) properties.get("truckGenTime"));
        carLifeTime = Long.parseLong((String) properties.get("carLifeTime"));
        truckLifeTime = Long.parseLong((String) properties.get("truckLifeTime"));
        carAICheckBox.setSelected(Boolean.parseBoolean((String) properties.get("carAi")));
        truckAICheckBox.setSelected(Boolean.parseBoolean((String) properties.get("truckAi")));

    }

    private void setGenTime(int selectedIndex, CarType carType) {
        if (CarType.CAR == carType) carGenTime = selectedIndex;
        else truckGenTime = selectedIndex;
    }

    private void setProb(int value, CarType carType) {
        if (CarType.CAR == carType) carProb = ((float) value) / 10;
        else truckProb = ((float) value) / 10;
    }


    private void menuInit() {
        JMenuBar menuBar = new JMenuBar();
        JMenu simMenu = new JMenu("Simulation");
        JMenu objMenu = new JMenu("Objects");

        JMenuItem startSimItem = new JMenuItem("Start simulation");
        startSimItem.addActionListener(e -> startSim());

        JMenuItem stopSimItem = new JMenuItem("Stop simulation");
        stopSimItem.addActionListener(e -> stopSim());

        JMenuItem saveObjects = new JMenuItem("Save objects");
        saveObjects.addActionListener(e -> saveObjects());

        JMenuItem loadObjects = new JMenuItem("Load objects");
        loadObjects.addActionListener(e -> loadObjects());

        simMenu.add(startSimItem);
        simMenu.add(stopSimItem);

        objMenu.add(saveObjects);
        objMenu.add(loadObjects);

        menuBar.add(simMenu);
        menuBar.add(objMenu);

        setJMenuBar(menuBar);
    }

    private void loadObjects() {
        JFileChooser c = new JFileChooser();
        // Demonstrate "Open" dialog:
        if (c.showOpenDialog(this) == 0){
            String path = c.getSelectedFile().getPath();
            System.out.println(path);
            try {
                FileInputStream fos = new FileInputStream(path);
                ObjectInputStream oos = new ObjectInputStream(fos);
                ArrayList<AbstractCar> tmp = (ArrayList<AbstractCar>) oos.readObject();
                fixLoadedObjects(tmp);
                objects.clear();
                objects.addAll(tmp);
                gamePanel.removeAll();
                uuidTree.clear();
                images.clear();
                birthTimeMap.clear();

                for (AbstractCar car:objects) {
                    addCar(car);
                }

                oos.close();
                System.out.println("DONE");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void fixLoadedObjects(ArrayList<AbstractCar> tmp) {
        for (AbstractCar car: tmp) {
            car.setBirthTime(simTime);
        }
    }


    private void saveObjects() {
        JFileChooser c = new JFileChooser();
        // Demonstrate "Open" dialog:
        if (c.showOpenDialog(this) == 0) {
            String path = c.getSelectedFile().getPath();
            System.out.println(path);
            try {
                FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(objects);
                oos.flush();
                oos.close();
                System.out.println("DONE");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Функция возвращающая диалог окончания симуляции
     *
     * @return Диалог окончания симуляции
     */
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
        if (simTimer != null)
            return;
        objects.clear();

        priorCheckBox.addActionListener(e -> {
            if (!priorCheckBox.isSelected()) {
                passengerCarAI.setPriority(Thread.MAX_PRIORITY);
                truckAI.setPriority(Thread.MIN_PRIORITY);
            } else {
                passengerCarAI.setPriority(Thread.MIN_PRIORITY);
                truckAI.setPriority(Thread.MAX_PRIORITY);
            }
        });

        controlCarThread(carAICheckBox.isSelected());
        controlTruckThread(truckAICheckBox.isSelected());

        passengerCarAI.start();
        truckAI.start();

        attachListeners();

        simTimer = new Timer(); // создаем таймер
        simTimer.schedule(new TimerTask() { //запуск таймера
            @Override
            public void run() { //метод для таймера
                if (firstRun) {
                    firstTime = System.currentTimeMillis(); //начало симуляции
                    firstRun = false;
                }

                long currentTime = System.currentTimeMillis();
                simTime = (currentTime - firstTime); // время прошедшее с начала симуляции

                timerLabel.setText("Время: " + Math.round(simTime / 1000.f) + " Легковые: " + PassengerCarNum + " Грузовые: " + TruckNum);

                Update(simTime); // обновляем таймер
            }
        }, 0, 1000); // каждую 0,1 секунду запускается update
    }

    private synchronized void attachListeners() {
        carAICheckBox.addActionListener(e -> controlCarThread(carAICheckBox.isSelected()));

        truckAICheckBox.addActionListener(e -> controlTruckThread(truckAICheckBox.isSelected()));
    }

    private synchronized void controlCarThread(boolean checked) {
        if (!checked) {
            passengerCarAI.stopAI();
        } else {
            passengerCarAI.resumeAI();
        }
    }

    private synchronized void controlTruckThread(boolean checked) {
        if (!checked) {
            truckAI.stopAI();
        } else {
            truckAI.resumeAI();
        }
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

            if (simTimer == null)
                return;
            simTimer.cancel(); //останавливаем таймер
            simTimer = null;
            objects.clear(); // очищаем список объектов
            PassengerCarNum = 0;
            TruckNum = 0;
            carTime = 0;
            truckTime = 0;


            truckAI.stop();
            passengerCarAI.stop();

            images.clear();

            firstRun = true;
            gamePanel.removeAll();
            gamePanel.revalidate();
        }
    }

    public void repaintGamePanel() {
        gamePanel.repaint();
        gamePanel.revalidate();
    }


    /**
     * Функция управляющая генерацией объектов
     *
     * @param timeFromStart время симуляции
     */
    private void Update(long timeFromStart) {

        if (timeFromStart > truckTime + truckGenTime * 1000 && truckLifeTime != 0) {
            truckTime += truckGenTime * 1000;

            if ((float) Math.random() <= truckProb) {
                TruckNum++;
                objects.add(factory.createTruck((float) (Math.random() * gamePanel.getWidth() - 115),
                        (float) (Math.random() * gamePanel.getHeight() - 25), timeFromStart, truckLifeTime));
                addCar(objects.get(objects.size() - 1));
            }
            checkLifetime(timeFromStart);
            repaint();
            revalidate();
        }

        if (timeFromStart > carTime + carGenTime * 1000 && carLifeTime != 0) {
            carTime += carGenTime * 1000;
            if ((float) Math.random() <= carProb) {
                PassengerCarNum++;
                objects.add(factory.createPassengerCar((float) (Math.random() * gamePanel.getWidth() - 100),
                        (float) (Math.random() * gamePanel.getHeight() - 25), timeFromStart, carLifeTime));
                addCar(objects.get(objects.size() - 1));
            }
            checkLifetime(timeFromStart);
            repaint();
            revalidate();
        }
    }

    private void addCar(AbstractCar car) {
        JLabel tmp = new JLabel(new ImageIcon(PictureLoader.load(car)));

        tmp.setSize(PictureLoader.load(car).getWidth(), PictureLoader.load(car).getHeight());
        tmp.setLocation(Math.round(car.getX()), Math.round(car.getY()));
        gamePanel.add(tmp);
        images.put(car.getId(), tmp);
        uuidTree.add(car.getId());
        birthTimeMap.put(car.getId(), car.getBirthTime());
    }

    private void checkLifetime(long timeFromStart) {
        if (objects.size() != 0) {
            Iterator<AbstractCar> iterator = objects.iterator();
            for (AbstractCar car = iterator.next(); iterator.hasNext(); car = iterator.next()) {
                if (car.getLifetime() + car.getBirthTime() <= timeFromStart) {
                    iterator.remove();
                    JLabel img = images.get(car.getId());
                    images.remove(car.getId());
                    gamePanel.remove(img);
                    if (car instanceof Truck) TruckNum--;
                    else PassengerCarNum--;
                }
            }
        }
    }

}










