package helpers;

public class UnequalDimensionsException extends Exception {
	private static final long serialVersionUID = 1L;
	
	String message;
	
	public UnequalDimensionsException(String message) {
		this.message=message;
	}
	
	public String getMessage() {
		return message;
	}

}
