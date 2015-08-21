package com.aaron.loginraduisdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.loginradius.sdk.api.AlbumAPI;
import com.loginradius.sdk.api.AudioAPI;
import com.loginradius.sdk.api.CheckInAPI;
import com.loginradius.sdk.api.CompanyAPI;
import com.loginradius.sdk.api.ContactAPI;
import com.loginradius.sdk.api.EventAPI;
import com.loginradius.sdk.api.FollowingAPI;
import com.loginradius.sdk.api.GroupAPI;
import com.loginradius.sdk.api.LikeAPI;
import com.loginradius.sdk.api.MentionAPI;
import com.loginradius.sdk.api.PageAPI;
import com.loginradius.sdk.api.PhotoAPI;
import com.loginradius.sdk.api.PostAPI;
import com.loginradius.sdk.api.StatusAPI;
import com.loginradius.sdk.api.UserProfileAPI;
import com.loginradius.sdk.api.VideoAPI;
import com.loginradius.sdk.models.AppInformation;
import com.loginradius.sdk.models.LoginRadiusContactCursorResponse;
import com.loginradius.sdk.models.lrAccessToken;
import com.loginradius.sdk.models.album.LoginRadiusAlbum;
import com.loginradius.sdk.models.audio.LoginRadiusAudio;
import com.loginradius.sdk.models.checkin.LoginRadiusCheckIn;
import com.loginradius.sdk.models.company.LoginRadiusCompany;
import com.loginradius.sdk.models.contact.LoginRadiusContact;
import com.loginradius.sdk.models.event.LoginRadiusEvent;
import com.loginradius.sdk.models.following.LoginRadiusFollowing;
import com.loginradius.sdk.models.group.LoginRadiusGroup;
import com.loginradius.sdk.models.like.LoginRadiusLike;
import com.loginradius.sdk.models.mention.LoginRadiusMention;
import com.loginradius.sdk.models.page.LoginRadiusPage;
import com.loginradius.sdk.models.photo.LoginRadiusPhoto;
import com.loginradius.sdk.models.post.LoginRadiusPost;
import com.loginradius.sdk.models.status.LoginRadiusStatus;
import com.loginradius.sdk.models.userprofile.LoginRadiusUltimateUserProfile;
import com.loginradius.sdk.models.video.LoginRadiusVideo;
import com.loginradius.sdk.ui.FB_Permission;
import com.loginradius.sdk.ui.LRinterfaceimpl;
import com.loginradius.sdk.ui.ProviderPermissions;
import com.loginradius.sdk.ui.lrLoginManager;
import com.loginradius.sdk.util.AsyncHandler;

public class LoginRadiusActivity extends Activity implements OnItemSelectedListener{
	
	private ListView listView;
	private CallbackManager callbackManager;
	private lrAccessToken response_token;
	private Spinner api_spinner;
	private TextView info_textView;
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_activity);
		// initial UI
		api_spinner=(Spinner)findViewById(R.id.API_spinner);
		
		info_textView=(TextView)findViewById(R.id.info_textview);
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.API_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		api_spinner.setAdapter(adapter);
		api_spinner.setOnItemSelectedListener(this);
		
		imageView=(ImageView)findViewById(R.id.profile_image);
		
		FacebookSdk.sdkInitialize(getApplicationContext());
		lrLoginManager.nativeLogin=true;
		callbackManager=CallbackManager.Factory.create();
		ProviderPermissions.resetPermissions();
		ProviderPermissions.addFbPermission(FB_Permission.USER_BASIC_INFO,FB_Permission.USER_WEBSITE,
				FB_Permission.USER_STATUS,FB_Permission.USER_LOCATION,FB_Permission.USER_STATUS,FB_Permission.USER_EVENTS,
				FB_Permission.USER_LIKES,FB_Permission.USER_VIDEOS,FB_Permission.USER_PHOTOS,FB_Permission.USER_FRIEND_USING_APP,
				FB_Permission.MANAGE_PAGES,FB_Permission.USER_MUSIC);
		
		// configuration of the loginradius
		lrLoginManager.getNativeAppConfiguration(getString(R.string.LoginRadius_API_key), callbackManager, new AsyncHandler<AppInformation>() {

			@Override
			public void onSuccess(AppInformation data) {
				// TODO Auto-generated method stub
				populateListView(data);
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	// get the list of providers
	private void populateListView(AppInformation appInformation){
		
		lrLoginManager.ImageUrl="https://cdn.loginradius.com/mobile/android/images/";
		lrLoginManager.ImgVersion="v1";
		listView=(ListView)findViewById(R.id.listview);
		LRinterfaceimpl lRinterfaceimpl=new LRinterfaceimpl();
		
		lRinterfaceimpl.attachListView(appInformation, listView, LoginRadiusActivity.this, new AsyncHandler<lrAccessToken>() {

			@Override
			public void onSuccess(lrAccessToken data) {
				// TODO Auto-generated method stub
				getProfileInfo(data);
				response_token=data;
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	// handle the profile of the user.
	private void getProfileInfo(lrAccessToken token){
		
		UserProfileAPI userProfileAPI=new UserProfileAPI();
		userProfileAPI.getResponse(token, new AsyncHandler<LoginRadiusUltimateUserProfile>() {

			@Override
			public void onSuccess(LoginRadiusUltimateUserProfile data) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "Hello,"+data.FullName+data.ProfileUrl, Toast.LENGTH_LONG).show();
				info_textView.setText("User Name:"+data.FullName+"\n"+"Birthday:"+data.BirthDate+"\n"+"Gender:"+data.Gender
						+"\n"+"City:"+data.City);
				api_spinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	// get user status
	private void getStatus(lrAccessToken token){
		StatusAPI statusAPI=new StatusAPI();
		statusAPI.getResponse(token, new AsyncHandler<LoginRadiusStatus[]>() {

			@Override
			public void onSuccess(LoginRadiusStatus[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}
				info_textView.setText("Most Recent Status:"+data[0]);
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);			
				}
		});
	}
	
	//get user's contacts
	private void getContacts(lrAccessToken token){
		ContactAPI contactAPI=new ContactAPI();
		contactAPI.getResponse(token, new AsyncHandler<LoginRadiusContactCursorResponse>() {

			@Override
			public void onSuccess(LoginRadiusContactCursorResponse data) {
				// TODO Auto-generated method stub
				int index=0;
				String contactString=null;
				for(LoginRadiusContact contact:data.Data){
					if (index>=5) break;
					contactString+=contact.name+"\n";
					index++;
				}
				info_textView.setText(contactString);
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
				
			}
			
		});
	}
	
	//get Albums
	private void getAlbums(lrAccessToken token){
		AlbumAPI albumAPI=new AlbumAPI();
		albumAPI.getResponse(token, new AsyncHandler<LoginRadiusAlbum[]>() {

			@Override
			public void onSuccess(LoginRadiusAlbum[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent album:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}

	// get user's audio
	private void getAudio(lrAccessToken token){
		AudioAPI audioAPI=new AudioAPI();
		audioAPI.getResponse(token, new AsyncHandler<LoginRadiusAudio[]>() {

			@Override
			public void onSuccess(LoginRadiusAudio[] data) {
				// TODO Auto-generated method stub
				if (data.length==0|| data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent audio:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	//get user's checkins
	private void getCheckins(lrAccessToken token){
		CheckInAPI checkInAPI=new CheckInAPI();
		checkInAPI.getResponse(token, new AsyncHandler<LoginRadiusCheckIn[]>() {

			@Override
			public void onSuccess(LoginRadiusCheckIn[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent checkins:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	//get user's followed companies
	private void getCompany(lrAccessToken token){
		CompanyAPI companyAPI=new CompanyAPI();
		companyAPI.getResponse(token, new AsyncHandler<LoginRadiusCompany[]>() {

			@Override
			public void onSuccess(LoginRadiusCompany[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent followed company:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	// get EventAPI
	private void getEvents(lrAccessToken token){
		EventAPI eventAPI=new EventAPI();
		eventAPI.getResponse(token, new AsyncHandler<LoginRadiusEvent[]>() {

			@Override
			public void onSuccess(LoginRadiusEvent[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent event:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	// get user's Followers
	private void getFollowers(lrAccessToken token){
		FollowingAPI followingAPI=new FollowingAPI();
		followingAPI.getResponse(token, new AsyncHandler<LoginRadiusFollowing[]>() {

			@Override
			public void onSuccess(LoginRadiusFollowing[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent follower:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				
					info_textView.setText(errorcode);
				
			}
		});
	}
	
	// get user's groups
	private void getGroups(lrAccessToken token){
		GroupAPI groupAPI=new GroupAPI();
		groupAPI.getResponse(token, new AsyncHandler<LoginRadiusGroup[]>() {

			@Override
			public void onSuccess(LoginRadiusGroup[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent group:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	
	//get user's likes
	private void getLikes(lrAccessToken token){
		LikeAPI likeAPI=new LikeAPI();
		likeAPI.getResponse(token, new AsyncHandler<LoginRadiusLike[]>() {

			@Override
			public void onSuccess(LoginRadiusLike[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent like:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	// get user's mentions
	private void getMentions(lrAccessToken token){
		MentionAPI mentionAPI=new MentionAPI();
		mentionAPI.getResponse(token, new AsyncHandler<LoginRadiusMention[]>() {

			@Override
			public void onSuccess(LoginRadiusMention[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent mention:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	//get user's photos
	private void getPhotos(lrAccessToken token){
		PhotoAPI photoAPI=new PhotoAPI();
		photoAPI.getResponse(token, new AsyncHandler<LoginRadiusPhoto[]>() {

			@Override
			public void onSuccess(LoginRadiusPhoto[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent photo:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	// get user's pages
	private void getPages(lrAccessToken token){
		PageAPI pageAPI=new PageAPI();
		pageAPI.getResponse(token, new AsyncHandler<LoginRadiusPage>() {

			@Override
			public void onSuccess(LoginRadiusPage data) {
				// TODO Auto-generated method stub
				if (data==null) {
					return;
				}else {
					info_textView.setText("page is :"+ data.Name);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	// get user's Videos
	private void getVideos(lrAccessToken token){
		VideoAPI videoAPI=new VideoAPI();
		videoAPI.getResponse(token, new AsyncHandler<LoginRadiusVideo[]>() {

			@Override
			public void onSuccess(LoginRadiusVideo[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent video:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	
	// get postAPI
	private void getPosts(lrAccessToken token){
		PostAPI postAPI=new PostAPI();
		postAPI.getResponse(token, new AsyncHandler<LoginRadiusPost[]>() {

			@Override
			public void onSuccess(LoginRadiusPost[] data) {
				// TODO Auto-generated method stub
				if (data.length==0||data[0]==null) {
					return;
				}else {
					info_textView.setText("Most recent post:"+data[0]);
				}
			}

			@Override
			public void onFailure(Throwable error, String errorcode) {
				// TODO Auto-generated method stub
				info_textView.setText(errorcode);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		//info_textView.setText(parent.getItemAtPosition(position).toString());
		switch (position) {
		case 0:
			//getProfileInfo(response_token);
			break;
		case 1:
			getStatus(response_token);
			break;
		case 2:
			getContacts(response_token);
			break;
		case 3:
			getAlbums(response_token);
			break;
		case 4:
			getAudio(response_token);
			break;
		case 5:
			getCheckins(response_token);
			break;
		case 6:
			getCompany(response_token);
			break;
		case 7:
			getEvents(response_token);
			break;
		case 8:
			getFollowers(response_token);
			break;
		case 9:
			getGroups(response_token);
			break;
		case 10:
			getLikes(response_token);
			break;
		case 11:
			getMentions(response_token);
			break;
		case 12:
			getPhotos(response_token);
			break;
		case 13:
			getPages(response_token);
			break;
		case 14:
			getVideos(response_token);
			break;
		case 15:
			getPosts(response_token);
			break;
		default:
			break;
		}
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
