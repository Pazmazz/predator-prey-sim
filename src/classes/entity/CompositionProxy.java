package classes.entity;

public class CompositionProxy<T> {
	private T proxy;

	public void setProxy(T proxy) {
		this.proxy = proxy;
	}

	public T getProxy() {
		return proxy;
	}
}
