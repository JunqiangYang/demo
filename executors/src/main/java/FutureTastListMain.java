/**
 * Created by admin on 2016-12-22 0022.
 */
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ExecutionException;
public class FutureTastListMain {
    private Map<String, FutureTask<String>> tasks = new HashMap<String, FutureTask<String>>();
// 构造一个线程
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    /**
     * @param tasksList
     */
    public void addTaskList(List<Callable<String>> tasksList) {
        for (Callable<String> t : tasksList) {
            FutureTask<String> futureTask = new FutureTask<String>(t);
            executor.execute(futureTask);
            String key = Long.toHexString(System.nanoTime());
            tasks.put(key, futureTask);
        }
    }
    /**
     * @param task
     * @return
     */
    public String addTask(Callable<String> task) {
        FutureTask<String> futureTask = new FutureTask<String>(task);
        executor.execute(futureTask);
        String key = Long.toHexString(System.nanoTime());
        tasks.put(key, futureTask);
        return key;
    }
    /**
     * @param task
     * @return
     */
    public String addDBTask(Callable<String> task) {
        FutureTask<String> futureTask = new FutureTask<String>(task) {
            public boolean cancel(boolean mayInterruptIfRunning) {
                System.out.println("Roll Back and Closs Session");
                return super.cancel(mayInterruptIfRunning);
            }
        };
        executor.execute(futureTask);
        String key = Long.toHexString(System.nanoTime());
        tasks.put(key, futureTask);
        return key;
    }
    /**
     * @param key
     * @return
     */
    public boolean taskIsDone(String key) {
        FutureTask<String> futureTask = tasks.get(key);
        if (futureTask != null) {
            return futureTask.isDone();
        }
        return false;
    }
    /**
     * @param key
     * @return
     */
    public boolean taskIsCancelled(String key) {
        FutureTask<String> futureTask = tasks.get(key);
        if (futureTask != null) {
            return futureTask.isCancelled();
        }
        return false;
    }
    /**
     * @param key
     * @return
     */
    public String getTaskResult(String key) {
        FutureTask<String> futureTask = tasks.get(key);
        if (futureTask.isDone()) {
            try {
                String result = futureTask.get();
                tasks.remove(key);
                return result;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
    /**
     * @param task
     * @return
     */
    public String addTaskAndWaitResult(Callable<String> task) {
        FutureTask<String> futureTask = new FutureTask<String>(task);
        executor.execute(futureTask);
        String key = Long.toHexString(System.nanoTime());
        tasks.put(key, futureTask);
        try {
            return futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     *
     */
    public void removeAllTask() {
        for (String key : tasks.keySet()) {
            executor.remove((Runnable) tasks.get(key));
            tasks.remove(key);
        }
    }
    /**
     * @param key
     */
    public void removeQueryTask(String key) {
        executor.remove((Runnable) tasks.get(key));
    }
    /**
     * @param key
     */
    public void removeTask(String key) {
        tasks.remove(key);
    }
    /**
     *
     */
    public void clearTaskList() {
        tasks.clear();
    }
    public synchronized void stop() {
        try {
            executor.shutdownNow();
            executor.awaitTermination(1L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor = null;
            tasks.clear();
            tasks = null;
        }
    }
    /**
     * @param key
     */
    public void cancelTask(String key) {
        FutureTask<String> futureTask = tasks.get(key);
        if (futureTask != null) {
            if (!futureTask.isDone()) {
                futureTask.cancel(true);
            }
        }
    }
    public void purgeCancelTask() {
        executor.purge();
        executor.getQueue();
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        FutureTastListMain exec = new FutureTastListMain();
        ArrayList<String> keyList = new ArrayList<String>();
        ArrayList<String> removeKeyList = new ArrayList<String>();
        ArrayList<String> cancelKeyList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
// 产生一个任务，并将其加入到线程池
            final String task = "task@ " + (i + 1);
            System.out.println("put " + task);
            keyList.add(exec.addDBTask(new Callable<String>() {
                public String call() throws Exception {
                    Thread.sleep(10000);
                    return task;
                }
            }));
        }
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            if (exec.taskIsDone(keyList.get(i))) {
                System.out.println(exec.getTaskResult(keyList.get(i)));
                exec.removeTask(keyList.get(i));
                removeKeyList.add(keyList.get(i));
            } else {
                exec.cancelTask(keyList.get(i));
                System.out.println("Cancel task: " + (i + 1));
                exec.removeTask(keyList.get(i));
                cancelKeyList.add(keyList.get(i));
            }
        }
        exec.purgeCancelTask();
        exec.stop();
        try {
            Thread.sleep(6000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String key : cancelKeyList) {
            if (exec.taskIsCancelled(key)) {
                System.out.println("Cancel: " + key);
            }
        }
        for (int i = 0; i < 10; i++) {
            keyList.get(i);
        }
    }
}
