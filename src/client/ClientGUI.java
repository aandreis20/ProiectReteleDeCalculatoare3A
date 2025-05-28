package client;

import model.Masina;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;

public class ClientGUI extends JFrame {
    private JTextField vinField, marcaField, modelField, anField;
    private JTextArea outputArea;
    private JTable table;
    private DefaultTableModel tableModel;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JButton addBtn, selectBtn, updateBtn, deleteBtn, listBtn, exitBtn;

    public ClientGUI() {
        setTitle("Gestiune Mașini - Client");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        vinField = new JTextField();
        marcaField = new JTextField();
        modelField = new JTextField();
        anField = new JTextField();

        inputPanel.setBorder(BorderFactory.createTitledBorder("Date Mașină"));
        inputPanel.add(new JLabel("VIN:"));
        inputPanel.add(vinField);
        inputPanel.add(new JLabel("Marca:"));
        inputPanel.add(marcaField);
        inputPanel.add(new JLabel("Model:"));
        inputPanel.add(modelField);
        inputPanel.add(new JLabel("An fabricație:"));
        inputPanel.add(anField);
        add(inputPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Log și Tabel Mașini"));

        outputArea = new JTextArea(5, 20);
        outputArea.setEditable(false);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        centerPanel.add(new JScrollPane(outputArea), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"VIN", "Marca", "Model", "An"}, 0);
        table = new JTable(tableModel);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.setComparator(3, Comparator.comparingInt(s -> Integer.parseInt(s.toString())));
        List<RowSorter.SortKey> sortKeys = List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        addBtn = createButton("ADD");
        selectBtn = createButton("SELECT");
        updateBtn = createButton("UPDATE");
        deleteBtn = createButton("DELETE");
        listBtn = createButton("LIST");
        exitBtn = createButton("EXIT");

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        buttonPanel.add(addBtn);
        buttonPanel.add(selectBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(listBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        conecteazaLaServer();
        setVisible(true);
    }

    private JButton createButton(String name) {
        JButton btn = new JButton(name);
        btn.addActionListener(e -> trimiteComanda(name));
        return btn;
    }

    private void conecteazaLaServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(new ClientListener(in, outputArea, tableModel)).start();
        } catch (Exception e) {
            appendMsg("Eroare la conectare: " + e.getMessage(), Color.RED);
            dezactiveazaButoane();
        }
    }

    private void dezactiveazaButoane() {
        addBtn.setEnabled(false);
        selectBtn.setEnabled(false);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        listBtn.setEnabled(false);
        exitBtn.setEnabled(false);
    }

    private void trimiteComanda(String tip) {
        try {
            String vin = vinField.getText().trim();
            String marca = marcaField.getText().trim();
            String model = modelField.getText().trim();
            String an = anField.getText().trim();

            switch (tip) {
                case "ADD", "UPDATE" -> {
                    if (vin.isEmpty() || marca.isEmpty() || model.isEmpty() || an.isEmpty()) {
                        appendMsg("Toate câmpurile sunt necesare pentru " + tip, Color.RED);
                        return;
                    }
                    if (!an.matches("\\d+")) {
                        appendMsg("Anul trebuie să fie un număr valid!", Color.RED);
                        return;
                    }

                    int anInt = Integer.parseInt(an);
                    Masina masina = new Masina(vin, marca, model, anInt);

                    out.writeObject(tip);
                    out.writeObject(masina);
                    out.flush();
                }

                case "SELECT", "DELETE" -> {
                    if (vin.isEmpty()) {
                        appendMsg("VIN-ul este necesar pentru " + tip, Color.RED);
                        return;
                    }
                    out.writeObject(tip);
                    out.writeObject(vin);
                    out.flush();
                }

                case "LIST" -> {
                    out.writeObject("LIST");
                    out.flush();
                }

                case "EXIT" -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Sigur dorești să închizi aplicația?",
                            "Confirmare ieșire",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        out.writeObject("EXIT");
                        out.flush();
                        out.close();
                        in.close();
                        System.exit(0);
                    } else {
                        appendMsg("Ieșire anulată.", Color.GRAY);
                    }
                }

                default -> throw new IllegalArgumentException("Comandă necunoscută.");
            }

        } catch (Exception e) {
            appendMsg("Eroare la trimitere: " + e.getMessage(), Color.RED);
        }
    }

    private void appendMsg(String msg, Color culoare) {
        outputArea.setForeground(culoare);
        outputArea.append(msg + "\n");
        outputArea.setForeground(Color.BLACK);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGUI::new);
    }
}