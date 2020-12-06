package smart.home.monitor.models;

public interface DatabaseWriteHandler<T> {
    void onSuccess(boolean success);
    void onFailure(Exception e);
}
