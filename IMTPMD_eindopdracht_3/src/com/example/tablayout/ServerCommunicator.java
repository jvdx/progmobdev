/* Jan van Dijk
 * s1070923
 * INF2C
 */

package com.example.tablayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class ServerCommunicator implements Runnable
{
	private Activity activity;
	private Thread thread;
	private String naam, vraag, ip;
	private int poort;
	private String serverBericht;
	public ServerCommunicator( Activity activity, String naam, String vraag, String ip, int poort )
	{
		this.activity = activity;
		this.naam = naam;
		this.vraag = vraag;
		this.ip = ip;
		this.poort = poort;
		
		//nieuwe thread maken en starten
		this.setThread(new Thread(this));
		getThread().start();
	}

	//de thread
	@Override
	public void run()
	{
		try
		{
			Socket socket = new Socket();
			socket.connect( new InetSocketAddress( this.ip, this.poort ), 4000 );
			//verzend een bericht naar de server
			this.sendMessage( "Naam: " + naam + " , vraag: " + vraag + "\n", socket );
			//wacht op een antwoord.
			final String respons = waitForResponse(socket);
			Log.d("debug", "RESPONS: " + respons);
			if(!respons.equals("Your message has been received on the server!"))
			{
				activity.runOnUiThread( new Runnable() {
					public void run() {
						JSONObject json = null;
						JSONArray array = null;
						Object opdracht = null;
						try {
							json = new JSONObject( respons );
						} catch (JSONException e) {
							Log.d("debug", e.getMessage());
						}
						try {
							array = (JSONArray) json.get( "opdracht" );
						} catch (JSONException e) {
							Log.d("debug", e.getMessage());
						}
						for( int i = 0; i < array.length(); i++ )
						{
							try {
								opdracht =  array.get( i );
								serverBericht = opdracht.toString();
								
							} catch (JSONException e) {
								Log.d("debug", e.getMessage());
							}
						}

					}
				} );
			}
		}
		catch( UnknownHostException e )
		{
			Log.d("debug", "ServerCommunicator, can't find host");
		}
		catch( SocketTimeoutException e )
		{
			Log.d("debug", "ServerCommunicator, time-out");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	//verzenden vna het bericht
	private void sendMessage( String message, Socket socket )
	{
		DataOutputStream dataOutputStream = null;
		DataInputStream dataInputStream = null;
		try {
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream.writeUTF(message);
			Log.d("debug", "just send to server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//wacht op server bericht (na versturen)
	private String waitForResponse(Socket socket)
	{
		BufferedReader bufferedReader = null;
		String returnMessage = null;
		try
		{
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader( inputStream );
			bufferedReader = new BufferedReader( inputStreamReader );
		}
		
		catch (IOException e1){
			e1.printStackTrace();
			InetAddress adress = socket.getInetAddress();
			Log.d("debug", "Can't create inputStreamReader to talk to client " + adress);
			Log.d("debug", e1.getMessage());
		}
		
		if(bufferedReader != null)
		{
			try
			{
				Log.d("debug", "start receiving...");
				String messageLine = bufferedReader.readLine();
				Log.d("debug", "Server op " + socket.getInetAddress() + " zegt: > " + messageLine);
				returnMessage = messageLine;
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return returnMessage;
	}

	public Thread getThread(){
		return thread;
	}

	public void setThread(Thread thread){
		this.thread = thread;
	}

	public String getServerBericht(){
		return serverBericht;
	}

}