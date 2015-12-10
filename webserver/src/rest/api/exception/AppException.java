package rest.api.exception;

public class AppException extends Exception {

	private static final long serialVersionUID = -2300766411249095676L;

	public AppException(String msg) {
		super(msg);
	}

	public AppException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
