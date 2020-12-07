package smart.home.monitor.models;

import java.util.Map;

public interface DatabaseWriteHandler<T> {
    void onSuccess(boolean success);
    void onFailure(Exception e);
}

interface DatabaseObserveHandler {
    void onChange(Map<String, String> data);
}
