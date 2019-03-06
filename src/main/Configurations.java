// Configurations.java
// Description: Classe pour lire le fichier de configuration
// du programme

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 * Classe pour lire le fichier de configuration du programme
 *
 */
public class Configurations
{
    // Le nom des paramètres.
    private static final String TAMPON_C = "TAMPON_C";
    private static final String TAMPON_LLC = "TAMPON_LLC";
    private static final String DELAI = "DELAI";
    private static final String PATHS = "PATHS";
    private static final String HAMMING = "HAMMING";
    private static final String ERREURS = "ERREURS";

    private static HashMap<String, String> configs = new HashMap<String, String>();

    private static String nomFichierConfig = new String();

    private static boolean isInitialized = false;

    /*
     * Récupère les configurations dans le fichier.
     */
    public static HashMap<String, String> getConfigs(String leFichier)
    {
        nomFichierConfig = leFichier;
        if (!isInitialized)
        {

            buildConfigs();
        }
        return configs;
    }

    private static void buildConfigs()
    {
        try
        {
            FileReader fr = new FileReader(nomFichierConfig);
            BufferedReader br = new BufferedReader(fr);
            String line = new String();
            line = br.readLine();

            while (line != null)
            {
                switch (line)
                {
                    case "[" + TAMPON_C + "]":
                        getThisConfig(br, TAMPON_C);
                        break;
                    case "[" + TAMPON_LLC + "]":
                        getThisConfig(br, TAMPON_LLC);
                        break;
                    case "[" + DELAI + "]":
                        getThisConfig(br, DELAI);
                        break;
                    case "[" + PATHS + "]":
                        getThisConfig(br, PATHS);
                        break;
                    case "[" + HAMMING + "]":
                        getThisConfig(br, HAMMING);
                        break;
                    case "[" + ERREURS + "]":
                        getThisConfig(br, ERREURS);
                        break;
                }
                line = br.readLine();
            }
        }
        catch (IOException ex)
        {
            System.out.println("Erreur à la lecture des configurations.");
            ex.printStackTrace();
        }
        isInitialized = true;
    }

    private static void getThisConfig(BufferedReader br, String conf)
    {
        String line = new String();
        String[] wordsArray;
        try
        {
            while (true)
            {
                line = br.readLine();
                // Séparer la lecture à la tabulation.
                wordsArray = line.split("\t");
                // Si c'est la fin de cette config.
                for (String str : wordsArray)
                {
                    if (str.equals("[/" + conf + "]"))
                    {
                        return;
                    }
                }
                configs.put(wordsArray[0], wordsArray[1]);
            }
        }
        catch (IOException ex)
        {
            System.out.println("Problème à la lecture de " + conf);
            ex.printStackTrace();
        }
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
 * */
