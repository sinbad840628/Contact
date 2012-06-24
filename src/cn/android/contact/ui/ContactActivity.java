package cn.android.contact.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.android.contact.R;
import cn.android.contact.biz.ContactManager;
import cn.android.contact.biz.GroupManager;
import cn.android.contact.model.Contact;
import cn.android.contact.model.Group;
import cn.android.contact.tools.ImageTools;
import cn.android.contact.tools.Toasttool;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebIconDatabase.IconListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ContactActivity extends Activity {

	/**
	 * requestCode
	 */
	public final static int GROUP_TO_ADD_CONTACT = 0;// �ɷ���˵�����ת���½���ϵ�˽���
	public final static int CTREAT_TO_ADDNEW = 1; // ������Ͻ��½���ת���½���ϵ�˽���
	public final static int GROUPCONTACT_MOVETO = 2;// ȫ���Ա�ƶ���ת�����ѡ��
	public final static int CONTACT_MOVETO = 4;// �����ƶ���ת�����ѡ��
	public final static int EDIT_CONTACT = 11;// ����༭��ת���޸���ϵ�˽���
	public final static int MARK_MOVE_TO = 5;// �������ƶ�..��ת�����ѡ��
	public final static int INFO = 6;// ����鿴����

	private ExpandableListAdapter adapter;
	private ExpandableListView expandableListView;
	private Context context;
	private GroupManager groupManager;
	private ContactManager contactManager;

	private ImageView imgContacts;// ���Ͻ��л�����ϵ���б��ImageView
	private ImageView imgCreate;// ���Ͻ��л����½���ϵ�˵�ImageView
	private RelativeLayout rl_mark;

	private boolean[] isOpen;// ���ڴ���expandableListView��ͷСͼ�������
	private int moveToGroupId = -1;// �ƶ�������Ŀ�����ID
	private int moveReGroupId;// �ƶ�������ԭ����ID
	private int moveReContactId;// �ƶ���������ϵ��ID

	private boolean isMarkedState = false;// �Ƿ��Ǳ��״̬
	private boolean isFirst = true;// Ϊ�˵�һ����onResume����ִ�����ڵĲ���
	private List<Group> list;// ������е�group

	private GridView bottomMenuGrid;// ��Ļ�·����˵��Ĳ���
	
	
	private String[] bottom_menu_itemName = { "������", "ȫ��", "�˳�" };// ���˵�����
	
	private int[] bottom_menu_itemSource = { R.drawable.menu_tool, // ���˵�ͼƬ
			R.drawable.full_screen, R.drawable.menu_exit };


	// ΪMyExpandableListAdapter����������Դ
	private List<String> groupItem = new ArrayList<String>();// ������е��������
	private List<List<Contact>> contacts = new ArrayList<List<Contact>>();// �������contact
	private List<Integer> groupCount = new ArrayList<Integer>();// ��Ÿ�������contact������

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//�Ƿ�ȫ���ж�
		isMakeFull();
		
		setContentView(R.layout.main);

		context = this;
		groupManager = new GroupManager(context);
		contactManager = new ContactManager(context);

		// ��ʼ�����״̬��false:ȫ��δ��ǣ�true:ȫ�����
		contactManager.initIsMark("false");

		getId();

		expandableListView.setGroupIndicator(null);// ��ͷСͼ�꣬�����Դ�����
		expandableListView.setCacheColorHint(0);// ��ֹexpandableListView������

		// ΪexpandableListViewע�������Ĳ˵�SETP.һ
		expandableListView.setOnCreateContextMenuListener(this);

		// �����ݣ�ˢ�½���
		bindView();
		// ͷ��ѡ�����

		// Group�������
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView mExpandableList,
					View arg1, final int groupPosition, long id) {
				return false;
			}

		});

		// Contact�������
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				Contact contact = contacts.get(groupPosition)
						.get(childPosition);
				if (isMarkedState) {

					ImageView img = (ImageView) v.findViewById(R.id.imgChk);
					if (img.getVisibility() == View.GONE) {
						contactManager.initIsMarkById("true", contact.getId());
						img.setVisibility(View.VISIBLE);
					} else {
						contactManager.initIsMarkById("false", contact.getId());

						img.setVisibility(View.GONE);
					}
				} else {
					Intent intent = new Intent(context,
							AddNewContactActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("requestCode", INFO);
					bundle.putInt("id", contact.getId());
					intent.putExtras(bundle);
					startActivity(intent);

					// Toast.makeText(context, contact.toString(),
					// Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});

		// ����expandableListView�Զ����ͷСͼ�걻����SETP.1
		expandableListView
				.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

					public void onGroupCollapse(int groupPosition) {
						isOpen[groupPosition] = false;

					}
				});

		// SETP.2
		expandableListView
				.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

					public void onGroupExpand(int groupPosition) {
						isOpen[groupPosition] = true;

					}
				});

		// �л�����ϵ����ͼ
		imgContacts.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Toasttool.MyToast(context, "��ϵ��");
				Intent intent = new Intent(context,
						AllContactShowActivity.class);
				startActivity(intent);

			}
		});
		// �½���ϵ��
		imgCreate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent intent2 = new Intent(context,
						AddNewContactActivity.class);
				intent2.putExtra("requestCode", CTREAT_TO_ADDNEW);
				startActivity(intent2);

			}
		});

	}

	private void isMakeFull() {
		SharedPreferences	sp = getSharedPreferences("parameter", Context.MODE_PRIVATE);
		boolean isFull = sp.getBoolean("isFull", false);
		if(isFull){
			getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,                
                    WindowManager.LayoutParams. FLAG_FULLSCREEN); 
			
		}
	}

	private void getId() {
		// bottomMenuGrid = (GridView) findViewById(R.id.gv_buttom_menu);

		rl_mark = (RelativeLayout) findViewById(R.id.rl_mark);
		imgContacts = (ImageView) findViewById(R.id.imgContacts);
		imgCreate = (ImageView) findViewById(R.id.imgCreate);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
	}

	// ����ϵͳ����EMAIL
	public void sendEmail(String Email) {
		Uri uri = Uri.parse("mailto:" + Email);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		startActivity(it);

	}

	// ����ϵͳ������
	public void sendMsg(String phone) {
		if (phone.equals("")) {
			Toasttool.MyToast(context, "û�к��룡");

		} else {
			Uri uri = Uri.parse("smsto:" + phone);

			Intent it = new Intent(Intent.ACTION_SENDTO, uri);

			it.putExtra("sms_body", "");

			startActivity(it);
		}

	}

	// ����ϵͳ��绰
	public void makeCall(String phone) {
		if (phone.equals("")) {
			Toasttool.MyToast(context, "û�к��룡");

		} else {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + phone));
			startActivity(intent);
		}
	}

	private void bindView() {
		getData();
		if (contacts != null) {
			adapter = new MyExpandableListAdapter(groupCount, groupItem,
					contacts, isOpen, context);
			expandableListView.setAdapter(adapter);
		}
	}

	private void getData() {
		groupItem.clear();
		contacts.clear();
		groupCount.clear();
		list = groupManager.getAllGroup();
		Log.i("Group", "1111111111");

		for (Group p : list) {
			groupItem.add(p.getGroupName());
			groupCount.add(contactManager.getCountByGroupId(p.getGroupId()));
			contacts.add(contactManager.getContactByGroupId(p.getGroupId()));
		}

		isOpen = new boolean[groupManager.getGroupCount()];
	}

	// ���������Ĳ˵����� STEP.��
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(info.packedPosition);
		// int childPosition = ExpandableListView
		// .getPackedPositionChild(info.packedPosition);

		if (type == 0)// �������GROUP

		{
			if (groupPosition != 0) {// Ĭ�Ϸ����ֹɾ��
				menu.add(0, 1, 1, "ɾ������");
			}
			// ��Ӳ˵���
			menu.add(0, 0, 0, "��ӷ���");
			menu.add(0, 2, 2, "������");
			menu.add(0, 3, 3, "�����ϵ��");
			menu.add(0, 4, 5, "ȡ��");
			menu.add(0, 5, 4, "ȫ���Ա�ƶ�..");
		} else if (type == 1) {// ���������ϵ��

			if (isMarkedState) {// ���״̬�˵�
				menu.add(2, 20, 0, "�˳����״̬");
				menu.add(2, 21, 1, "ȫ�����");
				menu.add(2, 22, 2, "ȡ�����б��");
				menu.add(2, 23, 3, "ɾ�����");
				menu.add(2, 24, 4, "�ƶ���ǵ�..");
				menu.add(2, 25, 5, "ȡ��");

			} else {// ��ͨ״̬�˵�
				menu.add(1, 10, 0, "������״̬");
				menu.add(1, 12, 2, "ɾ��");
				menu.add(1, 13, 4, "�ƶ���..");
				menu.add(1, 14, 5, "ȡ��");
				menu.add(1, 15, 3, "�༭");
			}

		}

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	// menuItem����¼���Ӧ STEP.��
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListView.ExpandableListContextMenuInfo menuInfo = (ExpandableListView.ExpandableListContextMenuInfo) item
				.getMenuInfo();
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(menuInfo.packedPosition);
		int childPosition = ExpandableListView
				.getPackedPositionChild(menuInfo.packedPosition);

		Contact contact = null;

		if (childPosition != -1) {// ��child����ʾ�ٵõ�contact
			contact = contacts.get(groupPosition).get(childPosition);
		}

		Group group = list.get(groupPosition);
		switch (item.getItemId()) {
		case 0:// ��ӷ���
			Intent intent = new Intent(context, AddNewGroupActivity.class);
			startActivity(intent);

			break;
		case 1:// ɾ������

			contactManager.changeGroupByGroup(group.getGroupId(), 1);// �����Ա�ƶ���Ĭ�Ϸ�����
			groupManager.deleteGroupByID(group.getGroupId());// ɾ��ָ���ķ���
			bindView();

			break;
		case 2:// ������

			Intent intent1 = new Intent(context, AddNewGroupActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("groupId", group.getGroupId());
			bundle.putString("groupName", group.getGroupName());
			intent1.putExtras(bundle);
			startActivity(intent1);

			break;
		case 3:// �����ϵ��
			Intent intent2 = new Intent(context, AddNewContactActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putInt("groupId", group.getGroupId());
			bundle1.putString("groupName", group.getGroupName());
			bundle1.putInt("requestCode", GROUP_TO_ADD_CONTACT);
			intent2.putExtras(bundle1);
			startActivity(intent2);

			break;
		case 4:// ȡ��

			break;
		case 5:// ȫ���Ա�ƶ�..
			moveReGroupId = group.getGroupId();
			Intent intent3 = new Intent(context, SelectGroupActivity.class);
			intent3.putExtra("requestCode", GROUPCONTACT_MOVETO);
			startActivityForResult(intent3, 0);

			break;
		case 10:// ������״̬
			rl_mark.setVisibility(View.VISIBLE);
			Toasttool.MyToast(context, "11");

			isMarkedState = true;

			break;

		case 12:// ɾ��
			if (contact != null){
				final int contactId = contact.getId();
				(new AlertDialog.Builder(context)).setTitle("����")
				.setIcon(R.drawable.contact).setMessage("��ȷ��Ҫɾ������ϵ����Ϣ��")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						contactManager.deleteById(contactId);
						bindView();
					}}).setNegativeButton("ȡ��", null).create().show();
				
			}
				
			break;
		case 13:// �ƶ���..

			Intent intent4 = new Intent(context, SelectGroupActivity.class);
			intent4.putExtra("requestCode", CONTACT_MOVETO);
			intent4.putExtra("groupName",group.getGroupName());
			startActivityForResult(intent4, 0);
			if (contact != null)
				moveReContactId = contact.getId();

			break;
		case 14:// ȡ��

			break;
		case 15:// ��݋
			Intent intent5 = new Intent(context, AddNewContactActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putInt("id", contact.getId());
			bundle2.putInt("requestCode", EDIT_CONTACT);
			intent5.putExtras(bundle2);
			startActivity(intent5);

			break;
		case 20:// �˳����״̬
			rl_mark.setVisibility(View.GONE);
			contactManager.initIsMark("false");
			isMarkedState = false;
			((MyExpandableListAdapter) adapter).refresh();

			break;
		case 21:// ȫ�����
			contactManager.initIsMark("true");
			// bindView();
			((MyExpandableListAdapter) adapter).refresh();

			break;
		case 22:// ȡ�����б��
			contactManager.initIsMark("false");
			((MyExpandableListAdapter) adapter).refresh();

			break;
		case 23:// ɾ�����
			contactManager.deletByIsMark();
			bindView();

			break;
		case 24:// �ƶ���ǵ�����
			Intent intent6 = new Intent(context, SelectGroupActivity.class);
			intent6.putExtra("requestCode", MARK_MOVE_TO);
			startActivityForResult(intent6, 0);

			break;
		case 25:// ȡ��

			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	protected void onResume() {
		if (!isFirst) {
			bindView();
		}
		isFirst = false;
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("group", "resultCode:"+resultCode);

		if (resultCode == 2 && data != null) {// ȫ���Ա�ƶ�..��ת�����ѡ��Ļ�
			moveToGroupId = data.getExtras().getInt("groupId");
			contactManager.changeGroupByGroup(moveReGroupId, moveToGroupId);
		} else if (resultCode == 4) {// �����ƶ�..��ת�����ѡ��Ļ�
			moveToGroupId = data.getExtras().getInt("groupId");
			contactManager.changeGroupByCotact(moveReContactId, moveToGroupId);

		} else if (resultCode == 5) {// ����ƶ�..��ת�����ѡ��Ļ�
			moveToGroupId = data.getExtras().getInt("groupId");

			contactManager.changeGroupByMark(moveToGroupId);
			contactManager.initIsMark("false");

		} else if (resultCode == 500) {// ���ֻ�����Ļ�

			Log.i("group", "33");
			List<Object> list = (List<Object>) data.getExtras().get("contacts");
			List<String> name = (List<String>) list.get(0);
			List<String> phone = (List<String>) list.get(1);
			List<Bitmap> photo = Const.mContactsPhonto;//(List<Bitmap>) list.get(2);

			Log.i("Item", name.size() + "");
			
			// List<Bitmap> photo = (List<Bitmap>) list.get(2);

			//Bitmap picture = BitmapFactory.decodeResource(getResources(),
					//R.drawable.bg_photo_default);
			//byte[] photo = ImageTools.getByteFromBitmap(picture);
			// Log.i("Item", "name1:"+name.size() + "");
			for (int i = 0; i < name.size(); i++) {
				Contact contact = new Contact(ImageTools.getByteFromBitmap(Const.mContactsPhonto.get(i)), name.get(i), 1,
						phone.get(i), "", "", "", "", "", "", "false");
				contactManager.addContact(contact);
				Log.i("Item", contact.toString());
			}

			getData();
			//adapter.refresh();
			

			
		} else if (resultCode == 600 || resultCode==700) {// ��SIM�������SDCard��ԭ��

			List<Object> list = (List<Object>) data.getExtras().get("contacts");

			if (((List<String>)list.get(0)).size()==0) {
				Toasttool.MyToast(context, "û����ϵ�ˣ�");

			} else {
				List<String> name = (List<String>) list.get(0);
				List<String> phone = (List<String>) list.get(1);

				Bitmap picture = BitmapFactory.decodeResource(getResources(),
						R.drawable.bg_photo_default);
				byte[] photo = ImageTools.getByteFromBitmap(picture);
				//Log.i("Item", "name1:"+name.size() + "");
				for (int i = 0; i < name.size() ; i++) {
					Contact contact = new Contact(photo, name.get(i), 1,
							phone.get(i), "", "", "", "", "", "",
							"false");
					contactManager.addContact(contact);
					Log.i("Item", contact.toString());
				}
				
			}
		}
		else if(resultCode ==1000){//����ȫ���Ļ�
			getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,                
                    WindowManager.LayoutParams. FLAG_FULLSCREEN);    
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			// rl_mark.setVisibility(View.VISIBLE);
			loadBottomMenu();
			Toasttool.MyToast(context, "menu");
			if (bottomMenuGrid.getVisibility() == View.VISIBLE) {
				bottomMenuGrid.setVisibility(View.GONE);
			} else {
				bottomMenuGrid.setVisibility(View.VISIBLE);
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	// button_menu���˵���Adapter
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}

	private void loadBottomMenu() {

		if (bottomMenuGrid == null) {

			bottomMenuGrid = (GridView) findViewById(R.id.gv_buttom_menu);
			bottomMenuGrid.setBackgroundResource(R.drawable.channelgallery_bg);// ���ñ���
			bottomMenuGrid.setNumColumns(3);// ����ÿ������
			bottomMenuGrid.setGravity(Gravity.CENTER);// λ�þ���
			bottomMenuGrid.setVerticalSpacing(10);// ��ֱ���
			bottomMenuGrid.setHorizontalSpacing(10);// ˮƽ���
			bottomMenuGrid.setAdapter(getMenuAdapter(bottom_menu_itemName,
					bottom_menu_itemSource));// ���ò˵�Adapter
			/** �����ײ��˵�ѡ�� **/
			bottomMenuGrid.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int item, long arg3) {
					switch (item) {
					case 0:// ������
						Toasttool.MyToast(context, "������");
						Intent intent = new Intent(context,ToolGriewActivity.class);
						startActivityForResult(intent, 8);
						
//						loadMainMenuDialog();
//						mainMenuDialog.show();

						break;
					case 1:// ȫ��
//						Toasttool.MyToast(context, "����");
						Intent intent2 = new Intent(context,FullScreenActivity.class);
						startActivityForResult(intent2, 6);
						
						

						break;
					case 2:// �˳�
						Toasttool.MyToast(context, "�˳�");
						finish();

						break;

					default:
						break;
					}

				}

			});
		}
	}

	

}
