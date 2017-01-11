package spring_mogo.thread;

import spring_mogo.dao.UserDao;

import com.mongodb.DBObject;

public class MyThread implements Runnable {

	private DBObject obj;
	private UserDao userDao;

	public MyThread() {
		super();
	}

	public MyThread(DBObject obj, UserDao userDao) {
		this.obj = obj;
		this.userDao = userDao;
	}

	@Override
	public void run() {
		userDao.saveData(obj);
	}

}
