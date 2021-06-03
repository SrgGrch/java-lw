package ui;

import model.AbstractCar;
import model.SendCarsRequest;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class SendDialog extends JDialog {

    private final Habitat context;

    JList<String> list;

    SendDialog(Habitat context, Socket socket, List<UUID> clients, List<AbstractCar> cars) { //команда "консоль"
        super(context, "Console");
        this.context = context;
        JPanel consolePanel = new JPanel();
        setSize(800, 400);

        String[] result = new String[clients.size()];
        int i = 0;
        for (UUID client : clients) {
            result[i++] = client.toString();
        }
        list = new JList<>(result);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setSize(800, 400);
        list.addListSelectionListener(e -> {
            int selected = ((JList<?>) e.getSource()).getSelectedIndex();
            UUID id = clients.get(selected);
            try {
                OutputStream os = socket.getOutputStream();
                os.write(new SendCarsRequest(cars, id).getBytes());
                os.flush();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        consolePanel.add(list);

        getContentPane().add(consolePanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        validate();
        setVisible(true);
    }
}
