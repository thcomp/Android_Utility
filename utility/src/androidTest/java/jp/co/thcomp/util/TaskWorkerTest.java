package jp.co.thcomp.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import jp.co.thcomp.http_abstract_layer.HttpAccessLayer;
import jp.co.thcomp.http_abstract_layer.Response;
import jp.co.thcomp.http_abstract_layer.ResponseCallback;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TaskWorkerTest {
    @Test
    public void useTaskWorker() throws Exception {
        // Context of the app under test.
        final Context appContext = InstrumentationRegistry.getTargetContext();

        TaskWorker taskWorker = new TaskWorker(appContext);
        taskWorker.addTask(new TaskWorker.Task() {
            @Override
            public Object run(Context context, Object param, final TaskWorker.TaskController taskController) throws TaskWorker.TaskException {
                taskController.sync();

                HttpAccessLayer accessLayer = HttpAccessLayer.getInstance(appContext, HttpAccessLayer.Accessor.OkHttp);
                accessLayer.responseCallback(new ResponseCallback() {
                    @Override
                    public void onFail(Response response) {
                        taskController.release(response);
                    }

                    @Override
                    public void onSuccess(Response response) {
                        taskController.release(response);
                    }
                }).uri("http://api.zipaddress.net/?zipcode=1200005").get();

                return null;
            }
        }).addTask(new TaskWorker.Task() {
            @Override
            public Object run(Context context, Object param, TaskWorker.TaskController taskController) throws TaskWorker.TaskException {
                Assert.assertTrue(param instanceof Response);

                HttpAccessLayer accessLayer = HttpAccessLayer.getInstance(appContext, HttpAccessLayer.Accessor.OkHttp);
                Response response = accessLayer.uri("http://api.zipaddress.net/?zipcode=2700023").getSync();

                synchronized (TaskWorkerTest.this){
                    TaskWorkerTest.this.notify();
                }
                return response;
            }
        }).start();

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
