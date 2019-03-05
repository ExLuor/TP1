package main;

public class Station {
    private SousCoucheFichier coucheFichier;
    private SousCoucheMac coucheMac;
    private SousCoucheLLC coucheLLC;
    private int ID;

    public Station(String nomFichierEntrant, String nomFichierSortant, Transmission transmission, String nomStation,
            int timeout, int buffersize, boolean hamming) {
        ID = IdGenerator.GetID();
        coucheFichier = new SousCoucheFichier(nomFichierEntrant, nomFichierSortant, nomStation + 1);
        coucheMac = new SousCoucheMac(nomStation + 2);
        coucheLLC = new SousCoucheLLC(nomStation + 3, buffersize, timeout, transmission, hamming, ID);

        coucheFichier.setCouches(null, coucheMac);
        coucheMac.setCouches(coucheFichier, coucheLLC);
        coucheLLC.setCouches(coucheMac, null);

        transmission.addCoucheReceptrice(ID, coucheLLC);
    }

    public SousCoucheLLC getCoucheReceptrice() {
        return coucheLLC;
    }

    public void start() {
        new Thread(coucheLLC).start();
        new Thread(coucheMac).start();
        new Thread(coucheFichier).start();
    }
}
