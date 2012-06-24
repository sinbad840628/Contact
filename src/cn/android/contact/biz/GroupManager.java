package cn.android.contact.biz;

import java.util.List;

import android.content.Context;
import android.util.Log;
import cn.android.contact.dao.IGroup;
import cn.android.contact.daompl.GroupService;
import cn.android.contact.dbconstant.DB.TABLES.GROUP;
import cn.android.contact.model.Group;

public class GroupManager {
	private IGroup groupService = null;
	public GroupManager(Context context){
		groupService = new GroupService(context);
		
	}
	
	
	public int getIdByName(String name){
		Group group = groupService.getByName(name);
		Log.i("child", group.toString());
		return group.getGroupId();
		
	}
	/**
	 * ������з���
	 * @return
	 */
	
	public List<Group> getAllGroup(){
		String sql = String.format(GROUP.SQL.SELECT, "1=1");
		return groupService.select(sql);
	}
	
	/**
	 * ��÷�������
	 * @return
	 */
	
	public int getGroupCount(){
		return groupService.getCount();
	}
	
	/**
	 * ͨ��ID��÷���
	 * @param id
	 * @return
	 */
	public Group getGroupByID(int id){
		return groupService.getById(id);
		
	}
	
	/**
	 * ��ӷ���
	 */
	public void insertGroupByName(String groupName){
		Group group = new Group(groupName);
		groupService.insert(group);
		
	}
	/**
	 * ������
	 * @param group
	 */
	public void updateGroup(Group group){
		
		groupService.update(group);
		
	}
	/**
	 * ɾ������
	 * @param id
	 */
	public void deleteGroupByID(int id){
		groupService.delete(id);
		
	}

}
