package main;

import java.util.Arrays;

import echanges.Trame;

public class TamponLLC {

    class TamponFormat implements Comparable<TamponFormat> {
        public Trame trame = null;
        public boolean isSent = false;
        public long timeSent = 0;
        public boolean valid = false;

        @Override
        public int compareTo(TamponFormat t) {
            if (this.trame == null && t.trame == null)
                return 0;
            if (this.trame == null)
                return 1;
            if (t.trame == null)
                return -1;
            int thisNum = this.valid ? this.trame.getNumTrame() : this.trame.getNumTrameHamming();
            int otherNum = t.valid ? t.trame.getNumTrame() : t.trame.getNumTrameHamming();

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

    public boolean addTrame(Trame trame) {
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

    public void removeTrame(int NumTrame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame.getNumTrame() == NumTrame) {
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
        for (TamponFormat t : tampon) {
            if (t.trame == null)
                return false;
        }
        return true;
    }

    public boolean isEmpty() {
        for (TamponFormat t : tampon) {
            if (t.trame != null)
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

    public void sendTrame(Trame t) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame == t) {
                tampon[i].isSent = true;
                tampon[i].timeSent = System.currentTimeMillis();
            }
        }
    }

    private void temporisation() {
        for (int i = 0; i < tampon.length; i++) {
            if (System.currentTimeMillis() - tampon[i].timeSent <= timeout) {
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
            }
        }
    }

    public int size() {
        return tampon.length;
    }

    public void resetTrame(int numTrame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame.getNumTrame() == numTrame) {
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
                tampon[i].valid = false;
            }
        }
    }

    public boolean alreadyExist(int numTrame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame.getNumTrame() == numTrame) {
                return true;
            }
        }
        return false;
    }

    public Trame getTrame(int numTrame) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame.getNumTrame() == numTrame) {
                return tampon[i].trame;
            }
        }
        return null;
    }

    public Trame getNextInvalidTrame() {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].valid == false) {
                tampon[i].valid = true;
                return tampon[i].trame;
            }
        }
        return null;
    }

    public void remove(Trame t) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].valid == false) {
                tampon[i].trame = null;
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
                tampon[i].valid = false;
                return;
            }
        }
    }
}
