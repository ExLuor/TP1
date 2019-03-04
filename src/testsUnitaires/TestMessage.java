package testsUnitaires;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import echanges.Paquet;

public class TestMessage
{

    // byte[] b1 = { 0, 0, 0, 0, 0, 0, 0, 0 };
    // byte[] b2 = { 0, 0, 0, 0, 0, 0, 0, 1 };
    // byte[] b3 = { 0, 0 };
    char[] c1 = { '0', '0', '0', '0', '0', '0', '0', '0' };
    char[] c2 = { '0', '0', '0', '0', '0', '0', '0', '1' };
    char[] c3 = { '0', '0' };
    Paquet p1;
    Paquet p2;
    Paquet p3;

    String str = new String();

    @Before
    public void setup()
    {
        // Message de fin.
        // m1 = new Paquet(b1);
        // m2 = new Paquet(b2);
        // m3 = new Paquet(b3);

        p1 = new Paquet(c1);
        p2 = new Paquet(c2);
        p3 = new Paquet(c3);

        str = "00000000";
    }

    @Test
    public void testIsEqual()
    {
        // assertEquals(m1, m1);
        assertEquals(p1, p1);
    }

    @Test
    public void testIsNotEqual()
    {
        // assertNotEquals(m1, m2);
        assertNotEquals(p1, p2);
        // assertNotEquals(m1, m3);
        assertNotEquals(p1, p3);
    }

    @Test
    public void testGetMsgEquals()
    {
        String local = p1.getMsg();
        // String local = m1.getMsg();
        System.out.println(local);
        assertEquals(str, local);
    }

    @Test
    public void testGetMsgNotEquals()
    {
        String local = p2.getMsg();
        // String local = m2.getMsg();
        assertNotEquals(str, local);
    }

    @Test
    public void testGetByteEquals()
    {
        char[] local = p1.getData();
        // byte[] local = m1.getData();
        // assertEquals(b1, local);
        assertEquals(c1, local);
    }

    @Test
    public void testGetByteNotEquals()
    {
        char[] local = p2.getData();
        // byte[] local = m2.getData();
        // assertNotEquals(b1, local);
        assertNotEquals(c1, local);
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
