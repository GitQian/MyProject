package com.example.testdialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.view.MyDialog;

public class MainActivity extends Activity implements OnClickListener ,OnItemClickListener{
	private Button btn_show_textview, btn_show_edittext, btn_show_listview;
	private MyDialog myDialog;

	private int position = -1;
	private int tmp = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ʵ�����ؼ�
		btn_show_textview = (Button) findViewById(R.id.btn_show_textview);
		btn_show_edittext = (Button) findViewById(R.id.btn_show_edittext);
		btn_show_listview = (Button) findViewById(R.id.btn_show_listview);
		// ���ü���
		btn_show_textview.setOnClickListener(this);
		btn_show_edittext.setOnClickListener(this);
		btn_show_listview.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.btn_show_textview:
			showTextView();
			break;
		case R.id.btn_show_edittext:
			showEditText();
			break;
		case R.id.btn_show_listview:
			showListView();
			break;

		default:
			break;
		}
	}

	/**
	 * ��ʾtextview
	 */
	private void showTextView() {
		// �����Զ��岼��
		View v = LayoutInflater.from(this).inflate(R.layout.textview, null);
		TextView textView = (TextView) v.findViewById(R.id.textv_content);
		textView.setText("������ʾ!");
		myDialog = new MyDialog(this, "������ʾŶ", v, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "������ʾ",
						Toast.LENGTH_SHORT).show();
				// �ر�dialog
				myDialog.dismiss();
			}
		});
		// ��ʾdialog
		myDialog.show();
	}

	/**
	 * ��ʾedittext
	 */
	private void showEditText() {
		// �����Զ��岼��
		View v = LayoutInflater.from(this).inflate(R.layout.edittext, null);
		EditText textView = (EditText) v.findViewById(R.id.edit_content);
		myDialog = new MyDialog(this, "����edittext", v, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "����edittext",
						Toast.LENGTH_SHORT).show();
				myDialog.dismiss();
			}
		});
		myDialog.show();
	}


	String strarray[] = new String[] {"21.5", "42", "55", "65", "74", "85", "����"};  
	/**
	 * ��ʾlistview
	 */
	private void showListView() {
		// �����Զ��岼��
		View v = LayoutInflater.from(this).inflate(R.layout.listview, null);
		ListView listview = (ListView) v.findViewById(R.id.listv_content);
		
		listview.setAdapter( new ArrayAdapter<String>( this,  
		                   R.layout.item_listview , strarray ));
		
		listview.setOnItemClickListener(this);
		if (position >= 0) {
			listview.setItemChecked(position, true);
		}
		myDialog = new MyDialog(this, "��ѡ����Ļ�ߴ磺", v, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "����listview",
						Toast.LENGTH_SHORT).show();
				position = tmp;
				myDialog.dismiss();
			}
		});
		myDialog.show();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), " " + position, Toast.LENGTH_SHORT).show();
		this.tmp = position;
	}
}
