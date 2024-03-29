// IdGenerator.java
// Description: Classe qui génère un ID

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
 * Classe qui génère un ID
 *
 */
public class IdGenerator {

    private static int ID = 1;

    public synchronized static int GetID() {
        return ID++;
    }
}
