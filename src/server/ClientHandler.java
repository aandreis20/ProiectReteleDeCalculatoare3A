package server;

import model.Masina;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectManager objectManager;
    private ObjectOutputStream out;

    public ClientHandler(Socket clientSocket, ObjectManager objectManager) {
        this.clientSocket = clientSocket;
        this.objectManager = objectManager;
    }

    public ObjectOutputStream getOutputStream() {
        return out;
    }

    @Override
    public void run() {
        try (
                Socket socket = this.clientSocket;
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            this.out = oos;
            ServerMain.clientiConectati.add(this);

            out.writeObject("Conexiune reușită. Foloseste comenzi (ADD/SELECT/UPDATE/DELETE/EXIT/LIST).");

            while (true) {
                Object objComanda = in.readObject();
                if (!(objComanda instanceof String comanda)) continue;

                switch (comanda.toUpperCase()) {
                    case "ADD" -> {
                        Object objMasina = in.readObject();
                        if (!(objMasina instanceof Masina masina)) {
                            out.writeObject("Obiect invalid pentru ADD.");
                            break;
                        }

                        String rezultat = objectManager.addMasina(masina);
                        out.writeObject(rezultat);

                        if (rezultat.equals("MASINA_ADAUGATA")) {
                            objectManager.notificaClienti("Masina " + masina.getVin() + " a fost adăugată.", this);
                        }
                    }

                    case "UPDATE" -> {
                        Object objMasina = in.readObject();
                        if (!(objMasina instanceof Masina masina)) {
                            out.writeObject("Obiect invalid pentru UPDATE.");
                            break;
                        }

                        String rezultat = objectManager.updateMasina(masina);
                        out.writeObject(rezultat);

                        if (rezultat.equals("MASINA_ACTUALIZATA")) {
                            objectManager.notificaClienti("Masina " + masina.getVin() + " a fost actualizată.", this);
                        }
                    }

                    case "DELETE" -> {
                        Object vinObj = in.readObject();
                        if (!(vinObj instanceof String vin)) {
                            out.writeObject("Obiect VIN invalid pentru DELETE.");
                            break;
                        }

                        String rezultat = objectManager.deleteMasina(vin);
                        out.writeObject(rezultat);

                        if (rezultat.equals("MASINA_STEARSA")) {
                            objectManager.notificaClienti("Masina " + vin + " a fost ștearsă.", this);
                        }
                    }

                    case "SELECT" -> {
                        Object vinObj = in.readObject();
                        if (!(vinObj instanceof String vin)) {
                            out.writeObject("Obiect VIN invalid pentru SELECT.");
                            break;
                        }

                        Masina masina = objectManager.getMasina(vin);
                        out.writeObject(masina != null ? masina.toString() : "Masina nu a fost găsită.");
                    }

                    case "LIST" -> {
                        List<Masina> lista = objectManager.listMasiniObiect();
                        out.writeObject(lista);
                    }

                    case "EXIT" -> {
                        out.writeObject("Conexiune închisă.");
                        return;
                    }

                    default -> out.writeObject("Comandă necunoscută.");
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Eroare la client: " + e.getMessage());
        } finally {
            ServerMain.clientiConectati.remove(this);
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
            System.out.println("Client deconectat.");
        }
    }
}