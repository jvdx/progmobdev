/* Jan van Dijk
 * s1070923
 * INF2C
 */
package com.example.tablayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener 
{
	private int poortNummer;
	private String opdracht, ipAdress, naam;
	private ServerCommunicator serverCommunicator;
	

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vraag_scherm);
		Button b = (Button)findViewById(R.id.button1);
		b.setOnClickListener(this);

	}

	public void onClick(View src) 
	{
		EditText naamEditText = (EditText) this.findViewById(R.id.naamVeld); 
		naam = naamEditText.getText().toString();
		
		EditText poortNummerEditText = (EditText) this.findViewById(R.id.poortNummerVeld); 
		poortNummer = Integer.parseInt(poortNummerEditText.getText().toString());
		
		EditText opdrachtEditText = (EditText) this.findViewById(R.id.opdrachtVeld); 
		opdracht = opdrachtEditText.getText().toString();
		
		EditText ipAdressEditText = (EditText) this.findViewById(R.id.ipAdressVeld); 
		ipAdress = ipAdressEditText.getText().toString();
		
		serverCommunicator = new ServerCommunicator(this,naam,opdracht, ipAdress, poortNummer);

	}
}