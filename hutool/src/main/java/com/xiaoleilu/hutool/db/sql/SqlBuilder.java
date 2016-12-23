package com.xiaoleilu.hutool.db.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * SQL构建器<br>
 * 首先拼接SQL语句，值使用 ? 占位<br>
 * 调用getParamValues()方法获得占位符对应的值
 * 
 * @author Looly
 *
 */
public class SqlBuilder {
	private final static Log log = StaticLog.get();
	
	//--------------------------------------------------------------- Static methods start
	/**
	 * 创建SQL构建器
	 * @return SQL构建器
	 */
	public static SqlBuilder create(){
		return new SqlBuilder();
	}
	
	/**
	 * 创建SQL构建器
	 * @param wrapper 包装器
	 * @return SQL构建器
	 */
	public static SqlBuilder create(Wrapper wrapper){
		return new SqlBuilder(wrapper);
	}
	//--------------------------------------------------------------- Static methods end
	
	//--------------------------------------------------------------- Enums start
	/**
	 * 逻辑运算符
	 * @author Looly
	 *
	 */
	public static enum LogicalOperator{
		/** 且，两个条件都满足 */
		AND,
		/** 或，满足多个条件的一个即可 */
		OR
	}
	
	/**
	 * 排序方式（升序或者降序）
	 * @author Looly
	 *
	 */
	public static enum Direction{
		/** 升序 */
		ASC,
		/** 降序 */
		DESC
	}
	
	/**
	 * SQL中多表关联用的关键字
	 * @author Looly
	 *
	 */
	public static enum Join{
		/** 如果表中有至少一个匹配，则返回行 */
		INNER,
		/** 即使右表中没有匹配，也从左表返回所有的行 */
		LEFT,
		/** 即使左表中没有匹配，也从右表返回所有的行 */
		RIGHT,
		/** 只要其中一个表中存在匹配，就返回行 */
		FULL
	}
	//--------------------------------------------------------------- Enums end
	
	final private StringBuilder sql = new StringBuilder();
	/** 占位符对应的值列表 */
	final private List<Object> paramValues = new ArrayList<Object>();
	/** 包装器 */
	private Wrapper wrapper;
	
	//--------------------------------------------------------------- Constructor start
	public SqlBuilder() {
	}
	
	public SqlBuilder(Wrapper wrapper) {
		this.wrapper = wrapper;
	}
	//--------------------------------------------------------------- Constructor end
	
	//--------------------------------------------------------------- Builder start
	/**
	 * 插入
	 * @param entity 实体
	 * @return 自己
	 */
	public SqlBuilder insert(Entity entity){
		//验证
		DbUtil.validateEntity(entity);
		
		if(null != wrapper) {
			//包装字段名
			entity = wrapper.wrap(entity);
		}
		
		sql.append("INSERT INTO ").append(entity.getTableName()).append(" (");

		final StringBuilder placeHolder = new StringBuilder(") VALUES (");

		for (Entry<String, Object> entry : entity.entrySet()) {
			//非第一个参数，追加逗号
			if (paramValues.size() > 0) {
				sql.append(", ");
				placeHolder.append(", ");
			}
			
			sql.append(entry.getKey());
			placeHolder.append("?");
			paramValues.add(entry.getValue());
		}
		sql.append(placeHolder.toString()).append(")");
		
		return this;
	}
	
	/**
	 * 删除
	 * @param tableName 表名
	 * @return 自己
	 */
	public SqlBuilder delete(String tableName){
		if(StrUtil.isBlank(tableName)) {
			throw new DbRuntimeException("Table name is blank !");
		}
		
		if(null != wrapper) {
			//包装表名
			tableName = wrapper.wrap(tableName);
		}
		
		sql.append("DELETE FROM ").append(tableName);
		
		return this;
	}
	
