package cn.android.contact.ui;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.android.contact.R;
import cn.android.contact.biz.ContactManager;
import cn.android.contact.biz.GroupManager;
import cn.android.contact.model.Contact;
import cn.android.contact.tools.ImageTools;
import cn.android.contact.tools.Toasttool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;

public class AddNewContactActivity extends Activity implements ViewFactory {

	/*
	 * �������� items�������ʾ������ paths������ļ�·�� rootPath����ʼĿ¼
	 */
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = "/";
	private TextView mPath;
	private View myView;
	private Bitmap mPhoto;
	private boolean isFromFile = false;

	private Dialog dialog = null;
	private Context context;
	private Button btnGroup;
	private Button btnDate;
	private Button btnMore;

	private String groupName;
	private int groupId = 1;
	private String birthday = "";

	private GroupManager groupManager;
	private ContactManager contactManager;

	private List<String> groupItem;
	private String itemPosition = "";
	private Button btnSure;
	private Button btnCancel;
	private ImageView imgBack;
	private ImageView imgEdit;
	private LinearLayout ll_btn;

	private ImageButton imgbtn;
	private EditText edtName;
	private EditText edtPhone;
	private EditText edtHomePhone;
	private EditText edtOtherPhone;
	private EditText edtEmail;
	private EditText edtAddress;
	private EditText edtComment;
	private boolean isSelectGroup = false;
	private PopupWindow popupWindow;
	private View popView;
	private boolean check = true;
	private TextView txtTitle;

	RelativeLayout position_0;
	RelativeLayout position_1;
	RelativeLayout position_2;
	RelativeLayout position_3;

