package com.tada.tawebcom;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Xml;
import android.widget.TextView;

public class Webcom1 extends Activity {
private TextView mView;

static private String mArticleTitle[];
static private String mArticleURL[];
static private int mArticleNum;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webcom1);
		mView = (TextView)findViewById(R.id.view);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		mView.setText(new String(httpGet(createURL())));
	}

	public String createURL(){
		String apiURL = "http://news.yahooapis.jp/NewsWebService/V2/topics?";
		String appid = "dj0zaiZpPTU3VEFwYzNYYThSQSZzPWNvbnN1bWVyc2VjcmV0Jng9NWQ-";
		String category ="top";
		return String.format("%sappid=%s&pickupcategory=%s", apiURL, appid,category);
	}


	public static String httpGet(String strURL){
		try{
			URL url=new URL(strURL);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			InputStream stream = connection.getInputStream();
			readXML(stream);
			String data="";
			for(int i=0;i<mArticleNum;i++){
				data+=mArticleTitle[i];
			}
			stream.close();
			return data;

		}catch(Exception e){
			return e.toString();
		}
	}

	public static void readXML(InputStream stream) throws XmlPullParserException{
			try{
				XmlPullParser myxmlPullParser = Xml.newPullParser();
				myxmlPullParser.setInput(stream,"UTF-8");

				int cntTitle = 0;
				int cntAddress = 0;
				for(int e = myxmlPullParser.getEventType(); e!= XmlPullParser.END_DOCUMENT;
						e = myxmlPullParser.next()){

					if (e == XmlPullParser.START_TAG) {
		                  if (myxmlPullParser.getName().equals("ResultSet")) {
		                      mArticleNum = Integer.parseInt(myxmlPullParser.getAttributeValue(null, "totalResultsReturned"));
		                      mArticleTitle = new String[mArticleNum];
		                      mArticleURL = new String[mArticleNum];
		                  } else if (myxmlPullParser.getName().equals("Title")) {
		                      mArticleTitle[cntTitle] = myxmlPullParser.nextText();
		                      cntTitle++;
		                  } else if (myxmlPullParser.getName().equals("SmartphoneUrl")) {
		                      mArticleURL[cntAddress] = myxmlPullParser.nextText();
		                      cntAddress++;
		                  }
		               }
		           }
		     } catch (XmlPullParserException e) {
		     } catch (IOException e) {
		  }
	}
}