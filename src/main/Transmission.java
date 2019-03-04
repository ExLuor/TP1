package main;

import java.util.HashMap;

import echanges.Trame;

public class Transmission implements Runnable {
	private String nomCouche;
	private TamponCirculaire tampon;
	private final long LATENCY = 100; // en ms
	private HashMap<Integer, SousCouche<?, Trame>> couchesReceptrices;

	public Transmission(int grandeurBuffer, String nomCouche) {
		this.nomCouche = nomCouche;
		tampon = new TamponCirculaire(grandeurBuffer);
		couchesReceptrices = new HashMap<Integer, SousCouche<?, Trame>>();
	}

	public void addCoucheReceptrice(int stationID, SousCouche<?, Trame> couche) {
		couchesReceptrices.put(stationID, couche);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (tampon.isEmpty()) {
			return;
		}

		long timeToWait = LATENCY - (System.currentTimeMillis() - tampon.getLastAddedTime());
		sleep(timeToWait);
		Trame t = tampon.poll();
		t = addErrors(t);
		sendTrame(t);
	}

	protected void sleep(long sleepTime) {
		try {
			if (sleepTime >= 0) {
				Thread.sleep(sleepTime);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public boolean addTrame(Trame trame) {
		if (tampon.add(trame)) {
			System.out.println(
			        "La couche " + nomCouche + " a ajouté la trame " + trame.getNumTrameHamming() + " à son tampon.");
			return true;
		}
		return false;
	}

	private Trame addErrors(Trame t) {
		// TODO: Ajouter les erreurs à la trame
		return t;
	}

	private void sendTrame(Trame t) {
		int numDest = t.getDestHamming();
		SousCouche<?, Trame> dest = couchesReceptrices.get(numDest);
		if (dest == null) {
			return;
		}
		dest.addFromDown(t);
		System.out.println("La couche " + nomCouche + " a envoyé la trame " + t.getNumTrameHamming() + " à la station "
		        + numDest + ".");
	}
}