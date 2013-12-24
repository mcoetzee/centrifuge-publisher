package org.centrifuge;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * JSON utility class.
 */
public class JsonUtility {

    private Logger logger = Logger.getLogger(getClass());

    /**
     * Serializes the Object to JSON. It can only do so if all fields of the given Object have public access modifiers.
     *
     * @param object The Object to serialize.
     * @return The JSON of the object.
     */
    public String serializeToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }
}
