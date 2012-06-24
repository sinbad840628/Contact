package cn.android.contact.ui;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openintents.convertcsv.opencsv.ConvertCSV;

import cn.android.contact.R;
import cn.android.contact.R.drawable;
import cn.android.contact.model.Contact;
import cn.android.contact.tools.ImageTools;
import cn.android.contact.tools.Toasttool;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class ToolGriewActivity extends Activity {
	private List<Map<String, Integer>> list;
	private GridView mGrid;
	private Context context;
	private View mainMenuView;// 子对话框视图
	private AlertDialog mainMenuDialog;// 子对话框的定义
	private GridView mainMenuGrid;// 子菜单的布局

	private String[] main_menu_itemName = { "从手机导入", "SIM卡导入", "备份数据", "黑名单",
			"联系人去重", "还原数据", "帮助", "更新", "后退" };// 子菜单文字
	private int[] main_menu_itemSource = { R.drawable.ic_menu_call,
			R.drawable.ic_menu_import_sim, R.drawable.ic_menu_backup,
			R.drawable.ic_menu_black, R.drawable.remove_contact,
			R.drawable.ic_menu_restore, R.drawable.menu_help,
			R.drawable.ic_menu_update, R.drawable.ic_menu_back };

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);
		context = this;
		AppsAdapter adpter = new AppsAdapter(context, main_menu_itemName,
				main_menu_itemSource);
		mGrid = (GridView) this.findViewById(R.id.gridview);
		mGrid.setNumColumns(3);// 设置每行列数
		mGrid.setGravity(Gravity.CENTER);// 位置居中
		mGrid.setVerticalSpacing(10);// 垂直间隔
		mGrid.setHorizontalSpacing(10);// 水平间隔
		mGrid.setAdapter(adpter);

		mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				boolean isRe = true;
				switch (arg2) {
				case 0:// 从手机导入
					//Toasttool.MyToast(context, "从手机导入");
					Intent intent = new Intent(context,
							GetContactActivity.class);
					intent.putExtra("requestCode", 8);
					startActivityForResult(intent, 8);
					break;
				case 1:// 从SIM卡导入
					Toasttool.MyToast(context, "从SIM卡导入");
					Intent intent2 = new Intent(context,
							GetContactActivity.class);
					intent2.putExtra("requestCode", 9);
					startActivityForResult(intent2, 9);
					break;
				case 2:// 备份
					try{
						String fileName = getSDPath()+"/test.csv";
						ConvertCSV convertCSV = new ConvertCSV(context,fileName);
						convertCSV.startExport();
						Toasttool.MyToast(context, "备份通讯录成功!");
					}catch(Exception ex){
						Toasttool.MyToast(context, "备份通讯录失败!原因如下："+ex.getMessage());
					}
					break;
				case 3:// 黑名单
					break;
				case 4:// 联系人去重
					break;
				case 5:// 还原
					try{
						String fileName = getSDPath()+"/test.csv";
						ConvertCSV convertCSV = new ConvertCSV(context,fileName);
						/** 联系人名称 **/
						ArrayList<String> mContactsName = new ArrayList<String>();

						/** 联系人电话 **/
						ArrayList<String> mContactsNumber = new ArrayList<String>();

						convertCSV.startImport(mContactsName,mContactsNumber);
						List<Object> list = new ArrayList<Object>();
						list.add(mContactsName);
						list.add(mContactsNumber);
						Intent intent21 = new Intent(context, ContactActivity.class);

						Bundle bundle = new Bundle();
						bundle.putSerializable("contacts", (Serializable) list);
			
						intent21.putExtras(bundle);

						setResult(700, intent21);
						
						finish();
						//Toasttool.MyToast(context, "备份通讯录成功!");
					}catch(Exception ex){
						//Toasttool.MyToast(context, "备份通讯录失败!原因如下："+ex.getMessage());
					}
					break;
				case 6:// 帮助
					/*
					 * Intent intent3 = new
					 * Intent(context,HelpExplainActivity.class);
					 * startActivity(intent3);
					 */
					break;
				case 7:// 更新
					break;
				case 8:// 退出
					finish();
					break;
				default:
					break;
				}

			}
		});
	}

	// GridView所绑定之Adapter
	public class AppsAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] main_menu_itemName;
		private int[] main_menu_itemSource;

		public AppsAdapter(Context context, String[] main_menu_itemName,
				int[] main_menu_itemSource) {

			this.mInflater = LayoutInflater.from(context);// 用于反射layout的工具对象
			this.mContext = context;
			// 菜单名
			this.main_menu_itemName = main_menu_itemName;
			// 菜单名对应之图片资源
			this.main_menu_itemSource = main_menu_itemSource;
		}

		public int getCount() {

			return main_menu_itemName.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = mInflater.inflate(R.layout.grid_goodslist, null);
			ImageView img = (ImageView) convertView.findViewById(R.id.goodImg);
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

			img.setImageResource(main_menu_itemSource[position]);
			tvName.setText(main_menu_itemName[position]);
			return convertView;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("group", "resultCode1:" + resultCode);
		if (resultCode == 500) {// 从手机导入的回
			Intent intent = new Intent(context, ContactActivity.class);
			intent.putExtras(data);
			ToolGriewActivity.this.setResult(500, intent);
			finish();
		} else if (resultCode == 600) {// 从SIM卡导入 的回
			Intent intent = new Intent(context, ContactActivity.class);
			intent.putExtras(data);
			ToolGriewActivity.this.setResult(600, intent);
			finish();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}