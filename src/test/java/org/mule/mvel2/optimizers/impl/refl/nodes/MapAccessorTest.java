package org.mule.mvel2.optimizers.impl.refl.nodes;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class MapAccessorTest extends TestCase {

    private static String PROPERTY = "MY_PROPERTY";
    private static String VALUE = "MY_VALUE";


    public static void testGetValue(){
        MapAccessor mapAccessor = new MapAccessor(PROPERTY);
        Map<String, String> map = new HashMap<String, String>();
        map.put(PROPERTY, VALUE);
        Object result = mapAccessor.getValue(map, null, null);
        assertTrue(result instanceof String);
        assertTrue(result.equals(VALUE));
    }

    public static void testGetValueWithIncorrectCtx(){
        MapAccessor mapAccessor = new MapAccessor(PROPERTY);
        Object result = mapAccessor.getValue(PROPERTY, null, null);
        assertTrue(result == null);
    }
}
