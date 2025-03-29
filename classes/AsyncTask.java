package classes;

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
