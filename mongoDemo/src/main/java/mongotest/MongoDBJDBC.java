package mongotest;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by admin on 2017-1-10 0010.
 */


public class MongoDBJDBC {
    public static void main( String args[] ){
        try{
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mongodemo1");
            System.out.println("Connect to database successfully");

        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

}
