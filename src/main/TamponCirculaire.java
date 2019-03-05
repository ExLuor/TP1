package main;

import echanges.Trame;

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

    public synchronized boolean add(Trame value)
    {
        if (!isFull())
        {
            nbElems++;
            rear = (rear + 1) % tampon.length;
            tampon[rear].trame = value;
            tampon[rear].timeAdded = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public Trame poll()
    {
        if (!isEmpty())
        {
            nbElems--;
            front = (front + 1) % tampon.length;
            return tampon[front].trame;
        }
        return null;
    }

    public long getLastAddedTime()
    {
        if (!isEmpty())
        {
            return tampon[front].timeAdded;
        }
        return 0;
    }

    public boolean isFull()
    {
        return nbElems == tampon.length;
    }

    public boolean isEmpty()
    {
        return nbElems == 0;
    }
}
