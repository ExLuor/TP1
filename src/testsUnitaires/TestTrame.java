// TestTrame.java
// Description: Contient les tests unitaires pour la classe Trame.java.

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
import echanges.Trame;

public class TestTrame
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
        // Type de la trame.
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
    public void testIdentiqueTrame()
    {
        assertEquals(tSansHamming, tSansHamming);
    }

    @Test
    public void testTrameLongDiff()
    {
        Octet[] o2 = new Octet[3];
        o2[0] = new Octet(b0SansHam);
        o2[1] = new Octet(b1SansHam);
        o2[2] = new Octet(b2SansHam);
        Trame t = new Trame(o2);
        assertNotEquals(tSansHamming, t);
    }

    @Test
    public void testNull()
    {
        Trame t = new Trame();
        assertNotEquals(tSansHamming, t);
    }

    @Test
    public void testOctetsDiff()
    {
        Octet[] o2 = new Octet[6];
        o2[0] = new Octet(b0SansHam);
        o2[1] = new Octet(b0SansHam);
        o2[2] = new Octet(b2SansHam);
        o2[3] = new Octet(b3SansHam);
        o2[4] = new Octet(b4SansHam);
        o2[5] = new Octet(b5SansHam);
        Trame t = new Trame(o2);
        assertNotEquals(tSansHamming, t);
    }

    @Test
    public void testToString()
    {
        String str1 = "00000010 00000001 01011010 00111011 00110000 00000000 ";
        String str2 = tSansHamming.toString();
        assertEquals(str1, str2);
    }

    @Test
    public void testCloneTrame()
    {
        Trame t = new Trame(tSansHamming);
        assertEquals(tSansHamming, t);
    }

    @Test
    public void testGetData()
    {
        Octet[] o2 = tSansHamming.getData();
        String str1 = new String();
        String str2 = new String();
        for (int i = 0; i < o2.length; i++)
        {
            str1 += o2[i].toString();
            str2 += octDataSansHam[i].toString();
        }
        assertEquals(str1, str2);
    }

    @Test
    public void testGetOctet()
    {
        Octet o2 = tSansHamming.getOctet(0);
        Octet o3 = new Octet(b0SansHam);
        assertEquals(o2, o3);
    }

    @Test
    public void testSetData()
    {
        Trame t = new Trame();
        t.setData(octDataSansHam);
        assertEquals(tSansHamming, t);
    }

    @Test
    public void testGetDestAvecHam()
    {
        int destOriginal = (int) b0SansHam;
        int destTestee = tAvecHamming.getDestHamming();
        assertEquals(destOriginal, destTestee);
    }

    @Test
    public void testGetDestSansHam()
    {
        int destOriginal = (int) b0SansHam;
        int destTestee = tSansHamming.getDest();
        assertEquals(destOriginal, destTestee);
    }

    @Test
    public void testSetDest()
    {
        Trame t = new Trame(tSansHamming);
        t.setDest(45);
        int destSupp = 45;
        int destReel = t.getDest();
        assertEquals(destSupp, destReel);
    }

    @Test
    public void testGetSendAvecHam()
    {
        int sendOriginal = (int) b1SansHam;
        int sendTestee = tAvecHamming.getSenderHamming();
        assertEquals(sendOriginal, sendTestee);
    }

    @Test
    public void testGetSendSansHam()
    {
        int sendOriginal = (int) b1SansHam;
        int sendTestee = tSansHamming.getSender();
        assertEquals(sendOriginal, sendTestee);
    }

    @Test
    public void testSetSend()
    {
        Trame t = new Trame(tSansHamming);
        t.setSender(32);
        int sendSupp = 32;
        int sendReel = t.getSender();
        assertEquals(sendSupp, sendReel);
    }

    @Test
    public void testTypeDataHamming()
    {
        Trame t1 = new Trame();
        t1.setType(tAvecHamming.getTypeHamming());
        Trame.Type type1 = t1.getType();
        Trame.Type type2 = tSansHamming.getType();
        assertEquals(type1, type2);
    }

    @Test
    public void testTypeData()
    {
        Trame t1 = new Trame();
        Trame t2 = new Trame();
        t1.setType(Trame.Type.Data);
        t2.setType(t1.getType());
        assertEquals(t1, t2);
    }

    @Test
    public void testTypeACK()
    {
        Trame t1 = new Trame();
        Trame t2 = new Trame();
        t1.setType(Trame.Type.ACK);
        t2.setType(t1.getType());
        assertEquals(t1, t2);
    }

    @Test
    public void testTypeNAK()
    {
        Trame t1 = new Trame();
        Trame t2 = new Trame();
        t1.setType(Trame.Type.NAK);
        t2.setType(t1.getType());
        assertEquals(t1, t2);
    }

    @Test
    public void testGetNumTrameHamming()
    {
        int numSupp = 59;
        int numReel = (int) tAvecHamming.getNumTrameHamming();
        assertEquals(numSupp, numReel);
    }

    @Test
    public void testGetNumTrame()
    {
        int numSupp = 59;
        int numReel = (int) tSansHamming.getNumTrame();
        assertEquals(numSupp, numReel);
    }

    @Test
    public void testSetNumTrame()
    {
        Trame t = new Trame(tSansHamming);
        t.setNumTrame((byte) 25);
        int numSupp = 25;
        int numReel = (int) t.getNumTrame();
        assertEquals(numSupp, numReel);
    }

}