package main;

import java.util.HashSet;

import echanges.Trame;

/*
 * Représente la couche A3 (sous-couche LLC de la station émettrice) et la
 * couche B3 (sous-couche LLC de la station réceptrice).
 */

public class SousCoucheLLC extends SousCouche<Trame, Trame> {
    private Transmission transmission;
    private TamponLLC LLC_In;
    private TamponLLC LLC_Out;
    private HashSet<Integer> NAK_History;
    private int pos;
    private boolean corrigerHamming;

    public SousCoucheLLC(String nomCouche, int grandeurTampon, int timeout, Transmission transmission,
            boolean corrigerHamming) {
        super(nomCouche);
        this.transmission = transmission;
        LLC_In = new TamponLLC(grandeurTampon, timeout);
        LLC_Out = new TamponLLC(grandeurTampon, timeout);
        pos = 1;
        NAK_History = new HashSet<Integer>();
        this.corrigerHamming = corrigerHamming;
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
        return false;
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
                System.out.println("La station " + nomCouche + "a reçu une trame invalide et la rejette.");
                LLC_In.remove(t);
                return;
            }
        }

        int sender = t.getSenderHamming();
        removeInfosToTrame(t);
        int numTrame = t.getNumTrame();

        // TODO: Rendre ça plus clean si on a le temps.. ce qu'on a pas
        switch (t.getType()) {
        case Data:
            if (LLC_In.alreadyExist(numTrame)) {
                System.out.println(
                        "La station " + nomCouche + "envoie un ACK " + numTrame + " car la trame a déjà été reçue.");
                sendACK(numTrame, sender);
                LLC_In.remove(t);
                return;
            }

            if (pos == numTrame) {
                if (NAK_History.contains(numTrame)) {
                    sendTrames();
                }
                sendACK(pos - 1, sender);
                System.out.println(
                        "La station " + nomCouche + "envoie un ACK " + (pos - 1) + " car la trame a bien été reçue.");
                return;
            }

            if (NAK_History.contains(pos)) {
                sendACK(pos - 1, sender);
                System.out.println("La station " + nomCouche + "envoie un ACK " + (pos - 1)
                        + " car la station attend toujours la trame " + pos + ".");
            } else {
                System.out.println(
                        "La station " + nomCouche + "envoie un NAK " + (pos) + " car la trame n'a jamais été reçue.");
                sendNAK(pos, sender);
                NAK_History.add(pos);
            }
            break;

        case ACK:
            removeTrames(numTrame);
            System.out.println(
                    "La station " + nomCouche + "a reçu un ACK " + numTrame + " et retire la trame correspondante.");
            break;

        case NAK:
            LLC_Out.resetTrame(numTrame);
            System.out.println("La station " + nomCouche + "a reçu un NAK " + numTrame
                    + " et se prépare à envoyer la trame à nouveau.");
            break;
        }
    }

    private Trame createNAKTrame(int numTrame, int destinataire) {
        // TODO: Créer la trame NAK
        return null;
    }

    private void sendNAK(int numTrame, int destinataire) {
        Trame t = createNAKTrame(numTrame, destinataire);
        LLC_Out.addTrame(t);
    }

    private Trame createACKTrame(int numTrame, int destinataire) {
        // TODO: Créer la trame ACK
        return null;
    }

    private void sendACK(int numTrame, int destinataire) {
        Trame t = createACKTrame(numTrame, destinataire);
        LLC_Out.addTrame(t);
    }

    private void sendTrames() {
        for (Trame t = LLC_In.getTrame(pos); t != null; pos++) {
            sendToUp(t);
            LLC_In.removeTrame(pos);
        }
    }

    private void removeTrames(int numTrame) {
        int maxTrames = LLC_Out.size();
        int first = numTrame - maxTrames;

        for (int i = first; i <= numTrame; i++) {
            LLC_Out.removeTrame(i);
        }
    }

    @Override
    protected void sendMessageToDown() {
        Trame trameIn = bufferFromUp.peek();

        if (!LLC_Out.isFull() && trameIn != null) {
            addInfosToTrame(trameIn);
            LLC_Out.addTrame(trameIn);
            bufferFromUp.poll();
        }

        if (!LLC_Out.isEmpty()) {
            Trame trameOut = LLC_Out.getNextTrame();
            if (transmission.addTrame(trameOut)) {
                LLC_Out.sendTrame(trameOut);
                System.out.println("La station " + nomCouche + " envoie la trame " + trameOut.getNumTrameHamming()
                        + " au support de transmission.");
            }
        }

    }
}
