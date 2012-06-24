package cn.android.contact.test;

import java.util.List;

import cn.android.contact.daompl.GroupService;
import cn.android.contact.model.Group;
import android.test.AndroidTestCase;
import android.util.Log;

public class TextGroupService extends AndroidTestCase {
	
	private static final String TAG = "testGroupService";
	public void testinsert(){
		GroupService gs = new GroupService(getContext());
		Group group = new Group("好友");
		Group group1 = new Group("家人");
		gs.insert(group);
		gs.insert(group1);
	}
	
	public void testgetCount(){
		GroupService gs = new GroupService(getContext());
		gs.getCount();
		Log.i(TAG, gs.getCount()+"");
		
	}
	
	public void testgetAll(){
		GroupService gs = new GroupService(getContext());
		List<Group> list = gs.getAll();
		Log.i(TAG, list.size()+"");
		
	}
	
//	public void testdelete(){
//		GroupService gs = new GroupService(getContext());
//		gs.delete(1);
//		
//	}
	
	public void testupdate(){
		GroupService gs = new GroupService(getContext());
		Group group = new Group(2,"人人");
		gs.update(group);
		
	}
	
	public void testgetById(){
		GroupService gs = new GroupService(getContext());
		gs.getById(1);
		Log.i(TAG, gs.getById(1).toString());
		
	}
	
	

}
