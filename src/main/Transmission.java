package main;

import java.util.HashMap;
import java.util.Random;

import echanges.Octet;
import echanges.Trame;

public class Transmission implements Runnable {
    private String nomCouche;
    private TamponCirculaire tampon;
    private final long LATENCY = 500; // en ms
    private HashMap<Integer, SousCouche<?, Trame>> couchesReceptrices;
    private int freqErreur;

    public Transmission(int grandeurBuffer, String nomCouche, int freqErreur) {
        this.nomCouche = nomCouche;
        tampon = new TamponCirculaire(grandeurBuffer);
        couchesReceptrices = new HashMap<Integer, SousCouche<?, Trame>>();
        this.freqErreur = freqErreur;
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
            if (timeToWait > 0) {
                continue;
            }
            Trame t = tampon.poll();
            if (t == null)
                continue;
            addErrors(t);
            sendTrame(t);
        }
    }

    public synchronized boolean addTrame(Trame trame) {
        if (tampon.add(trame)) {
            System.out.println("La couche " + nomCouche + " a ajouté la trame "
                    + Byte.toUnsignedInt(trame.getNumTrameHamming()) + " à son tampon.");
            return true;
        }
        return false;
    }

    private void addErrors(Trame t) {
        Random rnd = new Random();
        int luckyNumber = rnd.nextInt(freqErreur);
        if (luckyNumber == 0) {
            modifierBits(t);
            System.out.println("Ajout d'erreurs dans la trame " + Byte.toUnsignedInt(t.getNumTrameHamming())
                    + " en modifiant certain bits.");
        }
    }

    private void modifierBits(Trame t) {
        Random rnd = new Random();
        int maxErreurs = t.getData().length / 2; // On ne veut pas trop en mettre quand même
        Octet[] octData = t.getData(); // Les données de cette trame.
        int nbErreurs = rnd.nextInt(maxErreurs) + 1;

        for (int i = 0; i < nbErreurs; i++) {
            // Sélection un octet au hasard.
            int posByte = rnd.nextInt(octData.length);
            // Position du bit à modifier.
            int posBit = rnd.nextInt(8);
            // Valeur du bit.
            int bit = octData[posByte].getBit(posBit);
            // On alterne la valeur du bit
            octData[posByte].changeBit(posBit, !(bit == 1));
        }

        t.setData(octData);
    }

    private synchronized void sendTrame(Trame t) {
        int numDest = t.getDestHamming();
        SousCouche<?, Trame> dest = couchesReceptrices.get(numDest);
        if (dest == null) {
            return;
        }
        dest.addFromDown(t);
        System.out.println("La couche " + nomCouche + " a envoyé la trame " + Byte.toUnsignedInt(t.getNumTrameHamming())
                + " à la station " + numDest + ".");
    }
}

/* 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */
