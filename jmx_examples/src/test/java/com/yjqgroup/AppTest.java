package com.yjqgroup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        Set set = new HashSet();
        List lis= new ArrayList();
        List lis2= new LinkedList();
        ConcurrentHashMap a  ;
        TreeSet f;

        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {

        Set<Ent> set1 =new HashSet<Ent>() ;
        set1.add(new Ent("123"));
        set1.add(new Ent("234"));
        set1.add(new Ent("345"));
        set1.add(new Ent("456"));
         set1.add(new Ent("123"));
        Set<String> set2 =new HashSet<String>() ;
        set2.add("1234");
        set2.add("5678");

        //set1.addAll(set2) ;
       Ent e1 = new Ent("123") ;
       Ent e2 = new Ent("123") ;
        System.out.println(e1 == e2 ) ;
        System.out.println(e1.equals( e2)) ;
        System.out.println(set1.toString());

        assertTrue(true);
    }
}
