package main;

import java.util.HashMap;

public class TP1Main {

    public static void main(String[] args) {
        // Liste des arguments en ordre.
        // Path pour lire le fichier à transmettre.
        // Path pour écrire le fichier à transmettre.
        // Path pour lire les erreurs de transmissions.
        // Paramètre des erreurs de transmissions.
        String pathConfig = "FichiersTxt/Config.txt";

        HashMap<String, String> config = Configurations.getConfigs(pathConfig);

        int delai = Integer.parseInt(config.get("delais0"));
        int tailleTamponC = Integer.parseInt(config.get("TailleTamponC"));
        int taileTamponLLC = Integer.parseInt(config.get("TailleTamponLLC"));
        boolean hamming = Integer.parseInt(config.get("Hamming")) == 1;
        String pathLecture = config.get("PathLecture");
        String pathEcriture = config.get("PathEcriture");
        String pathErreurs = "FichiersTxt/ErreursTransmission.txt";

        Transmission transmission = new Transmission(tailleTamponC, "Support de transmission");
        transmission.setConfigs(config);

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