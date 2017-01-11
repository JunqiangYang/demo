package spring_mogo.dao.daoImp;

import java.util.List;

import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import spring_mogo.dao.UserDao;
import spring_mongo.models.UserModel;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Component("UserDaoImp")
public class UserDaoImp extends AbstractBaseMongoTemplete implements UserDao {

	/**
	 * 查询所有数据
	 * 
	 * @author：tuzongxun
	 * @Title: findAll
	 * @Description: TODO
	 * @param @return
	 * @date May 13, 2016 3:10:29 PM
	 * @throws
	 */
	@Override
	public List<UserModel> findAll() {
		// 需要设置集合对应的尸体类和相应的集合名，从而查询结果直接映射
		List<UserModel> userList = mongoTemplate.findAll(UserModel.class, "user");
		return userList;
	}

	/**
	 * 新增数据
	 * 
	 * @author：tuzongxun
	 * @Title: insertUser
	 * @Description: TODO
	 * @param @param user
	 * @date May 13, 2016 3:10:45 PM
	 * @throws
	 */
	@Override
	public void insertUser(UserModel user) {
		// 设置需要插入到数据库的文档对象
		DBObject object = new BasicDBObject();
		object.put("name", user.getName());
		object.put("age", user.getAge());
		mongoTemplate.insert(object, "user");
	}

	/**
	 * 按条件删除数据
	 * 
	 * @author：tuzongxun
	 * @Title: removeUser
	 * @Description: TODO
	 * @param @param userName
	 * @date May 13, 2016 3:11:01 PM
	 * @throws
	 */
	@Override
	public void removeUser(String userName) {
		// 设置删除条件，如果条件内容为空则删除所有
		Query query = new Query();
		Criteria criteria = new Criteria("name");
		criteria.is(userName);
		query.addCriteria(criteria);
		mongoTemplate.remove(query, "user");
	}

	/**
	 * 修改数据
	 * 
	 * @author：tuzongxun
	 * @Title: updateUser
	 * @Description: TODO
	 * @param @param user
	 * @date May 13, 2016 3:11:12 PM
	 * @throws
	 */
	@Override
	public void updateUser(UserModel user) {
		// 设置修改条件
		Query query = new Query();
		Criteria criteria = new Criteria("name");
		criteria.is(user.getName());
		query.addCriteria(criteria);
		// 设置修改内容
		Update update = Update.update("age", user.getAge());
		// 参数：查询条件，更改结果，集合名
		mongoTemplate.updateFirst(query, update, "user");
	}

	/**
	 * 根据条件查询
	 * 
	 * @author：tuzongxun
	 * @Title: findForRequery
	 * @Description: TODO
	 * @param @param userName
	 * @date May 13, 2016 4:08:15 PM
	 * @throws
	 */
	@Override
	public List<UserModel> findForRequery(String userName) {
		Query query = new Query();
		Criteria criteria = new Criteria("name");
		criteria.regex(".*?" + "a" + ".*");
		// criteria.is(userName);
		// Pattern pattern = Pattern.compile("^.*a.*$",
		// Pattern.CASE_INSENSITIVE);
		// DBObject object = new BasicDBObject();
		// object.put("name", pattern);
		// criteria.is(object);
		// criteria.
		// query.addCriteria(criteria);
		// mongoTemplate.find(new Query(new Criteria(
		// "name").regex(".*?"+"张"+".*")).limit(9), User.class);
		// 查询条件，集合对应的实体类，集合名
		query.addCriteria(criteria);
		List<UserModel> userList = mongoTemplate.find(query, UserModel.class, "test");
		return userList;
	}

	/**
	 * mongodb简单分组查询
	 * 
	 * @author：tuzongxun
	 * @Title: mongoGroup
	 * @param @return
	 * @date Jul 19, 2016 8:36:19 AM
	 * @throws
	 */
	@Override
	public BasicDBList mongoGroup() {
		// TODO Auto-generated method stub
		GroupBy groupBy = GroupBy.key("age").initialDocument("{count:0}").reduceFunction("function(doc, out){out.count++}")
		    .finalizeFunction("function(out){return out;}");
		GroupByResults<UserModel> res = mongoTemplate.group("test", groupBy, UserModel.class);
		DBObject obj = res.getRawResults();
		BasicDBList dbList = (BasicDBList) obj.get("retval");
		return dbList;
	}

	@Override
	public void saveData(DBObject obj) {
		mongoTemplate.save(obj, "test");
	}

}
