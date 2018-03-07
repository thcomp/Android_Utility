package jp.co.thcomp.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class StackTraceUtilTest {
    @Test
    public void test() throws Exception {
        caller0();
        new CallerCL4().caller();
    }

    private void caller0(){
        caller1();
    }

    private void caller1(){
        caller2();
    }

    private void caller2(){
        caller3();
    }

    private void caller3(){
        caller4();
    }

    private void caller4(){
        StackTraceElement element;

        element = StackTraceUtil.getCaller();
        assertTrue(element.getClassName().equals(StackTraceUtilTest.class.getName()));
        assertTrue(element.getMethodName().equals("caller3"));
        element = StackTraceUtil.getClassController(this.getClass());
        assertTrue(!element.getClassName().equals(StackTraceUtil.class.getName()));
        assertTrue(!element.getClassName().equals(StackTraceUtilTest.class.getName()));
    }

    abstract private static class CallerCL{
        protected void caller(){
            caller1();
        }

        protected void caller1(){
            StackTraceElement element;

            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
            element = StackTraceUtil.getClassController(this.getClass());
            assertTrue(element.getClassName().equals(CallerCL1.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
        }
    }

    static class CallerCL1 extends CallerCL{
        @Override
        protected void caller() {
            super.caller();

            StackTraceElement element;
            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(CallerCL2.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
            element = StackTraceUtil.getClassController(CallerCL1.class);
            assertTrue(element.getClassName().equals(CallerCL2.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
        }

        @Override
        protected void caller1() {
            StackTraceElement element;

            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
            element = StackTraceUtil.getClassController(CallerCL1.class);
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
        }
    }

    static class CallerCL2 extends CallerCL1{
        @Override
        protected void caller() {
            super.caller();

            StackTraceElement element;
            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(CallerCL3.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
            element = StackTraceUtil.getClassController(CallerCL2.class);
            assertTrue(element.getClassName().equals(CallerCL3.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
        }

        @Override
        protected void caller1() {
            StackTraceElement element;

            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
            element = StackTraceUtil.getClassController(CallerCL2.class);
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
        }
    }

    static class CallerCL3 extends CallerCL2{
        @Override
        protected void caller() {
            super.caller();

            StackTraceElement element;
            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(CallerCL4.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
            element = StackTraceUtil.getClassController(CallerCL3.class);
            assertTrue(element.getClassName().equals(CallerCL4.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
        }

        @Override
        protected void caller1() {
            StackTraceElement element;

            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
            element = StackTraceUtil.getClassController(CallerCL3.class);
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
        }
    }

    static class CallerCL4 extends CallerCL3{
        @Override
        protected void caller() {
            super.caller();

            StackTraceElement element;
            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(StackTraceUtilTest.class.getName()));
            assertTrue(element.getMethodName().equals("test"));
            element = StackTraceUtil.getClassController(CallerCL4.class);
            assertTrue(element.getClassName().equals(StackTraceUtilTest.class.getName()));
            assertTrue(element.getMethodName().equals("test"));
        }

        @Override
        protected void caller1() {
            StackTraceElement element;

            element = StackTraceUtil.getCaller();
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
            element = StackTraceUtil.getClassController(CallerCL4.class);
            assertTrue(element.getClassName().equals(CallerCL.class.getName()));
            assertTrue(element.getMethodName().equals("caller"));
        }
    }
}