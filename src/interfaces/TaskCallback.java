package interfaces;

import classes.abstracts.FrameProcessor.Task;

public interface TaskCallback {
	void call(Task task);
}
