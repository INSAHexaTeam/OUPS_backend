package fr.insa.hexanome.OUPS.services;

/**
 * (ENUM) Design pattern état
 */
public enum EtatType {
    /**
     * Enregistrer un simple fichier
     */
    ENREGISTREMENT_SIMPLE,
    /**
     * Enregister un fichier puis changer d'état
     */
    ENREGISTREMENT,
    /**
     * Charger un fichier
     */
    CHARGEMENT,
}
