package core;

import java.io.*;

/**
 *
 */
public class Copy {

    /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     * @param orig .
     * @return .
     */
    public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
                out.writeObject(orig);
                out.flush();
            }

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

}