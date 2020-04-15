package com.tekdevisal.skype_clone;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

//public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {
//
//    private static String API_KEY = "46670712";
//    private static String SESSION_ID = "1_MX40NjY3MDcxMn5-MTU4Njc5ODIwMTI2M35ic2M5S1BXV3dzVTZsZDQwelZtT3F0Qkh-fg";
//    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjY3MDcxMiZzaWc9MGYzYmIwNzIzMTRiOTUyOTZmODQ3YjM1Nzc1Yzc0ZTlmYjlhZTViMDpzZXNzaW9uX2lkPTFfTVg0ME5qWTNNRGN4TW41LU1UVTROamM1T0RJd01USTJNMzVpYzJNNVMxQlhWM2R6VlRac1pEUXdlbFp0VDNGMFFraC1mZyZjcmVhdGVfdGltZT0xNTg2Nzk4MjMzJm5vbmNlPTAuODkwMDA1ODQ3NTAwODI1OSZyb2xlPXN1YnNjcmliZXImZXhwaXJlX3RpbWU9MTU4OTM5MDIzMSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
//    private static String LOG_TAG = MainActivity.class.getSimpleName();
//    private static final int RC_SETTINGS = 123;
//
//    private Session session;
//    private FrameLayout publisher_container, subscripber_container;
//    private Publisher publisher;
//
//    private Subscriber subscriber;
//    private String userid="";
//    private DatabaseReference usersRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        usersRef = FirebaseDatabase.getInstance().getReference("users");
//
//        requestPermission();
//        publisher_container = findViewById(R.id.publisher_container);
//        subscripber_container = findViewById(R.id.subscripber_container);
//
//        findViewById(R.id.close_video_chat).setOnClickListener(v -> {
//            usersRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.child(userid).hasChild("Ringing")){
//                        usersRef.child(userid).child("Ringing")
//                                .removeValue();
//
////                        /*check*/
////                        session.unpublish(publisher);
//                        if(publisher != null){
//                            publisher.destroy();
//                        }
//
//                        if(subscriber != null){
//                            subscriber.destroy();
//                        }
//
//                        startActivity(new Intent(MainActivity.this, Home.class));
//                        finish();
//                    }
//                    if(dataSnapshot.child(userid).hasChild("Calling")){
//                        usersRef.child(userid).child("Calling")
//                                .removeValue();
//
////                        /*check*/
////                        session.unpublish(publisher);
//
//                        if(publisher != null){
//                            publisher.destroy();
//                        }
//
//                        if(subscriber != null){
//                            subscriber.destroy();
//                        }
//
//                        startActivity(new Intent(MainActivity.this, Home.class));
//                        finish();
//                    }else{
//
//                        if(publisher != null){
//                            publisher.destroy();
//                        }
//
//                        if(subscriber != null){
//                            subscriber.destroy();
//                        }
//
//                        startActivity(new Intent(MainActivity.this, Home.class));
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this);
//    }
//
//    @AfterPermissionGranted(RC_SETTINGS)
//    private void requestPermission(){
//         String[] perm = {
//                Manifest.permission.INTERNET,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        };
//         if(EasyPermissions.hasPermissions(this, perm)){
//            session = new Session.Builder(this,API_KEY, SESSION_ID).sessionOptions(new Session.SessionOptions() {
//                @Override
//                public boolean isCamera2Capable() {
//                    return super.isCamera2Capable();
//                }
//
//                @Override
//                public boolean useTextureViews() {
//                    return super.useTextureViews();
//                }
//            }).build();
//            session.setSessionListener(this);
//            session.connect(TOKEN);
//         }else{
//
//             EasyPermissions.requestPermissions(this, "This app needs to access your camera and mic",RC_SETTINGS, perm);
//         }
//
//    }
//
//    @Override
//    public void onConnected(Session session) {
//        publisher = new Publisher.Builder(this).build();
//        publisher.setPublisherListener(this);
//
//        publisher_container.addView(publisher.getView());
////        publisher.startPreview();
//
////        if(publisher.getView() instanceof GLSurfaceView){
////            ((GLSurfaceView) publisher.getView()).setZOrderOnTop(true);
////        }
//        session.publish(publisher);
//    }
//
//    @Override
//    public void onDisconnected(Session session) {
//
//    }
//
//    @Override
//    public void onStreamReceived(Session session, Stream stream) {
//        Toast.makeText(MainActivity.this, "isreceived", Toast.LENGTH_LONG).show();
//        if(subscriber == null){
//            subscriber = new Subscriber.Builder(MainActivity.this, stream).build();
//            session.subscribe(subscriber);
//            subscripber_container.addView(subscriber.getView());
//        }
//    }
//
//    @Override
//    public void onStreamDropped(Session session, Stream stream) {
//        if(subscriber != null){
//            subscriber = null;
//            subscripber_container.removeAllViews();
//        }
//    }
//
//    @Override
//    public void onError(Session session, OpentokError opentokError) {
//
//    }
//
//    @Override
//    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
//
//    }
//
//    @Override
//    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
//        session.unpublish(publisher);
//    }
//
//    @Override
//    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
//
//    }
//}

