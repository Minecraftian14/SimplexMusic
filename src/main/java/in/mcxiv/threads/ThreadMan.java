package in.mcxiv.threads;

import java.util.concurrent.*;

public class ThreadMan {

    public static final ThreadMan instance = new ThreadMan();

    public static ThreadMan getInstance() {
        return instance;
    }

    private final int processors;
    private final ScheduledExecutorService executorService;

    private ThreadMan() {
        processors = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newScheduledThreadPool(processors);
    }

    public int getProcessors() {
        return processors;
    }

    public ExecutorService executor() {
        return executorService;
    }

    public ScheduledExecutorService scheduler() {
        return executorService;
    }

    public Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return executorService.scheduleAtFixedRate(command, initialDelay, period, unit);
    }
}
