package spring_mongo.test;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring_mogo.dao.UserDao;
import spring_mogo.thread.MyThread;
import spring_mongo.models.UserModel;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml" })
public class mongoTest {

	@Autowired
	private UserDao userDao;

	/**
	 * 查询测试
	 * 
	 * @author：tuzongxun
	 * @Title: monFindTest
	 * @param
	 * @return void
	 * @date May 13, 2016 3:27:51 PM
	 * @throws
	 */
	@Test
	public void monFindTest() {
		List<UserModel> userModels = userDao.findAll();
		if (userModels != null && userModels.size() > 0) {
			for (UserModel user : userModels) {
				System.out.println(user.getName() + ":" + user.getAge());
			}
		}
	}

	/**
	 * 插入数据测试
	 * 
	 * @author：tuzongxun
	 * @Title: monInsertTest
	 * @param
	 * @return void
	 * @date May 13, 2016 3:27:38 PM
	 * @throws
	 */
	@Test
	public void monInsertTest() {
		UserModel user = new UserModel("test111", 22);
		userDao.insertUser(user);
		this.monFindTest();
	}

	/**
	 * 删除测试
	 * 
	 * @author：tuzongxun
	 * @Title: monRemoveTest
	 * @param
	 * @return void
	 * @date May 13, 2016 3:28:06 PM
	 * @throws
	 */
	@Test
	public void monRemoveTest() {
		String userName = "test111";
		userDao.removeUser(userName);
		this.monFindTest();
	}

	/**
	 * 测试修改
	 * 
	 * @author：tuzongxun
	 * @Title: monUpdateTest
	 * @param
	 * @return void
	 * @date May 13, 2016 3:50:08 PM
	 * @throws
	 */
	@Test
	public void monUpdateTest() {
		UserModel user = new UserModel("test111", 22);
		userDao.updateUser(user);
		this.monFindTest();
	}

	/**
	 * 按条件查询
	 * 
	 * @author：tuzongxun
	 * @Title: monFindForRuq
	 * @param
	 * @return void
	 * @date May 13, 2016 4:10:53 PM
	 * @throws
	 */
	@Test
	public void monFindForRuq() {
		String userName = "test111";
		List<UserModel> userModels = userDao.findForRequery(userName);
		if (userModels != null && userModels.size() > 0) {
			for (UserModel user : userModels) {
				System.out.println(user.getName() + ":" + user.getAge());
			}
		}
	}

	@Test
	public void mongoGroup() {
		BasicDBList dbList = userDao.mongoGroup();
		if (dbList != null) {
			for (int i = 0; i < dbList.size(); i++) {
				DBObject obj = (DBObject) dbList.get(i);
				Object age = obj.get("age");
				Object count = obj.get("count");
				System.out.println("age:" + age + ",count:" + count);
			}
		}
	}

	/**
	 * 单线程保存数据
	 * 
	 * @author：tuzongxun
	 * @Title: saveTest
	 * @param
	 * @return void
	 * @date Jul 19, 2016 9:47:37 AM
	 * @throws
	 */
	@Test
	public void saveTest() {
		int n = 10000;
		Date date1 = new Date();
		for (int i = 0; i < n; i++) {
			DBObject obj = new BasicDBObject();
			obj.put("name", "test" + i);
			obj.put("age", i);
			userDao.saveData(obj);
		}
		Date date2 = new Date();
		System.out.println("单线程保存【" + n + "】条数据用时：" + (date2.getTime() - date1.getTime()));
	}

	/**
	 * 多线程保存数据
	 * 
	 * @author：tuzongxun
	 * @Title: saveTest1
	 * @param
	 * @return void
	 * @date Jul 19, 2016 9:48:09 AM
	 * @throws
	 */
	@Test
	public void saveTest1() {
		int n = 60;
		Date date1 = new Date();
		for (int i = 0; i < n; i++) {
			DBObject obj = new BasicDBObject();
			obj.put("name", "test" + i);
			obj.put("age", i);
			Runnable myThread = new MyThread(obj, userDao);
			Thread thread = new Thread(myThread);
			thread.start();
		}
		Date date2 = new Date();
		System.out.println("多线程保存【" + n + "】条数据用时：" + (date2.getTime() - date1.getTime()));
	}

}
