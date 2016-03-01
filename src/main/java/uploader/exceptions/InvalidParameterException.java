package uploader.exceptions;

public class InvalidParameterException extends Exception {

    String message;

    public InvalidParameterException(String paramName) {
        message = ("Invalid parameter was received: " + paramName);
    }

    public InvalidParameterException(NumberFormatException cause) {
        message = ("Invalid parameter was received: " + cause.getMessage().substring(cause.getMessage().indexOf("\"")));
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