public class MainActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        WebServiceCoordinator.Listener,
        Session.SessionListener,
        PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    // Suppressing this warning. mWebServiceCoordinator will get GarbageCollected if it is local.
    @SuppressWarnings("FieldCanBeLocal")
    private WebServiceCoordinator mWebServiceCoordinator;

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize view objects from your layout
        mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscripber_container);

        requestPermissions();
    }

    /* Activity lifecycle methods */

    @Override
    protected void onPause() {

        Log.d(LOG_TAG, "onPause");

        super.onPause();

        if (mSession != null) {
            mSession.onPause();
        }

    }

    @Override
    protected void onResume() {

        Log.d(LOG_TAG, "onResume");

        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setRationale(getString(R.string.rationale_ask_again))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(RC_SETTINGS_SCREEN_PERM)
                    .build()
                    .show();
        }
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {

        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // if there is no server URL set
            if (OpenTokConfig.CHAT_SERVER_URL == null) {
                // use hard coded session values
                if (OpenTokConfig.areHardCodedConfigsValid()) {
                    initializeSession(OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID, OpenTokConfig.TOKEN);
                } else {
                    showConfigError("Configuration Error", OpenTokConfig.hardCodedConfigErrorMessage);
                }
            } else {
                // otherwise initialize WebServiceCoordinator and kick off request for session data
                // session initialization occurs once data is returned, in onSessionConnectionDataReady
                if (OpenTokConfig.isWebServerConfigUrlValid()) {
                    mWebServiceCoordinator = new WebServiceCoordinator(this, this);
                    mWebServiceCoordinator.fetchSessionConnectionData(OpenTokConfig.SESSION_INFO_ENDPOINT);
                } else {
                    showConfigError("Configuration Error", OpenTokConfig.webServerConfigErrorMessage);
                }
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_video_app), RC_VIDEO_APP_PERM, perms);
        }
    }

    private void initializeSession(String apiKey, String sessionId, String token) {

        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
    }

    /* Web Service Coordinator delegate methods */

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {

        Log.d(LOG_TAG, "ApiKey: "+apiKey + " SessionId: "+ sessionId + " Token: "+token);
        initializeSession(apiKey, sessionId, token);
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {

        Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
        Toast.makeText(this, "Web Service error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        finish();

    }

    /* Session Listener methods */

    @Override
    public void onConnected(Session session) {

        Log.d(LOG_TAG, "onConnected: Connected to session: "+session.getSessionId());

        // initialize Publisher and set this object to listen to Publisher events
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        // set publisher video style to fill view
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisherViewContainer.addView(mPublisher.getView());
        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: "+session.getSessionId());
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamReceived: New Stream Received "+stream.getStreamId() + " in session: "+session.getSessionId());

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamDropped: Stream Dropped: "+stream.getStreamId() +" in session: "+session.getSessionId());

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: "+ opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - "+opentokError.getMessage() + " in session: "+ session.getSessionId());

        showOpenTokError(opentokError);
    }

    /* Publisher Listener methods */

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamCreated: Publisher Stream Created. Own stream "+stream.getStreamId());

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream "+stream.getStreamId());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: "+opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() +  " - "+opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: "+subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, "onDisconnected: Subscriber disconnected. Stream: "+subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: "+opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() +  " - "+opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    private void showOpenTokError(OpentokError opentokError) {

        Toast.makeText(this, opentokError.getErrorDomain().name() +": " +opentokError.getMessage() + " Please, see the logcat.", Toast.LENGTH_LONG).show();
        finish();
    }

    private void showConfigError(String alertTitle, final String errorMessage) {
        Log.e(LOG_TAG, "Error " + alertTitle + ": " + errorMessage);
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(errorMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
