package interfaces;

import classes.abstracts.FrameRunner.Task;

public interface TaskCallback {
	void call(Task task);
}