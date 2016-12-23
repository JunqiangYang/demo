package com.xiaoleilu.hutool.db.sql;

import com.xiaoleilu.hutool.db.sql.SqlBuilder.Direction;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * SQL排序对象
 * @author Looly
 *
 */
public class Order {
	/** 排序的字段 */
	private String field;
	/** 排序方式（正序还是反序） */
	private Direction direction;
	
	//---------------------------------------------------------- Constructor start
	public Order() {
	}
	
	/**
	 * 构造
	 * @param field 排序字段
	 */
	public Order(String field) {
		this.field = field;
	}
	
	/**
	 * 构造
	 * @param field 排序字段
	 * @param direction 排序方式
	 */
	public Order(String field, Direction direction) {
		this(field);
		this.direction = direction;
	}
	
	//---------------------------------------------------------- Constructor end

	//---------------------------------------------------------- Getters and Setters start
	/**
	 * @return 排序字段
	 */
	public String getField() {
		return this.field;
	}
	/**
	 * 设置排序字段
	 * @param field 排序字段
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return 排序方向
	 */
	public Direction getDirection() {
		return direction;
	}
	/**
	 * 设置排序方向
	 * @param direction 排序方向
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	//---------------------------------------------------------- Getters and Setters end
	
	@Override
	public String toString() {
		return StrUtil.builder().append(this.field).append(StrUtil.SPACE).append(null == direction ? StrUtil.EMPTY : direction).toString();
	}
}
