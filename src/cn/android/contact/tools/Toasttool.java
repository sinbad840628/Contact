package cn.android.contact.tools;

import android.content.Context;
import android.widget.Toast;

public class Toasttool {
	public static void MyToast(Context context ,String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		
	}

}
