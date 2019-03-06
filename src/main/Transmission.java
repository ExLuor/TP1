// Transmission.java
// Description: Classe du support de transmission qui connecte A3 à B3

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

package main;

import echanges.Octet;
import echanges.Trame;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * Classe du support de transmission qui connecte A3 à B3
 *
 */
public class Transmission implements Runnable
{
    private String nomCouche;
    private TamponCirculaire tampon;
    private final long LATENCY = 500; // en ms
    private HashMap<Integer, SousCouche<?, Trame>> couchesReceptrices;
    private int freqErreur;

    public Transmission(int grandeurBuffer, String nomCouche, int freqErreur)
    {
        this.nomCouche = nomCouche;
        tampon = new TamponCirculaire(grandeurBuffer);
        couchesReceptrices = new HashMap<Integer, SousCouche<?, Trame>>();
        this.freqErreur = freqErreur;
    }

    public synchronized void addCoucheReceptrice(int stationID, SousCouche<?, Trame> couche)
    {
        couchesReceptrices.put(stationID, couche);
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (tampon.isEmpty())
            {
                continue;
            }

            long timeToWait = LATENCY - (System.currentTimeMillis() - tampon.getLastAddedTime());
            if (timeToWait > 0)
            {
                continue;
            }
            Trame trame = tampon.poll();
            if (trame == null)
                continue;
            int numDest = trame.getDestHamming();
            addError(trame);
            sendTrame(trame, numDest);
        }
    }

    public synchronized boolean addTrame(Trame trame)
    {
        if (tampon.add(trame))
        {
            System.out.println("La couche " + nomCouche + " a ajouté la trame "
                    + Byte.toUnsignedInt(trame.getNumTrameHamming()) + " à son tampon.");
            return true;
        }
        return false;
    }

    private void addError(Trame trame) {
        if (freqErreur == 0)
        {
            return;
        }
        Random rnd = new Random();
        int luckyNumber = rnd.nextInt(freqErreur);
        if (luckyNumber == 0) {
            modifierBit(trame);
            System.out.println("Ajout d'une erreur dans la trame " + Byte.toUnsignedInt(trame.getNumTrameHamming())
                    + " en modifiant un bit.");
        }
    }

    private void modifierBit(Trame trame) {
        Random rnd = new Random();
        int maxErreurs = trame.getData().length / 2; // On ne veut pas trop en
                                                     // mettre quand même
        Octet[] octData = trame.getData(); // Les données de cette trame.

        // Sélection un octet au hasard.
        int posByte = rnd.nextInt(octData.length);
        // Position du bit à modifier.
        int posBit = rnd.nextInt(8);
        // Valeur du bit.
        int bit = octData[posByte].getBit(posBit);
        // On alterne la valeur du bit
        octData[posByte].changeBit(posBit, !(bit == 1));

        trame.setData(octData);
    }

    private synchronized void sendTrame(Trame trame, int numDest) {
        SousCouche<?, Trame> dest = couchesReceptrices.get(numDest);
        if (dest == null)
        {
            return;
        }
        dest.addFromDown(trame);
        System.out.println("La couche " + nomCouche + " a envoyé la trame "
                + Byte.toUnsignedInt(trame.getNumTrameHamming()) + " à la station " + numDest + ".");
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
