package echanges;

/*
 * L'objet échangé entre les couches de niveau 1 et 2.
 */
public class Octet
{

    private byte value;

    public Octet()
    {
        value = 0;
    }

    public Octet(byte octet)
    {
        value = octet;
    }

    public byte getValue()
    {
        return this.value;
    }

    public void changeBit(int position, boolean bitValue)
    {
        if (bitValue)
        {
            value = (byte) (value | (1 << (position /*- 1*/)));
        }
        else
        {
            value = (byte) (value & ~(1 << (position /*- 1*/)));
        }
    }

    public int getBit(int position)
    {
        return this.value >> position & 1;
        // return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        Octet m = (Octet) obj;
        if (m == null)
        {
            return false;
        }
        return (value == m.value);
    }

    @Override
    public String toString()
    {
        return String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
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
*/