package cn.android.contact.ui;

import cn.android.contact.R;
import cn.android.contact.biz.GroupManager;
import cn.android.contact.model.Group;
import cn.android.contact.tools.Toasttool;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewGroupActivity extends Activity {

	private EditText edtNewGroupName;
	private Button btnSure;
	private Button btnCancel;
	private GroupManager groupManager;
	private Context context;
	private Bundle bundle;
	private TextView txtTitle;
	private TextView txtMTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_group_view);
		context = this;
		groupManager = new GroupManager(context);
		bundle = this.getIntent().getExtras();

		edtNewGroupName = (EditText) findViewById(R.id.edtNewGroupName);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtMTitle =  (TextView) findViewById(R.id.txtMTitle);
		
		if (bundle != null) {
			txtTitle.setText("提示");
			txtMTitle.setText("请编辑分组名称");
			String name = bundle.getString("groupName");
			edtNewGroupName.setText(name);
		}else{
			txtTitle.setText("新建分组");
			txtMTitle.setText("请输入分组名称");
			
		}
		
		btnSure = (Button) findViewById(R.id.btnSure);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		btnSure.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (bundle != null) {
					String newGroupName = edtNewGroupName.getText().toString();
					Group group = new Group( bundle.getInt("groupId"),newGroupName);
					groupManager.updateGroup(group);
				} else {
					String newGroupName = edtNewGroupName.getText().toString();
					if (!newGroupName.equals("")) {
						groupManager.insertGroupByName(newGroupName);
						Toasttool.MyToast(context, "添加分组成功");
						
					}
				}
				finish();

			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

	}

}
