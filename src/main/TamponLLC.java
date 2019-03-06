/* TamponLLC.java
 * Description: Tampon T-LLC de chaque côté du support de transmission qui rend le protocole 6 possible
 * Auteurs: Boulanger, Sammy       - 18 058 904
 *          Durand-Chorel, Michael - 17 141 086
 *          Leroux, Jérémie        - 16 186 994
 * Date de fin: 6 mars 2019
 * Entrées du programme : -
 * Sotrties du programme : -
 * 
 */

package main;

import echanges.Trame;
import java.util.Arrays;

/**
 * 
 * Tampon T-LLC de chaque côté du support de transmission qui rend le protocole
 * 6 possible
 *
 */
public class TamponLLC {
    class TamponFormat implements Comparable<TamponFormat> {
        public Trame trame = null;
        public boolean isSent = false;
        public long timeSent = 0;
        public boolean valid = false;

        @Override
        public int compareTo(TamponFormat format) {
            Trame thisTrame = null;
            if (this.trame != null) {
                thisTrame = new Trame(this.trame);
            }

            Trame otherTrame = null;
            if (format.trame != null) {
                otherTrame = new Trame(format.trame);
            }

            if (thisTrame == null && otherTrame == null)
                return 0;
            if (thisTrame == null)
                return 1;
            if (otherTrame == null)
                return -1;

            int thisNum = this.valid ? thisTrame.getNumTrame() : thisTrame.getNumTrameHamming();
            int otherNum = format.valid ? otherTrame.getNumTrame() : otherTrame.getNumTrameHamming();

            if (thisNum == otherNum)
                return 0;
            if (thisNum < otherNum)
                return -1;
            return 1;
        }
    }

    private TamponFormat[] tampon;
    private long timeout;

    public TamponLLC(int size, int timeout) {
        this.timeout = timeout;
        tampon = new TamponFormat[size];
        for (int i = 0; i < tampon.length; i++) {
            tampon[i] = new TamponFormat();
        }
    }

    public synchronized boolean addTrame(Trame trame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame == null) {
                tampon[i].trame = trame;
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
                tampon[i].valid = false;
                Arrays.sort(tampon);
                return true;
            }
        }
        return false;
    }

    public synchronized void removeTrame(byte numTrame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame.getNumTrame() == numTrame) {
                tampon[i].trame = null;
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
                tampon[i].valid = false;
                Arrays.sort(tampon);
                return;
            }
        }
    }

    public boolean isFull() {
        for (TamponFormat format : tampon) {
            if (format.trame == null)
                return false;
        }
        return true;
    }

    public boolean isEmpty() {
        for (TamponFormat format : tampon) {
            if (format.trame != null)
                return false;
        }
        return true;
    }

    public Trame getNextTrame() {
        temporisation();

        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && !tampon[i].isSent) {
                return tampon[i].trame;
            }
        }
        return null;
    }

    public void sendTrame(Trame trame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame == trame) {
                tampon[i].isSent = true;
                tampon[i].timeSent = System.currentTimeMillis();
            }
        }
    }

    private void temporisation() {
        for (int i = 0; i < tampon.length; i++) {
            long timeLeft = timeout - (System.currentTimeMillis() - tampon[i].timeSent);
            if (timeLeft <= 0) {
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
            }
        }
    }

    public int size() {
        return tampon.length;
    }

    public void resetTrame(byte numTrame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame.getNumTrame() == numTrame) {
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
                tampon[i].valid = false;
            }
        }
    }

    public boolean alreadyExist(byte numTrame) {
        int repetition = 0;
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame.getNumTrame() == numTrame) {
                repetition++;
            }
        }
        return repetition > 1;
    }

    public Trame getTrame(byte numTrame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame.getNumTrame() == numTrame) {
                return tampon[i].trame;
            }
        }
        return null;
    }

    public Trame getNextInvalidTrame() {
        for (int i = 0; i < tampon.length; i++) {
            if (!tampon[i].valid) {
                tampon[i].valid = true;
                return tampon[i].trame;
            }
        }
        return null;
    }

    public void remove(Trame trame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame == trame) {
                tampon[i].trame = null;
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
                tampon[i].valid = false;
                return;
            }
        }
    }
}
