// SousCoucheMac.java
// Description: Représente la couche A2 (sous-couche MAC de la station
// émettrice) et la couche B2 (sous-couche MAC de la station réceptrice).

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

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
    private int stationID;
    private int stationDest;

    public SousCoucheMac(String nomCouche, int stationID)
    {
        super(nomCouche);
        this.stationID = stationID;
        this.stationDest = 0;
    }

    public void setStationDest(int stationDest)
    {
        this.stationDest = stationDest;
    }

    private Octet TrameToOctet(Trame trame)
    {
        return trame.getDonnees();
    }

    /*
     * Reçoit un octet et lui ajout l'octet du numéro de cette trame. 255
     * numéros possibles mais ne seront pas utilisés en même temps à travers les
     * buffers.
     */
    private Trame OctetToTrame(Octet octet)
    {
        Trame trame = new Trame();
        trame.setDonnees(octet);
        trame.setNumTrame(nextNumeroTrame);
        trame.setType(Trame.Type.Data);
        trame.setSender(stationID);
        trame.setDest(stationDest);
        nextNumeroTrame++;
        return trame;
    }

    /*
     * À l'aide du numéro de trame reçu, produit un message de confirmation ou
     * non de la réception d'un numéro. Quand elle dispose d'une trame ayant un
     * numéro valide, elle transmet tous les octets de cette trame 1 à la fois.
     */
    @Override
    protected void sendMessageToUp()
    {
        Trame trame = bufferFromDown.poll();

        if (trame == null)
        {
            return;
        }

        Octet octet = TrameToOctet(trame);
        sendToUp(octet);
    }

    @Override
    protected void sendMessageToDown()
    {
        Octet octet = bufferFromUp.poll();
        if (octet == null)
            return;

        Trame t = OctetToTrame(octet);
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
