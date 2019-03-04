package main;

public class IdGenerator {

	private static int ID = 1;

	public synchronized static int GetID() {
		return ID++;
	}
}
