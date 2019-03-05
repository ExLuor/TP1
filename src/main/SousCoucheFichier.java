package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import echanges.Octet;

/*
 * Représente la couche A1 (lecture du fichier) et la couche B1 (écriture du
 * fichier).
 */
public class SousCoucheFichier extends SousCouche<Octet, Octet> {
    private FileOutputStream stream;

    public SousCoucheFichier(String nomFichierEntrant, String nomFichierSortant, String nomCouche) {
        super(nomCouche);

        if (!nomFichierEntrant.isEmpty())
            readFile(nomFichierEntrant);

        if (!nomFichierSortant.isEmpty())
            setOutputStream(nomFichierSortant);
    }

    // Insère le contenu du fichier dans le buffer dès le départ.
    private void readFile(String file) {
        try {
            byte[] array = Files.readAllBytes(new File(file).toPath());
            for (byte b : array) {
                bufferFromUp.add(new Octet(b));
            }

        } catch (Exception e) {
            System.out.println("La lecture du fichier \"" + file + "\" a échoué.");
        }
    }

    private void setOutputStream(String file) {
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("L'écriture du fichier \"" + file + "\" a échoué.");
        }
    }

    @Override
    protected void sendMessageToUp() {
        Octet data = bufferFromDown.poll();
        if (data == null)
            return;

        try {
            stream.write(data.value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void sendMessageToDown() {
        Octet o = bufferFromUp.peek();
        if (o == null)
            return;

        if (sendToDown(o)) {
            bufferFromUp.poll();
        }
    }

}
