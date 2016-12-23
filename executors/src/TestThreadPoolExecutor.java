import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016-12-21 0021.
 */


public class TestThreadPoolExecutor {
    public static void main(String[] args)
    {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(6);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 60, TimeUnit.SECONDS, queue);
                                                                   // keepailvetime  为心创建的大于corethreadnum 的那部分线程最大的空闲时间 就会被回收了
        for (int i = 0; i < 20; i++)
        {
            final int index = i;
            try
            {
                executor.execute(new Runnable() {
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(4000);
                        }
                        catch (InterruptedException e)
                        {
                            //e.printStackTrace();
                            System.out.println(String.format("thread %d InterruptedException ===", index));
                        }
                        System.out.println(String.format("thread %d finished", index));
                    }
                });
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                System.out.println(String.format("thread %d droped ===", index));
            }
        }
        executor.shutdown();
    }

}
