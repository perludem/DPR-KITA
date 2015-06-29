package id.gits.wplib.apis;

public interface WpCallback<T> {

    /**
     * Successful response.
     */
    void success(T t, String json);

    /**
     * Unsuccessful response due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    void failure(String error);
}