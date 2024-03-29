// SousCoucheLLC.java
// Description: Représente la couche A3 (sous-couche LLC de la station
// émettrice) et la couche B3 (sous-couche LLC de la station réceptrice).

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

package main;

import java.util.HashSet;

import echanges.Hamming;
import echanges.Trame;
import echanges.Trame.Type;

/*
 * Représente la couche A3 (sous-couche LLC de la station émettrice) et la
 * couche B3 (sous-couche LLC de la station réceptrice).
 */
public class SousCoucheLLC extends SousCouche<Trame, Trame> {
    private Transmission transmission;
    private TamponLLC LLC_In;
    private TamponLLC LLC_Out;
    private HashSet<Byte> NAK_History;
    private byte pos;
    private boolean corrigerHamming;
    private int ID;

    public SousCoucheLLC(String nomCouche, int grandeurTampon, int timeout, Transmission transmission,
            boolean corrigerHamming, int stationID) {
        super(nomCouche);
        this.transmission = transmission;
        LLC_In = new TamponLLC(grandeurTampon, timeout);
        LLC_Out = new TamponLLC(grandeurTampon, timeout);
        pos = 0;
        NAK_History = new HashSet<Byte>();
        this.corrigerHamming = corrigerHamming;
        ID = stationID;
    }

    private void addInfosToTrame(Trame trame) {
        Hamming.addHamming(trame);
    }

    private void removeInfosToTrame(Trame trame) {
        Hamming.retireHamming(trame);
    }

    private boolean verifierTrameValide(Trame trame) {
        return Hamming.valideHamming(trame);
    }

    private void corrigerTrame(Trame trame) {
        Hamming.corrigerTrame(trame);
    }

    @Override
    public boolean addFromDown(Trame trame) {
        if (LLC_In.isFull())
            return false;
        if (LLC_In.size() - LLC_In.getNbElements() == 1 && trame.getNumTrameHamming() != pos)
            return false;
        if (isTrameOldHamming(trame) && trame.getTypeHamming() == Type.Data) {
            return false;
        }
        return LLC_In.addTrame(trame);
    }

    @Override
    protected void sendMessageToUp() {
        Trame trame = LLC_In.getNextInvalidTrame();
        if (trame == null)
            return;

        if (!verifierTrameValide(trame)) {
            if (corrigerHamming) {
                System.out.println(
                        "La station " + nomCouche + " a reçu une trame endommagée et la corrige " + trame.toString());
                corrigerTrame(trame);
                System.out.println(
                        "La station " + nomCouche + " a recu une trame endommagée et l'a corrigee " + trame.toString());
            } else {
                System.out.println("La station " + nomCouche + " a reçu une trame invalide et la rejette.");
                LLC_In.remove(trame);
                return;
            }
        }

        int sender = trame.getSenderHamming();
        removeInfosToTrame(trame);
        byte numTrame = trame.getNumTrame();

        switch (trame.getType()) {
        case Data:
            receiveData(numTrame, sender, trame);
            break;
        case ACK:
            receiveACK(numTrame, trame);
            break;
        case NAK:
            receiveNAK(numTrame, trame);
            break;
        default:
            break;
        }
    }

    private void receiveData(byte numTrame, int sender, Trame trame) {
        if (isTrameOld(trame)) {
            LLC_In.remove(trame);
            return;
        }

        if (pos == numTrame) {
            sendTrames();
            sendACK((byte) (pos - 1), sender);
            System.out.println("La station " + nomCouche + " envoie un ACK " + Byte.toUnsignedInt((byte) (pos - 1))
                    + " car la trame a bien été reçue.");
            return;
        }

        if (LLC_In.alreadyExist(numTrame)) {
            LLC_In.remove(trame);
        }

        if (NAK_History.contains(pos)) {
            sendACK((byte) (pos - 1), sender);
            System.out.println("La station " + nomCouche + " envoie un ACK " + Byte.toUnsignedInt((byte) (pos - 1))
                    + " car la station attend toujours la trame " + Byte.toUnsignedInt(pos) + ".");
        } else {
            System.out.println("La station " + nomCouche + " envoie un NAK " + Byte.toUnsignedInt(pos)
                    + " car la trame n'a jamais été reçue.");
            sendNAK(pos, sender);

            NAK_History.add(pos);
        }
    }

