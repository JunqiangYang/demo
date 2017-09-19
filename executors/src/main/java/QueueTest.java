import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Junqiang.Yang
 * @create 2017-09-19 11:53
 **/

public class QueueTest {

    public static void main(String[] args) {
        BlockingQueue<String> completionQueue = new LinkedBlockingQueue<String>();
        completionQueue.add("1");
        completionQueue.add("2");
        completionQueue.add("3");
        completionQueue.add("4");
        completionQueue.add("5");

        for(int i = 0 ; i < 10 ; i++){
            try {
                System.out.println(completionQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" end  ");


    }


}
