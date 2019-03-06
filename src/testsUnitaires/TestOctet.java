// TestOctet.java
// Description: Contient les tests unitaires pour la classe Octet.java.

// Auteurs:
// Boulanger, Sammy - 18 058 904
// Durand-Chorel, Michael - 17 141 086
// Leroux, Jérémie - 16 186 994

// Date de fin: 6 mars 2019
// Cours : IFT585
// Entrées du programme : -
// Sorties du programme : -

package testsUnitaires;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import echanges.Octet;

public class TestOctet
{
    byte b0;
    byte b1;
    byte b2;
    Octet o1;

    @Before
    public void setup()
    {
        b0 = 0;
        b1 = 1;
        b2 = 2;
        o1 = new Octet(b0);
    }

    @Test
    public void testIdentique()
    {
        assertEquals(o1, o1);
    }

    @Test
    public void testValue0()
    {
        Octet o = new Octet();
        assertEquals(o, o1);
    }

    @Test
    public void testNull()
    {
        Octet o = null;
        assertNotEquals(o1, o);
    }

    @Test
    public void testToString()
    {
        String str1 = o1.toString();
        String str2 = "00000000";
        assertEquals(str1, str2);
    }
}
