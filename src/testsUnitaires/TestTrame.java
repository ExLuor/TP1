package testsUnitaires;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import echanges.Octet;
import echanges.Trame;

public class TestTrame
{
    byte b1;
    byte b2;
    Octet[] o1;
    Trame t1;

    @Before
    public void setup()
    {
        b1 = (byte) 0b01010101;
        b2 = (byte) 0b00001010;
        o1 = new Octet[2];
        o1[0] = new Octet(b1);
        o1[1] = new Octet(b2);
        t1 = new Trame(o1);
    }

    @Test
    public void testIdentiqueTrame()
    {
        assertEquals(t1, t1);
    }

    @Test
    public void testTrameLongDiff()
    {
        Octet[] o2 = new Octet[3];
        o2[0] = new Octet(b1);
        o2[1] = new Octet(b2);
        o2[2] = new Octet(b2);
        Trame t = new Trame(o2);
        assertNotEquals(t1, t);
    }

    @Test
    public void testNull()
    {
        Trame t = new Trame();
        assertNotEquals(t1, t);
    }

    @Test
    public void testOctetsDiff()
    {
        Octet[] o2 = new Octet[2];
        o2[0] = new Octet(b2);
        o2[1] = new Octet(b1);
        Trame t = new Trame(o2);
        assertNotEquals(t1, t);
    }

    @Test
    public void buildTrame()
    {
        Octet[] o2 = new Octet[2];
        o2[0] = new Octet(b1);
        o2[1] = new Octet(b2);
        Trame t2 = new Trame(o2);
        assertEquals(t1, t2);
    }

    @Test
    public void testToString()
    {
        String str1 = "0101010100001010";
        String str2 = t1.toString();
        assertEquals(str1, str2);
    }

    @Test
    public void testCloneTrame()
    {
        Trame t = new Trame(t1);
        assertEquals(t1, t);
    }

    @Test
    public void testGetData()
    {
        Octet[] o2 = t1.getData();
        for (int i = 0 ; i<o1.length;i++)
        {
            assertEquals(o1[i], o2[i]);
        }        
    }
    
    @Test
    public void testGetOctet()
    {
        Octet o2 = t1.getOctet(0);
        Octet o3 = new Octet(b1);
        assertEquals(o2, o3);
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
