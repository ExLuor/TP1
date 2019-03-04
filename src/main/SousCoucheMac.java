package main;

import echanges.Octet;
import echanges.Trame;

/*
 * Représente la couche A2 (sous-couche MAC de la station émettrice) et la
 * couche B2 (sous-couche MAC de la station réceptrice).
 */

public class SousCoucheMac extends SousCouche<Octet, Trame>
{

    // Le prochain numéro de trame à utiliser.
    private byte nextNumeroTrame = 0;

    public SousCoucheMac(String nomCouche)
    {
        super(nomCouche);
    }

    protected char[] fromDecToByte(int numTrame)
    {
        String str = new String();
        for (int i = 7; i >= 0; i--)
        {
            if (numTrame >= Math.pow(2, i))
            {
                str = str + "1";
                numTrame = numTrame - (int) Math.pow(2, i);
            }
            else
            {
                str = str + "0";
            }
        }
        char[] num = str.toCharArray();
        return num;
    }

    private Octet[] TrameToOctets(Trame t)
    {
        return t.getData();
    }

    /*
     * Reçoit un octet et lui ajout l'octet du numéro de cette trame. 255
     * numéros possibles mais ne seront pas utilisés en même temps à travers les
     * buffers.
     */
    private Trame OctetToTrame(Octet o)
    {
//        Octet[] octData = new Octet[2];

        Trame t = new Trame();
        
        t.setDonnees(o);
        t.setNumTrame(nextNumeroTrame);
        // Le numéro de cette trame.
//        octData[0] = new Octet(nextNumeroTrame);
        nextNumeroTrame++;
        // L'octet des données.
//        octData[1] = o;
//        return new Trame(octData);
        return t;
    }

    /*
     * À l'aide du numéro de trame reçu, produit un message de confirmation ou
     * non de la réception d'un numéro. Quand elle dispose d'une trame ayant un
     * numéro valide, elle transmet tous les octets de cette trame 1 à la fois.
     */
    @Override
    protected void sendMessageToUp()
    {
        Trame t = bufferFromDown.poll();

        if (t == null)
        {
            return;
        }

        Octet[] octets = TrameToOctets(t);

        for (Octet o : octets)
        {
            sendToUp(o);
        }
    }

    @Override
    protected void sendMessageToDown()
    {
        // TODO supporter les trames avec plusieurs octets
        Octet o = bufferFromUp.poll();
        if (o == null)
            return;

		Trame t = OctetToTrame(o);
		sendToDown(t);
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
 *
 *
 *
 *
 *
 */
