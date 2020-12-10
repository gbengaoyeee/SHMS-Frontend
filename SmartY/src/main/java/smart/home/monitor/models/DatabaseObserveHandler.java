package smart.home.monitor.models;

/**
 * @Team_Name: SMARTY
 */
public interface DatabaseObserveHandler {
    void onChange(Device device, boolean danger);
}
