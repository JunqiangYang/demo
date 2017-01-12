/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package index;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;  
import java.io.IOException;  
import java.io.Reader;  
import java.io.StringReader;  
import java.lang.reflect.Field;  
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;  
import java.util.ArrayList;  
import java.util.Date;  
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.StopFilter;  
import org.apache.lucene.analysis.Token;  
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.standard.StandardAnalyzer;  
import org.apache.lucene.document.Document;  
import org.apache.lucene.index.CorruptIndexException;  
import org.apache.lucene.index.IndexReader;  
import org.apache.lucene.index.Term;  
import org.apache.lucene.queryParser.MultiFieldQueryParser;  
import org.apache.lucene.queryParser.ParseException;  
import org.apache.lucene.queryParser.QueryParser;  
import org.apache.lucene.search.BooleanClause;  
import org.apache.lucene.search.BooleanQuery;  
import org.apache.lucene.search.Hits;  
import org.apache.lucene.search.IndexSearcher;  
import org.apache.lucene.search.Query;  
import org.apache.lucene.search.RangeQuery;  
import org.apache.lucene.search.ScoreDoc;  
import org.apache.lucene.search.TopDocCollector;  
import org.apache.lucene.search.highlight.Highlighter;  
import org.apache.lucene.search.highlight.QueryScorer;  
import org.apache.lucene.search.highlight.SimpleFragmenter;  
import org.apache.lucene.search.highlight.SimpleHTMLFormatter; 
import org.mira.lucene.analysis.IK_CAnalyzer; 
import org.springframework.util.NumberUtils;
/**
 *
 * @author Administrator
 */
public class IndexUtils {
     //0. 创建增量索引  
    public static void buildIndex(String indexFile, String storeIdFile,String sqlPath,String defaultPath,String classPath,String keyName) {  
        IncrementIndex.buildIndex(indexFile, storeIdFile,sqlPath,defaultPath,classPath,keyName);  
    }  
  
