package com.oracle.miecs.botsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft_6455;
//import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;


public class MainActivity extends Activity {
    private static final String TAG = "ChatActivity";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;

    private final WebSocketConnection mConnection = new WebSocketConnection();

//    private WebSocketClient socket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        try {
            createSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendChatMessage () {
        String text = chatText.getText().toString();
        chatArrayAdapter.add(new ChatMessage(true, text));
        chatText.setText("");

        //sending text to BOTS server

        //Request request = new Request.Builder().url("ws://46a3ae4b.ngrok.io/chat/ws?user=Weishi").build();
//        BotsWebSocketListener listener = new BotsWebSocketListener();
//        try {
//            listener.run();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        WebSocketCall.create(client, request).enqueue(listener);
//        client.dispatcher().executorService().shutdown();



//        {
//            "to": {
//            "type": "bot",
//                    "id": "367117DD-B099-4408-B07D-C334BBA2E6A4"
//        },
//            "text": "Hello"
//        }


//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("text", "Hello");
//            JSONObject to = new JSONObject();
//            to.put("type", "bot");
//            to.put("id", "367117DD-B099-4408-B07D-C334BBA2E6A4");
//
//            obj.put("to", to);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String message = obj.toString();

        //send message
        mConnection.sendTextMessage(text);
////        try {
//            socket.connect();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        socket.send(message);

        return true;
    }


//        try {
//            String json = "";
//            RequestBody body = RequestBody.create(JSON, json);
//            Request request = new Request.Builder()
//                    .url("http://bots-connectors:8000/connectors/v1/tenants/5c82a414-e2d0-45fd-b6a2-8ca3b9c09160/listeners/webhook/channels/367117DD-B099-4408-B07D-C334BBA2E6A4")
//                    .post(body)
//                    .build();
//            Response response = client.newCall(request).execute();
//            String resText = response.body().string();
//
//            showReceivingMessage(resText);
//        } catch (Exception e) {
//
//        }



    private boolean showReceivingMessage(String incomingText) {
        chatArrayAdapter.add(new ChatMessage(false, incomingText));
        return true;
    }

    private void createSocket() throws Exception {
//        WebSocketClient mWs = new WebSocketClient( new URI( "ws://46a3ae4b.ngrok.io/chat/ws?user=Weishi" );




//            final String wsuri = "ws://46a3ae4b.ngrok.io/chat/ws?user=Weishi";
        final String wsuri = "ws://slc07qsp.us.oracle.com:8888";


//            wsuri = "ws://46a3ae4b.ngrok.io:80/chat/ws?user=Weishi";

            try {
                mConnection.connect(wsuri, new WebSocketConnectionHandler() {

                    @Override
                    public void onOpen() {
                        Log.d(TAG, "Status: Connected to " + wsuri);
                        //mConnection.sendTextMessage("Hello, world!");
                    }

                    @Override
                    public void onTextMessage(String payload) {
                        Log.d(TAG, "Got echo: " + payload);
                        showReceivingMessage(payload);

                    }

                    @Override
                    public void onClose(int code, String reason) {
                        Log.d(TAG, "Connection lost: " + reason);
                    }
                });
            } catch (WebSocketException e) {

                Log.d(TAG, e.toString());
            }

    }

}