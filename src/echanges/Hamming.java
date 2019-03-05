package echanges;

public class Hamming
{

    public void addHamming(Trame t)
    {
        int offset = 0; // Combien de puissances de 2 ont été rencontrées.
        int position = 0; // Quelle position dans source on est rendu.
        String str = new String();
        // Décaler les positions dans la trame.
        Octet[] octDataAvant = t.getData();
        Octet[] octDataApres = new Octet[6];

        // Les bits de contrôle.
        int cBit1 = 0;
        int cBit2 = 0;
        int cBit4 = 0;
        int cBit8 = 0;
        int cBit16 = 0;
        int cBit32 = 0;

        for (int i = 0; i < octDataApres.length; i++)
        {
            octDataApres[i] = new Octet();
        }

        int[] iSans = new int[48];
        int[] iAvec = new int[48];

        str = "******************HAMMING AVANT";

        for (int i = 0; i < iSans.length; i++)
        {
            iSans[i] = (octDataAvant[i / 8].getValue() >> (7 - i % 8)) & 1;

        }

        printHam(str, iAvec, iSans);

        for (int i = 0; i < iAvec.length; i++)
        {
            // Si c'est une puissance de 2.
            if ((i + 1) == Math.pow(2, offset))
            {
                iAvec[i] = 0;
                offset++;
            }
            else
            {
                // iAvec[i] = 1;
                iAvec[i] = iSans[position];
                position++;
            }
        }

        str = "******************HAMMING EN COURS 1\n";

        printHam(str, iAvec, iSans);

//        for (int i = 0; i < iAvec.length; i++)
//        {
//            // Bit de contrôle de 2^0.
//            if (((i + 1) % 2) > 0)
//            {
//                cBit1 += iAvec[i];
//            }
//            // Bit de contrôle de 2^1.
//            if (((i + 1) % 4) > 1)
//            {
//                cBit2 += iAvec[i];
//            }
//            // Bit de contrôle de 2^2.
//            if (((i + 1) % 8) > 3)
//            {
//                cBit4 += iAvec[i];
//            }
//            // Bit de contrôle de 2^3.
//            if (((i + 1) % 16) > 7)
//            {
//                cBit8 += iAvec[i];
//            }
//            // Bit de contrôle de 2^4.
//            if (((i + 1) % 32) > 15)
//            {
//                cBit16 += iAvec[i];
//            }
//            // Bit de contrôle de 2^5.
//            if (((i + 1) % 64) > 31)
//            {
//                cBit32 += iAvec[i];
//            }
//
//        }

        int[] cBit = getBitControl(/*cBit1, cBit2, cBit4, cBit8, cBit16, cBit32,*/ iAvec);
        
         System.out.println("Valeur de cBit1 = " + cBit[0]);
         System.out.println("Valeur de cBit2 = " + cBit[1]);
         System.out.println("Valeur de cBit4 = " + cBit[2]);
         System.out.println("Valeur de cBit8 = " + cBit[3]);
         System.out.println("Valeur de cBit16 = " + cBit[4]);
         System.out.println("Valeur de cBit32 = " + cBit[5]);
        iAvec[0] = (cBit1 % 2) == 0 ? 0 : 1;
        iAvec[1] = (cBit2 % 2) == 0 ? 0 : 1;
        iAvec[3] = (cBit4 % 2) == 0 ? 0 : 1;
        iAvec[7] = (cBit8 % 2) == 0 ? 0 : 1;
        iAvec[15] = (cBit16 % 2) == 0 ? 0 : 1;
        iAvec[31] = (cBit32 % 2) == 0 ? 0 : 1;
        
        str = "******************HAMMING APRES \n";
        printHam(str, iAvec, iSans);

    }

    private int[] getBitControl(/*int cBit1, int cBit2, int cBit4, int cBit8, int cBit16,int cBit32, */int[] iAvec)
    {
//        int[] cBit = new int[6];
        int[] cBit = {0,0,0,0,0,0};
        for (int i = 0; i < iAvec.length; i++)
        {
            // Bit de contrôle de 2^0.
            if (((i + 1) % 2) > 0)
            {
//                cBit1 += iAvec[i];
                cBit[0]+= iAvec[i];
            }
            // Bit de contrôle de 2^1.
            if (((i + 1) % 4) > 1)
            {
//                cBit2 += iAvec[i];
                cBit[1]+= iAvec[i];
            }
            // Bit de contrôle de 2^2.
            if (((i + 1) % 8) > 3)
            {
//                cBit4 += iAvec[i];
                cBit[2]+= iAvec[i];
            }
            // Bit de contrôle de 2^3.
            if (((i + 1) % 16) > 7)
            {
//                cBit8 += iAvec[i];
                cBit[3]+= iAvec[i];
            }
            // Bit de contrôle de 2^4.
            if (((i + 1) % 32) > 15)
            {
//                cBit16 += iAvec[i];
                cBit[4]+= iAvec[i];
            }
            // Bit de contrôle de 2^5.
            if (((i + 1) % 64) > 31)
            {
//                cBit32 += iAvec[i];
                cBit[5]+= iAvec[i];
            }
        }
        return cBit;
    }
    
    private void printHam(String debut, int[] iAvec, int[] iSans)
    {
        String str = new String();
        str = debut + "\n";
        str += "\tiSans\t";

        for (int i = 0; i < iSans.length; i++)
        {
            if ((i % 8) == 0 && i > 0)
            {
                str += " ";
            }
            str += Integer.toString(iSans[i]);
        }
        str += "\n";

        str += "\tiAvec\t";
        for (int i = 0; i < iAvec.length; i++)
        {
            if ((i % 8) == 0 && i > 0)
            {
                str += " ";
            }
            str += Integer.toString(iAvec[i]);
        }

        str += "\n";

        System.out.println(str);
    }

    public void retireHamming(Trame t)
    {

    }

    public boolean valideHamming(Trame t)
    {
        return false;
    }

    public void corrigerTrame(Trame t)
    {

    }

}
