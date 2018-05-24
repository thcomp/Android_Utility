package jp.co.thcomp.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

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

                Request request = new Request.Builder().url("http://api.zipaddress.net/?zipcode=1200005").get().build();
                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        taskController.release(e);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        taskController.release(response);
                    }
                });

                return null;
            }
        }).addTask(new TaskWorker.Task() {
            @Override
            public Object run(Context context, Object param, TaskWorker.TaskController taskController) throws TaskWorker.TaskException {
                Assert.assertTrue((param instanceof Response) || (param instanceof Exception));

                synchronized (TaskWorkerTest.this) {
                    TaskWorkerTest.this.notify();
                }

                return null;
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
