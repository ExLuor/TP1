// TestHamming.java
// Description: Contient les tests unitaires pour la classe Hamming.java.

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import echanges.Hamming;
import echanges.Octet;
import echanges.Trame;

public class TestHamming
{
    byte b0SansHam;
    byte b1SansHam;
    byte b2SansHam;
    byte b3SansHam;
    byte b4SansHam;
    byte b5SansHam;
    Octet[] octDataSansHam;
    Trame tSansHamming;

    byte b0AvecHam;
    byte b1AvecHam;
    byte b2AvecHam;
    byte b3AvecHam;
    byte b4AvecHam;
    byte b5AvecHam;
    Octet[] octDataAvecHam;
    Trame tAvecHamming;

    int[] iSans = { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0,
            0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    int[] iAvec = { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1,
            1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 };

    @Before
    public void setup()
    {
        byteSansHamming();
        byteAvecHamming();
        octSansHamming();
        octAvecHamming();

        tSansHamming = new Trame(octDataSansHam);
        tAvecHamming = new Trame(octDataAvecHam);
    }

    private void byteSansHamming()
    {
        // Adresse de destination.
        b0SansHam = (byte) 2;
        // Adresse source.
        b1SansHam = (byte) 1;
        // Type de la trame (Data).
        b2SansHam = (byte) 90;
        // Numéro de la trame.
        b3SansHam = (byte) 59;
        // Données.
        b4SansHam = (byte) 48;
        // Remplissage.
        b5SansHam = (byte) 0;
    }

    private void byteAvecHamming()
    {
        // Adresse de destination.

        b0AvecHam = (byte) 80;
        // Adresse source.

        b1AvecHam = (byte) 33;
        // Type de la trame (Data).

        b2AvecHam = (byte) 10;
        // Numéro de la trame.

        b3AvecHam = (byte) 209;
        // Données.

        b4AvecHam = (byte) 236;
        // Remplissage.

        b5AvecHam = (byte) 192;
    }

    private void octSansHamming()
    {
        octDataSansHam = new Octet[6];
        octDataSansHam[0] = new Octet(b0SansHam);
        octDataSansHam[1] = new Octet(b1SansHam);
        octDataSansHam[2] = new Octet(b2SansHam);
        octDataSansHam[3] = new Octet(b3SansHam);
        octDataSansHam[4] = new Octet(b4SansHam);
        octDataSansHam[5] = new Octet(b5SansHam);
    }

    private void octAvecHamming()
    {
        octDataAvecHam = new Octet[6];
        octDataAvecHam[0] = new Octet(b0AvecHam);
        octDataAvecHam[1] = new Octet(b1AvecHam);
        octDataAvecHam[2] = new Octet(b2AvecHam);
        octDataAvecHam[3] = new Octet(b3AvecHam);
        octDataAvecHam[4] = new Octet(b4AvecHam);
        octDataAvecHam[5] = new Octet(b5AvecHam);
    }

    @Test
    public void testAddHamming()
    {
        Hamming.addHamming(tSansHamming);
        assertEquals(tAvecHamming.toString(), tSansHamming.toString());
    }

    @Test
    public void testRetireHamming()
    {
        Hamming.retireHamming(tAvecHamming);
        assertEquals(tSansHamming.toString(), tAvecHamming.toString());
    }
    
    @Test
    public void testEncodeDecode() {
        String before = tSansHamming.toString();
        Hamming.addHamming(tSansHamming);
        Hamming.retireHamming(tSansHamming);
        String after = tSansHamming.toString();
        assertEquals(before, after);
    }

    @Test
    public void testOctToInt()
    {
        int[] iTest = Hamming.octToInt(octDataSansHam);
        String str1 = new String();
        String str2 = new String();
        for (int i = 0; i < iSans.length; i++)
        {
            str1 += Integer.toString(iSans[i]);
            str2 += Integer.toString(iTest[i]);
        }
        assertEquals(str1, str2);
    }

    @Test
    public void testIntToOct()
    {
        Octet[] oTest = Hamming.intToOct(iSans);
        Trame tS = new Trame(oTest);
        assertEquals(tSansHamming, tS);
    }

    @Test
    public void testFormatInt()
    {
        int[] iTest = Hamming.formatInt(iSans);
        int[] cBit = Hamming.calculBitControl(iTest);

        iTest[0] = (cBit[0] % 2) == 0 ? 0 : 1;
        iTest[1] = (cBit[1] % 2) == 0 ? 0 : 1;
        iTest[3] = (cBit[2] % 2) == 0 ? 0 : 1;
        iTest[7] = (cBit[3] % 2) == 0 ? 0 : 1;
        iTest[15] = (cBit[4] % 2) == 0 ? 0 : 1;
        iTest[31] = (cBit[5] % 2) == 0 ? 0 : 1;
        String str1 = new String();
        String str2 = new String();
        for (int i = 0; i < iAvec.length; i++)
        {
            str1 += Integer.toString(iAvec[i]);
            str2 += Integer.toString(iTest[i]);
        }
        assertEquals(str1, str2);
    }

    @Test
    public void testUnformatInt()
    {
        int[] iTest = Hamming.unformatInt(iAvec);
        String str1 = new String();
        String str2 = new String();
        for (int i = 0; i < iSans.length; i++)
        {
            str1 += Integer.toString(iSans[i]);
            str2 += Integer.toString(iTest[i]);
        }
        assertEquals(str1, str2);
    }

    @Test
    public void testTrameValide()
    {
        boolean estValide = Hamming.valideHamming(tAvecHamming);
        assertTrue(estValide);
    }

    @Test
    public void testTrameInvalide1()
    {
        boolean estValide = Hamming.valideHamming(tSansHamming);
        assertFalse(estValide);
    }

    @Test
    public void testTrameInvalide2()
    {
        Octet[] oTestAvec = Hamming.intToOct(iAvec);
        int[] iTest = Hamming.octToInt(oTestAvec);
        // Change le bit 9 (de 0 à 1).
        iTest[9] = 1;
        Octet[] oFalse = Hamming.intToOct(iTest);
        Trame tFalse = new Trame(oFalse);
        boolean estValide = Hamming.valideHamming(tFalse);
        assertFalse(estValide);
    }

    @Test
    public void testCorrigerTrame1()
    {
        Octet[] oTestAvec = Hamming.intToOct(iAvec);
        int[] iTest = Hamming.octToInt(oTestAvec);
        // Change le bit 9 (de 0 à 1).
        iTest[9] = 1;
        Octet[] oFalse = Hamming.intToOct(iTest);
        Trame tFalse = new Trame(oFalse);
        Hamming.corrigerTrame(tFalse);
        assertEquals(tAvecHamming.toString(), tFalse.toString());
    }

    @Test
    public void testCorrigerTrame2()
    {
        Octet[] oTestAvec = Hamming.intToOct(iAvec);
        int[] iTest = Hamming.octToInt(oTestAvec);
        // Change le bit 20 (de 1 à 0).
        iTest[20] = 0;
        Octet[] oFalse = Hamming.intToOct(iTest);
        Trame tFalse = new Trame(oFalse);
        Hamming.corrigerTrame(tFalse);
        assertEquals(tAvecHamming.toString(), tFalse.toString());
    }

    @Test
    public void testCorrigerTrameCorrecte()
    {
        String before = tAvecHamming.toString();
        Hamming.corrigerTrame(tAvecHamming);
        String after = tAvecHamming.toString();
        assertEquals(before, after);
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
 * */
