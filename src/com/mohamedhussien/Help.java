package com.mohamedhussien;

import com.mohamedhussien.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
 

public class Help extends Activity {

	private WebView browser;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

	   browser = (WebView)findViewById(R.id.yourwebview);

	    WebSettings settings = browser.getSettings();
	    settings.setJavaScriptEnabled(true);

	    browser.loadUrl("file:///android_asset/help.html");;
	}
}
