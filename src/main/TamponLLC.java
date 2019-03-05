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
            Trame thisTrame = null;
            if(this.trame != null) {
                thisTrame = new Trame(this.trame);
            }

            Trame otherTrame = null;
            if(t.trame != null) {
                otherTrame = new Trame(t.trame);
            }
            
            if (thisTrame == null && otherTrame == null)
                return 0;
            if (thisTrame == null)
                return 1;
            if (otherTrame == null)
                return -1;
            
            int thisNum = this.valid ? thisTrame.getNumTrame() : thisTrame.getNumTrameHamming();
            int otherNum = t.valid ? otherTrame.getNumTrame() : otherTrame.getNumTrameHamming();

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
            if (tampon[i].valid == false) {
                tampon[i].valid = true;
                return tampon[i].trame;
            }
        }
        return null;
    }

    public void remove(Trame t) {
        for (int i = 0; i < tampon.length; i++) {
            if (tampon[i].trame != null && tampon[i].trame == t) {
                tampon[i].trame = null;
                tampon[i].isSent = false;
                tampon[i].timeSent = 0;
                tampon[i].valid = false;
                return;
            }
        }
    }
}
