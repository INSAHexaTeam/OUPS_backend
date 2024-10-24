package fr.insa.hexanome.OUPS.model.exception;

import java.io.IOException;

public class FileExtension extends IOException {
    public FileExtension(String message) {
        super(message);
    }
}
