package interfaces;

import classes.abstracts.RunService.Task;

public interface TaskCallback {
	void call(Task task);
}