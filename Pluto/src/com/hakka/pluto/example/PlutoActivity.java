package com.hakka.pluto.example;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlutoActivity extends ListActivity {
	private String[] listItems = { "com.hakka.pluto.example.PradaPuzzleActivity", "com.hakka.pluto.example.PradaPuzzleGameScene",
			"com.hakka.pluto.example.test.TestClipActivity", "Other" };
	private ArrayAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, listItems);
		setListAdapter(adapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) adapter.getItem(position);
		Intent intent = new Intent();
		try {
			intent.setClass(this,Class.forName("" + item));
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}