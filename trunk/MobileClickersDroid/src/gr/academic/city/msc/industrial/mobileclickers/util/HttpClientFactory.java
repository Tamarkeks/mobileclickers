/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * The responsibility of the singleton {@link HttpClientFactory} object is to
 * provide factory methods for various types of {@link HttpClient}s.
 *
 * @author Ivo Neskovic <ineskovic@6pmplc.com>
 * @version 1.0
 * @since version 1.0
 */
final class HttpClientFactory {

    /**
     * The type of {@link HttpClient}s that can be produced by this factory.
     */
    protected enum HttpClientType {

        /**
         * A {@link HttpClient} suitable for multi-threaded applications. This
         * client is safe to be accessed by multiple threads.
         */
        THREAD_SAFE,
        /**
         * The standard implementation of a {@link HttpClient}.
         */
        DEFAULT
    }
    /**
     * The static instance of the {@link HttpClientFactory}, as per the typical
     * Singleton design pattern. Note the usage of the volatile keyword for
     * thread-safe programming.
     */
    private static volatile HttpClientFactory instance;

    /**
     * Static initialisation method. This method will always return the
     * singleton instance of the {@link HttpClientFactory} object and it will
     * initialise it the first time it is requested. It uses the double-checked
     * locking mechanism and initialisation on demand.
     *
     * @return the system-wide singleton instance of {@link HttpClientFactory}.
     */
    protected static HttpClientFactory getInstance() {
        if (instance == null) {
            synchronized (HttpClientFactory.class) {
                if (instance == null) {
                    instance = new HttpClientFactory();
                }
            }
        }

        return instance;
    }

    /**
     * A private no-args default constructor, preventing the manual
     * initialisation of this object as per the singleton pattern.
     */
    private HttpClientFactory() {
    }

    /**
     * The factory method as per the factory-method design pattern, producing a
     * {@link HttpClient} according to the type passed as a parameter.
     *
     * @param clientType the type of the {@link HttpClient} to be produced. One
     * of
     *            {@link HttpClientType.DEFAULT} or
     *            {@link HttpClientType.THREAD_SAFE}.
     * @return an instance of {@link HttpClient}, according to the type
     * specified.
     */
    protected HttpClient produceHttpClient(HttpClientType clientType) {
        HttpClient client;
        switch (clientType) {
            case THREAD_SAFE:
                client = produceThreadSafeClient();
                break;

            case DEFAULT:
                client = produceDefaultHttpClient();
                break;

            default:
                client = produceDefaultHttpClient();
                break;
        }
        return client;
    }

    /**
     * Produces a {@link DefaultHttpClient}.
     *
     * @return an instance of {@link DefaultHttpClient}.
     */
    private HttpClient produceDefaultHttpClient() {
        return new DefaultHttpClient();
    }

    /**
     * Produces a {@link DefaultHttpClient} with a
     * {@link ThreadSafeClientConnManager}, for thread-safe programming.
     *
     * @return an instance of a {@link DefaultHttpClient} with a
     *         {@link ThreadSafeClientConnManager}.
     */
    @SuppressWarnings("deprecation")
    private HttpClient produceThreadSafeClient() {
        HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        // Default connection and socket timeout of 20 seconds. Tweak to taste.
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager manager = new ThreadSafeClientConnManager(
                params, schemeRegistry);

        return new DefaultHttpClient(manager, params);
    }
}
