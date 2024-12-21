package org.belligolifoundation.depliantsmanager.exception;

public class DepliantNotFoundException extends RuntimeException {

    public DepliantNotFoundException(Long id) {
        super(String.format("Depliant with id %d not found", id));
    }
}
