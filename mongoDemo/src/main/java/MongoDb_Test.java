/**
 * Created by admin on 2017-1-10 0010.
 */
import java.net.UnknownHostException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDb_Test {
    public static void main(String[] args) {

        try {
            // 实例化Mongo对象，连接27017端口
            Mongo mongo = new Mongo("localhost", 27017);
            // 连接名为yourdb的数据库，假如数据库不存在的话，mongodb会自动建立
            DB db = mongo.getDB("yourdb");
            // Get collection from MongoDB, database named "yourDB"
            // 从Mongodb中获得名为yourColleection的数据集合，如果该数据集合不存在，Mongodb会为其新建立
            DBCollection collection = db.getCollection("yourCollection");
            // 使用BasicDBObject对象创建一个mongodb的document,并给予赋值。
            BasicDBObject document = new BasicDBObject();
            document.put("id", 1001);
            document.put("msg", "hello world mongoDB in Java");
            // 将新建立的document保存到collection中去
            collection.insert(document);
            // 创建要查询的document
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("id", 1001);
            // 使用collection的find方法查找document
            DBCursor cursor = collection.find(searchQuery);
            // 循环输出结果
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
