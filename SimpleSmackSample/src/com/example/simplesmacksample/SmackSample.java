package com.example.simplesmacksample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SmackSample extends Activity {

	private static final int duration = Toast.LENGTH_SHORT;

	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 送信ボタンの動作を設定する
		setButton();
	}

	// 送信ボタンの動作を設定する
	private void setButton() {
		Button button = (Button)findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 設定値を取得してXMPP送信アプリを起動させる（失敗したらエラー表示）
				if(!startXmppSendApp())Toast.makeText(context, "option error", duration).show();
			}
		});
	}

	// 設定値を取得してXMPP送信アプリを起動させる
	private boolean startXmppSendApp() {
		String host, id, pass, dst, body, cs;
		if(
				(host	= getTextEditValue(R.id.edit_host	)).equals("") ||
				(id		= getTextEditValue(R.id.edit_id		)).equals("") ||
				(pass	= getTextEditValue(R.id.edit_pass	)).equals("") ||
				(dst	= getTextEditValue(R.id.edit_dst	)).equals("") 
				)return false;

		body = getTextEditValue(R.id.edit_body);
		cs = getTextEditValue(R.id.edit_cs);

		Intent intent = new Intent();

		intent.setClassName("com.mibe.simplesmack", "com.mibe.simplesmack.SimpleSmack");

		intent
		.putExtra("HOST", host)
		.putExtra("ID", id)
		.putExtra("PASS", pass)
		.putExtra("DST", dst)
		.putExtra("BODY", body);
		
		if(!cs.equals(""))intent.putExtra("CS", cs);
		
		startActivity(intent);

		return true;
	}

	// 指定したテキストボックスの内容を取得する
	private String getTextEditValue(int id){
		EditText editText = (EditText)findViewById(id);
		if(editText == null)return null;
		return editText.getText().toString();
	}

}
