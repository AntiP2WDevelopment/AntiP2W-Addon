package addon.antip2w.irc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class DataStreamUtil {
    public static byte[] readByteArray(DataInputStream in, int maxSize) throws IOException {
        int size = in.readInt();
        if (size > maxSize || size < 0) throw new IOException("invalid byte array with size = " + size + " (maxSize = " + maxSize + ")");
        byte[] data = new byte[size];
        for (int i = 0; i < data.length; i++) {
            int value = in.read();
            if(value == -1 /* EOF */) throw new EOFException();
            data[i] = (byte) value;
        }
        return data;
    }

    public static void writeByteArray(DataOutputStream out, byte[] data) throws IOException {
        out.writeInt(data.length);
        for (byte b : data) out.write(b);
    }
}
