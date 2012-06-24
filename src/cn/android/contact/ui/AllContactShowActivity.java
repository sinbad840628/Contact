package cn.android.contact.ui;

import java.util.List;

import cn.android.contact.R;
import cn.android.contact.biz.ContactManager;
import cn.android.contact.model.Contact;
import cn.android.contact.tools.ImageTools;
import cn.android.contact.tools.Toasttool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AllContactShowActivity extends Activity {
	public final static int EDIT_TO_UPDATE_ALL = 20;// ����༭�˵�����ת���༭����
	public final static int CONTACT_MOVETO_All = 30;// ����ƶ�..�˵�����ת�����ѡ��
	public final static int MARK_MOVE_TO_ALL = 40;// ����ƶ����..�˵�����ת�����ѡ��
	public final static int ADD_NEW_ALL = 50;// ������Ͻ��½���ת�������ϵ�˽���
	public final static int INFO_ALL = 60;// ����鿴����

	private ListView listView;
	private AllListAdapeter adapter;
	private Context context;
	private List<Contact> list;
	private ContactManager contactManager;
	private boolean isMarkedState = false;// �Ƿ��Ǳ��״̬
	private int totalcount;// ��ϵ������

	private int moveReContactId;// �ƶ���������ϵ��ID
	private int moveToGroupId = -1;// �ƶ�������Ŀ�����ID

	private EditText edtSearch;// �����༭��
	private ImageView imgDelInput;// ����ɾ���ѿ������
	private ImageView imgBack;// ���ؼ�
	private ImageView imgCreate;// �����ϵ��
	private RelativeLayout rl_mark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		isMakeFull();
		
		setContentView(R.layout.allcontacts_show_view);
		context = this;
		contactManager = new ContactManager(context);
		totalcount = contactManager.getAllCount();

		listView = (ListView) findViewById(R.id.listView);
		edtSearch = (EditText) findViewById(R.id.edtSearch);
		imgDelInput = (ImageView) findViewById(R.id.imgDelInput);
		imgBack = (ImageView) findViewById(R.id.imgBack);
		imgCreate = (ImageView) findViewById(R.id.imgCreate);
		rl_mark = (RelativeLayout) findViewById(R.id.rl_mark);

		list = contactManager.getAllContacts();
		if (list != null)
			bindView(list);

		imgCreate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(context, AddNewContactActivity.class);
				intent.putExtra("requestCode", ADD_NEW_ALL);
				startActivity(intent);

			}
		});

		// ΪlistViewע�������Ĳ˵�SETP.һ
		listView.setOnCreateContextMenuListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {

				Contact contact = (Contact) arg0.getItemAtPosition(position);
				if (isMarkedState) {

					ImageView imgChk = (ImageView) v.findViewById(R.id.imgChk);
					if (imgChk.getVisibility() == View.GONE) {
						contactManager.initIsMarkById("true", contact.getId());
						imgChk.setVisibility(View.VISIBLE);
					} else {
						contactManager.initIsMarkById("false", contact.getId());
						imgChk.setVisibility(View.GONE);
					}
				} else {
					// Toasttool.MyToast(context, contact.toString());
					Intent intent = new Intent(context,
							AddNewContactActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("requestCode", INFO_ALL);
					bundle.putInt("id", contact.getId());
					intent.putExtras(bundle);
					startActivity(intent);

				}
			}
		});

		edtSearch.setHint("����" + totalcount + "����ϵ��");
		edtSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (edtSearch.getText().toString().equals("")) {

					imgDelInput.setVisibility(View.GONE);

					edtSearch.setHint("����" + totalcount + "����ϵ��");

				} else {

					imgDelInput.setVisibility(View.VISIBLE);

				}
				String condition = edtSearch.getText().toString();
				List<Contact> list = contactManager
						.getContactByCondition(condition);
				if (list != null)
					bindView(list);

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {

			}
		});

		imgDelInput.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				edtSearch.setText("");
				list = contactManager.getAllContacts();
				if (list != null)
					bindView(list);

			};
		});

		imgBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();

			}
		});

	}

	private void isMakeFull() {
		SharedPreferences	sp = getSharedPreferences("parameter", Context.MODE_PRIVATE);
		boolean isFull = sp.getBoolean("isFull", false);
		if(isFull){
			getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,                
                    WindowManager.LayoutParams. FLAG_FULLSCREEN); }
	}

	private void bindView(List<Contact> list) {

		adapter = new AllListAdapeter(context, list);
		listView.setAdapter(adapter);
	}

	// ���������Ĳ˵����� STEP.��
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (isMarkedState) {// ���״̬�˵�
			menu.add(0, 0, 0, "�˳����״̬");
			menu.add(0, 1, 1, "ȫ�����");
			menu.add(0, 2, 2, "ȡ�����б��");
			menu.add(0, 3, 3, "ɾ�����");
			menu.add(0, 4, 4, "�ƶ���ǵ�..");
			menu.add(0, 5, 5, "ȡ��");

		} else {// ��ͨ״̬�˵�
			menu.add(1, 10, 0, "������״̬");
			menu.add(1, 11, 1, "ɾ��");
			menu.add(1, 12, 2, "�༭");
			menu.add(1, 13, 3, "�ƶ���..");
			menu.add(1, 14, 4, "ȡ��");

		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	// menuItem����¼���Ӧ STEP.��
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Contact contact = contactManager.getContactById((int) menuInfo.id);
		switch (item.getItemId()) {
		case 10:// ������״̬
			isMarkedState = true;
			rl_mark.setVisibility(View.VISIBLE);

			break;
		case 11:// ɾ��
			contactManager.deleteById(contact.getId());

			break;
		case 12:// �༭
			Intent intent = new Intent(context, AddNewContactActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("id", contact.getId());
			bundle.putInt("requestCode", EDIT_TO_UPDATE_ALL);
			intent.putExtras(bundle);
			startActivity(intent);

			break;
		case 13:// �ƶ���..
			Intent intent1 = new Intent(context, SelectGroupActivity.class);
			intent1.putExtra("requestCode", CONTACT_MOVETO_All);
			startActivityForResult(intent1, 0);

			moveReContactId = contact.getId();

			break;
		case 14:// ȡ��

			break;

		case 0:// �˳����״̬
			isMarkedState = false;
			rl_mark.setVisibility(View.GONE);
			// list = contactManager.getAllContacts();
			// if (list != null)
			// bindView(list);

			break;
		case 1:// ȫ�����
			contactManager.initIsMark("true");
			list = contactManager.getAllContacts();
			if (list != null)
				bindView(list);

			break;
		case 2:// ȡ�����б��
			contactManager.initIsMark("false");
			list = contactManager.getAllContacts();
			if (list != null)
				bindView(list);

			break;
		case 3:// ɾ�����
			contactManager.deletByIsMark();
			list = contactManager.getAllContacts();
			if (list != null)
				bindView(list);

			break;
		case 4:// �ƶ���ǵ�..
			Log.i("Item", "1111111");
			Intent intent2 = new Intent(context, SelectGroupActivity.class);
			intent2.putExtra("requestCode", MARK_MOVE_TO_ALL);
			startActivityForResult(intent2, 0);

			break;

		case 5:// ȡ��

			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 30) {// �����ƶ�..��ת�����ѡ��Ļ�
			moveToGroupId = data.getExtras().getInt("groupId");
			contactManager.changeGroupByCotact(moveReContactId, moveToGroupId);
			contactManager.initIsMarkById("false", moveReContactId);
		} else if (resultCode == 40) {// ����ƶ�..��ת�����ѡ��Ļ�
			moveToGroupId = data.getExtras().getInt("groupId");
			contactManager.changeGroupByMark(moveToGroupId);
			contactManager.initIsMark("false");

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		list = contactManager.getAllContacts();
		if (list != null)
			bindView(list);
		super.onResume();
	}

}

class AllListAdapeter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mcontext;
	private List<Contact> list;
	private boolean check = true;
	public PopupWindow popupWindow;
	private View popView;

	public AllListAdapeter(Context context, List<Contact> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mcontext = context;
		this.list = list;

	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {

		return list.get(position);
	}

	public long getItemId(int position) {

		return list.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.child, null);
		final Contact contact = (Contact) getItem(position);

		ImageView imgPhoto = (ImageView) convertView
				.findViewById(R.id.imgContact);
		TextView txtName = (TextView) convertView.findViewById(R.id.childName);
		TextView txtPhone = (TextView) convertView
				.findViewById(R.id.childPhone);
		// Log.i("testGroupService", contact.toString());
		imgPhoto.setImageBitmap(ImageTools.getBitmapFromByte(contact.getImage()));
		ImageView imgChk = (ImageView) convertView.findViewById(R.id.imgChk);
		if (contact.getIsMark().equals("true")) {
			imgChk.setVisibility(View.VISIBLE);
		} else {
			imgChk.setVisibility(View.GONE);
		}

		txtName.setText(contact.getName());

		txtPhone.setText(contact.getPhone());

		ImageView popImg = (ImageView) convertView.findViewById(R.id.popImg);
		popImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// if (check) {
				// check = false;
				if (popupWindow == null) {
					popView = mInflater.inflate(R.layout.popupwindow_view,
							null, false);
					popupWindow = new PopupWindow(popView,
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, true);

					popupWindow.setBackgroundDrawable(new BitmapDrawable());

				}
				// if (popupWindow.isShowing())
				// popupWindow.dismiss();
				// Toasttool.MyToast(mcontext, "pop");
				popupWindow.showAsDropDown(v);

				ImageButton imgCall = (ImageButton) popView
						.findViewById(R.id.imgCall);
				ImageButton imgMsg = (ImageButton) popView
						.findViewById(R.id.imgMsg);
				ImageButton imgEmail = (ImageButton) popView
						.findViewById(R.id.imgEmail);

				imgCall.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						Toasttool.MyToast(mcontext, "call");

						((AllContactShowActivity) mcontext).makeCall(contact
								.getPhone());

						popupWindow.dismiss();
					}

				});
				imgMsg.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Toasttool.MyToast(mcontext, "Msg");
						((AllContactShowActivity) mcontext).sendMsg(contact
								.getPhone());

						popupWindow.dismiss();

					}
				});
				imgEmail.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Toasttool.MyToast(mcontext, "Email");
						((AllContactShowActivity) mcontext).sendEmail(contact
								.getE_mail().toString());

						popupWindow.dismiss();

					}
				});

				// } else {
				// Toasttool.MyToast(mcontext, "pop2");
				// check = true;
				// popupWindow.dismiss();
				//
				// }
			}
		});

		return convertView;
	}

}
