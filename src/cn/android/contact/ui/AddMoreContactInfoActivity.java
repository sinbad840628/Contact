package cn.android.contact.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.android.contact.R;
import cn.android.contact.tools.Toasttool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AddMoreContactInfoActivity extends Activity {
	private MyAdapter1 adapter;
	private Context context;
	private ListView listView;
	private List<String> groupItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isMakeFull();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_more_contact_info_selectview);
		context = this;
		listView = (ListView) findViewById(R.id.listView);

		groupItem = (List<String>) getIntent().getExtras().get("groupItem");

		adapter = new MyAdapter1(context, groupItem);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Toasttool.MyToast(context, arg3 + "");
				TextView txt = (TextView) arg1.findViewById(position);

				Intent intent = new Intent(context, AddNewContactActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("groupItem", (Serializable) groupItem);
				bundle.putString("itemPosition", groupItem.get(position));
				intent.putExtras(bundle);
				AddMoreContactInfoActivity.this.setResult(200, intent);
				Toasttool.MyToast(context, position + "");
				groupItem.remove(position);
				finish();

			}
		});

	}

	private void isMakeFull() {
		SharedPreferences sp = getSharedPreferences("parameter",
				Context.MODE_PRIVATE);
		boolean isFull = sp.getBoolean("isFull", false);
		if (isFull) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

}

class MyAdapter1 extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context context;
	private List<String> list;

	public MyAdapter1(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);

	}

	public int getCount() {

		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.groupitem_view, null);
		TextView txt = (TextView) convertView.findViewById(R.id.txt);
		txt.setText(list.get(position));
		return convertView;
	}

}