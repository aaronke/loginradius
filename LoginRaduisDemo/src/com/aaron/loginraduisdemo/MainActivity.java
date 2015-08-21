package com.aaron.loginraduisdemo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity implements OnClickListener{
	private Button facebook_native_login_button,listview_login_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		facebook_native_login_button=(Button)findViewById(R.id.facebook_native_login_button);
		facebook_native_login_button.setOnClickListener(this);
		
		listview_login_button=(Button)findViewById(R.id.listview_login_button);
		listview_login_button.setOnClickListener(this);
		
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.facebook_native_login_button:
			Intent facebook_native_loginIntent=new Intent(MainActivity.this, FacebookNativeLogin.class);
			startActivity(facebook_native_loginIntent);
			break;
		case R.id.listview_login_button:
			Intent listview_loginIntent=new Intent(MainActivity.this, LoginRadiusActivity.class);
			startActivity(listview_loginIntent);
			break;
		default:
			break;
		}
	}
	
	
	/*private void populateListView(AppInformation appInformation){
		
		
		lrLoginManager.ImageUrl="https://cdn.loginradius.com/mobile/android/images/";
		lrLoginManager.ImgVersion="v1";
		listView=(ListView)findViewById(R.id.listview);
		LRinterfaceimpl lRinterfaceimpl=new LRinterfaceimpl();
		
		lRinterfaceimpl.attachListView(appInformation, listView, MainActivity.this, new AsyncHandler<lrAccessToken>() {

			@Override
			public void onSuccess(lrAccessToken data) {
				// TODO Auto-generated method stub
				getProfileInfo(data);
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	
	private void getProfileInfo(lrAccessToken token){
		
		UserProfileAPI userProfileAPI=new UserProfileAPI();
		userProfileAPI.getResponse(token, new AsyncHandler<LoginRadiusUltimateUserProfile>() {

			@Override
			public void onSuccess(LoginRadiusUltimateUserProfile data) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Hello,"+data.FirstName, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	*/
}
