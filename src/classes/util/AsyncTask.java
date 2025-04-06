/*
 * @written 3/29/2025
 * 
 * class AsyncTask:
 * 
 * Responsible for creating new executable threads on the
 * spot. Mostly used for testing.
 */
package classes.util;

public class AsyncTask implements Runnable {

    public Thread newThread;
    public AsyncCallback asyncCallback;

    public AsyncTask(AsyncCallback asyncCallback) {
        this.asyncCallback = asyncCallback;
        newThread = new Thread(this);
    }

    @Override
    public void run() {
        asyncCallback.callback();
    }

    public void start() {
        newThread.start();
    }
}
