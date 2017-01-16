/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import index.IndexResult;  
import index.IndexUtils;  
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;  
import java.io.FileInputStream;  
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;  
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import org.apache.lucene.search.IndexSearcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class Test {
    private static java.util.Properties config = new java.util.Properties();
//    //存放索引文件  
//    private static String indexFile = "E:\\workspace2\\Test\\lucene_test\\poiIdext";  
//  
//    //存放id  
//    private static String storeIdFile = "E:\\workspace2\\Test\\lucene_test\\storeId.txt";
    
    private static String sqlPath="D:\\variable\\config\\config.property";
    
    private static String defaultPath="\\default\\default.property";
  
    public static void main(String[] args) throws Exception {
        // 获得自己配置的配置文件的整体路径
        defaultPath=System.getProperty("user.dir")+defaultPath;
//        // 加载自行配置文件
        config.load(new FileInputStream(defaultPath));  
//        // 获取自己的配置文件
        String indexFile=config.getProperty("indexFile");
        String storeIdFile=config.getProperty("storeIdFile");
        String classPath=config.getProperty("entityClassPath"); 
        String keyName=config.getProperty("keyName");
        //0. 创建增量索引  
        IndexUtils.buildIndex(indexFile, storeIdFile,sqlPath,defaultPath,classPath,keyName);  
            
//        //  按条件查询
        IndexSearcher indexSearcher = new IndexSearcher(indexFile); 
        //2.多条件查询  
        String[] fields = {"file_name", "file_pages"};  
        String[] keys = {IndexUtils.ik_CAnalyzer("电子文档管理系统"),"range,number,0,1"};   
        Date date1 = new Date();  
        List list = IndexUtils.queryByMultiKeys(indexSearcher, fields, keys,"index.IndexResult");  
        Date date2 = new Date();  
        System.out.println("耗时：" + (date2.getTime() - date1.getTime()) + "ms\n" + list.size()  
                + "条\n===============================多条件查询");  
        JSONArray jsonArray=printResults(list);  
        System.out.println(jsonArray.toString());
        //3.高亮显示  单字段查询  
//        System.out.println("\n\n");   
//        Date date1 = new Date();  
//        List list = IndexUtils.highlight(indexSearcher, key);  
//        Date date2 = new Date();      
//        System.out.println("耗时：" + (date2.getTime() - date1.getTime()) + "ms\n" + list.size()  
//                + "条\n======================================高亮显示");  
//        printResults(list);  
//       String[] fields = { "name", "citycode","address" }; 
//        //4. 多字段查询  
//        Date date1 = new Date();  
//        List list = IndexUtils.queryByMultiFileds(indexSearcher, fields, key);  
//        Date date2 = new Date();  
//        System.out.println("耗时：" + (date2.getTime() - date1.getTime()) + "ms\n" + list.size()  
//                + "条\n=====================================多字段查询");  
//        printResults(list);  
  
//        //5. 删除索引中的字段  根据id进行删除  
//        IndexUtils.deleteIndex(indexFile, "123");  
//         String key = IndexUtils.ik_CAnalyzer("数据保库海量数据");  
        
//        //1.单字段查询  
//        Date date1 = new Date();  
//        List<IndexResult> list = IndexUtils.queryByOneKey(indexSearcher, "name", key);  
//        Date date2 = new Date();  
//        System.out.println("耗时：" + (date2.getTime() - date1.getTime()) + "ms\n" + list.size()  
//                + "条=======================================单字段查询");  
////        //printResults(list);  
////  
    }  
    /**
     * 输出json类型的字符串
     * @param list
     * @return
     * @throws java.beans.IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static JSONArray printResults(List list) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException { 
        // 创建JSONArray对象
        JSONArray jsona=new JSONArray();
        if (list != null && list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) { 
                 Object obj=list.get(i);
                 // 获得Class
                 Class tClass =obj.getClass();
                 // 获得所有的属性
                  Field[] fields = tClass.getDeclaredFields();
                  // 创建json对象
                  JSONObject  json=new JSONObject();
                  // 循环遍历属性   
                  for(Field field:fields){
                     // 获得对应方法对象
                     PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);
                     //获得get方法
                     Method get = pd.getReadMethod();
                     // 添加值
                     json.put(field.getName(), get.invoke(obj, new Object[]{}));
                  }
                  jsona.add(json);
            }
        }  
        return jsona;
    }  
}
