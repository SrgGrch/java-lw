package ui;

import model.AbstractCar;
import model.Truck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StatDialog {
    private JList list;
    private JLabel label;
    private JOptionPane optionPane;
    private JButton okButton;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;

    private String[] convertList(ArrayList<AbstractCar> list){

        String[] out = new String[list.size()];
        int i = 0;
        for (AbstractCar car:list) {
            out[i] = " ";
            out[i] += car.getId().toString() + " " + car.getBirthTime() + " ";
            out[i++] += (car instanceof Truck)? "Truck ":"PassengerCar ";
        }
        return out;
    }

    public StatDialog(String message, ArrayList<AbstractCar> listToDisplay){
        list = new JList<>(convertList(listToDisplay));
        list.setFont(new Font("monospaced", Font.PLAIN, 12));
        label = new JLabel(message);
        createAndDisplayOptionPane();
    }

    public StatDialog(String title, String message,  ArrayList<AbstractCar> listToDisplay){
        this(message, listToDisplay);
        dialog.setTitle(title);
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton});
        dialog = optionPane.createDialog("Статистика");
        dialog.setBackground(Color.white);
    }

    private void setupButtons(){
        okButton = new JButton("Ок");
        okButton.addActionListener(e -> handleOkButtonClick(e));
    }

    private JPanel layoutComponents(){
        centerListElements();
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.add(label, BorderLayout.NORTH);
        panel.add(list, BorderLayout.CENTER);
        return panel;
    }

    private void centerListElements(){
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if(okEvent != null){ okEvent.actionPerformed(e); }
        hide();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        hide();
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }

    public Object getSelectedItem(){ return list.getSelectedValue(); }
}
