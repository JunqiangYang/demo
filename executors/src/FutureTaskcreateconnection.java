import java.sql.Connection;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * Created by admin on 2016-12-22 0022.
 */


public class FutureTaskcreateconnection {
    private ConcurrentHashMap<String,FutureTask<Connection>> connectionPool = new ConcurrentHashMap<String, FutureTask<Connection>>();

    public Connection getConnection(String key) throws Exception{
        FutureTask<Connection>connectionTask=connectionPool.get(key);
        if(connectionTask!=null){
            return connectionTask.get();
        }
        else{
            Callable<Connection> callable = new Callable<Connection>(){
                @Override
                public Connection call() throws Exception {
                    // TODO Auto-generated method stub
                    return createConnection();
                }
            };
            FutureTask<Connection>newTask = new FutureTask<Connection>(callable);
            connectionTask = connectionPool.putIfAbsent(key, newTask);
            if(connectionTask==null){
                connectionTask = newTask;
                connectionTask.run();
            }
            return connectionTask.get();
        }
    }



    private Connection createConnection(){
        return null;
    }


}
