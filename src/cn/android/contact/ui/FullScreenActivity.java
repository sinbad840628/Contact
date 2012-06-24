package cn.android.contact.ui;

import cn.android.contact.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class FullScreenActivity extends Activity {
	private CheckBox chk;
	private TextView txt;
	private SharedPreferences sp;
	private Editor editor;
	private boolean isRember;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.full_screen);
		chk = (CheckBox) findViewById(R.id.checkBox1);
		txt = (TextView) findViewById(R.id.txt);

			sp = getSharedPreferences("parameter", Context.MODE_PRIVATE);
			boolean mIsRember = sp.getBoolean("isRember", false);
			if (mIsRember) {
				chk.setChecked(true);
			}

		Intent intent = new Intent(this, ContactActivity.class);
		this.setResult(1000, intent);

		txt.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//sp = getSharedPreferences("parameter", Context.MODE_PRIVATE);
				editor = sp.edit();
				if (isRember) {
					editor.putBoolean("isFull", true);
					editor.putBoolean("isRember", true);

				} else {
					editor.putBoolean("isFull", false);
					editor.putBoolean("isRember", false);

				}

				editor.commit();
				finish();

			}
		});

		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				

				if (isChecked) {
					isRember = true;

					// editor.putBoolean("isFull", true);

				} else {
					isRember = false;

					// editor.putBoolean("isFull", false);

				}

			}
		});

	}

}
