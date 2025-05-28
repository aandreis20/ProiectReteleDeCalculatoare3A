package client;

import model.Masina;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.ObjectInputStream;
import java.util.List;

public class ClientListener implements Runnable {
    private final ObjectInputStream in;
    private final JTextArea outputArea;
    private final DefaultTableModel tableModel;

    public ClientListener(ObjectInputStream in, JTextArea outputArea, DefaultTableModel tableModel) {
        this.in = in;
        this.outputArea = outputArea;
        this.tableModel = tableModel;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object raspuns = in.readObject();

                if (raspuns instanceof List<?> lista && !lista.isEmpty() && lista.get(0) instanceof Masina) {
                    SwingUtilities.invokeLater(() -> updateTable((List<Masina>) lista));
                } else if (raspuns instanceof String msg) {
                    SwingUtilities.invokeLater(() -> outputArea.append("[Server] " + msg + "\n"));
                }
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() ->
                    outputArea.append("Conexiunea cu serverul a fost întreruptă.\n")
            );
        }
    }

private void updateTable(List<Masina> masini) {
    tableModel.setRowCount(0);
    for (Masina m : masini) {
        tableModel.addRow(new Object[]{
                m.getVin(), m.getMarca(), m.getModel(), m.getAnFabricatie()
        });
    }
}
}