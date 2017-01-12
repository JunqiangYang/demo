/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package index;

import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileReader;  
import java.io.FileWriter;  
import java.io.IOException;  
import java.io.PrintWriter;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  
import java.util.Date;  
import java.util.HashMap;    
import java.util.Iterator;  
import java.util.Map;  
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.standard.StandardAnalyzer;  
import org.apache.lucene.document.Document;  
import org.apache.lucene.document.Field;  
import org.apache.lucene.index.IndexWriter;  



//增量索引  
/* 
 * 实现思路:首次查询数据库表所有记录，对每条记录建立索引，并将最后一条记录的id存储到storeId.txt文件中 
 *         当新插入一条记录时，再建立索引时不必再对所有数据重新建一遍索引， 
 *         可根据存放在storeId.txt文件中的id查出新插入的数据，只对新增的数据新建索引，并把新增的索引追加到原来的索引文件中 
 * */  
public class IncrementIndex {  
  private static java.util.Properties config = new java.util.Properties();
  
  
  
    public static void buildIndex(String indexFile, String storeIdFile,String sqlPath,String defaultPath,String classPath,String keyName) {  
        try {  
            String path = indexFile;//索引文件的存放路径  
            String storeIdPath = storeIdFile;//存储ID的路径  
            String storeId = "";  
            storeId = getStoreId(storeIdPath);  
            ResultSet rs = getResult(storeId,sqlPath,defaultPath);  
            indexBuilding(path, storeIdPath, rs,classPath,keyName);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
     /**
     * 构建索引
     * @param path
     * @param storeIdPath
     * @param rs
     * @return 
     */
    public static boolean indexBuilding(String path, String storeIdPath, ResultSet rs,String classPath,String keyName) throws SQLException {  
        try {  
            Analyzer luceneAnalyzer = new StandardAnalyzer();  
            // 取得存储起来的ID，以判定是增量索引还是重新索引  
            boolean isEmpty = true;  
            try {  
                File file = new File(storeIdPath);  
                if (!file.exists()) {  
                    file.createNewFile();  
                }  
                FileReader fr = new FileReader(storeIdPath);  
                BufferedReader br = new BufferedReader(fr);  
                if (br.readLine() != null) {  
                    isEmpty = false;  
                }  
                br.close();  
                fr.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            //isEmpty=false表示增量索引  
            IndexWriter writer = new IndexWriter(path, luceneAnalyzer, isEmpty);  
            String storeId = "";  
            boolean indexFlag = false; 
            // 获得自定义类里面的属性
            Class c2 = Class.forName(classPath);
            // 获得类中的所有属性
            java.lang.reflect.Field[] fields=c2.getDeclaredFields();
            while (rs.next()) {
                // 创建存储的list的集合    
                Map map=new HashMap();
                // 循环遍历属性的名字
                for(java.lang.reflect.Field field:fields){
                    // 给关键字赋值 
                    if(keyName.equals(field.getName())){
                       storeId=rs.getString(field.getName().toUpperCase());
                    }
                    // 判断此字段是否从数据库中取出
                    if(whetherExist(field.getName().toUpperCase(),rs))
                    {
                       map.put(field.getName(),rs.getString(field.getName().toUpperCase()));
                    }   
                }       
                writer.addDocument(Document(map));  
                indexFlag = true;  
            }  
            writer.optimize();  
            writer.close();  
            if (indexFlag) {  
                // 将最后一个的ID存到磁盘文件中  
                writeStoreId(storeIdPath, storeId);  
            }  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.println("出错了" + e.getClass() + "\n   错误信息为:   " + e.getMessage());  
            return false;  
        }finally{
            if(null!=rs){
               rs.close();
            }
        }
  
    }  
   // 取得存储在磁盘中的ID  
    public static String getStoreId(String path) {  
        String storeId = "";  
        try {  
            File file = new File(path);  
            if (!file.exists()) {  
                file.createNewFile();  
            }  
            FileReader fr = new FileReader(path);  
            BufferedReader br = new BufferedReader(fr);  
            storeId = br.readLine();  
            if (storeId == null || storeId == "") storeId = "0";  
            br.close();  
            fr.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return storeId;  
    }  
    public static ResultSet getResult(String storeId,String sqlPath,String defaultPath) throws Exception {
        // 进行读取配置文件
        config.load(new FileInputStream(sqlPath));
        // 驱动类型
        String jdbcDriverClassName = config.getProperty("jdbcDriverClassName");
        String jdbcUrl = "jdbc:db2://";
        if(jdbcDriverClassName.contains("db2")){
            jdbcUrl = "jdbc:db2://";
        }else if(jdbcDriverClassName.contains("mysql")){
            jdbcUrl = "jdbc:mysql://";
        }else if(jdbcDriverClassName.contains("sqlserver")){
            jdbcUrl = "jdbc:sqlserver://";
        }else if(jdbcDriverClassName.contains("sourceforge")){
            jdbcUrl = "jdbc:jtds:sqlserver://";
        }else if(jdbcDriverClassName.contains("oracle")){
            jdbcUrl = "jdbc:oracle:thin:@";
        }
        // 拼凑url
        jdbcUrl = jdbcUrl + config.getProperty("iampDBIp") + ":" + config.getProperty("iampDBPort") + "/" + config.getProperty("iampDBName");
        String jdbcUsername = config.getProperty("iampDBUserName");
        String jdbcPassword = config.getProperty("iampDBPassword");
        // 驱动数据库
        Class.forName(jdbcDriverClassName).newInstance(); 
        Connection conn = DriverManager.getConnection(jdbcUrl,jdbcUsername,jdbcPassword);  
        // 获取数据库语句
        config.load(new FileInputStream(defaultPath));
        PreparedStatement stmt=null;
        ResultSet rs=null;
        String sql;
        if("0".equals(storeId)){
             sql = config.getProperty("nullSql"); 
             stmt = conn.prepareStatement(sql);
             rs = stmt.executeQuery();
        }else{
            sql = config.getProperty("entitySql"); 
            
            System.out.print("sql="+sql);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,storeId);
            // 得到结果集
            rs = stmt.executeQuery(); 
        }
        
        return rs;  
    }
    /**
     * 写入主键id
     * @param path
     * @param storeId
     * @return 
     */
    public static boolean writeStoreId(String path,String storeId) {  
        boolean b = false;  
        try {  
            File file = new File(path);  
            if (!file.exists()) {  
                file.createNewFile();  
            }  
            FileWriter fw = new FileWriter(path);  
            PrintWriter out = new PrintWriter(fw);  
            out.write(storeId);  
            out.close();  
            fw.close();  
            b = true;  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return b;  
    }
    /**
     * 写入文件方法
     * 
     */
     public static Document Document(Map map) {  
        Document doc = new Document(); 
        // 循环便利list
        if(map!=null){
            // 循环遍历map
             for (Object key :map.keySet()){ 
                doc.add(new Field(key.toString(), map.get(key).toString(), Field.Store.YES, Field.Index.TOKENIZED));
            }
       }    
       return doc;  
    }
    /**
     * 判断得出的结果集是否存在这个键值
     * @param key 键名称
     * @param res 结果集
     * @return 
     */
    public static boolean whetherExist(String key,ResultSet res){
       try{
            // 进行判断
            if(res.findColumn(key)>0){
                return true;
            }
        }catch(SQLException e){
            return false;
        }
       return false;
    }
}  
