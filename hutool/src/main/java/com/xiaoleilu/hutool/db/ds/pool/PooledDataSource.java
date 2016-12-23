package com.xiaoleilu.hutool.db.ds.pool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import com.xiaoleilu.hutool.db.ds.AbstractDataSource;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.xiaoleilu.hutool.util.ThreadUtil;

/**
 * 池化数据源
 * @author Looly
 *
 */
public class PooledDataSource extends AbstractDataSource{
	
	private Queue<PooledConnection> freePool;
	private int activeCount;			//活跃连接数
	
	private DbConfig config;
	
	/**
	 * 获得一个数据源
	 * 
	 * @param group 数据源分组
	 * @throws ConnException
	 */
	synchronized public static PooledDataSource getDataSource(String group) {
		return new PooledDataSource(group);
	}
	
	/**
	 * 获得一个数据源
	 * 
	 * @throws ConnException
	 */
	synchronized public static PooledDataSource getDataSource() {
		return new PooledDataSource();
	}
	
	//-------------------------------------------------------------------- Constructor start
	/**
	 * 构造，读取默认的配置文件和默认分组
	 */
	public PooledDataSource() {
		this(StrUtil.EMPTY);
	}
	
	/**
	 * 构造，读取默认的配置文件
	 * @param group 分组
	 */
	public PooledDataSource(String group) {
		this(new DbSetting(), group);
	}
	
	/**
	 * 构造
	 * @param setting 数据库配置文件对象
	 * @param group 分组
	 */
	public PooledDataSource(DbSetting setting, String group) {
		this(setting.getDbConfig(group));
	}
	
	/**
	 * 构造
	 * @param config 数据库配置
	 */
	public PooledDataSource(DbConfig config) {
		this.config = config;
		freePool = new LinkedList<PooledConnection>();
		int initialSize = config.getInitialSize();
		try {
			while(initialSize-- > 0){
				freePool.offer(newConnection());
			}
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		}
	}
	//-------------------------------------------------------------------- Constructor start

	/**
	 * 从数据库连接池中获取数据库连接对象
	 */
	@Override
	public synchronized Connection getConnection() throws SQLException {
		return getConnection(config.getMaxWait());
	}
	
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		throw new SQLException("Pooled DataSource is not allow to get special Connection!");
	}
	
	/**
	 * 释放连接，连接会被返回给连接池
	 * @param conn 连接
	 * @return 释放成功与否
	 */
	protected synchronized boolean free(PooledConnection conn){
		activeCount--;
		return freePool.offer(conn);
	}
	
	/**
	 * 释放全部连接
	 */
	public synchronized void release() {
		for (PooledConnection conn : freePool) {
			conn.release();
		}
		this.freePool.clear();
	}
	
	/**
	 * 创建新连接
	 * @return 新连接
	 * @throws java.sql.SQLException
	 */
	public PooledConnection newConnection() throws SQLException{
		return new PooledConnection(this);
	}
	
	public DbConfig getConfig() {
		return config;
	}
	
	/**
	 * 获取连接对象
	 * @param wait 当池中无连接等待的毫秒数
	 * @return 连接对象
	 * @throws java.sql.SQLException
	 */
	public PooledConnection getConnection(long wait) throws SQLException{
		try {
			return getConnectionDirect();
		} catch (Exception e) {
			ThreadUtil.sleep(wait);
		}
		return getConnectionDirect();
	}
	
	@Override
	synchronized public void close() throws IOException{
		if(CollectionUtil.isNotEmpty(this.freePool)){
			for (PooledConnection pooledConnection : freePool) {
				IoUtil.close(pooledConnection);
			}
			
			this.freePool.clear();
			this.freePool = null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		IoUtil.close(this);
	}
	
	/**
	 * 直接从连接池中获取连接，如果池中无连接直接抛出异常
	 * @return PooledConnection
	 * @throws java.sql.SQLException
	 */
	private PooledConnection getConnectionDirect() throws SQLException{
		if(null == freePool){
			throw new SQLException("PooledDataSource is closed!");
		}
		
		int maxActive = config.getMaxActive();
		if(maxActive <= 0 || maxActive < this.activeCount){
			//超过最大使用限制
			throw new SQLException("In used Connection is more than Max Active.");
		}
		
		PooledConnection conn = freePool.poll();
		if(null == conn){
			conn = this.newConnection();
		}
		activeCount++;
		return conn.open();
	}

}
