package cn.android.contact.test;

import java.io.ByteArrayOutputStream;

import cn.android.contact.daompl.ContactService;
import cn.android.contact.model.Contact;
import cn.android.contact.tools.ImageTools;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;
import android.util.Log;

public class TestContactService extends AndroidTestCase{
	
	private static final String TAG = "testContactService";
	
	
	public void testinsert(){
	ContactService cs = new ContactService(getContext());
	//Contact contact = new Contact(null,"11",1,1,1,1,"123@123","11","1-1","ok","false");
	//Contact contact2 = new Contact(null,"11",2,1,1,1,"123@123","11","1-1","ok","false");
	//Contact contact3 = new Contact(null,"11",3,1,1,1,"123@123","11","1-1","ok","false");
	
	
	 Bitmap  bitmap = BitmapFactory.decodeFile("/mnt/sdcard/p3.png");
	 
	 ByteArrayOutputStream  baos=new ByteArrayOutputStream();
	 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	 byte[] image=baos.toByteArray();
    // byte[] image = ImageTools.getByteFromBitmap(bitmap);
     
   //  contact.setImage(image);
   //  contact2.setImage(image);
   //  contact3.setImage(image);
	//cs.insert(contact);
	//cs.insert(contact2);
	//cs.insert(contact3);
	}
	
	
//	public void testdelete(){
//		ContactService cs = new ContactService(getContext());
//		cs.delete(1);
//		
//	}
	
	public void testupdate(){
		ContactService cs = new ContactService(getContext());
		//Contact contact = new Contact(2,null,"222",1,1,1,1,"22123@123","11","1-1","ok","false");
		//cs.update(contact);
		
	}
	public void testinitIsMark(){
		ContactService cs = new ContactService(getContext());
	//	cs.initIsMark("true");
		
	}
	public void testgetAll(){
		ContactService cs = new ContactService(getContext());
		cs.getAll();
		Log.i(TAG, cs.getAll().size()+"");
		
	}
	
	public void testgetProductByCondition(){
		ContactService cs = new ContactService(getContext());
		cs.getProductByCondition("groupId"+"="+1);
		Log.i(TAG, cs.getProductByCondition("groupId"+"="+3).size()+"");
		
	}
	
	public void testgetById(){
		ContactService cs = new ContactService(getContext());
		cs.getById(2);
		Log.i(TAG, cs.getById(2).toString());
		
	}
	
	

}
