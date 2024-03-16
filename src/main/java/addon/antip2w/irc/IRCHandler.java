package addon.antip2w.irc;

import addon.antip2w.modules.IRC;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class IRCHandler {
    private static final String HOST = "services-0x06.alwaysdata.net";
    private static final int PORT = 8300;
    private static boolean isRunning = false;
    private static Socket socket = null;
    private static DataOutputStream out = null;
    private static DataInputStream in = null;
    private static final HashSet<Consumer<String>> callbacks = new HashSet<>();

    public static void addCallback(Consumer<String> callback) {
        callbacks.add(callback);
    }
    
    public static void sendAsync(String s) {
        if(!isRunning) throw new IllegalStateException("not running");
        CompletableFuture.runAsync(() -> {
            try {
                byte[] bytes = EncryptionUtil.encrypt(s, IRC.getKey(), IRC.getSalt());
                DataStreamUtil.writeByteArray(out, bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setRunning(boolean setRunning) {
        if (setRunning && !isRunning) startAsync();
        else if (!setRunning && isRunning) stop();
        isRunning = setRunning;
    }

    private static void startAsync() {
        ChatUtils.infoPrefix("IRC", "Connecting...");
        CompletableFuture.runAsync(() -> {
            try {
                socket = new Socket(HOST, PORT);
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                SocketThread thread = new SocketThread(in);
                thread.start();
            } catch (IOException e) {
                cleanUp(e);
            }
        });
    }

    private static void stop() {
        try {
            if(socket == null) ChatUtils.errorPrefix("IRC", "socket is somehow null?!");
            socket.close();
            ChatUtils.infoPrefix("IRC", "Disconnected");
        } catch (IOException e) {
            cleanUp(e);
        }
    }

    private static void cleanUp(@Nullable Exception e) {
        try {
            if(socket != null)
                socket.close();
        } catch (IOException ignored) {}
        socket = null;
        out = null;
        in = null;
        if(isRunning) isRunning = false;
        ChatUtils.warningPrefix("IRC", "An error occurred, please toggle the module and/or check the logs for more");
        e.printStackTrace();
    }

    private static final class SocketThread extends Thread {
        private final DataInputStream in;

        public SocketThread(DataInputStream in) {
            this.in = in;
            ChatUtils.infoPrefix("IRC", "Connected");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    byte[] bytes = DataStreamUtil.readByteArray(in, 1200);
                    String s = EncryptionUtil.decrypt(bytes, IRC.getKey(), IRC.getSalt());
                    for(Consumer<String> callback: callbacks) {
                        callback.accept(s);
                    }
                }
            } catch (Exception e) {
                cleanUp(e);
            }
        }
    }
}