    //1. 单字段查询  
    @SuppressWarnings("deprecation")  
    public static List queryByOneKey(IndexSearcher indexSearcher, String field,  
            String key) throws ClassNotFoundException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException {  
        try {  
            Date date1 = new Date();  
            QueryParser queryParser = new QueryParser(field, new StandardAnalyzer());  
            Query query = queryParser.parse(key);  
            Hits hits = indexSearcher.search(query);  
            Date date2 = new Date();  
            System.out.println("耗时：" + (date2.getTime() - date1.getTime()) + "ms");  
            List list = new ArrayList();  
            for (int i = 0; i < hits.length(); i++) {  
                list.add(getIndexResult(hits.doc(i),"index.IndexResult"));  
            }  
            return list;  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    //2. 多条件查询。这里实现的是and操作  
    //注：要查询的字段必须是index的  
    //即doc.add(new Field("pid", rs.getString("pid"), Field.Store.YES,Field.Index.TOKENIZED));     
    @SuppressWarnings("deprecation")  
    public static List queryByMultiKeys(IndexSearcher indexSearcher, String[] fields,  
            String[] keys,String entityCLassPath) throws ClassNotFoundException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException { 
        try {  
            BooleanQuery m_BooleanQuery = new BooleanQuery();  
            if (keys != null && keys.length > 0) {  
                for (int i = 0; i < keys.length; i++) {  
                    // 创建查询对象
                    Query query = null; 
                    // 根据规范进行查询可知，查询无非就三种，相等，相似，范围
                    if(StringUtils.isNotBlank(keys[i])){
                       // 进行分割
                       String[] keyFlag=keys[i].split(",");
                       // 判断第一个的标识符 range代表着是范围条件
                       if("range".equals(keyFlag[0])){
                           // 判断是时间比较还是数值比较，number为数值比较，date为时间比较
                           if("number".equals(keyFlag[1])){
                               query =new RangeQuery(new Term(fields[i], keyFlag[2]),new Term(fields[i],keyFlag[3]),true);
                           }else{
                               query =new RangeQuery(new Term(fields[i], keyFlag[2]),new Term(fields[i],keyFlag[3]),true);  
                           }
                       }else{
                           QueryParser queryParser = new QueryParser(fields[i], new StandardAnalyzer()); 
                           query = queryParser.parse(keys[i]);  
                       }
                    }; 
                    m_BooleanQuery.add(query, BooleanClause.Occur.MUST);//and操作  
                }  
                Hits hits = indexSearcher.search(m_BooleanQuery);  
                List list = new ArrayList();  
                for (int i = 0; i < hits.length(); i++) {  
                    list.add(getIndexResult(hits.doc(i),entityCLassPath));  
                }  
                return list;  
            }  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    //3.高亮显示  实现了单条件查询  
    //可改造为多条件查询  
    public static List highlight(IndexSearcher indexSearcher, String key) throws ClassNotFoundException {  
        try {  
            QueryParser queryParser = new QueryParser("name", new StandardAnalyzer());  
            Query query = queryParser.parse(key);  
            TopDocCollector collector = new TopDocCollector(800);  
            indexSearcher.search(query, collector);  
            ScoreDoc[] hits = collector.topDocs().scoreDocs;  
  
            Highlighter highlighter = null;  
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>",  
                    "</font>");  
            highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
            highlighter.setTextFragmenter(new SimpleFragmenter(200));  
            List list = new ArrayList();  
            Document doc;  
            for (int i = 0; i < hits.length; i++) {  
                //System.out.println(hits[i].score);  
                doc = indexSearcher.doc(hits[i].doc);  
                TokenStream tokenStream = new StandardAnalyzer().tokenStream("name",  
                        new StringReader(doc.get("name")));  
//                IndexResult ir = getIndexResult(doc,"index.IndexResult");  
//                ir.setName(highlighter.getBestFragment(tokenStream, doc.get("name")));  
//                list.add(ir);  
            }  
            return list;  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
  
    }  
  
    //4. 多字段查询  
    @SuppressWarnings("deprecation")  
    public static List queryByMultiFileds(IndexSearcher indexSearcher,  
            String[] fields, String key) throws ClassNotFoundException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException {  
        try {  
            MultiFieldQueryParser mfq = new MultiFieldQueryParser(fields, new StandardAnalyzer());  
            Query query = mfq.parse(key);  
            Hits hits = indexSearcher.search(query);  
            List<IndexResult> list = new ArrayList<IndexResult>();  
            for (int i = 0; i < hits.length(); i++) {  
                list.add((IndexResult) getIndexResult(hits.doc(i),"index.IndexResult"));  
            }  
  
            return list;  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    //5. 删除索引  
    public static void deleteIndex(String indexFile, String id) throws CorruptIndexException,  
            IOException {  
        IndexReader indexReader = IndexReader.open(indexFile);  
        indexReader.deleteDocuments(new Term("id", id));  
        indexReader.close();  
    }  
  
    //6. 一元分词  
    @SuppressWarnings("deprecation")  
    public static String Standard_Analyzer(String str) {  
        Analyzer analyzer = new StandardAnalyzer();  
        Reader r = new StringReader(str);  
        StopFilter sf = (StopFilter) analyzer.tokenStream("", r);  
        System.out.println("=====StandardAnalyzer====");  
        System.out.println("分析方法：默认没有词只有字（一元分词）");  
        Token t;  
        String results = "";  
        try {  
            while ((t = sf.next()) != null) {  
                System.out.println(t.termText());  
                results = results + " " + t.termText();  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return results;  
    }  
  
    //7. 字典分词  
    @SuppressWarnings("deprecation")  
    public static String ik_CAnalyzer(String str) {  
        Analyzer analyzer = new IK_CAnalyzer();  
        Reader r = new StringReader(str);  
        TokenStream ts = (TokenStream) analyzer.tokenStream("", r);  
        System.out.println("=====IK_CAnalyzer====");  
        System.out.println("分析方法:字典分词,正反双向搜索");  
        Token t;  
        String results = "";  
        try {  
            while ((t = ts.next()) != null) {  
                System.out.println(t.termText());  
                results = results + " " + t.termText();  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return results;  
    }  
    //在结果中搜索  
    public static void queryFromResults() {  
  
    }  
   /**
    * 利用类反射得出结果
    * @param doc
    * @param entityPath
    * @return
    * @throws ClassNotFoundException
    * @throws java.beans.IntrospectionException
    * @throws IllegalAccessException
    * @throws IllegalArgumentException
    * @throws java.lang.reflect.InvocationTargetException
    * @throws InstantiationException
    * @throws NoSuchMethodException 
    */  
    public static Object getIndexResult(Document doc,String entityPath) throws ClassNotFoundException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException { 
        // 进行类反射
        Class c2 = Class.forName(entityPath);
        // 获得示例对象
        Object obj=c2.getConstructor(new Class[]{}).newInstance();
        // 获得类中的所有属性
        Field[] fields=c2.getDeclaredFields();
        // 循环遍历
        for(Field field:fields){
            // 获得描述
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), c2);
            // 获得对应的set和get方法
            Method method=pd.getWriteMethod();
            // 调用方法
            method.invoke(obj,new Object[]{doc.get(field.getName())});
        }  
        return obj;  
    }  
    public static void main(String argsp[]) throws ClassNotFoundException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException{
        Document doc = null;
       getIndexResult(doc,"index.IndexResult");
    }
}
