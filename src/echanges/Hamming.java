// Hamming.java
// Description: Classe en charge d'ajouter les bits de contrôle de Hamming, de
// les retirer, de valider une trame selon ses bits de contrôle ou de la
// corriger.

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

package echanges;

public class Hamming
{

    public static void addHamming(Trame trame)
    {
        // Décaler les positions dans la trame.
        Octet[] octDataAvant = trame.getData();
        int[] iSans = octToInt(octDataAvant);
        int[] iAvec = formatInt(iSans);
        int[] cBit = calculBitControl(iAvec);

        iAvec[0] = (cBit[0] % 2) == 0 ? 0 : 1;
        iAvec[1] = (cBit[1] % 2) == 0 ? 0 : 1;
        iAvec[3] = (cBit[2] % 2) == 0 ? 0 : 1;
        iAvec[7] = (cBit[3] % 2) == 0 ? 0 : 1;
        iAvec[15] = (cBit[4] % 2) == 0 ? 0 : 1;
        iAvec[31] = (cBit[5] % 2) == 0 ? 0 : 1;

        Octet[] octDataApres = intToOct(iAvec);

        trame.setData(octDataApres);
    }

    // ************************************************
    // JUSTE LE TEMPS DE DEBUG
    // ************************************************
    // public static String intToStr(int[] iPrint)
    // {
    // String str = new String();
    // for (int i = 0; i < iPrint.length; i++)
    // {
    // if ((i % 8) == 0)
    // {
    // str += " ";
    // }
    // str += iPrint[i];
    // }
    // return str;
    // }

    public static void retireHamming(Trame trame)
    {
        // Le tableau d'octets qui contient Hamming.
        Octet[] oAvec = trame.getData();
        // Le vecteur d'entier qui contient Hamming.
        int[] iAvec = octToInt(oAvec);
        // Le vecteur d'entier qui ne contient pas Hamming.
        int[] iSans = unformatInt(iAvec);
        // Le tableau d'octets qui ne contient pas Hamming.
        Octet[] oSans = intToOct(iSans);
        // La nouvelle trame sans hamming.
        trame.setData(oSans);
    }

    public static boolean valideHamming(Trame trame)
    {
        // Tableau d'octets qui contient Hamming.
        Octet[] oAvec = trame.getData();
        // Vecteur d'entiers qui contient Hamming.
        int[] iAvec = octToInt(oAvec);
        // Les bits de contrôle de Hamming.
        int[] cBit = calculBitControl(iAvec);

        for (int i = 0; i < cBit.length; i++)
        {
            if ((cBit[i] % 2) != 0)
            {
                return false;
            }
        }
        return true;
    }

    public static void corrigerTrame(Trame trame)
    {
        if (valideHamming(trame))
        {
            return;
        }
        // Objets qui contiennent Hamming et l'erreur.
        Octet[] oAvec = trame.getData();
        int[] iAvec = octToInt(oAvec);
        // Valeurs des sommes de contrôle (incluant celles-ci).
        int[] cBitAvant = calculBitControl(iAvec);
        int[] cBitActuel = new int[cBitAvant.length];
        for (int i = 0; i < cBitAvant.length; i++)
        {
            // Soustrait la valeur du bit à la somme.
            cBitAvant[i] -= iAvec[((int) Math.pow(2, i)) - 1];
            // Obtient les bits de contrôle tels qu'ils sont dans la trame.
            cBitActuel[i] = iAvec[((int) Math.pow(2, i)) - 1];
        }
        // Syndrome de l'erreur de la trame.
        int[] cBitApres = new int[cBitAvant.length];

        for (int i = 0; i < cBitAvant.length; i++)
        {
            if ((cBitAvant[i] % 2) == 0)
            {
                cBitApres[i] = cBitActuel[i] == 0 ? 0 : 1;
            }
            else
            {
                cBitApres[i] = cBitActuel[i] == 1 ? 0 : 1;
            }
        }
        // Le bit qui doit être inversé.
        int posErr = 0;
        for (int i = 0; i < cBitApres.length; i++)
        {
            posErr += (cBitApres[i] * Math.pow(2, i));
        }
        // Car en base 0 contrairement à Hamming.
        posErr--;
        // Changer le bit en erreur.
        iAvec[posErr] = (iAvec[posErr] == 1 ? 0 : 1);
        Octet[] oFinal = intToOct(iAvec);
        trame.setData(oFinal);
    }

    /*
     * Prend les caractères de la trame originale et les espaces pour permettre
     * d'y insérer les bits de contrôle.
     */
    public static int[] formatInt(int[] iSans)
    {
        int[] iAvec = new int[48];
        int offset = 0;
        int position = 0;

        for (int i = 0; i < iAvec.length; i++)
        {
            // Si c'est une puissance de 2.
            if ((i + 1) == Math.pow(2, offset))
            {
                iAvec[i] = 0;
                offset++;
            }
            else
            {
                iAvec[i] = iSans[position];
                position++;
            }
        }
        return iAvec;
    }

    /*
     * Prend les caractères de la trame originale et retire les bits de
     * contrôle.
     */
    public static int[] unformatInt(int[] iAvec)
    {
        int[] iSans = new int[48];
        int offset = 0;
        int position = 0;

        for (int i = 0; i < (iSans.length); i++)
        {
            // Si c'est une puissance de 2.
            if ((i + 1) == Math.pow(2, offset))
            {
                offset++;
            }
            else
            {
                iSans[position] = iAvec[i];
                position++;
            }
        }
        return iSans;
    }

    public static int[] calculBitControl(int[] iAvec)
    {
        int[] cBit = { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < iAvec.length; i++)
        {
            // Bit de contrôle de 2^0.
            if (((i + 1) % 2) > 0)
            {
                cBit[0] += iAvec[i];
            }
            // Bit de contrôle de 2^1.
            if (((i + 1) % 4) > 1)
            {
                cBit[1] += iAvec[i];
            }
            // Bit de contrôle de 2^2.
            if (((i + 1) % 8) > 3)
            {
                cBit[2] += iAvec[i];
            }
            // Bit de contrôle de 2^3.
            if (((i + 1) % 16) > 7)
            {
                cBit[3] += iAvec[i];
            }
            // Bit de contrôle de 2^4.
            if (((i + 1) % 32) > 15)
            {
                cBit[4] += iAvec[i];
            }
            // Bit de contrôle de 2^5.
            if (((i + 1) % 64) > 31)
            {
                cBit[5] += iAvec[i];
            }
        }
        return cBit;
    }

    /*
     * Permet de repasser du vecteur d'entiers au tableau d'octets.
     */
    public static Octet[] intToOct(int[] iAvec)
    {
        Octet[] octDataApres = new Octet[6];
        // Passer à travers tous les octets.
        for (int i = 0; i < 6; i++)
        {
            int leByte = 0;

            for (int j = 0; j < 8; j++)
            {
                leByte += iAvec[(i * 8) + j] * (int) Math.pow(2, (7 - j));
            }
            byte b = (byte) leByte;
            octDataApres[i] = new Octet(b);
        }
        return octDataApres;
    }

    /*
     * Permet de convertir un tableau d'octets en un vecteur d'entiers sans
     * aucune modification.
     */
    public static int[] octToInt(Octet[] octDataAvant)
    {
        // Temporaire.
        Trame t = new Trame(octDataAvant);
        int[] iSans = new int[48];
        for (int i = 0; i < iSans.length; i++)
        {
            iSans[i] = (octDataAvant[i / 8].getValue() >> (7 - i % 8)) & 1;
        }
        return iSans;
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
 * */
