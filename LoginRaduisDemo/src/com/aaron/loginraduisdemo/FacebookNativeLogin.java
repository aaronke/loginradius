package com.aaron.loginraduisdemo;



import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FacebookNativeLogin extends Activity{
	
	private CallbackManager callbackManager;
	private TextView textView;
	private LoginButton loginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.facebook_native_login);
		
		// generate the key hashes for facebook application
		/*try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.aaron.loginraduisdemo", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }*/
		callbackManager=CallbackManager.Factory.create();
		textView=(TextView)findViewById(R.id.info);
		loginButton=(LoginButton)findViewById(R.id.login_button);
		
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			
			@Override
			public void onSuccess(LoginResult result) {
				// TODO Auto-generated method stub
				textView.setText("User ID:"+result.getAccessToken().getUserId());
				
			}
			
			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				textView.setText("login error:"+error.getMessage());
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
}
