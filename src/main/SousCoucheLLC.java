package main;

import java.util.HashSet;

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
        // TODO: Ajouter les infos manquante à la trame avant de l'envoyer à la
        // couche C
        // TODO: Ajouter Hamming
    }

    private void removeInfosToTrame(Trame trame) {
        // TODO: Enlever les infos rajouter à la trame
    }

    private boolean verifierTrameValide(Trame trame) {
        // TODO: Vérifier si la trame est valide
        return true;
    }

    private void corrigerTrame(Trame trame) {
        // TODO: Corriger la trame avec Hamming
    }

    @Override
    public boolean addFromDown(Trame t) {
        if (LLC_In.isFull())
            return false;
        return LLC_In.addTrame(t);
    }

    @Override
    protected void sendMessageToUp() {
        Trame t = LLC_In.getNextInvalidTrame();
        if (t == null)
            return;

        if (!verifierTrameValide(t)) {
            if (corrigerHamming) {
                corrigerTrame(t);
            } else {
                System.out.println("La station " + nomCouche + " a reçu une trame invalide et la rejette.");
                LLC_In.remove(t);
                return;
            }
        }

        int sender = t.getSenderHamming();
        removeInfosToTrame(t);
        int numTrame = t.getNumTrame();

        switch (t.getType()) {
        case Data:
            if (LLC_In.alreadyExist(numTrame)) {
                System.out.println(
                        "La station " + nomCouche + " envoie un ACK " + numTrame + " car la trame a déjà été reçue.");
                sendACK(numTrame, sender);
                LLC_In.remove(t);
                return;
            }

            if (pos == numTrame) {
                if (NAK_History.contains(pos)) {
                    sendTrames();
                } else {
                    sendTrame();
                }
                sendACK(pos - 1, sender);
                System.out.println(
                        "La station " + nomCouche + " envoie un ACK " + (pos - 1) + " car la trame a bien été reçue.");
                return;
            }

            if (numTrame < pos)
                return;

            if (NAK_History.contains(pos)) {
                sendACK(pos - 1, sender);
                System.out.println("La station " + nomCouche + " envoie un ACK " + (pos - 1)
                        + " car la station attend toujours la trame " + pos + ".");
            } else {
                System.out.println(
                        "La station " + nomCouche + " envoie un NAK " + (pos) + " car la trame n'a jamais été reçue.");
                sendNAK(pos, sender);
                NAK_History.add(pos);
            }
            break;

        case ACK:
            removeTrames(numTrame);
            LLC_In.remove(t);
            System.out.println(
                    "La station " + nomCouche + " a reçu un ACK " + numTrame + " et retire la trame correspondante.");
            break;

        case NAK:
            LLC_Out.resetTrame(numTrame);
            LLC_In.remove(t);
            System.out.println("La station " + nomCouche + " a reçu un NAK " + numTrame
                    + " et se prépare à envoyer la trame à nouveau.");
            break;
        }
    }

    private Trame createNAKTrame(int numTrame, int destinataire) {
        Trame t = new Trame();
        t.setDest(destinataire);
        t.setSender(ID);
        t.setNumTrame(numTrame);
        t.setType(Trame.Type.NAK);
        return t;
    }

    private void sendNAK(int numTrame, int destinataire) {
        Trame t = createNAKTrame(numTrame, destinataire);
        addInfosToTrame(t);
        bufferFromUp.add(t);
    }

    private Trame createACKTrame(int numTrame, int destinataire) {
        Trame t = new Trame();
        t.setDest(destinataire);
        t.setSender(ID);
        t.setNumTrame(numTrame);
        t.setType(Trame.Type.ACK);
        return t;
    }

    private void sendACK(int numTrame, int destinataire) {
        Trame t = createACKTrame(numTrame, destinataire);
        addInfosToTrame(t);
        bufferFromUp.add(t);
    }

    private void sendTrames() {
        byte i;
        for (i = pos; i < pos + LLC_In.size(); i++) {
            Trame t = LLC_In.getTrame(i);
            if (t == null) {
                break;
            }
            sendToUp(t);
            LLC_In.removeTrame(i);
            NAK_History.remove(i);
        }
        pos = i;
    }

    private void sendTrame() {
        Trame t = LLC_In.getTrame(pos);
        sendToUp(t);
        LLC_In.removeTrame(pos);
        NAK_History.remove(pos);
        pos++;
    }

    private void removeTrames(int numTrame) {
        int maxTrames = LLC_Out.size() - 1;
        byte first = (byte) ((byte) numTrame - (byte) maxTrames);

        for (byte i = first; i <= numTrame; i++) {
            LLC_Out.removeTrame(i);
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
                if (trameOut.getType() == Type.Data) {
                    LLC_Out.sendTrame(trameOut);
                    System.out.println("La station " + nomCouche + " envoie la trame " + trameOut.getNumTrameHamming()
                            + " au support de transmission.");
                } else {
                    LLC_Out.remove(trameOut);
                }
            }
        }

    }
}
