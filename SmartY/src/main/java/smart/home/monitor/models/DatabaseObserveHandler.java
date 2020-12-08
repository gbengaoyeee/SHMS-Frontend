package smart.home.monitor.models;

import java.util.Map;

public interface DatabaseObserveHandler {
    void onChange(Device device, boolean danger);
}
