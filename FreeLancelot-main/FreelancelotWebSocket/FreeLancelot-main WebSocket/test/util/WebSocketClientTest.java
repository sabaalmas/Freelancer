package util;

import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.BoundRequestBuilder;
import play.shaded.ahc.org.asynchttpclient.netty.ws.NettyWebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketUpgradeHandler;
import play.shaded.ahc.org.asynchttpclient.ListenableFuture;

import java.util.concurrent.CompletableFuture;

    /**
     * Tests the functionality of WebSocket.
     *
     * @author Will Sargent (https://github.com/playframework/play-java-chatroom-example/blob/2.6.x/test/controllers/WebSocketClient.java)
     * @version 1.0.0
     */
    public class WebSocketClientTest {
        /**
         * Async Http Client for the WebSocket
         */
        private AsyncHttpClient client;

        /**
         * Creates WebSocket Test Client with the Async Http client
         */
        public WebSocketClientTest(AsyncHttpClient c) {
            this.client = c;
        }

        /**
         * Calls a WebSocket server and upgrades created connection to a WebSocket
         *
         * @param url    WebSocket Url
         * @param origin Defines an origin header during the connection
         * @return a websocket connection
         */
        public CompletableFuture<NettyWebSocket> call(String url, String origin) {
            BoundRequestBuilder requestBuilder;
            if (origin != null) {
                requestBuilder = client.prepareGet(url).addHeader("Origin", origin);
            } else {
                requestBuilder = client.prepareGet(url);
            }
            WebSocketUpgradeHandler handler = new WebSocketUpgradeHandler.Builder().build();
            ListenableFuture<NettyWebSocket> future = requestBuilder.execute(handler);
            return future.toCompletableFuture();

        }
    }