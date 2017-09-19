import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Junqiang.Yang
 * @create 2017-09-19 11:28
 **/

public class ConcurrentCalculator3 {


    private static int taskCapacity = 3; //  任务数量
    private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(taskCapacity);
    private static int cpuCoreNumber = Runtime.getRuntime().availableProcessors();
    private static ExecutorService exec = new ThreadPoolExecutor(cpuCoreNumber, cpuCoreNumber * 2, 60, TimeUnit.SECONDS, queue);
    private static CompletionService<Long> completionService = new ExecutorCompletionService<Long>(exec);



    // 内部类
    class SumCalculator implements Callable<Long> {
        private int[] numbers;
        private int start;
        private int end;

        public SumCalculator(final int[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        public Long call() throws Exception {
            Long sum = 0l;
            for (int i = start; i < end; i++) {
                sum += numbers[i];
                Thread.sleep(1000);
            }
            return sum;
        }
    }

    public Long sum(final int[] numbers) {
        // 根据CPU核心个数拆分任务，创建FutureTask并提交到Executor
        int size = 0 ;
        for (int i = 0; i < numbers.length; i+=3) {
            int start = i;
            int end = i+3 ;
            if (end > numbers.length)
                end = numbers.length;
            System.out.println("task : "+start+","+end);
            ConcurrentCalculator3.SumCalculator subCalc = new ConcurrentCalculator3.SumCalculator(numbers, start, end);
            if (!exec.isShutdown()) {
                try {
                    completionService.submit(subCalc);
                    size++;
                } catch (Exception e)
                {
                    //e.printStackTrace();
                    System.out.println("thread %d droped == "+start+","+end);
                }
            }

        }
        return getResult(size);
    }

    public Long getResult(int size) {
        Long result = 0l;
        for (int i = 0; i < size; i++) {
            try {
                Long subSum = completionService.take().get();
                result += subSum;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public void close() {
        exec.shutdown();
    }

    public static void main(String[] args) {
        System.out.println(cpuCoreNumber);
        int[] numbers = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11,12,13,14,15,16,17,18,19,11,12,13,14,15,16,17,18,19,11, 2, 3, 4, 5, 6, 7, 8, 10, 11,12,13,14,15,16,17,18,19,11,12,13,14,15,16,17,18,19,11, 2, 3, 4, 5, 6, 7, 8, 10, 11,12,13,14,15,16,17,18,19,11,12,13,14,15,16,17,18,19,11, 2, 3, 4, 5, 6, 7, 8, 10, 11,12,13,14,15,16,17,18,19,11,12,13,14,15,16,17,18,19,11};
        ConcurrentCalculator3 c3 =  new ConcurrentCalculator3() ;
        Long sum = c3.sum(numbers);
        System.out.println(sum);
        c3.close();
        Long sum2 = c3.sum(numbers);
        System.out.println(sum2);



    }


}
