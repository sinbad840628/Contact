package cn.android.contact.ui;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.android.contact.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;

public class GetContactActivity extends Activity {

	Context mContext = null;

	/** 获取库Phone表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** 头像ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;

	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	/** 联系人名称 **/
	private static ArrayList<String> mContactsName = new ArrayList<String>();

	/** 联系人电话 **/
	private static ArrayList<String> mContactsNumber = new ArrayList<String>();

	/** 联系人头像 **/
	//private static ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		//setContentView(R.layout.main);
		
		int requestCode = getIntent().getIntExtra("requestCode", 0);
		
		if(requestCode==8){//从手机得
			Log.i("group", "11");
			//从手机自带的通讯录中读取数据并添加到相应的集合
			getPhoneContacts();
			Intent intent = new Intent(mContext,ToolGriewActivity.class);
			List<Object> list =getContactsFromPhone();
			Log.i("Item", "list.size"+list.size());
			
			Bundle bundle = new Bundle();
			bundle.putSerializable("contacts", (Serializable) list);
			//bundle.putSerializable("photo", (Serializable)mContactsPhonto);
			//bundle.putParcelable("contacts", (Parcelable)list);
			intent.putExtras(bundle);
			
			//intent.putParcelableArrayListExtra("contacts", Const.mContactsPhonto);
			this.setResult(500, intent);
			
			finish();
			
		}else if(requestCode==9){//从SIM卡得
			getSIMContacts();
			Intent intent = new Intent(mContext,ToolGriewActivity.class);
			List<Object> list =getContactsFromSIM();
			Bundle bundle = new Bundle();
			bundle.putSerializable("contacts", (Serializable) list);
			intent.putExtras(bundle);
			this.setResult(600, intent);
			
			finish();
			
		}
		
	
	}
	

	//从外部SIM卡中获取联系人信息
	public List<Object> getContactsFromSIM() {

		List<Object> list = new ArrayList<Object>();
		list.add(mContactsName);
		list.add(mContactsNumber);
		Log.i("getContactsFromSIM", "mContactsName.size:" + mContactsName.size());
		return list;

	}

	//从手机中获取联系人信息
	public List<Object> getContactsFromPhone() {

		List<Object> list = new ArrayList<Object>();
		list.add(mContactsName);
		list.add(mContactsNumber);
		//list.add(mContactsPhonto);
		Log.i("getContactsFromPhone", "mContactsName.size:" + mContactsName.size());
		return list;

	}
	
	private void getPhoneContacts() {
		
		mContactsName.clear();
		mContactsNumber.clear();
		Const.mContactsPhonto.clear();
		ContentResolver resolver = mContext.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);


		if (phoneCursor != null) {
		    while (phoneCursor.moveToNext()) {

			//得到手机号码
			String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
			//当手机号码为空的或者为空字段 跳过当前循环
			if (TextUtils.isEmpty(phoneNumber))
			    continue;
			
			//得到联系人名称
			String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
			
			//得到联系人ID
			Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

			//得到联系人头像ID
			Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
			
			//得到联系人头像Bitamp
			Bitmap contactPhoto = null;

			//photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
			if(photoid > 0 ) {
			    Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
			    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
			    contactPhoto = BitmapFactory.decodeStream(input);
			}else {
			    contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.bg_photo_default);
			}
			
			mContactsName.add(contactName);
			mContactsNumber.add(phoneNumber);
			Const.mContactsPhonto.add(contactPhoto);
		    }

		    phoneCursor.close();
		}
		
	}
	
	
	 /**得到手机SIM卡联系人人信息**/
    private void getSIMContacts() {
    	mContactsName.clear();
		mContactsNumber.clear();
		ContentResolver resolver = mContext.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
			null);
		if (phoneCursor != null) {
		    while (phoneCursor.moveToNext()) {
	
			// 得到手机号码
			String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
			// 当手机号码为空的或者为空字段 跳过当前循环
			if (TextUtils.isEmpty(phoneNumber))
			    continue;
			// 得到联系人名称
			String contactName = phoneCursor
				.getString(PHONES_DISPLAY_NAME_INDEX);
	
			//Sim卡中没有联系人头像
			
			mContactsName.add(contactName);
			mContactsNumber.add(phoneNumber);
		    }
	
		    phoneCursor.close();
		}
    }

}
