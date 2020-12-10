package smart.home.monitor.models;

/**
 * @Team_Name: SMARTY
 */
public interface DatabaseWriteHandler<T> {
    void onSuccess(boolean success);
    void onFailure(Exception e);
}

