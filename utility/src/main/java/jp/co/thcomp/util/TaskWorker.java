package jp.co.thcomp.util;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.List;

public class TaskWorker {
    public static class TaskException extends Exception {

    }

    public interface Task {
        Object run(Context context, Object param, TaskController controller) throws TaskException;
    }

    public static class TaskController{
        private boolean mNeedSync = false;
        private boolean mRerunTask = false;
        Object mResult;

        public void sync(){
            synchronized (this){
                if(!mNeedSync){
                    mNeedSync = true;
                }
            }
        }

        public void release(Object result){
            synchronized (this){
                if(mNeedSync){
                    mNeedSync = false;
                    mResult = result;
                    notifyAll();
                }
            }
        }

        public void rerunTask(){
            mRerunTask = true;
        }

        Object waitRelease(Object tempResult){
            Object ret = tempResult;

            synchronized (this){
                if(mNeedSync){
                    try{
                        wait();
                        ret = mResult;
                    } catch (InterruptedException e) {
                    }
                }
            }

            return ret;
        }
    }

    private class Worker implements Runnable {
        private int mTaskIndex = 0;
        private Object mParam;

        public Worker(Object param, int index) {
            mParam = param;
            mTaskIndex = index;
        }

        @Override
        public void run() {
            TaskController taskController = new TaskController();
            Object ret = null;

            try {
                Task task = null;
                synchronized (mTaskList) {
                    task = mTaskList.get(mTaskIndex);
                }
                ret = task.run(mContext, mParam, taskController);

                // 同期が必要な場合は待っている。必要ない場合はそのまま抜けてくる
                ret = taskController.waitRelease(ret);
            } catch (IndexOutOfBoundsException e) {
                stop();
                return;
            } catch (TaskException e) {
                stop();
                return;
            }

            if (mWorking) {
                if(taskController.mRerunTask){
                    // 同じタスクを再度実行
                    mWorkerHandler.post(new Worker(ret, mTaskIndex));
                }else{
                    mWorkerHandler.post(new Worker(ret, ++mTaskIndex));
                }
            }
        }
    }

    private List<Task> mTaskList = new ArrayList<Task>();
    private boolean mWorking = false;

    private Context mContext;
    private HandlerThread mWorkerThread;
    private Handler mWorkerHandler;

    public TaskWorker(Context context) {
        mContext = context;
    }

    public TaskWorker addTask(Task task) {
        synchronized (mTaskList) {
            mTaskList.add(task);
        }
        return this;
    }

    public TaskWorker removeTask(Task task) {
        synchronized (mTaskList){
            mTaskList.remove(task);
        }
        return this;
    }

    public void start() {
        if (mWorkerThread == null) {
            mWorking = true;
            mWorkerThread = new HandlerThread(TaskWorker.class.getSimpleName());
            mWorkerThread.start();

            mWorkerHandler = new Handler(mWorkerThread.getLooper());
            mWorkerHandler.post(new Worker(null, 0));
        }
    }

    public void stop() {
        if (mWorkerThread != null) {
            mWorking = false;
            mWorkerThread.quitSafely();
            mWorkerThread = null;
        }
    }
}
