package echanges;

/*
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
        Data, ACK, NAK
    };

    Octet[] octData;

    public Trame()
    {
        octData = null;
    }

    public Trame(Octet[] o)
    {
        this.octData = o;
    }

    public Trame(Trame t)
    {
        octData = t.octData.clone();
    }

    /**
     * Cette méthode retourne toutes les données qui se trouvent dans la trame,
     * pas seulement les données utiles aux applications.
     */
    public Octet[] getData()
    {
        return this.octData;
    }

    @Override
    public boolean equals(Object obj)
    {
        Trame t = (Trame) obj;
        // Si la trame reçue est vide.
        if (t.octData == null)
        {
            return false;
        }
        else if (octData.length != t.octData.length)
        {
            return false;
        }
        for (int i = 0; i < octData.length; i++)
        {
            if (!octData[i].equals(t.octData[i]))
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

        for (Octet o : octData)
        {
            s += o.toString();
        }
        return s;
    }

    public int getNumTrame()
    {     
        return Byte.toUnsignedInt(octData[3].value);

    }
    
    public int getNumTrameHamming()
    {
     // Crée un clone.
        Trame tAvecHamming = new Trame(this);
        Trame tSansHamming = retireHamming(tAvecHamming);
        // Le temps que Hamming soit mis en place.
        if (tSansHamming == null)
        {
            return -1;
        }
        return Byte.toUnsignedInt(octData[3].value);
    }

    public Type getType()
    {
        if (octData[2].value == (byte) 0b01011010)
        {
            return Type.Data;
        }
        else if (octData[2].value == (byte) 0b10000001)
        {
            return Type.ACK;
        }
        else if (octData[2].value == (byte) 0b1101101)
        {
            return Type.NAK;
        }
        return null;
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
        return Byte.toUnsignedInt(octData[0].value);
    }

    public void setDest(int dest)
    {
        byte b = (byte) dest;
        octData[0] = new Octet(b);
    }

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
        return Byte.toUnsignedInt(octData[1].value);
    }

    public void setSender(int sender)
    {
        byte b = (byte) sender;
        octData[1] = new Octet(b);
    }

    public static Trame ajouteHamming(Trame t)
    {
        return null;
    }

    public static Trame retireHamming(Trame t)
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
