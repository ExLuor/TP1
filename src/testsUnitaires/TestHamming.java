package testsUnitaires;

import org.junit.Before;
import org.junit.Test;

import echanges.Hamming;
import echanges.Octet;
import echanges.Trame;

public class TestHamming
{
    byte b0;
    byte b1;
    byte b2;
    byte b3;
    byte b4;
    byte b5;

    Octet[] octData;

    Hamming ham;

    Trame tSansHamming;

    @Before
    public void setup()
    {

        b0 = 2;     // Adresse de destination.
        b1 = 1;     // Adresse source.
        b2 = 90;    // Type de la trame (Data).
        b3 = 59;    // Numéro de la trame.
        b4 = 48;    // Données.
        b5 = 0;     // Remplissage.

        octData = new Octet[6];
        octData[0] = new Octet(b0);
        octData[1] = new Octet(b1);
        octData[2] = new Octet(b2);
        octData[3] = new Octet(b3);
        octData[4] = new Octet(b4);
        octData[5] = new Octet(b5);

        tSansHamming = new Trame(octData);

        ham = new Hamming();
    }

    @Test
    public void testAddHamming()
    {
        ham.addHamming(tSansHamming);
    }

    // @Test
    // public void test()
    // {
    // fail("Not yet implemented");
    // }

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
 * */
