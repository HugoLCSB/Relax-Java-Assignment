package relax.gaming.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * This class provides utility methods to help with json workflows.
 */
public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    /**
     * Marshals an Object into a Json string
     *
     * @param o the object to be marshalled
     * @return the json String
     */
    public static String toJson(Object o) {
        try {
            return writer.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}