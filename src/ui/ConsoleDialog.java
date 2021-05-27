package ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConsoleDialog extends JDialog implements KeyListener {

    private final Habitat context;

    JTextArea textArea = null;

    ConsoleDialog(Habitat context) { //команда "консоль"
        super(context, "Console");
        this.context = context;
        JPanel consolePanel = new JPanel();
        textArea = new JTextArea(25, 100);
        setSize(800, 400);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(true);

        consolePanel.add(scrollPane);

        getContentPane().add(consolePanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        textArea.getLineCount();

        textArea.addKeyListener(this);
        validate();
        setVisible(true);

    }


    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            sendLine();
        }
    }

    private void sendLine() {
        int end = textArea.getDocument().getLength();
        int start = 0;
        try {
            start = Utilities.getRowStart(textArea, end);

            while (start == end) {
                end--;
                start = Utilities.getRowStart(textArea, end);
            }
            String text = textArea.getText(start, end - start);
            switch (text) {
                case "start carAI": { //команды
                    context.startAICommand("car");
                }
                break;
                case "stop carAI": {
                    context.stopAICommand("car");
                }
                break;
                case "start truckAI": {
                    context.startAICommand("truck");
                }
                break;
                case "stop truckAI": {
                    context.stopAICommand("truck");
                }
                break;
                default: {
                    textArea.append("wrong command\n");
                }

            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
