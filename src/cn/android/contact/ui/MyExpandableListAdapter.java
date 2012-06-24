package cn.android.contact.ui;

import java.util.ArrayList;
import java.util.List;

import cn.android.contact.R;
import cn.android.contact.biz.ContactManager;
import cn.android.contact.biz.GroupManager;
import cn.android.contact.model.Contact;
import cn.android.contact.tools.ImageTools;
import cn.android.contact.tools.Toasttool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	public PopupWindow popupWindow;
	private Context mcontext = null;
	private boolean[] isOpen = null;
	private LayoutInflater mInflater;
	private ContactManager contactManager;
	private GroupManager groupManager;
	private boolean check = true;
	private View popView;
	private int id;
	private List<Integer> contactCountByGroup;
	private List<String> groupItem = new ArrayList<String>();
	private List<List<Contact>> contacts = new ArrayList<List<Contact>>();

	

	public MyExpandableListAdapter(List<Integer> contactCountByGroup,List<String> groupItem,
			List<List<Contact>> contacts, boolean[] isOpen, Context context) {
		this.contactCountByGroup = contactCountByGroup;
		this.isOpen = isOpen;
		this.mcontext = context;
		this.groupItem = groupItem;
		this.contacts = contacts;
		this.mInflater = LayoutInflater.from(mcontext);
		this.contactManager = new ContactManager(context);
		this.groupManager = new GroupManager(context);

	}

	public MyExpandableListAdapter() {

	}

	public void refresh() {
		this.notifyDataSetChanged();
	}

	public Object getChild(int groupPosition, int childPosition) {
		System.out.println(groupPosition);
		return contacts.get(groupPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return contacts.get(groupPosition).size();
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.child, null);
		List<Contact> contacts = (List<Contact>) getChild(groupPosition,
				childPosition);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgContact);
		TextView childName = (TextView) convertView
				.findViewById(R.id.childName);
		TextView childPhone = (TextView) convertView
				.findViewById(R.id.childPhone);
		imgPhoto.setImageBitmap(ImageTools.getBitmapFromByte(contacts.get(childPosition).getImage()));
		
		childName.setTextColor(Color.BLACK);
		childName.setText(contacts.get(childPosition).getName());

		childPhone.setTextColor(Color.BLACK);
		
			childPhone.setText(contacts.get(childPosition).getPhone() );
		
		
		

		ImageView imgChk = (ImageView) convertView.findViewById(R.id.imgChk);
		id = contacts.get(childPosition).getId();
		if (contactManager.getContactById(id).getIsMark().equals("true"))
			imgChk.setVisibility(View.VISIBLE);

		ImageView popImg = (ImageView) convertView.findViewById(R.id.popImg);
		popImg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					if (popupWindow == null) {
						popView = mInflater.inflate(R.layout.popupwindow_view,
								null, false);
						popupWindow = new PopupWindow(popView,
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT, true);

						popupWindow.setBackgroundDrawable(new BitmapDrawable());

					}
					Toasttool.MyToast(mcontext, "pop");
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

							((ContactActivity) mcontext)
									.makeCall(contactManager.getContactById(id).getPhone());

							popupWindow.dismiss();
						}

					});
					imgMsg.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							Toasttool.MyToast(mcontext, "Msg");
							((ContactActivity) mcontext).sendMsg(contactManager
									.getContactById(id).getPhone());

							popupWindow.dismiss();

						}
					});
					imgEmail.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							Toasttool.MyToast(mcontext, "Email");
							((ContactActivity) mcontext)
									.sendEmail(contactManager
											.getContactById(id).getE_mail()
											.toString());

							popupWindow.dismiss();

						}
					});

//				} else {
//					Toasttool.MyToast(mcontext, "pop2");
//					check = true;
//					popupWindow.dismiss();
//
//				}
			}
		});

		return convertView;
	}

	public Object getGroup(int groupPosition) {
		return groupItem.get(groupPosition);
	}

	public int getGroupCount() {
		return groupItem.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.group, null);
		TextView textView = (TextView) convertView.findViewById(R.id.groupto);
		TextView textCount = (TextView) convertView.findViewById(R.id.txtCount);
        
	
		textCount.setText("("+contactCountByGroup.get(groupPosition).toString()+")");

		textView.setTextColor(Color.BLACK);
		textView.setText(getGroup(groupPosition).toString());
		Log.i("hubin", "groupPosition" + groupPosition);
		if (isOpen[groupPosition]) {
			Drawable leftDrawable = mcontext.getResources().getDrawable(
					R.drawable.expend);
			leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
					leftDrawable.getMinimumHeight());
			textView.setCompoundDrawables(leftDrawable, null, null, null);
		} else {
			Drawable leftDrawable = mcontext.getResources().getDrawable(
					R.drawable.unexpend);
			leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
					leftDrawable.getMinimumHeight());
			textView.setCompoundDrawables(leftDrawable, null, null, null);

		}

		return convertView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

}
