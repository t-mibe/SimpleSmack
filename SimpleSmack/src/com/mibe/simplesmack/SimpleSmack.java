package com.mibe.simplesmack;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * adb am start -n com.mibe.simplesmack/.SimpleSmack
 * --es HOST tyurai.com --es ID mibe@tyurai.com --es PASS hoge
 * --es DST mibe2@tyurai.com --es BODY test --es CS w:85,h:180
 * @author mibe
 *
 */
public class SimpleSmack extends Activity {

	private static final String TAG = "SimpleSmack";
	
	private static final int duration = Toast.LENGTH_SHORT;

	private static final String DEFAULT_HOST	= "tyurai.com";
	private static final String DEFAULT_ID		= "mibe@tyurai.com";
	private static final String DEFAULT_PASS	= "hoge";
	private static final String DEFAULT_DST		= "mibe2@tyurai.com";
	private static final String DEFAULT_BODY	= "body";
	

	private String host, myId, pass, dst, body, cStanza;
	private boolean mode;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

		Log.d(TAG, "start");
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){

			Log.d(TAG, "getOption");
			
			if((host	= bundle.getString("HOST"	)) == null)host	= DEFAULT_HOST;
			if((myId	= bundle.getString("ID"		)) == null)myId	= DEFAULT_ID;
			if((pass	= bundle.getString("PASS"	)) == null)pass	= DEFAULT_PASS;
			if((dst		= bundle.getString("DST"	)) == null)dst	= DEFAULT_DST;
			if((body	= bundle.getString("BODY"	)) == null)body	= DEFAULT_BODY;
			if((cStanza	= bundle.getString("CS"		)) == null)cStanza = null;
			
			mode = bundle.getBoolean("MODE");
			
		} else {
			Toast.makeText(this, "オプションを指定してください", duration).show();
			finish();
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {            
				//XMPPConnection xmpp = new XMPPConnection("jabber.iitsp.com");
				XMPPConnection xmpp = new XMPPConnection(host);
				try {
					xmpp.connect();

					// for other jabber accounts, truncate after the @
					//xmpp.login("username", "password"); 

					// for gtalk / gmail, include the @
					xmpp.login(myId, pass);

				} catch (XMPPException e) {
					Log.v(TAG, "Failed to connect to " + xmpp.getHost());
					e.printStackTrace();
				}
				ChatManager chatmanager = xmpp.getChatManager();
				Chat newChat = chatmanager.createChat(dst, new MessageListener() {
					// THIS CODE NEVER GETS CALLED FOR SOME REASON
					@Override
					public void processMessage(Chat chat, Message message) {
						try {
							Log.v(TAG, "Got:" + message.getBody());
							chat.sendMessage(message.getBody());
						} catch (XMPPException e) {
							Log.v(TAG, "Couldn't respond:" + e);
						}
						Log.v(TAG, message.toString());
					}
				});

				// Send something to friend@gmail.com
				try {
					CustomMessage msg = new CustomMessage();
					msg.setBody(body);
					
					if(mode){
						// カスタムスタンザ文記述モード
						msg.setCostomStanzaText(cStanza);
						
					} else {
						
					}
					
					if(cStanza != null && !cStanza.equals("")){
						
						String csArray[] = cStanza.split(",");
						
						int len = csArray.length;
						for(int i = 0;i < len; i++){
							Log.d(TAG, "new stanza");
							String cs[] = csArray[i].split(":");
							msg.addStanza(cs[0], cs[1]);
						}
					}
					newChat.sendMessage(msg);
				} catch (XMPPException e) {
					Log.v(TAG, "couldn't send:" + e.toString());
				}
				
				finish();
			}
		}).start();
	}
}

