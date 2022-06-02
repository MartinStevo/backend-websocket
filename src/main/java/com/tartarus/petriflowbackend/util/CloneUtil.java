package com.tartarus.petriflowbackend.util;

import java.io.*;

public class CloneUtil {

    public static void serialize(OutputStream os, Serializable o) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(o);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static byte[] serialize(Serializable o) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            serialize(bos, o);
            bos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public static Object deserialize(InputStream is) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
