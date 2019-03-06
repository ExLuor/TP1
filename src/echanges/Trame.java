/* Trame.java
 * Description: L'objet échangé entre les couches (2 et 3) et (3 ete C).
 * Auteurs: Boulanger, Sammy       - 18 058 904
 *          Durand-Chorel, Michael - 17 141 086
 *          Leroux, Jérémie        - 16 186 994
 * Date de fin: 6 mars 2019
 * Entrées du programme : -
 * Sotrties du programme : -
 *
 */

package echanges;

/**
 * L'objet échangé entre les couches (2 et 3) et (3 ete C).
 */
public class Trame
{
    // Dans une trame complète.
    // byte0 : Adresse de destination.
    // byte1 : Adresse source.
    // byte2 : Type de la trame (Data, ACK ou NAK).
    // byte3 : Numéro de trame.
    // byte4 : Données.
    // byte5 : Remplissage (après hamming).

    public enum Type
    {
        Data, ACK, NAK, Unknown
    };

    Octet[] octData;

    public Trame()
    {
        octData = new Octet[6];
        for (int i = 0; i < octData.length; i++)
        {
            octData[i] = new Octet();
        }
    }

    public Trame(Octet[] octets)
    {
        this.octData = octets;
    }

    public Trame(Trame trame)
    {
        octData = trame.octData.clone();
    }

    /**
     * Cette méthode retourne toutes les données qui se trouvent dans la trame,
     * pas seulement les données utiles aux applications.
     */
    public Octet[] getData()
    {
        return this.octData;
    }

    public void setData(Octet[] octets) {
        this.octData = octets;
    }

    public Octet getOctet(int position)
    {
        return octData[position];
    }

    /*
     * GET/SET DU BYTE DE DESTINATAIRE.
     */
    public int getDestHamming()
    {
        // Crée un clone.
        Trame tAvecHamming = new Trame(this);
        Trame tSansHamming = retireHamming(tAvecHamming);
        // Le temps que Hamming soit mis en place.
        if (tSansHamming == null)
        {
            return -1;
        }
        return Byte.toUnsignedInt(tSansHamming.octData[0].getValue());
    }

    public void setDest(int dest)
    {
        byte destination = (byte) dest;
        octData[0] = new Octet(destination);
    }

    /**
     *  GET/SET DU BYTE DE L'EXPÉDITEUR.
     */
    public int getSenderHamming()
    {
        // Crée un clone.
        Trame tAvecHamming = new Trame(this);
        Trame tSansHamming = retireHamming(tAvecHamming);
        // Le temps que Hamming soit mis en place.
        if (tSansHamming == null)
        {
            return -1;
        }
        return Byte.toUnsignedInt(tSansHamming.octData[1].getValue());
    }

    public void setSender(int senderID)
    {
        byte sender = (byte) senderID;
        octData[1] = new Octet(sender);
    }

    /*
     * GET/SET DU TYPE DE LA TRAME.
     */
    public Type getType()
    {
        if (octData[2].getValue() == (byte) 0b01011010)
        {
            return Type.Data;
        }
        else if (octData[2].getValue() == (byte) 0b10000001)
        {
            return Type.ACK;
        }
        else if (octData[2].getValue() == (byte) 0b1101101)
        {
            return Type.NAK;
        }
        return Type.Unknown;
    }

    /*
     * Combinaisons de bits qui permettent une grande distance de Hamming
     * moyenne entre les combinaisons.
     */
    public void setType(Type type)
    {
        if (type == Type.Data)
        {
            // [Data | ACK] = 6 et [Data | NAK] = 5.
            octData[2] = new Octet((byte) 0b01011010);
        }
        else if (type == Type.ACK)
        {
            // [ACK | Data] = 6 et [ACK | NAK] = 5.
            octData[2] = new Octet((byte) 0b10000001);
        }
        else if (type == Type.NAK)
        {
            // [NAK | Data] = 5 et [NAK | ACK] = 5.
            octData[2] = new Octet((byte) 0b1101101);
        }
    }

    /*
     * GET/SET DU BYTE NUMERO DE TRAME.
     */
    public byte getNumTrame()
    {
        return octData[3].getValue();
    }

    public void setNumTrame(byte numTrame)
    {
        octData[3] = new Octet(numTrame);
    }

    public byte getNumTrameHamming()
    {
        // Crée un clone.
        Trame tAvecHamming = new Trame(this);
        Trame tSansHamming = retireHamming(tAvecHamming);
        // Le temps que Hamming soit mis en place.
        if (tSansHamming == null)
        {
            return -1;
        }
        return tSansHamming.octData[3].getValue();
    }

    /*
     * GET/SET DU BYTE DONNÉES.
     */
    public Octet getDonnees()
    {
        return this.octData[4];
    }

    public void setDonnees(Octet octet)
    {
        this.octData[4] = octet;
    }

    @Override
    public boolean equals(Object obj)
    {
        Trame trame = (Trame) obj;
        // Si la trame reçue est vide.
        if (trame.octData == null)
        {
            return false;
        }
        else if (octData.length != trame.octData.length)
        {
            return false;
        }
        for (int i = 0; i < octData.length; i++)
        {
            if (!octData[i].equals(trame.octData[i]))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        String s = new String();

        for (Octet octet : octData)
        {
            s += octet.toString() + " ";
        }
        return s;
    }

    public static Trame ajouteHamming(Trame trame)
    {
        return null;
    }

    public static Trame retireHamming(Trame trame)
    {
        return null;
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
 * */
