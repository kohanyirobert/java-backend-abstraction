package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IO {

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[32];
        while (true) {
            int read = is.read(bytes);
            if (read == -1) {
                break;
            }
            os.write(bytes, 0, read);
            if (read < bytes.length) {
                break;
            }
        }
    }

    public static String toString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(is, baos);
        return baos.toString();
    }

    private IO() {
    }
}
