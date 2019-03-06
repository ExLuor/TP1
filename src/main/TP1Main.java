// TP1Main.java
// Description: Classe principale du programme

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

package main;

import java.util.HashMap;

/**
 * 
 * Classe principale du programme
 *
 */
public class TP1Main
{
    public static void main(String[] args)
    {
        String pathConfig = "FichiersTxt/Config.txt";

        HashMap<String, String> config = Configurations.getConfigs(pathConfig);

        int delai = Integer.parseInt(config.get("delais0"));
        int tailleTamponC = Integer.parseInt(config.get("TailleTamponC"));
        int taileTamponLLC = Integer.parseInt(config.get("TailleTamponLLC"));
        boolean hamming = Integer.parseInt(config.get("Hamming")) == 1;
        String pathLecture = config.get("PathLecture");
        String pathEcriture = config.get("PathEcriture");
        int freqErreurs = Integer.parseInt(config.get("freqErreurs"));

        Transmission transmission = new Transmission(tailleTamponC, "Support de transmission", freqErreurs);

        Station stationA = new Station(pathLecture, "", transmission, "A", delai, taileTamponLLC, hamming);
        Station stationB = new Station("", pathEcriture, transmission, "B", delai, taileTamponLLC, hamming);

        stationA.setStationDest(stationB.getID());
        stationB.setStationDest(stationA.getID());

        new Thread(transmission).start();
        stationB.start();
        stationA.start();

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
 * 
 * 
 * 
 */