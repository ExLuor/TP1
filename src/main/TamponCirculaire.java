// TamponCirculaire.java
// Description: Tampon circulaire utilisé par le support de transmission

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

package main;

import echanges.Trame;

/**
 * Tampon circulaire utilisé par le support de transmission
 */
public class TamponCirculaire
{
    class CirculaireFormat
    {
        Trame trame = null;
        long timeAdded = 0;
    }

    private CirculaireFormat tampon[];
    private int front;
    private int rear;
    private int nbElems;

    public TamponCirculaire(int size)
    {
        tampon = new CirculaireFormat[size];
        for (int i = 0; i < size; i++)
        {
            tampon[i] = new CirculaireFormat();
        }
        front = 0;
        rear = 0;
        nbElems = 0;
    }

    public synchronized boolean add(Trame trame)
    {
        if (!isFull())
        {
            Trame copy = new Trame(trame);
            nbElems++;
            rear = (rear + 1) % tampon.length;
            tampon[rear].trame = copy;
            tampon[rear].timeAdded = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public synchronized Trame poll()
    {
        if (!isEmpty())
        {
            nbElems--;
            front = (front + 1) % tampon.length;
            Trame trame = tampon[front].trame;
            tampon[front].trame = null;
            tampon[front].timeAdded = 0;
            return trame;
        }
        return null;
    }

    public long getLastAddedTime()
    {
        if (!isEmpty())
        {
            return tampon[(front + 1) % tampon.length].timeAdded;
        }
        return 0;
    }

    public boolean isFull()
    {
        return nbElems == tampon.length;
    }

    public synchronized boolean isEmpty()
    {
        return nbElems == 0;
    }
}