	private View imageChooseView;// ͷ��ѡ����ͼ
	private AlertDialog imageChooseDialog;// ͷ��ѡ��Ի���
	private Gallery gallery;// ͷ���Gallery
	private ImageSwitcher imageSwitcher;// ͷ���ImageSwitcher
	int currentImagePosition;// ���ڼ�¼��ǰѡ��ͼ����ͼ�������е�λ��
	int previousImagePosition;// ���ڼ�¼��һ��ͼƬ��λ��
	boolean imageChanged = false;// �ж�ͷ����û�б仯
	// ͷ������
	private int[] images = new int[] { R.drawable.p1, R.drawable.p2,
			R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6,
			R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10,
			R.drawable.p11, R.drawable.p12, R.drawable.p13 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		isMakeFull();
		
		
		setContentView(R.layout.add_contact);
		context = this;
		groupManager = new GroupManager(context);
		contactManager = new ContactManager(context);

		groupItem = new ArrayList<String>();
		groupItem.add("��ͥ�绰");
		groupItem.add("�����绰");
		groupItem.add("Email");
		groupItem.add("��ַ");
		groupItem.add("��������");
		groupItem.add("��ӱ�ע");

		getId();
		/**
		 * requestCode = 1 ��ʾ�ǵ�����Ͻǵ��½���ϵ����ת������ 
		 * requestCode = 0��ʾ�ǵ������Ҽ��˵����½���ϵ����ת������ 
		 * requestCode = 11��ʾ�ǵ���༭��ת������
		 * requestCode =20��ʾ�Ǵ�������ϵ���б�ı༭��ת������ 
		 * requestCode = 50 ��ʾ�Ǵ�������ϵ���б�������ת������
		 * requestCode = 60 ��ʾ��������ϵ���б�Ĳ鿴������ת������
		 * requestCode = 6 ��ʾ��������Ĳ鿴������ת������
		 */

		int requestCode = getIntent().getExtras().getInt("requestCode");
		if (requestCode == 0) {
			String groupName = getIntent().getExtras().getString("groupName");
			Log.i("testGroupService", groupName);
			btnGroup.setText(groupName);
		} else if (requestCode == 1 || requestCode == 50) {
			groupName = groupManager.getGroupByID(1).getGroupName();
		} else if (requestCode == 11 || requestCode == 20 || requestCode == 60
				|| requestCode == 6) {

			txtTitle.setText("�༭��ϵ��");
			LinearLayout ll_left = (LinearLayout) findViewById(R.id.ll_left);
			LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);

			if (requestCode == 60 || requestCode == 6) {

				ll_left.setVisibility(View.VISIBLE);
				imgBack.setVisibility(View.VISIBLE);

				ll_right.setVisibility(View.VISIBLE);
				imgEdit.setVisibility(View.VISIBLE);

				ll_btn.setVisibility(View.GONE);

				isEnalbe(false);
				txtTitle.setText("��ϵ������");

			}

			Contact contact = contactManager.getContactById(getIntent()
					.getExtras().getInt("id"));
			// ͷ��
			
			imgbtn.setImageBitmap(ImageTools.getBitmapFromByte(contact
					.getImage()));
			edtName.setText(contact.getName());
			//�绰
			edtPhone.setText(contact.getPhone().toString() );
			String groupName = (new GroupManager(context)).getGroupByID(contact.getGroupId()).getGroupName();
			btnGroup.setText(groupName);
			
			if(!contact.getHomePhone().equals("")){
				edtHomePhone.setText(contact.getHomePhone().toString() );
				position_0.setVisibility(View.VISIBLE);
				removeGroupItem("��ͥ�绰");
				
			}
				
			
			if(!contact.getOtherPhone().equals("")){
				edtOtherPhone.setText(contact.getOtherPhone().toString() );
				position_1.setVisibility(View.VISIBLE);
				removeGroupItem("�����绰");
				
			}
				
		
			if (!contact.getE_mail().equals("")) {

				edtEmail.setText(contact.getE_mail());
				position_2.setVisibility(View.VISIBLE);
				removeGroupItem("Email");
			}
			if (!contact.getAddress().equals("")) {
				edtAddress.setText(contact.getAddress());
				position_3.setVisibility(View.VISIBLE);
				removeGroupItem("��ַ");
			}
			if (!contact.getBirthday().equals("")) {
				btnDate.setTag(contact.getBirthday());
				btnDate.setVisibility(View.VISIBLE);
				removeGroupItem("��������");
			}
			if (!contact.getComment().equals("")) {
				edtComment.setText(contact.getComment());
				edtComment.setVisibility(View.VISIBLE);
				removeGroupItem("��ӱ�ע");

			}
		}

		// ͷ��ѡ�����
		imgbtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Intent intent = new
				// Intent(context,ContactPhotoSelectActiviyt.class);
				// startActivity(intent);
				//

				Toasttool.MyToast(context, "p");
				// if (check) {
				Toasttool.MyToast(context, "pop");
				check = false;
				if (popupWindow == null) {
					popView = LayoutInflater.from(context).inflate(
							R.layout.photo_select_view, null, false);
					popupWindow = new PopupWindow(popView,
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, true);

					popupWindow.setBackgroundDrawable(new BitmapDrawable());
					// if (popupWindow.isShowing())
					// popupWindow.dismiss();
				}

				popupWindow.showAsDropDown(v, 60, -80);

				TextView txtPhoto = (TextView) popView.findViewById(R.id.txtPhoto);
				TextView txtSystem = (TextView) popView.findViewById(R.id.txtSystem);
				TextView txtFile = (TextView) popView.findViewById(R.id.txtFile);