    private void receiveACK(byte numTrame, Trame trame) {
        removeTrames(numTrame);
        LLC_In.remove(trame);
        System.out.println("La station " + nomCouche + " a reçu un ACK " + Byte.toUnsignedInt(numTrame)
                + " et retire la trame correspondante.");
    }

    private void receiveNAK(byte numTrame, Trame trame) {
        LLC_Out.resetTrame(numTrame);
        LLC_In.remove(trame);
        System.out.println("La station " + nomCouche + " a reçu un NAK " + Byte.toUnsignedInt(numTrame)
                + " et se prépare à envoyer la trame à nouveau.");
    }

    private boolean isTrameOld(Trame trame) {
        for (byte i = pos; i < pos + LLC_In.size(); i++) {
            if (trame.getNumTrame() == i) {
                return false;
            }
        }
        return true;
    }

    private boolean isTrameOldHamming(Trame trame) {
        for (byte i = pos; i < pos + LLC_In.size(); i++) {
            if (trame.getNumTrameHamming() == i) {
                return false;
            }
        }
        return true;
    }

    private Trame createNAKTrame(byte numTrame, int destinataire) {
        Trame trame = new Trame();
        trame.setDest(destinataire);
        trame.setSender(ID);
        trame.setNumTrame(numTrame);
        trame.setType(Trame.Type.NAK);
        return trame;
    }

    private void sendNAK(byte numTrame, int destinataire) {
        Trame trame = createNAKTrame(numTrame, destinataire);
        bufferFromUp.add(trame);
    }

    private Trame createACKTrame(byte numTrame, int destinataire) {
        Trame trame = new Trame();
        trame.setDest(destinataire);
        trame.setSender(ID);
        trame.setNumTrame(numTrame);
        trame.setType(Trame.Type.ACK);
        return trame;
    }

    private void sendACK(byte numTrame, int destinataire) {
        Trame trame = createACKTrame(numTrame, destinataire);
        bufferFromUp.add(trame);
    }

    private void sendTrames() {
        for (int i = 0; i < LLC_In.size(); i++, pos++) {
            Trame trame = LLC_In.getTrame(pos);
            if (trame == null) {
                return;
            }
            sendToUp(trame);
            LLC_In.removeTrame(pos);
            NAK_History.remove(pos);
        }
    }

    private void removeTrames(byte numTrame) {
        int maxTrames = LLC_Out.size() - 1;
        byte first = (byte) (numTrame - (byte) maxTrames);

        for (int i = 0; i < LLC_Out.size(); i++) {
            LLC_Out.removeTrameHamming((byte) (first + i));
        }
    }

    @Override
    protected void sendMessageToDown() {
        Trame trameIn = bufferFromUp.peek();

        if (!LLC_Out.isFull() && trameIn != null) {
            bufferFromUp.poll();
            addInfosToTrame(trameIn);
            LLC_Out.addTrame(trameIn);
        }

        if (!LLC_Out.isEmpty()) {
            Trame trameOut = LLC_Out.getNextTrame();
            if (trameOut != null && transmission.addTrame(trameOut)) {
                if (trameOut.getTypeHamming() == Type.Data) {
                    LLC_Out.sendTrame(trameOut);
                    System.out.println("La station " + nomCouche + " envoie la trame "
                            + Byte.toUnsignedInt(trameOut.getNumTrameHamming()) + " au support de transmission.");
                } else {
                    LLC_Out.remove(trameOut);
                }
            }
        }

    }
}
