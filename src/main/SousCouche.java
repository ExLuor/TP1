package main;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class SousCouche<T1, T2> implements Runnable {
    protected String nomCouche;
    protected Queue<T1> bufferFromUp = new ConcurrentLinkedQueue<T1>();
    protected Queue<T2> bufferFromDown = new ConcurrentLinkedQueue<T2>();
    private SousCouche<?, T1> coucheUp;
    private SousCouche<T2, ?> coucheDown;

    // A2 envoie des données à A1
    protected boolean sendToUp(T1 data) {
        if (coucheUp == null)
            return false;

        if (!coucheUp.addFromDown(data))
            return false;

        System.out.println("La couche " + nomCouche + " envoie: " + data.toString() + " vers la couche supérieure.");
        return true;
    }

    // A1 envoie des données à A2
    protected boolean sendToDown(T2 data) {
        if (coucheDown == null)
            return false;

        if (!coucheDown.addFromUp(data))
            return false;

        System.out.println("La couche " + nomCouche + " envoie: " + data.toString() + " vers la couche inférieure.");
        return true;
    }

    public boolean addFromDown(T2 data) {
        if (data != null)
            return bufferFromDown.add(data);
        return false;
    }

    public boolean addFromUp(T1 data) {
        if (data != null)
            return bufferFromUp.add(data);
        return false;
    }

    public SousCouche(String nomCouche) {
        this.nomCouche = nomCouche;
    }

    public void setCouches(SousCouche<?, T1> coucheUp, SousCouche<T2, ?> coucheDown) {
        this.coucheUp = coucheUp;
        this.coucheDown = coucheDown;
    }

    protected abstract void sendMessageToUp();

    protected abstract void sendMessageToDown();

    public void run() {
        // Pour allonger le délais s'il ne se passe rien.
        while (true) {
            sendMessageToUp();
            sendMessageToDown();
        }
    }
}
