package cn.android.contact.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;

public class Contact implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5034871569203923761L;
	private int id;
	private byte[] image;
	private String name;
	private int groupId;
	private String phone;
	private String homePhone;
	private String otherPhone;
	private String e_mail;
	private String address;
	private String birthday;
	private String comment;
	private String isMark;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getOtherPhone() {
		return otherPhone;
	}
	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getIsMark() {
		return isMark;
	}
	public void setIsMark(String isMark) {
		this.isMark = isMark;
	}
	public Contact(int id, byte[] image, String name, int groupId, String phone,
			String homePhone, String otherPhone, String e_mail, String address,
			String birthday, String comment, String isMark) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.groupId = groupId;
		this.phone = phone;
		this.homePhone = homePhone;
		this.otherPhone = otherPhone;
		this.e_mail = e_mail;
		this.address = address;
		this.birthday = birthday;
		this.comment = comment;
		this.isMark = isMark;
	}
	public Contact( byte[] image, String name, int groupId, String phone,
			String homePhone, String otherPhone, String e_mail, String address,
			String birthday, String comment, String isMark) {
		super();
		this.image = image;
		this.name = name;
		this.groupId = groupId;
		this.phone = phone;
		this.homePhone = homePhone;
		this.otherPhone = otherPhone;
		this.e_mail = e_mail;
		this.address = address;
		this.birthday = birthday;
		this.comment = comment;
		this.isMark = isMark;
	}
	
	public Contact() {
		
	}
	@Override
	public String toString() {
		return "Contact [id=" + id + ", image=" + Arrays.toString(image)
				+ ", name=" + name + ", groupId=" + groupId + ", phone="
				+ phone + ", homePhone=" + homePhone + ", otherPhone="
				+ otherPhone + ", e_mail=" + e_mail + ", address=" + address
				+ ", birthday=" + birthday + ", comment=" + comment
				+ ", isMark=" + isMark + "]";
	}
	





	

}
