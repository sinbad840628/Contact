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

	/** ��ȡ��Phone���ֶ� **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** ��ϵ����ʾ���� **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** �绰���� **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** ͷ��ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;

	/** ��ϵ�˵�ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	/** ��ϵ������ **/
	private static ArrayList<String> mContactsName = new ArrayList<String>();

	/** ��ϵ�˵绰 **/
	private static ArrayList<String> mContactsNumber = new ArrayList<String>();

	/** ��ϵ��ͷ�� **/
	//private static ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		//setContentView(R.layout.main);
		
		int requestCode = getIntent().getIntExtra("requestCode", 0);
		
		if(requestCode==8){//���ֻ���
			Log.i("group", "11");
			//���ֻ��Դ���ͨѶ¼�ж�ȡ���ݲ���ӵ���Ӧ�ļ���
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
			
		}else if(requestCode==9){//��SIM����
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
	

	//���ⲿSIM���л�ȡ��ϵ����Ϣ
	public List<Object> getContactsFromSIM() {

		List<Object> list = new ArrayList<Object>();
		list.add(mContactsName);
		list.add(mContactsNumber);
		Log.i("getContactsFromSIM", "mContactsName.size:" + mContactsName.size());
		return list;

	}

	//���ֻ��л�ȡ��ϵ����Ϣ
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

		// ��ȡ�ֻ���ϵ��
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);


		if (phoneCursor != null) {
		    while (phoneCursor.moveToNext()) {

			//�õ��ֻ�����
			String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
			//���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
			if (TextUtils.isEmpty(phoneNumber))
			    continue;
			
			//�õ���ϵ������
			String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
			
			//�õ���ϵ��ID
			Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

			//�õ���ϵ��ͷ��ID
			Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
			
			//�õ���ϵ��ͷ��Bitamp
			Bitmap contactPhoto = null;

			//photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�
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
	
	
	 /**�õ��ֻ�SIM����ϵ������Ϣ**/
    private void getSIMContacts() {
    	mContactsName.clear();
		mContactsNumber.clear();
		ContentResolver resolver = mContext.getContentResolver();
		// ��ȡSims����ϵ��
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
			null);
		if (phoneCursor != null) {
		    while (phoneCursor.moveToNext()) {
	
			// �õ��ֻ�����
			String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
			// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
			if (TextUtils.isEmpty(phoneNumber))
			    continue;
			// �õ���ϵ������
			String contactName = phoneCursor
				.getString(PHONES_DISPLAY_NAME_INDEX);
	
			//Sim����û����ϵ��ͷ��
			
			mContactsName.add(contactName);
			mContactsNumber.add(phoneNumber);
		    }
	
		    phoneCursor.close();
		}
    }

}
