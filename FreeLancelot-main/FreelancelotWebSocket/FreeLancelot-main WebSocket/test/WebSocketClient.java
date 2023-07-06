import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.BoundRequestBuilder;
import play.shaded.ahc.org.asynchttpclient.ListenableFuture;
import play.shaded.ahc.org.asynchttpclient.netty.ws.NettyWebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketListener;

import play.shaded.ahc.org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * A quick wrapper around AHC WebSocket
 * @author  Aditi Aditi
 */
public class WebSocketClient {

    private AsyncHttpClient client;

    /**
     * Constructor
     * @param c AsyncHttpClient
     * @author Aditi Aditi
     */
    public WebSocketClient(AsyncHttpClient c) {
        this.client = c;
    }
    
    /**
     * 
     * @param url
     * @param listener
     * @return CompletableFuture<WebSocket> future.toCompletableFuture
     * @throws ExecutionException exception
     * @throws InterruptedException exception
     * @author Aditi Aditi
     */
    public CompletableFuture<NettyWebSocket> call(String url, WebSocketListener listener) throws ExecutionException, InterruptedException {
        final BoundRequestBuilder requestBuilder = client.prepareGet(url);

        final WebSocketUpgradeHandler handler = new WebSocketUpgradeHandler.Builder().addWebSocketListener(listener).build();
        final ListenableFuture<NettyWebSocket> future = requestBuilder.execute(handler);
        return future.toCompletableFuture();
    }

    /**
     * LoggingListener
     * @author Aditi Aditi
     */
    static class LoggingListener implements WebSocketListener {
        private final Consumer<String> onMessageCallback;

        /**
         * Constructor
         * @param onMessageCallback callback
         * @author Aditi Aditi
         */
        public LoggingListener(Consumer<String> onMessageCallback) {
            this.onMessageCallback = onMessageCallback;
        }

        private Logger logger = org.slf4j.LoggerFactory.getLogger(LoggingListener.class);

        private Throwable throwableFound = null;

        /**
         * Get throwable
         * @return throwable
         * @author Aditi Aditi
         */
        public Throwable getThrowable() {
            return throwableFound;
        }

        /**
         * Action performed on opening socket
         * @param websocket
         * @author Aditi Aditi
         */
        public void onOpen(WebSocket websocket) {
        }

        @Override
        public void onClose(WebSocket webSocket, int i, String s) {

        }

        /**
         * Action performed on closing socket
         * @param websocket
         * @author Aditi Aditi
         */
        public void onClose(WebSocket websocket) {
        }

        /**
         * Action performed when an error is thrown
         * @param t throwable
         * @author Aditi Aditi
         */
        public void onError(Throwable t) {
            throwableFound = t;
        }


    }

}
