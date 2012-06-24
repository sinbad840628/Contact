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
	 * 对象声明 items：存放显示的名称 paths：存放文件路径 rootPath：起始目录
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

	private View imageChooseView;// 头像选择视图
	private AlertDialog imageChooseDialog;// 头像选择对话框
	private Gallery gallery;// 头像的Gallery
	private ImageSwitcher imageSwitcher;// 头像的ImageSwitcher
	int currentImagePosition;// 用于记录当前选中图像在图像数组中的位置
	int previousImagePosition;// 用于记录上一次图片的位置
	boolean imageChanged = false;// 判断头像有没有变化
	// 头像数组
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
		groupItem.add("家庭电话");
		groupItem.add("其他电话");
		groupItem.add("Email");
		groupItem.add("地址");
		groupItem.add("出生日期");
		groupItem.add("添加备注");

		getId();
		/**
		 * requestCode = 1 表示是点击右上角的新建联系人跳转过来的 
		 * requestCode = 0表示是点击组别右键菜单的新建联系人跳转过来的 
		 * requestCode = 11表示是点击编辑跳转过来的
		 * requestCode =20表示是从所有联系人列表的编辑跳转过来的 
		 * requestCode = 50 表示是从所有联系人列表的添加跳转过来的
		 * requestCode = 60 表示从所有联系人列表的查看详情跳转过来的
		 * requestCode = 6 表示从主界面的查看详情跳转过来的
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

			txtTitle.setText("编辑联系人");
			LinearLayout ll_left = (LinearLayout) findViewById(R.id.ll_left);
			LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);

			if (requestCode == 60 || requestCode == 6) {

				ll_left.setVisibility(View.VISIBLE);
				imgBack.setVisibility(View.VISIBLE);

				ll_right.setVisibility(View.VISIBLE);
				imgEdit.setVisibility(View.VISIBLE);

				ll_btn.setVisibility(View.GONE);

				isEnalbe(false);
				txtTitle.setText("联系人详情");

			}

			Contact contact = contactManager.getContactById(getIntent()
					.getExtras().getInt("id"));
			// 头像
			
			imgbtn.setImageBitmap(ImageTools.getBitmapFromByte(contact
					.getImage()));
			edtName.setText(contact.getName());
			//电话
			edtPhone.setText(contact.getPhone().toString() );
			String groupName = (new GroupManager(context)).getGroupByID(contact.getGroupId()).getGroupName();
			btnGroup.setText(groupName);
			
			if(!contact.getHomePhone().equals("")){
				edtHomePhone.setText(contact.getHomePhone().toString() );
				position_0.setVisibility(View.VISIBLE);
				removeGroupItem("家庭电话");
				
			}
				
			
			if(!contact.getOtherPhone().equals("")){
				edtOtherPhone.setText(contact.getOtherPhone().toString() );
				position_1.setVisibility(View.VISIBLE);
				removeGroupItem("其他电话");
				
			}
				
		
			if (!contact.getE_mail().equals("")) {

				edtEmail.setText(contact.getE_mail());
				position_2.setVisibility(View.VISIBLE);
				removeGroupItem("Email");
			}
			if (!contact.getAddress().equals("")) {
				edtAddress.setText(contact.getAddress());
				position_3.setVisibility(View.VISIBLE);
				removeGroupItem("地址");
			}
			if (!contact.getBirthday().equals("")) {
				btnDate.setTag(contact.getBirthday());
				btnDate.setVisibility(View.VISIBLE);
				removeGroupItem("出生日期");
			}
			if (!contact.getComment().equals("")) {
				edtComment.setText(contact.getComment());
				edtComment.setVisibility(View.VISIBLE);
				removeGroupItem("添加备注");

			}
		}

		// 头像选择监听
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
						Toasttool.MyToast(context, "拍照");
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						startActivityForResult(intent,Activity.DEFAULT_KEYS_DIALER);
						popupWindow.dismiss();
					}
				});

				txtSystem.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Toasttool.MyToast(context, "系统");
						initImageChooseDialog();// 初始化imageChooseDialog
						imageChooseDialog.show();
						popupWindow.dismiss();

					}

				});
				txtFile.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Toasttool.MyToast(context, "文件");
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
		// 返回键监听
		imgBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();

			}
		});
		// 编辑键监听
		imgEdit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				ll_btn.setVisibility(View.VISIBLE);
				isEnalbe(true);
				txtTitle.setText("编辑联系人");

			}
		});

		btnSure.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// 头像
				String name = edtName.getText().toString();
				int mGroupId = 1;
				if (isSelectGroup) {// 手动选择了组别
					mGroupId = groupId;
					Log.i("hubin", "1mGroupId:" + mGroupId);
				} else {// 没有手动选择组别
					int requestCode = getIntent().getExtras().getInt(
							"requestCode");
					if (requestCode == 11 || requestCode == 60
							|| requestCode == 6) {// 点击编辑跳转过来的
						Contact contact = contactManager
								.getContactById(getIntent().getExtras().getInt(
										"id"));
						mGroupId = contact.getGroupId();

					} else if (requestCode == 1 || requestCode == 50) {// 点击右上角添加跳转过来的
						mGroupId = 1;

					} else {// 点击某个组别跳转过来的
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
					Toasttool.MyToast(context, "名字不能为空！");

				} else {
					Contact contact = contactManager.getContactById(getIntent()
							.getExtras().getInt("id"));

					// 修改头像的，
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
							|| requestCode == 60 || requestCode == 6) {// 修改

						int id = getIntent().getExtras().getInt("id");
						Contact contact1 = new Contact(id, photo, name,
								mGroupId, phone, homePhone, otherPhone, e_mail,
								address, mBirthday, comment, "false");
						// contact.setImage(photo);
						contactManager.modifyContact(contact1);

					} else {// 添加
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

		// 选择更多添加 属性
		btnMore.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Log.i("child", "1111111121" + groupItem);

				if (groupItem.size() == 0) {
					Toasttool.MyToast(context, "没有可添加的属性");
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

		// 选择组别
		btnGroup.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(context, SelectGroupActivity.class);
				intent.putExtra("requestCode", 3);
				intent.putExtra("groupName", btnGroup.getText().toString().trim());
				startActivityForResult(intent, 0);

			}
		});
		// 选择生日
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
		dialog = new AlertDialog.Builder(context).setTitle("选择文件")
				.setIcon(R.drawable.flag).setView(view)
				.setNegativeButton("退 出", null).create();
		dialog.show();
		/* 初始化mPath，用以显示目前路径 */
		mPath = (TextView) view.findViewById(R.id.mPath);
		getFileDir(rootPath);
	}

	/* 取得文件架构的method */
	private void getFileDir(String filePath) {
		/* 设定目前所存路径 */
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();

		File f = new File(filePath);
		File[] files = f.listFiles();

		if (!filePath.equals(rootPath)) {
			/* 第一笔设定为[并到根目录] */
			items.add("b1");
			paths.add(rootPath);
			/* 第二笔设定为[并勺层] */
			items.add("b2");
			paths.add(f.getParent());
		}
		/* 将所有文件放入ArrayList中 */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}

		/* 使用自定义的MyAdapter来将数据传入ListActivity */

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
							/* 如果是文件夹就运行getFileDir() */
							getFileDir(paths.get(position));
						} else {
							/* 如果是文件调用fileHandle() */
							fileHandle(file);
						}
					} else {
						/* 弹出AlertDialog显示权限不足 */
						new AlertDialog.Builder(context)
								.setTitle("Message")
								.setMessage("权限不足!")
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
		if (imageChooseView == null) { // 为gallery加图片，做出ImageChooseView的视图
			LayoutInflater li = LayoutInflater.from(context);
			imageChooseView = li.inflate(R.layout.imagechoose, null);

			gallery = (Gallery) imageChooseView.findViewById(R.id.imggallery);// 得到视图中gallery的位置
			gallery.setAdapter(new ImageAdapter(context));// 为Gallery装载图片
			gallery.setSelection(images.length / 2);// 初始显示中间位置的头像

			imageSwitcher = (ImageSwitcher) imageChooseView
					.findViewById(R.id.imgswitch);
			imageSwitcher.setFactory(AddNewContactActivity.this);
			imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,// 加载动画效果（效果可以自己写）
					android.R.anim.fade_in));
			imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,// 卸载图片的动画效果
					android.R.anim.fade_out));

			// 为头像gallery设置监听，记录选中的头像
			gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					currentImagePosition = arg2;// 当前的头像位置为选中的位置
					imageSwitcher
							.setImageResource(images[arg2 % images.length]);// 为ImageSwitcher设置图像
				}

				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 点击头像，弹出一个头像选择的对话框
		builder.setTitle("请选择图像");
		builder.setView(imageChooseView);// 加载供选择的头像选择视图
		builder.setPositiveButton("确定",// 点击确定，则把当前的图片赋值给头像按钮显示
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						imageChanged = true;
						previousImagePosition = currentImagePosition;
						imgbtn.setImageResource(images[currentImagePosition
								% images.length]);
					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				currentImagePosition = previousImagePosition;

			}
		});
		imageChooseDialog = builder.create();

	}

	/* 处理文件的method */
	private void fileHandle(final File file) {
		/* 按下文件时的OnClickListener */
		OnClickListener listener1 = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					/* 选择的item为打开文件预览 */
					previewFile(file);
				} else if (which == 1)// 选择文件
				{
					openFile(file);
				} else // 删除文件
				{
					/* 选择的item为删除文件 */
					new AlertDialog.Builder(context)
							.setTitle("注意!")
							.setMessage("确定要删除文件吗?")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											/* 删除文件 */
											file.delete();
											getFileDir(file.getParent());
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).show();
				}
			}

		};

		/* 选择几个文件时，弹出要处理文件的ListDialog */
		String[] menu = { "预览图片", "选择图片", "删除文件" };
		new AlertDialog.Builder(context).setTitle("你要做甚么?")
				.setItems(menu, listener1)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	/* 手机预览文件的method */
	private void previewFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */
		String type = getMIMEType(f);
		/* 设定intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		startActivity(intent);
	}

	/* 判断文件MimeType的method */
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 按扩展名的类型决定MimeType */
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
		/* 如果无法直接打开，就弹出软件列表给用户选择 */
		type += "/*";
		return type;
	}

	/* 手机选择文件的method */
	private void openFile(File f) {
		Bitmap bm = ImageTools.getBitemapFromFile(f);
		if (bm != null) {
			imgbtn.setImageBitmap(bm);
			mPhoto = bm;
			isFromFile = true;
		} else {
			Toasttool.MyToast(context, "加载图片文件失败!");
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

			if (itemPosition.equals("家庭电话")) {

				position_0.setVisibility(View.VISIBLE);
			} else if (itemPosition.equals("其他电话")) {

				position_1.setVisibility(View.VISIBLE);

			} else if (itemPosition.equals("Email")) {

				position_2.setVisibility(View.VISIBLE);

			} else if (itemPosition.equals("地址")) {

				position_3.setVisibility(View.VISIBLE);

			} else if (itemPosition.equals("出生日期")) {
				btnDate.setVisibility(View.VISIBLE);

			} else if (itemPosition.equals("添加备注")) {

				edtComment.setVisibility(View.VISIBLE);

			}
		}

		super.onResume();
	}

	// Gallery的适配器,内部类
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

		// 得到选中的图片，放到view视图中
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
