// Station.java
// Description: Représente une station qui contient trois couches

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

package main;

/**
 * 
 * Représente une station qui contient trois couches
 *
 */
public class Station
{
    private SousCoucheFichier coucheFichier;
    private SousCoucheMac coucheMac;
    private SousCoucheLLC coucheLLC;
    private int ID;

    public Station(String nomFichierEntrant, String nomFichierSortant, Transmission transmission, String nomStation,
            int timeout, int buffersize, boolean hamming)
    {
        ID = IdGenerator.GetID();
        coucheFichier = new SousCoucheFichier(nomFichierEntrant, nomFichierSortant, nomStation + 1);
        coucheMac = new SousCoucheMac(nomStation + 2, ID);
        coucheLLC = new SousCoucheLLC(nomStation + 3, buffersize, timeout, transmission, hamming, ID);

        coucheFichier.setCouches(null, coucheMac);
        coucheMac.setCouches(coucheFichier, coucheLLC);
        coucheLLC.setCouches(coucheMac, null);

        transmission.addCoucheReceptrice(ID, coucheLLC);
    }

    public SousCoucheLLC getCoucheReceptrice()
    {
        return coucheLLC;
    }

    public void setStationDest(int stationID)
    {
        coucheMac.setStationDest(stationID);
    }

    public void start()
    {
        new Thread(coucheLLC).start();
        new Thread(coucheMac).start();
        new Thread(coucheFichier).start();
    }

    public int getID()
    {
        return ID;
    }
}
