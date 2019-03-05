package main;

import java.util.HashMap;
import java.util.Random;

import echanges.Octet;
import echanges.Trame;

public class Transmission implements Runnable
{
    private String nomCouche;
    private TamponCirculaire tampon;
    private final long LATENCY = 500; // en ms
    private HashMap<Integer, SousCouche<?, Trame>> couchesReceptrices;
    private HashMap<String, String> configs;

    public Transmission(int grandeurBuffer, String nomCouche)
    {
        this.nomCouche = nomCouche;
        tampon = new TamponCirculaire(grandeurBuffer);
        couchesReceptrices = new HashMap<Integer, SousCouche<?, Trame>>();
        configs = new HashMap<String, String>();
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
            Trame t = tampon.poll();
            if (t == null)
                continue;
            addErrors(t);
            sendTrame(t);
        }
    }

    public void setConfigs(HashMap<String, String> conf)
    {
        this.configs = conf;
    }

    public synchronized boolean addTrame(Trame trame)
    {
        if (tampon.add(trame))
        {
            System.out.println(
                    "La couche " + nomCouche + " a ajouté la trame " + trame.getNumTrameHamming() + " à son tampon.");
            return true;
        }
        return false;
    }

    private void addErrors(Trame t)
    {
        Random rnd = new Random();
        int e1 = (int) (Double.parseDouble(configs.get("ErrType0")) * 100);
        int e2 = (int) (Double.parseDouble(configs.get("ErrType1")) * 100);
        int e3 = (int) (Double.parseDouble(configs.get("ErrType2")) * 100);
        int seuil = rnd.nextInt(100);
        if (e1 >= seuil)
        {
            applyErrType0(t);
        }
        if (e2 >= seuil)
        {
            applyErrType1(t);
        }
        if (e3 >= seuil)
        {
            applyErrType2(t);
        }

    }

    private void applyErrType0(Trame t)
    {
        String tIni = t.toString();
        Random rnd = new Random();
        // Les données de cette trame.
        Octet[] octData = t.getData();
        // Sélection un octet au hasard.
        int posByte = rnd.nextInt(octData.length);

        byte leByte = octData[posByte].getValue();

        // Position du bit à modifier.
        int posBit = (rnd.nextInt(8) /*+ 1*/);
        // Valeur du bit.
        int bit1 = octData[posByte].getBit(posBit);
        int bit2 = -1;
        // Le bit était à 1.
        if (bit1 == 1)
        {
            // Le bit sera maintenant 0.
            octData[posByte].changeBit(posBit, false);
            bit2 = 0;
        }
        // Le bit était à 0.
        else
        {
            // Le bit sera maintenant 1.
            octData[posByte].changeBit(posBit, true);
            bit2 = 1;
        }
        t = new Trame(octData);
        printErr(t, bit1, bit2, posByte, posBit, tIni);
    }

    private void applyErrType1(Trame t)
    {
        // TODO : Si on ajoute la perte complète de la trame.
    }

    private void applyErrType2(Trame t)
    {
        // TODO : Si on ajoute l'interchange de deux trames.
    }

    // Temporaire pour les erreurs.
    private void printErr(Trame t, int bit1, int bit2, int posByte, int posBit, String tIni)
    {
        String str = new String();
        str = "********************** Application de l'errType0\n";
        str += "\tTrame initiale = " + tIni + "\n";
        str += "\tTrame finale   = " + t.toString() + "\n";
        str += "\tByte = " + posByte + "\t de valeur finale = " + t.getData()[posByte] + "\n";
        str += "\tBit = " + posBit + "\tInitial = " + bit1 + "\tFinale = " + bit2 + "\n";
        System.out.println(str);
    }

    private synchronized void sendTrame(Trame t)
    {
        int numDest = t.getDestHamming();
        SousCouche<?, Trame> dest = couchesReceptrices.get(numDest);
        if (dest == null)
        {
            return;
        }
        dest.addFromDown(t);
        System.out.println("La couche " + nomCouche + " a envoyé la trame " + t.getNumTrameHamming() + " à la station "
                + numDest + ".");
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
