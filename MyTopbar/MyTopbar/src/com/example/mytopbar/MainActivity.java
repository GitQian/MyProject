package com.example.mytopbar;

import com.example.mytopbar.TopBar.TopBarClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		//获取自定义控件
		TopBar topBar = (TopBar) findViewById(R.id.topbar);  //id是在acitivity_main中定义的
		topBar.setOnTopBarClickListener(new TopBarClickListener() {
			@Override
			public void rightclick(){
				Toast.makeText(MainActivity.this, "Right Clicked", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void leftclick(){
				Toast.makeText(MainActivity.this, "Left Clicked", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
