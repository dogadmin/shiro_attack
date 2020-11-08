package vulgui.deser.plugins.keytest;


import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * @className KeyEcho
 * @Description TODO
 * @Author sunnylast0
 * @Date 2020/11/5 10:14
 * @Version 1.0
 **/
public class KeyEcho {
    public static Object getObject() {
        return new SimplePrincipalCollection();
    }
}
