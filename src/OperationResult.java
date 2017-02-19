public class OperationResult<T> {

    private boolean success;
    private String message;
    private T result;

    public static <E> OperationResult<E> Success() {
        return new OperationResult<E>(true, "", null);
    }

    public static <E> OperationResult<E> Success(E result) {
        return new OperationResult<E>(true, "", result);
    }

    public static <E> OperationResult<E> Fail(String information) {
        return new OperationResult<E>(false, information, null);
    }

    public static <E> OperationResult<E> Fail(String information, E result) {
        return new OperationResult<E>(false, information, result);
    }

    public OperationResult(boolean success, String information, T result) {
        this.success = success;
        this.message = information;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }
}