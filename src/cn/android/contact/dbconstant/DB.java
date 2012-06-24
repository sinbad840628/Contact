package cn.android.contact.dbconstant;

public interface DB {
	public static final String DATABASE_NAME = "MyContact";
	public static final int DATABASE_VERSION = 1;

	public interface TABLES {
		public interface GROUP {
			public static final String TABLENAME = "MyGroup";

			public interface FIELDS {
				public static final String GROUPID = "groupId";
				public static final String GROUPNAME = "groupName";

			}

			public interface SQL {
				public static final String CREAT = "create table if not exists MyGroup (groupId integer PRIMARY KEY AUTOINCREMENT NOT NULL, groupName varchar(20));";
				public static final String DROP = "DROP TABLE IF EXISTS MyGroup";
				public static final String INSERT = "insert into MyGroup (groupName)   values ('%s');";
				public static final String DELETE_BY_ID = "delete from MyGroup where groupId = %s;";
				public static final String UPDATE = " update MyGroup set groupName = '%s' where groupId = %s;";
				public static final String SELECT = "select * from MyGroup where %s";
				public static final String COUNT = "select count(*) from MyGroup";

			}

		}

		public interface CONTACT {
			public static final String TABLENAME = "MyContact";

			public interface FIELDS {
				public static final String ID = "id";
				public static final String IMAGE = "image";
				public static final String NAME = "name";
				public static final String GROUPID = "groupId";
				public static final String PHONE = "phone";
				public static final String HOMEPHONE = "homePhone";
				public static final String OTHERPHONE = "otherPhone";
				public static final String EMAIL = "e_mail";
				public static final String ADDRESS = "address";
				public static final String BIRTHDAY = "birthday";
				public static final String COMMENT = "comment";
				public static final String ISMARK = "isMark";

			}

			public interface SQL {
				public static final String CREAT = " create table if not exists MyContact (id integer PRIMARY KEY AUTOINCREMENT NOT NULL,image blob, name varchar(20),"
						+ "  groupId integer ,phone integer , homePhone integer ,otherPhone integer ,e_mail varchar(30),address varchar(200),birthday varchar(50),comment varchar(200),isMark varchar(20)  );";
				public static final String DROP = "DROP TABLE IF EXISTS MyContact";
				public final static String INIT_ISMARK ="update MyContact set isMark = '%s' where %s";
				public final static String COUNT =  "select count(*) from MyContact where %s";
				public static final String INSERT = "insert into MyContact ( image ,name,   groupId ,phone  , homePhone  ,otherPhone  ,e_mail ,address ,birthday ,comment,isMark )  "
					+"values (%s,'%s',%s,%s,%s,%s,'%s','%s','%s','%s','%s');";
				public static final String DELETE_BY_ID = "delete from MyContact where  %s;";
				public static final String UPDATE = " update MyContact set image = %s ,name = '%s',groupId =%s ,phone= %s , homePhone = %s, otherPhone = %s,e_mail = '%s',address = '%s',birthday = '%s',comment = '%s',isMark = '%s'   where id = %s;";
				public static final String CHANGE_GROUP="update MyContact set groupId = %s where %s";
				public static final String SELECT = "select * from MyContact where %s";

			}

		}
	}

}
