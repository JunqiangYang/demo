package com.yjqgroup.guava;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.yjqgroup.guava.bean.Type;
import com.yjqgroup.guava.bean.TypeThatsTooLongForItsOwnGood;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Junqiang.Yang
 * @create 2017-07-10 14:03
 **/

public class GuavaStudy1 {


    public static void main(String[] args) {
        List<TypeThatsTooLongForItsOwnGood> list = Lists.newArrayList();
        List<TypeThatsTooLongForItsOwnGood> list2 = new ArrayList<>();
        Map<String, String> map = Maps.newLinkedHashMap();

        Type[] elements = new Type[5] ;
        Set<Type> copySet = Sets.newHashSet(elements);
        List<String> theseElements = Lists.newArrayList("alpha", "beta", "gamma");

        List<Type> exactly100 = Lists.newArrayListWithCapacity(100);
        List<Type> approx100 = Lists.newArrayListWithExpectedSize(100);
        Set<Type> approx100Set = Sets.newHashSetWithExpectedSize(100);

        Iterable<Integer> concatenated = Iterables.concat(
                Ints.asList(1, 2, 3),
                Ints.asList(4, 5, 6)); // concatenated包括元素 1, 2, 3, 4, 5, 6
        //String lastAdded = Iterables.getLast(myLinkedHashSet);
        //String theElement = Iterables.getOnlyElement(thisSetIsDefinitelyASingleton);
        //如果set不是单元素集，就会出错了！


    }

}
