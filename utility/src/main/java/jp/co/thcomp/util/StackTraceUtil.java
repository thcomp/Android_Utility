package jp.co.thcomp.util;

public class StackTraceUtil {
    /**
     * baseClassを使用しているところを取得
     * @param baseClass
     * @return
     */
    public static StackTraceElement getClassController(Class baseClass){
        StackTraceElement ret = null;
        Throwable th = new Throwable();

        th.fillInStackTrace();
        StackTraceElement[] elements = th.getStackTrace();

        if((elements != null) && (elements.length > 0)){
            String baseClassName = baseClass.getName();
            String ownClassName = StackTraceUtil.class.getName();

            for(StackTraceElement element : elements){
                String className = element.getClassName();

                if(!className.equals(baseClassName) && !className.equals(ownClassName)){
                    ret = element;
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * コール元のメソッドを使用しているところを取得
     * @return
     */
    public static StackTraceElement getCaller(){
        StackTraceElement ret = null;
        Throwable th = new Throwable();

        th.fillInStackTrace();
        StackTraceElement[] elements = th.getStackTrace();

        if((elements != null) && (elements.length > 0)){
            String ownClassName = StackTraceUtil.class.getName();
            int otherClassMethodCount = 0;

            for(StackTraceElement element : elements){
                String className = element.getClassName();

                if(!className.equals(ownClassName)){
                    otherClassMethodCount++;

                    if(otherClassMethodCount > 1){
                        // otherClassMethodCountが1のとき、StackTraceUtil系メソッドのコール元で、
                        // otherClassMethodCountが2のとき、そのコール元となる
                        ret = element;
                        break;
                    }
                }
            }
        }

        return ret;
    }
}