				txtPhoto.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Toasttool.MyToast(context, "����");
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						startActivityForResult(intent,Activity.DEFAULT_KEYS_DIALER);
						popupWindow.dismiss();
					}
				});

				txtSystem.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Toasttool.MyToast(context, "ϵͳ");
						initImageChooseDialog();// ��ʼ��imageChooseDialog
						imageChooseDialog.show();
						popupWindow.dismiss();

					}

				});
				txtFile.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Toasttool.MyToast(context, "�ļ�");
						selectImageFile();
						popupWindow.dismiss();

					}

				});

				// } else {
				// Toasttool.MyToast(context, "pop2");
				// check = true;
				// popupWindow.dismiss();
				//
				// }
			}
		});
		// ���ؼ�����
		imgBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();

			}
		});
		// �༭������
		imgEdit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				ll_btn.setVisibility(View.VISIBLE);
				isEnalbe(true);
				txtTitle.setText("�༭��ϵ��");

			}
		});

		btnSure.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// ͷ��
				String name = edtName.getText().toString();
				int mGroupId = 1;
				if (isSelectGroup) {// �ֶ�ѡ�������
					mGroupId = groupId;
					Log.i("hubin", "1mGroupId:" + mGroupId);
				} else {// û���ֶ�ѡ�����
					int requestCode = getIntent().getExtras().getInt(
							"requestCode");
					if (requestCode == 11 || requestCode == 60
							|| requestCode == 6) {// ����༭��ת������
						Contact contact = contactManager
								.getContactById(getIntent().getExtras().getInt(
										"id"));
						mGroupId = contact.getGroupId();

					} else if (requestCode == 1 || requestCode == 50) {// ������Ͻ������ת������
						mGroupId = 1;

					} else {// ���ĳ�������ת������
						int groupId = getIntent().getExtras().getInt("groupId");
						mGroupId = groupId;
					}
					Log.i("hubin", "2mGroupId:" + mGroupId);
				}

			String phone = edtPhone.getText().toString();
			String homePhone = edtHomePhone.getText().toString();
			String otherPhone = edtOtherPhone.getText().toString();
				
				

				

				String e_mail = edtEmail.getText().toString();
				String address = edtAddress.getText().toString();
				String mBirthday = birthday;
				String comment = edtComment.getText().toString();
				if (name.equals("")) {
					Toasttool.MyToast(context, "���ֲ���Ϊ�գ�");

				} else {
					Contact contact = contactManager.getContactById(getIntent()
							.getExtras().getInt("id"));

					// �޸�ͷ��ģ�
					byte[] photo;

					if (imageChanged) {
						Resources res = getResources();

						Bitmap picture = BitmapFactory.decodeResource(res,
								images[currentImagePosition % images.length]);
						photo = ImageTools.getByteFromBitmap(picture);
						//Log.i("testContactService", photo + "");
					} else {
						if (isFromFile) {
							photo = ImageTools.getByteFromBitmap(mPhoto);

						} else {
							try {
								photo = contact.getImage();
							} catch (Exception e) {
								photo = ImageTools.getByteFromBitmap(BitmapFactory
										.decodeResource(getResources(),
												R.drawable.default_avatar_big));
							}

						}

						// picture = images[previousImagePosition
						// % images.length];
					}

					int requestCode = getIntent().getExtras().getInt(
							"requestCode");

					if (requestCode == 11 || requestCode == 20
							|| requestCode == 60 || requestCode == 6) {// �޸�

						int id = getIntent().getExtras().getInt("id");
						Contact contact1 = new Contact(id, photo, name,
								mGroupId, phone, homePhone, otherPhone, e_mail,
								address, mBirthday, comment, "false");
						// contact.setImage(photo);
						contactManager.modifyContact(contact1);

					} else {// ���
						Contact contact1 = new Contact(photo, name, mGroupId,
								phone, homePhone, otherPhone, e_mail, address,
								mBirthday, comment, "false");
						// contact1.setImage(photo);
						Log.i("testContactService", contact1.toString());
						contactManager.addContact(contact1);
					}

					finish();
				}

			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();

			}
		});

		// ѡ�������� ����
		btnMore.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Log.i("child", "1111111121" + groupItem);

				if (groupItem.size() == 0) {
					Toasttool.MyToast(context, "û�п���ӵ�����");
				} else {
					Intent intent = new Intent(context,
							AddMoreContactInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("groupItem",
							(Serializable) groupItem);

					intent.putExtras(bundle);
					startActivityForResult(intent, 0);
				}

			}
		});

		// ѡ�����
		btnGroup.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(context, SelectGroupActivity.class);
				intent.putExtra("requestCode", 3);
				intent.putExtra("groupName", btnGroup.getText().toString().trim());
				startActivityForResult(intent, 0);

			}
		});
		// ѡ������
		btnDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				DatePickerDialog dateDialog = new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								btnDate.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
								birthday = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
							}
						}, 2011, 10, 20);
				dateDialog.show();
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

	private void selectImageFile() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.select_folder_main, null);
		dialog = new AlertDialog.Builder(context).setTitle("ѡ���ļ�")
				.setIcon(R.drawable.flag).setView(view)
				.setNegativeButton("�� ��", null).create();
		dialog.show();
		/* ��ʼ��mPath��������ʾĿǰ·�� */
		mPath = (TextView) view.findViewById(R.id.mPath);
		getFileDir(rootPath);
	}

	/* ȡ���ļ��ܹ���method */
	private void getFileDir(String filePath) {
		/* �趨Ŀǰ����·�� */
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();

		File f = new File(filePath);
		File[] files = f.listFiles();

		if (!filePath.equals(rootPath)) {
			/* ��һ���趨Ϊ[������Ŀ¼] */
			items.add("b1");
			paths.add(rootPath);
			/* �ڶ����趨Ϊ[���ײ�] */
			items.add("b2");
			paths.add(f.getParent());
		}
		/* �������ļ�����ArrayList�� */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}

		/* ʹ���Զ����MyAdapter�������ݴ���ListActivity */

		if (dialog != null) {
			ListView lv = (ListView) dialog.findViewById(R.id.mList);
			lv.setAdapter(new MyFolderAdapter(this, items, paths));
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> l, View v, int position,
						long id) {
					// TODO Auto-generated method stub
					File file = new File(paths.get(position));
					if (file.canRead()) {
						if (file.isDirectory()) {
							/* ������ļ��о�����getFileDir() */
							getFileDir(paths.get(position));
						} else {
							/* ������ļ�����fileHandle() */
							fileHandle(file);
						}
					} else {
						/* ����AlertDialog��ʾȨ�޲��� */
						new AlertDialog.Builder(context)
								.setTitle("Message")
								.setMessage("Ȩ�޲���!")
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										}).show();
					}

				}

			});
		}

	}

	private void initImageChooseDialog() {
		if (imageChooseDialog != null)
			return;
		if (imageChooseView == null) { // Ϊgallery��ͼƬ������ImageChooseView����ͼ
			LayoutInflater li = LayoutInflater.from(context);
			imageChooseView = li.inflate(R.layout.imagechoose, null);

			gallery = (Gallery) imageChooseView.findViewById(R.id.imggallery);// �õ���ͼ��gallery��λ��
			gallery.setAdapter(new ImageAdapter(context));// ΪGalleryװ��ͼƬ
			gallery.setSelection(images.length / 2);// ��ʼ��ʾ�м�λ�õ�ͷ��

			imageSwitcher = (ImageSwitcher) imageChooseView
					.findViewById(R.id.imgswitch);
			imageSwitcher.setFactory(AddNewContactActivity.this);
			imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,// ���ض���Ч����Ч�������Լ�д��
					android.R.anim.fade_in));
			imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,// ж��ͼƬ�Ķ���Ч��
					android.R.anim.fade_out));

			// Ϊͷ��gallery���ü�������¼ѡ�е�ͷ��
			gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					currentImagePosition = arg2;// ��ǰ��ͷ��λ��Ϊѡ�е�λ��
					imageSwitcher
							.setImageResource(images[arg2 % images.length]);// ΪImageSwitcher����ͼ��
				}

				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // ���ͷ�񣬵���һ��ͷ��ѡ��ĶԻ���
		builder.setTitle("��ѡ��ͼ��");
		builder.setView(imageChooseView);// ���ع�ѡ���ͷ��ѡ����ͼ
		builder.setPositiveButton("ȷ��",// ���ȷ������ѵ�ǰ��ͼƬ��ֵ��ͷ��ť��ʾ
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						imageChanged = true;
						previousImagePosition = currentImagePosition;
						imgbtn.setImageResource(images[currentImagePosition
								% images.length]);
					}
				});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				currentImagePosition = previousImagePosition;

			}
		});
		imageChooseDialog = builder.create();

	}

	/* �����ļ���method */
	private void fileHandle(final File file) {
		/* �����ļ�ʱ��OnClickListener */
		OnClickListener listener1 = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					/* ѡ���itemΪ���ļ�Ԥ�� */
					previewFile(file);
				} else if (which == 1)// ѡ���ļ�
				{
					openFile(file);
				} else // ɾ���ļ�
				{
					/* ѡ���itemΪɾ���ļ� */
					new AlertDialog.Builder(context)
							.setTitle("ע��!")
							.setMessage("ȷ��Ҫɾ���ļ���?")
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											/* ɾ���ļ� */
											file.delete();
											getFileDir(file.getParent());
										}
									})
							.setNegativeButton("ȡ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).show();
				}
			}

		};

		/* ѡ�񼸸��ļ�ʱ������Ҫ�����ļ���ListDialog */
		String[] menu = { "Ԥ��ͼƬ", "ѡ��ͼƬ", "ɾ���ļ�" };
		new AlertDialog.Builder(context).setTitle("��Ҫ����ô?")
				.setItems(menu, listener1)
				.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	/* �ֻ�Ԥ���ļ���method */
	private void previewFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* ����getMIMEType()��ȡ��MimeType */
		String type = getMIMEType(f);
		/* �趨intent��file��MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		startActivity(intent);
	}

	/* �ж��ļ�MimeType��method */
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* ȡ����չ�� */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* ����չ�������;���MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else {
			type = "*";
		}
		/* ����޷�ֱ�Ӵ򿪣��͵�������б���û�ѡ�� */
		type += "/*";
		return type;
	}

	/* �ֻ�ѡ���ļ���method */
	private void openFile(File f) {
		Bitmap bm = ImageTools.getBitemapFromFile(f);
		if (bm != null) {
			imgbtn.setImageBitmap(bm);
			mPhoto = bm;
			isFromFile = true;
		} else {
			Toasttool.MyToast(context, "����ͼƬ�ļ�ʧ��!");
		}
		if (dialog != null)
			dialog.dismiss();
	}

	private void isEnalbe(boolean isEnalbe) {
		imgbtn.setEnabled(isEnalbe);

		edtName.setEnabled(isEnalbe);
		edtName.setFocusable(isEnalbe);
		edtName.setFocusableInTouchMode(isEnalbe);

		btnGroup.setEnabled(isEnalbe);

		edtPhone.setEnabled(isEnalbe);
		edtPhone.setFocusable(isEnalbe);
		edtPhone.setFocusableInTouchMode(isEnalbe);

		edtHomePhone.setEnabled(isEnalbe);
		edtHomePhone.setFocusable(isEnalbe);
		edtHomePhone.setFocusableInTouchMode(isEnalbe);

		edtOtherPhone.setEnabled(isEnalbe);
		edtOtherPhone.setFocusable(isEnalbe);
		edtOtherPhone.setFocusableInTouchMode(isEnalbe);

		edtEmail.setEnabled(isEnalbe);
		edtEmail.setFocusable(isEnalbe);
		edtEmail.setFocusableInTouchMode(isEnalbe);

		edtAddress.setEnabled(isEnalbe);
		edtAddress.setFocusable(isEnalbe);
		edtAddress.setFocusableInTouchMode(isEnalbe);

		btnDate.setEnabled(isEnalbe);

		edtComment.setEnabled(isEnalbe);
		edtComment.setFocusable(isEnalbe);
		edtComment.setFocusableInTouchMode(isEnalbe);

		if (isEnalbe) {

			btnMore.setVisibility(View.VISIBLE);

		} else {

			btnMore.setVisibility(View.GONE);
		}

	}

	private void removeGroupItem(String name) {
		for (int i = 0; i < groupItem.size(); i++) {
			if (groupItem.get(i).equals(name)) {
				groupItem.remove(i);
			}
		}
	}

	private void getId() {
		txtTitle = (TextView) findViewById(R.id.txtTitle);

		ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
		imgBack = (ImageView) findViewById(R.id.imgBack);
		imgEdit = (ImageView) findViewById(R.id.imgEdit);

		btnGroup = (Button) findViewById(R.id.btnGroup);
		btnDate = (Button) findViewById(R.id.btnDate);
		btnMore = (Button) findViewById(R.id.btnMore);
		btnSure = (Button) findViewById(R.id.btnSure);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		imgbtn = (ImageButton) findViewById(R.id.imgbtn);
		edtName = (EditText) findViewById(R.id.edtName);
		edtPhone = (EditText) findViewById(R.id.edtPhone);
		edtHomePhone = (EditText) findViewById(R.id.edtHomePhone);
		edtOtherPhone = (EditText) findViewById(R.id.edtOtherPhone);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtAddress = (EditText) findViewById(R.id.edtAddress);
		edtComment = (EditText) findViewById(R.id.edtComment);

		position_0 = (RelativeLayout) findViewById(R.id.postion_0);
		position_1 = (RelativeLayout) findViewById(R.id.postion_1);
		position_2 = (RelativeLayout) findViewById(R.id.postion_2);
		position_3 = (RelativeLayout) findViewById(R.id.postion_3);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (resultCode == 300) {
				groupName = data.getStringExtra("groupName");
				groupId = data.getIntExtra("groupId", -1);
				isSelectGroup = true;
			}
			if (resultCode == 200) {
				groupItem = (List<String>) data
						.getSerializableExtra("groupItem");
				itemPosition = data.getStringExtra("itemPosition");
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		if (groupName != null) {
			Log.i("testGroupService", groupName);
			btnGroup.setText(groupName);
		}

		if (itemPosition != null) {

			if (itemPosition.equals("��ͥ�绰")) {

				position_0.setVisibility(View.VISIBLE);
			} else if (itemPosition.equals("�����绰")) {

				position_1.setVisibility(View.VISIBLE);

			} else if (itemPosition.equals("Email")) {

				position_2.setVisibility(View.VISIBLE);

			} else if (itemPosition.equals("��ַ")) {

				position_3.setVisibility(View.VISIBLE);

			} else if (itemPosition.equals("��������")) {
				btnDate.setVisibility(View.VISIBLE);

			} else if (itemPosition.equals("��ӱ�ע")) {

				edtComment.setVisibility(View.VISIBLE);

			}
		}

		super.onResume();
	}

	// Gallery��������,�ڲ���
	class ImageAdapter extends BaseAdapter {

		private Context context;

		public ImageAdapter(Context context) {
			this.context = context;
		}

		public int getCount() {
			return Integer.MAX_VALUE;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// �õ�ѡ�е�ͼƬ���ŵ�view��ͼ��
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imgv = new ImageView(context);
			imgv.setImageResource(images[position % images.length]);
			imgv.setAdjustViewBounds(true);
			imgv.setLayoutParams(new Gallery.LayoutParams(80, 80));
			imgv.setPadding(15, 10, 15, 10);
			return imgv;
		}
	}

	public View makeView() {
		ImageView view = new ImageView(this);
		view.setBackgroundColor(Color.WHITE);
		view.setScaleType(ScaleType.FIT_CENTER);
		view.setLayoutParams(new ImageSwitcher.LayoutParams(90, 90));
		return view;
	}

}
