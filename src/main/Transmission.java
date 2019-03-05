package main;

import java.util.HashMap;

import echanges.Trame;

public class Transmission implements Runnable {
    private String nomCouche;
    private TamponCirculaire tampon;
    private final long LATENCY = 500; // en ms
    private HashMap<Integer, SousCouche<?, Trame>> couchesReceptrices;

    public Transmission(int grandeurBuffer, String nomCouche) {
        this.nomCouche = nomCouche;
        tampon = new TamponCirculaire(grandeurBuffer);
        couchesReceptrices = new HashMap<Integer, SousCouche<?, Trame>>();
    }

    public synchronized void addCoucheReceptrice(int stationID, SousCouche<?, Trame> couche) {
        couchesReceptrices.put(stationID, couche);
    }

    @Override
    public void run() {
        while (true) {
            if (tampon.isEmpty()) {
                continue;
            }

            long timeToWait = LATENCY - (System.currentTimeMillis() - tampon.getLastAddedTime());
            if(timeToWait > 0) {
                continue;
            }
            Trame t = tampon.poll();
            addErrors(t);
            sendTrame(t);
        }
    }

    public synchronized boolean addTrame(Trame trame) {
        if (tampon.add(trame)) {
            System.out.println(
                    "La couche " + nomCouche + " a ajouté la trame " + trame.getNumTrameHamming() + " à son tampon.");
            return true;
        }
        return false;
    }

    private void addErrors(Trame t) {
        // TODO: Ajouter les erreurs à la trame
    }

    private void sendTrame(Trame t) {
        int numDest = t.getDestHamming();
        SousCouche<?, Trame> dest = couchesReceptrices.get(numDest);
        if (dest == null) {
            return;
        }
        dest.addFromDown(t);
        System.out.println("La couche " + nomCouche + " a envoyé la trame " + t.getNumTrameHamming() + " à la station "
                + numDest + ".");
    }
}