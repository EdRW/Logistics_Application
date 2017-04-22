package example.common;

import example.identification.Identifiable;
import example.identification.IdentifiableImpl;

public class IdentifiableImplFactory {

    private static int idCounter = 1; // Used to generate id's

    public static Identifiable createIdentifiable() throws InvalidDataException {
        // Not much logic here - only one kind of Impl is available currently
        return new IdentifiableImpl(generateId());
    }

    private static String generateId() {
        return "ID" + idCounter++; // Use the counter then increment it
    }
}
