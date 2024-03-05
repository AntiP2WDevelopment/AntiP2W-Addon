package addon.antip2w.utils;

public class TimerUtils {
    private long lastMS = 0L;

    public boolean hasReached(long ms) {
        return (System.currentTimeMillis() - this.lastMS >= ms);
    }

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public long getLastMS() {
        return this.lastMS;
    }

    public void setLastMS(long lastMS) {
        this.lastMS = lastMS;
    }

    public long getDifference() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public long getDifference(long time) {
        return System.currentTimeMillis() - time;
    }

    public long getDifferenceLastMS(long lastMS) {
        return System.currentTimeMillis() - lastMS;
    }

    public boolean hasReached(long ms, boolean reset) {
        if (System.currentTimeMillis() - this.lastMS >= ms) {
            if (reset)
                reset();
            return true;
        }
        return false;
    }

    public boolean hasReached(long ms, long lastMS) {
        return (System.currentTimeMillis() - lastMS >= ms);
    }

    public boolean hasReached(long ms, long lastMS, boolean reset) {
        if (System.currentTimeMillis() - lastMS >= ms) {
            if (reset)
                reset();
            return true;
        }
        return false;
    }

    public boolean hasReached(long ms, boolean reset, long lastMS) {
        if (System.currentTimeMillis() - lastMS >= ms) {
            if (reset)
                reset();
            return true;
        }
        return false;
    }

    public boolean hasReached(long ms, long lastMS, long difference) {
        return (System.currentTimeMillis() - lastMS >= ms);
    }

    public boolean hasReached(long ms, long lastMS, long difference, boolean reset) {
        if (System.currentTimeMillis() - lastMS >= ms) {
            if (reset)
                reset();
            return true;
        }
        return false;
    }

    public long getTimeSinceLastReset(long ms) {
        return System.currentTimeMillis() - ms;
    }
}
