package main;

import echanges.Trame;

public class TamponCirculaire {
    class CirculaireFormat {
        Trame trame = null;
        long timeAdded = 0;
    }

    private CirculaireFormat tampon[];
    private int front;
    private int rear;
    private int nbElems;

    public TamponCirculaire(int size) {
        tampon = new CirculaireFormat[size];
        for (int i = 0; i < size; i++) {
            tampon[i] = new CirculaireFormat();
        }
        front = 0;
        rear = 0;
        nbElems = 0;
    }

    public synchronized boolean add(Trame value) {
        if (!isFull()) {
            nbElems++;
            rear = (rear + 1) % tampon.length;
            tampon[rear].trame = value;
            tampon[rear].timeAdded = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public synchronized Trame poll() {
        if (!isEmpty()) {
            nbElems--;
            front = (front + 1) % tampon.length;
            Trame t = tampon[front].trame;
            tampon[front].trame = null;
            tampon[front].timeAdded = 0;
            return t;
        }
        return null;
    }

    public long getLastAddedTime() {
        if (!isEmpty()) {
            return tampon[(front + 1) % tampon.length].timeAdded;
        }
        return 0;
    }

    public boolean isFull() {
        return nbElems == tampon.length;
    }

    public synchronized boolean isEmpty() {
        return nbElems == 0;
    }
}
