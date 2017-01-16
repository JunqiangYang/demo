import java.util.concurrent.TimeUnit;

import com.tqd.test.*;

/**
 *FileName:Main.java
 * Creator:tanqidong
 * Create Time:2015年4月30日 下午1:45:27
 * Description:
 * 
 * 
 * 
 * 
 * 
 */

/**
 * @author tanqidong
 *
 */
public class Main {
	
	public static void main(String[] args) {
		
		BaseThread bt=new BaseThread();
		SleepThread st=new SleepThread();
		st.start();
//		启动线程
	//	bt.start();
//		休眠１０００毫秒ue
		while(true)
		{
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	//		中断线程
			st.interrupt();
		}
		
	}

}
