package fr.insa.hexanome.OUPS.model.exception;

import java.io.IOException;
/**
 * (DATA TO OBJECT) Exception pour le traitement des fichiers XML
 */
public class FileExtension extends IOException {
    public FileExtension(String message) {
        super(message);
    }
}
