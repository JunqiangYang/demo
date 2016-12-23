package com.xiaoleilu.hutool.db.ds.pool;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;

/**
 * 数据库配置
 * @author Looly
 *
 */
public class DbConfig {
	
	//-------------------------------------------------------------------- Fields start
	private String driver;		//数据库驱动
	private String url;			//jdbc url
	private String user;			//用户名
	private String pass;			//密码
	
	private int initialSize;		//初始连接数
	private int minIdle;			//最小闲置连接数
	private int maxActive;		//最大活跃连接数
	private long maxWait;		//获取连接的超时等待
	//-------------------------------------------------------------------- Fields end
	
	//-------------------------------------------------------------------- Constructor start
	public DbConfig() {
	}
	
	/**
	 * 构造
	 * @param url jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public DbConfig(String url, String user, String pass) {
		init(url, user, pass);
	}
	//-------------------------------------------------------------------- Constructor end
	
	/**
	 * 初始化
	 * @param url jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public void init(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.driver = DbUtil.identifyDriver(url);
		try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			throw new DbRuntimeException(e, "Get jdbc driver from [{}] error!", url);
		}
	}
	
	//-------------------------------------------------------------------- Getters and Setters start
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public int getInitialSize() {
		return initialSize;
	}
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}
	public int getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	public int getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	public long getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}
	//-------------------------------------------------------------------- Getters and Setters end
}