	/**
	 * 更新
	 * @param entity 要更新的实体
	 * @return 自己
	 */
	public SqlBuilder update(Entity entity){
		//验证
		DbUtil.validateEntity(entity);
		
		if(null != wrapper) {
			//包装字段名
			entity = wrapper.wrap(entity);
		}
		
		sql.append("UPDATE ").append(entity.getTableName()).append(" SET ");
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (paramValues.size() > 0) {
				sql.append(", ");
			}
			sql.append(entry.getKey()).append(" = ? ");
			paramValues.add(entry.getValue());
		}
		
		return this;
	}
	
	/**
	 * 查询
	 * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(boolean isDistinct, String... fields){
		return select(isDistinct, Arrays.asList(fields));
	}
	
	/**
	 * 查询
	 * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(boolean isDistinct, Collection<String> fields){
		sql.append("SELECT ");
		if(isDistinct) {
			sql.append("DISTINCT ");
		}
		
		if (CollectionUtil.isEmpty(fields)) {
			sql.append("*");
		} else {
			if(null != wrapper) {
				//包装字段名
				fields = wrapper.wrap(fields);
			}
			sql.append(CollectionUtil.join(fields, StrUtil.COMMA));
		}
		
		return this;
	}
	
	/**
	 * 查询（非Distinct）
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(String... fields){
		return select(false, fields);
	}
	
	/**
	 * 查询（非Distinct）
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(Collection<String> fields){
		return select(false, fields);
	}
	
	/**
	 * 添加 from语句
	 * @param tableNames 表名列表（多个表名用于多表查询）
	 * @return 自己
	 */
	public SqlBuilder from(String... tableNames){
		if(CollectionUtil.isEmpty(tableNames) || StrUtil.hasBlank(tableNames)) {
			throw new DbRuntimeException("Table name is blank in table names !");
		}
		
		if(null != wrapper) {
			//包装表名
			tableNames = wrapper.wrap(tableNames);
		}
		
		sql.append(" FROM ").append(CollectionUtil.join(tableNames, StrUtil.COMMA));
		
		return this;
	}
	
	/**
	 * 添加Where语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 * 
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件，当条件为空时，只添加WHERE关键字
	 * @return 自己
	 */
	public SqlBuilder where(LogicalOperator logicalOperator, Condition... conditions){
		if(CollectionUtil.isNotEmpty(conditions)) {
			sql.append(" WHERE ");
			if(null != wrapper) {
				//包装字段名
				conditions = wrapper.wrap(conditions);
			}
			
			sql.append(buildCondition(logicalOperator, conditions));
		}
		
		return this;
	}
	
	/**
	 * 多值选择
	 * @param field 字段名
	 * @param values 值列表
	 * @return 自身
	 */
	@SuppressWarnings("unchecked")
	public <T> SqlBuilder in(String field, T... values) {
		sql.append(wrapper.wrap(field)).append(" IN ").append("(").append(CollectionUtil.join(values, StrUtil.COMMA)).append(")");
		return this;
	}
	
	/**
	 * 分组
	 * @param fields 字段
	 * @return 自己
	 */
	public SqlBuilder groupBy(String... fields){
		if(CollectionUtil.isNotEmpty(fields)) {
			if(null != wrapper) {
				//包装字段名
				fields = wrapper.wrap(fields);
			}
			
			sql.append(" GROUP BY ").append(CollectionUtil.join(fields, StrUtil.COMMA));
		}
		
		return this;
	}
	
	/**
	 * 添加Having语句
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件
	 * @return 自己
	 */
	public SqlBuilder having(LogicalOperator logicalOperator, Condition... conditions){
		if(CollectionUtil.isNotEmpty(conditions)) {
			if(null != wrapper) {
				//包装字段名
				conditions = wrapper.wrap(conditions);
			}
			
			sql.append(" HAVING ").append(buildCondition(logicalOperator, conditions));
		}
		
		return this;
	}
	
	/**
	 * 排序
	 * @param orders 排序对象
	 * @return 自己
	 */
	public SqlBuilder orderBy(Order... orders){
		if(CollectionUtil.isEmpty(orders)){
			return this;
		}
		
		sql.append(" ORDER BY ");
		String field = null;
		boolean isFirst = true;
		for (Order order : orders) {
			if(null != wrapper) {
				//包装字段名
				field = wrapper.wrap(order.getField());
			}
			if(StrUtil.isBlank(field)){
				continue;
			}
			
			//只有在非第一项前添加逗号
			if (isFirst) {
				isFirst = false;
			} else {
				sql.append(StrUtil.COMMA);
			}
			sql.append(field);
			final Direction direction = order.getDirection();
			if(null != direction){
				sql.append(StrUtil.SPACE).append(direction);
			}
		}
		return this;
	}
	
	/**
	 * 多表关联
	 * @param tableName 被关联的表名
	 * @param join 内联方式
	 * @return 自己
	 */
	public SqlBuilder join(String tableName, Join join){
		if(StrUtil.isBlank(tableName)) {
			throw new DbRuntimeException("Table name is blank !");
		}
		
		if(null != join) {
			sql.append(StrUtil.SPACE).append(join);
		}
		
		if(null != wrapper) {
			//包装表名
			tableName = wrapper.wrap(tableName);
		}
		
		sql.append(" JOIN ").append(tableName);
		
		return this;
	}
	
	/**
	 * 配合JOIN的 ON语句，多表关联的条件语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件
	 * @return 自己
	 */
	public SqlBuilder on(LogicalOperator logicalOperator, Condition... conditions){
		if(CollectionUtil.isNotEmpty(conditions)) {
			if(null != wrapper) {
				//包装字段名
				conditions = wrapper.wrap(conditions);
			}
			
			sql.append(" ON ").append(buildCondition(logicalOperator, conditions));
		}
		
		return this;
	}
	
	/**
	 * 追加SQL其它部分
	 * @param sqlPart SQL其它部分
	 * @return 自己
	 */
	public SqlBuilder append(Object sqlPart){
		if(null != sqlPart) {
			this.sql.append(sqlPart);
		}
		
		return this;
	}
	//--------------------------------------------------------------- Builder end
	
	/**
	 * 获得占位符对应的值列表<br>
	 * @return 占位符对应的值列表
	 */
	public List<Object> getParamValues() {
		return paramValues;
	}
	
	/**
	 * 获得占位符对应的值列表<br>
	 * @return 占位符对应的值列表
	 */
	public Object[] getParamValueArray() {
		return paramValues.toArray(new Object[paramValues.size()]);
	}
	
	/**
	 * 构建
	 * @return 构建好的SQL语句
	 */
	public String build(){
		return this.build(false);
	}
	
	/**
	 * 构建
	 * @param isShowDebugSql 显示SQL的debug日志
	 * @return 构建好的SQL语句
	 */
	public String build(boolean isShowDebugSql){
		final String sqlStr = this.sql.toString().trim();
		if(isShowDebugSql){
			log.debug("\n{}", sqlStr);
		}
		return sqlStr;
	}
	
	@Override
	public String toString() {
		return this.build();
	}
	
	//--------------------------------------------------------------- private method start
	/**
	 * 构建组合条件
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件对象
	 * @return 构建后的SQL语句条件部分
	 */
	private String buildCondition(LogicalOperator logicalOperator, Condition... conditions){
		if(CollectionUtil.isEmpty(conditions)) {
			return StrUtil.EMPTY;
		}
		if(null == logicalOperator) {
			logicalOperator = LogicalOperator.AND;
		}
		
		final StringBuilder conditionStr = new StringBuilder();
		boolean isFirst = true;
		for (Condition condition : conditions) {
			//添加逻辑运算符
			if(isFirst){
				isFirst = false;
			}else {
				conditionStr.append(StrUtil.SPACE).append(logicalOperator).append(StrUtil.SPACE);
			}
			
			//添加条件表达式
			conditionStr.append(condition.getField()).append(StrUtil.SPACE).append(condition.getOperator());
			
			if(condition.isPlaceHolder()) {
				//使用条件表达式占位符
				conditionStr.append(" ?");
				paramValues.add(condition.getValue());
			}else {
				//直接使用条件值
				conditionStr.append(condition.getValue());
			}
		}
		
		return conditionStr.toString();
	}
	//--------------------------------------------------------------- private method end
}
