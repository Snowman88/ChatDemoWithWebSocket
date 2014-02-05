package com.example.websocketclientdemo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

public class MainActivity extends Activity {
	private final static String TAG = MainActivity.class.getSimpleName();
	
	private WebSocket webSocket;
	
	private TextView statusTextView;
	private Button connectButton;
	private EditText messaeEditText;
	private Button sendButton;
	private ListView messageListView;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		/**
		 * ListViewÇ…åãâ ÇìWäJ
		 * ï∂éöâªÇØ
		 * Browserï\é¶
		 */
		//View
		statusTextView = (TextView) findViewById(R.id.status_textview);
		connectButton = (Button) findViewById(R.id.connect_button);
		messaeEditText = (EditText) findViewById(R.id.message_edit);
		sendButton = (Button) findViewById(R.id.send_button);
		messageListView = (ListView) findViewById(R.id.message_listview);
		
		// setEvent
		connectButton.setOnClickListener(new ConnectOnClickListener());
		sendButton.setOnClickListener(new SendOnClickListener());
		
		// listView
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
		messageListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	class ConnectOnClickListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View v) {
		
			Log.d(TAG, "START");
			URI uri = null;
			try {
				uri = new URI("ws://192.168.11.5:8888/ws");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			webSocket = new WebSocket(uri);
			webSocket.setEventHandler(new WebSocketEventHandlerImplemented());
			new AsyncTask<WebSocket, Integer, Integer>(){
				@Override
				protected Integer doInBackground(WebSocket... webSocket) {
					try{
						webSocket[0].connect();
					}catch(WebSocketException e){
						e.printStackTrace();
						return -1;
					}
					return 0;
				}
				protected void onPostExecute(Integer result) {
					if(result == -1){
						statusTextView.setText("Can't establish websocket... Did you establish a server for websocket?");
					}
				};
			}.execute(webSocket);
				
//				webSocket.close();
		}
	}
	
	class SendOnClickListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View v) {
			// message
			String message = messaeEditText.getText().toString();
			// async
			new AsyncTask<String, String, Integer>(){

				@Override
				protected Integer doInBackground(String... message) {
					try{
						if(webSocket == null) return -1;
						webSocket.send(message[0]);
					}catch(WebSocketException e){
						e.printStackTrace();
						return -1;
					}
					return 0;
				}
				protected void onPostExecute(Integer result) {
					if(result == -1){
						statusTextView.setText("Can't establish websocket... Did you establish a server for websocket?");
					}
				};
				
			}.execute(message);
		}
	}
	
	class WebSocketEventHandlerImplemented implements WebSocketEventHandler{
		@Override
		public void onPong() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPing() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onOpen() {
			Log.d(TAG, "----open");
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "----open", Toast.LENGTH_SHORT).show();
					statusTextView.setText("Opened");
				}
			});
			
		}
		
		@Override
		public void onMessage(final WebSocketMessage message) {
			Log.d(TAG, message.getText());
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "----onMessage" + message.getText(),
							Toast.LENGTH_SHORT).show();
					// Use better one! Å´ I like the first one!
					adapter.insert(message.getText(), 0);
//					adapter.add(message.getText());
				}
			});
		}
		
		@Override
		public void onError(IOException exception) {
			Log.d(TAG, "----error");
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "----error",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		@Override
		public void onClose() {
			Log.d(TAG, "----close");
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "----close",
							Toast.LENGTH_SHORT).show();
					statusTextView.setText("Closed");
				}
			});
		}
	}

}
