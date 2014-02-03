package com.example.websocketclientdemo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final static String TAG = MainActivity.class.getSimpleName();
	
	private WebSocket webSocket;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try{
			Log.d(TAG, "START");
			URI uri = new URI("ws://192.168.11.6:8888/ws");
			webSocket = new WebSocket(uri);
			webSocket.setEventHandler(new WebSocketEventHandler() {
				
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
					// TODO Auto-generated method stub
					Log.d(TAG, "----open");
					MyAsyncTask myAsyncTask = new MyAsyncTask(){
						@Override
						protected Long doInBackground(WebSocket... webSocket) {
							// TODO Auto-generated method stub
							webSocket[0].send("hello world");
							return null;
						}
					};
					myAsyncTask.execute(webSocket);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "----open", Toast.LENGTH_SHORT).show();
						}
					});
					
				}
				
				@Override
				public void onMessage(final WebSocketMessage message) {
					// TODO Auto-generated method stub
					Log.d(TAG, message.getText());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "----onMessage" + message.getText(),
									Toast.LENGTH_SHORT).show();
						}
					});
				}
				
				@Override
				public void onError(IOException exception) {
					// TODO Auto-generated method stub
					Log.d(TAG, "----error");
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "----error",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
				
				@Override
				public void onClose() {
					// TODO Auto-generated method stub
					Log.d(TAG, "----close");
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "----close",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			MyAsyncTask myAsyncTask = new MyAsyncTask();
			myAsyncTask.execute(webSocket);
//			webSocket.connect();
			
			
			
//			webSocket.send("hello world");
			
//			webSocket.close();
		}catch(WebSocketException wse){
			wse.printStackTrace();
		}catch(URISyntaxException use){
			use.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class MyAsyncTask extends AsyncTask<WebSocket, Integer, Long>{

		@Override
		protected Long doInBackground(WebSocket... webSocket) {
			// TODO Auto-generated method stub
			webSocket[0].connect();
			return null;
		}
		
	}

}
