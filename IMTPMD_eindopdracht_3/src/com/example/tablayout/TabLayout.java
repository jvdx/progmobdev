/* Jan van Dijk
 * s1070923
 * inf2c
 */

package com.example.tablayout;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;

public class TabLayout extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Resources res = getResources();
		TabHost tabHost = getTabHost();

		Intent intent = new Intent(this, Opdr_listview.class);
		tabHost.addTab(tabHost
				.newTabSpec("Opdrachten")
				.setIndicator("Opdrachten", res.getDrawable(R.drawable.icon))
				.setContent(intent));

		Intent intent2 = new Intent(this, MainActivity.class);
		tabHost.addTab(tabHost.newTabSpec("Vraag Verzenden")
				.setIndicator("Invoerscherm", res.getDrawable(R.drawable.icon))
				.setContent(intent2));
				
		//tab openen wanneer de app start
		tabHost.setCurrentTab(1);
		
		// kleuren van tabs
		tabHost.setBackgroundColor(Color.BLACK);
		tabHost.getTabWidget().setBackgroundColor(Color.BLACK);

	}
}