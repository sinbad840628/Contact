package cn.android.contact.ui;

import java.util.List;

import cn.android.contact.R;
import cn.android.contact.biz.GroupManager;
import cn.android.contact.model.Group;
import cn.android.contact.tools.Toasttool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class SelectGroupActivity extends Activity {
	private GroupManager groupManager;
	private Context context;
	private ListView listView;
	private MyAdapter adapter;
	private List<Group> groups;

	private String groupName="未分组";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.group_select_listview);
		// if(savedInstanceState.getInt("markPosition")!=null)
		// if(savedInstanceState!=null)
		// markPosition= savedInstanceState.getInt("markPosition");
		context = this;
		groupManager = new GroupManager(context);
		groups = groupManager.getAllGroup();
		
		groupName =getIntent().getStringExtra("groupName");
		

		listView = (ListView) findViewById(R.id.listView);

		adapter = new MyAdapter(groupName,groups, context);
		listView.setAdapter(adapter);

		Log.i("Item", "22222");
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				CheckBox chkGroup = (CheckBox) arg1.findViewById(R.id.chkGroup);

				if (chkGroup.isChecked()) {
					chkGroup.setChecked(false);
				} else {
					chkGroup.setChecked(true);
					Group group = groups.get(position);
					Log.i("hubin", group.toString());


					int requestCode = getIntent().getExtras().getInt(
							"requestCode");
					switch (requestCode) {
					case 2:// requestCode = 2 全组成员移动..跳转过来的
						Intent intent = new Intent(context,
								ContactActivity.class);
						intent.putExtra("groupId", group.getGroupId());
						SelectGroupActivity.this.setResult(2, intent);

						break;
					case 3://requestCode = 3新建联系人选择组别跳转过来的
						Toasttool.MyToast(context, group.toString());
						Intent intent1 = new Intent(context,
								AddNewContactActivity.class);

						Bundle bundle = new Bundle();
						bundle.putInt("groupId", group.getGroupId());
						bundle.putString("groupName", group.getGroupName());
						intent1.putExtras(bundle);
						SelectGroupActivity.this.setResult(300, intent1);

						break;
					case 4://requestCode = 4 单人移动..跳转过来的
						Intent intent2 = new Intent(context,
								ContactActivity.class);
						intent2.putExtra("groupId", group.getGroupId());
						SelectGroupActivity.this.setResult(4, intent2);

						break;
					case 5://requestCode =5 点击标记移动..跳转到组别选择
						Intent intent3 = new Intent(context,
								ContactActivity.class);
						intent3.putExtra("groupId", group.getGroupId());
						SelectGroupActivity.this.setResult(5, intent3);


						break;
					case 30://requestCode = 30联系人列表界面： 单人移动..跳转过来的
						Intent intent4 = new Intent(context,
								AllContactShowActivity.class);
						intent4.putExtra("groupId", group.getGroupId());
						SelectGroupActivity.this.setResult(30, intent4);

						break;
					case 40://requestCode =5 点击标记移动..跳转到组别选择
						Intent intent5 = new Intent(context,
								AllContactShowActivity.class);
						intent5.putExtra("groupId", group.getGroupId());
						SelectGroupActivity.this.setResult(40, intent5);

					default:
						break;
					}

				}

				finish();

			}
		});

	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// Log.i("child", markPosition+"save");
	// outState.putInt("markPosition", markPosition);
	// super.onSaveInstanceState(outState);
	// }

}

class MyAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Group> groups;
	private Context context;
	private String groupName =null;

	public MyAdapter(String groupName,List<Group> groups, Context context) {

		this.groupName = groupName;
		this.groups = groups;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);

	}

	public int getCount() {
		return groups.size();
	}

	public Object getItem(int position) {

		return groups.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = mInflater.inflate(R.layout.group_select_view, null);
		TextView txtGroupName = (TextView) convertView
				.findViewById(R.id.txtGroupName);
		Group group = (Group) getItem(position);
		txtGroupName.setText(group.getGroupName().toString());

		CheckBox chkGroup = (CheckBox) convertView.findViewById(R.id.chkGroup);
		if (groupName!=null){
			boolean isChecked = groupName.equals(txtGroupName.getText());
		    chkGroup.setChecked(isChecked);
		}
		// if(position==markPosition){
		// chkGroup.setChecked(true);}

		chkGroup.setClickable(false);

		return convertView;
	}

}
