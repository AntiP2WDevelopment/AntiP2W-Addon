package addon.antip2w;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;

import java.util.Map;

public class ClientWatchdog implements Runnable {
    public static long lastTickStartTime = Long.MAX_VALUE;
    private final MinecraftClient client;
    public ClientWatchdog(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            long time = Util.getMeasuringTimeMs() - lastTickStartTime;
            if(time < -1 && client.player != null && client.world != null) {
                System.out.println("Client tick took longer than 5000 ms (" + time + "ms), treating it as a crash");
                Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
                for(Thread thread: stackTraces.keySet()) {
                    System.out.println("---------------");
                    System.out.println("thread: " + thread.getName());
                    System.out.println("stackTrace:");
                    for (StackTraceElement stackTraceElement: stackTraces.get(thread)) {
                        System.out.println(stackTraceElement);
                    }
                }
                System.exit(-1);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {}
        }
    }
}
