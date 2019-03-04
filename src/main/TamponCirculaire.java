package main;

import echanges.Trame;

public class TamponCirculaire {
    class CirculaireFormat {
        Trame trame = null;
        long timeAdded = 0;
    }

    private CirculaireFormat tampon[];
    private int head;
    private int tail;

    public TamponCirculaire(int size) {
        tampon = new CirculaireFormat[size];
        for (int i = 0; i < size; i++) {
            tampon[i] = new CirculaireFormat();
        }
        head = 0;
        tail = 0;
    }

    public boolean add(Trame value) {
        if (!isFull()) {
            tampon[tail].trame = value;
            tampon[tail].timeAdded = System.currentTimeMillis();
            head = (head + 1) % tampon.length;
            return true;
        }
        return false;
    }

    public Trame poll() {
        if (head != tail) {
            Trame value = tampon[head].trame;
            head = (head + 1) % tampon.length;
            return value;
        }
        return null;
    }

    public long getLastAddedTime() {
        if (head != tail) {
            return tampon[head].timeAdded;
        }
        return 0;
    }

    public boolean isFull() {
        return (tail + 1) % tampon.length == head;
    }

    public boolean isEmpty() {
        return tail == head;
    }
}
