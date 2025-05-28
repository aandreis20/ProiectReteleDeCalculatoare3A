package server;

import model.Masina;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectManager {
    private final Map<String, Masina> masini;
    private final DatabaseManager db;

    public ObjectManager() {
        db = new DatabaseManager();
        masini = db.incarcaToateMasinile();
    }

    public synchronized String addMasina(Masina masina) {
        if (masini.containsKey(masina.getVin())) {
            return "MASINA_EXISTENTA";
        }
        masini.put(masina.getVin(), masina);
        db.adaugaMasina(masina);
        return "MASINA_ADAUGATA";
    }

    public synchronized Masina getMasina(String vin) {
        return masini.get(vin);
    }

    public synchronized String updateMasina(Masina masina) {
        if (!masini.containsKey(masina.getVin())) {
            return "MASINA_INEXISTENTA";
        }
        masini.put(masina.getVin(), masina);
        db.actualizeazaMasina(masina);
        return "MASINA_ACTUALIZATA";
    }

    public synchronized String deleteMasina(String vin) {
        if (!masini.containsKey(vin)) {
            return "MASINA_INEXISTENTA";
        }
        masini.remove(vin);
        db.stergeMasina(vin);
        return "MASINA_STEARSA";
    }

    public synchronized List<Masina> listMasiniObiect() {
        return new ArrayList<>(masini.values());
    }

    public void notificaClienti(String mesaj, ClientHandler expeditor) {
        synchronized (ServerMain.clientiConectati) {
            for (ClientHandler client : ServerMain.clientiConectati) {
                if (client != expeditor) {
                    try {
                        client.getOutputStream().writeObject("[Notificare] " + mesaj);
                        client.getOutputStream().flush();
                    } catch (IOException e) {
                        System.out.println("Eroare notificare: " + e.getMessage());
                    }
                }
            }
        }
    }
}