3
### YouTube app.

Learning Objectives.
====================

Libraries/API
Dynamically add widgets.
Designing classes.
Callbacks/Event handling.


Let's create a new project in the usual way, keeping all the setting at the default value and selecting the 'empty activity'


Setting Up An API
=================

Google has created an Application Platform Interface for its youTube content. An API is essentially a library of classes and methods which act as interface between us the remote content.

In a browser, go to the following link;
https://developers.google.com/youtube/android/player
Here you will find documentation on all the available functionality. Feel free to read up and experiment with different options after completing this app.
First, however, we need to download the library. Click downloads and d/l the youTube player zip file to your computer.
Unzip the file. Inside the libs directory, there should be a .jar file file. We need to copy this to our project library in Android Studio.
To do this we will need to change the project view from 'Android' to 'Project'.
Expand the top level folder, then expand the app folder within. Right click on the libs folder and paste the jar file in.
Now, we must tell our project to use it. This is done via the build.gradle file.
Switch the project back to Android view and expand the gradle.scripts drop down. There are two build.gradle files here.
We are adding an application dependency, we need to use build.gradle which is immediately followed by (Module:app).
Add our new library to the dependencies.

```
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:27.+'
    compile 'com.android.support.constraint:constraint-layout:1.0.2'
    compile files('libs/YouTubeAndroidPlayerApi.jar')
    testCompile 'junit:junit:4.12'

}
```

Following any change to this file you will need to re-sync the project. There should be a prompt ('sync now') for this in the main window. Click it and sync the project.


Create & Launch a New Activity.
===============================

In our app we will be launching a new activity via a button click. To do this, we must first give Android Studio an activity to launch.
Right click on the package containing out main activity and select new/activity/empty activity.
Name the new activity YouTubeActivity. This will also change the layout name to suit automatically.
Ordinarily, we wouldn't check the 'launch activity' button as we don't need any additional launch icons for our app, however, we will check it for the moment. This will allow us to test it. We'll change this back later using the manifest file.

Ok, open the new YouTube activity and change the top to extend YouTubeBaseActivity;
```
public class YouTubeActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
    }
}
```

Next, add the interface that we will be implementing;
```
public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
    }
}
```

You will notice that we are now getting an error. This is because we are not implementing the methods required by our interface. By hovering over the error, we can see which methods we are expected to implement.

**
A quick note on interfaces. Interfaces are essentially a contract that requires any java class implementing an interface MUST implement the methods required by the interface. Think about a car. Pretty much every car has a pretty standard interface (steering wheel, brake, accelerator). We can think of the car as having implemented the 'drive' interface.
**

So, lets make a start on fixing this.
With your cursor inside the class, but outside the onCreate method, right click and select 'implement methods'.
Android studio will create empty version of the required methods, removing the error. We'll come back and fill these in a bit later.


Dynamically Adding Views/Widgets
================================

Lets look at how we might create widgets programmatically, such that a button click could launch a youtube video (for example).
This will be necessary because widgets provided by an external library/API cannot be added to the layout using the GUI designer. They must be added using xml.

In the YouTubeActivity;
The first thing we need to do is get hold of the layout.
One way to do this would be as follows;
```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_you_tube);
    ConstraintLayout layout = (ConstraintLayout) = (ConstraintLayout) findViewById(R.id.activity_layout);
}
```
What we are doing here is setting our content view (activity_you_tube) then casting it to a constraint view. This however, would require us to manually create an id in the activity_you_tube.xml. So, we'll go with the alternative;
```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ConstraintLayout layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_you_tube, null);
    setContentView(layout);
}
```
Here we are just doing the same thing the other way around. So, we inflate our xml by referencing its layout name (as opposed to id) then pass the inflated view into out setContentView.

You may notice a yellow warning on the null keyword. Hovering over it reveals that it is not recommended to pass null as the root view. Ordinarily this true. It can be the cause of display problems, but this case is an exception. Here we are inflating the root layout, so this IS the root view.

Ok, next task is to create a new YouTubePlayer object and add it to our constraint layout.
Lets demonstrate how this works using a simple button;
In our onCreate method, below the rest of the code;
```
Button button1 = new Button(this);
button1.setLayoutParams(new ConstraintLayout.LayoutParams(300, 80));
button1.setText("new button");
layout.addView(button1);
```
When we create a new object like this, we must pass it the context in which it will be created. As we are in the YouTubeActivity onCreate, 'this' is the context. I.e. referring to self.
Next we set the layout params (width 300dp, height 80dp).
Set the text to be displayed on the button.
Then add the widget to the layout.

Lets run the app and see if our button displays. (remember the app will launch the main activity so if we want to launch the YouTubeActivity, you will need to right click somewhere in the code and select it for launch as opposed to the usual 'play' button).

Ok, all being well, we can now dynamically add widgets.
Lets comment our button code (it was just for demonstration) and set up our YouTUbe player.
```
YouTubePlayerView playerView = new YouTubePlayerView(this);
playerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
layout.addView(playerView);
```
Again, we create an instance of the YouTubePlayerView and assign it the 'playerView' variable.
We set out the layout parameters.
Then add our new playerView to the layout.


API Keys.
=========

Let's get ourselves an API with which we can access the YouTube content.
Go to https://console.developers.google.com/apis and click on YouTube Data API.
Create a new project using the default settings.
Click enable.
Click create credentials. Using Android. For Public data.
Voila! You have your API key.

Go back into your YouTubeActivity and declare the following;
```
static final String GOOGLE_API_KEY = "AIzaSyAutXe5s3Tv6D9WiTt-9qgs2NFD4lYcbfI";
static final String YOUTUBE_VIDEO_ID = "TODO";
static final String YOUTUBE_PLAYLIST = "TODO";
```
Next we need to get a video id. In the browser, go to youtube and select a playlist of your choice, then the first video in the playlist.
e.g: https://www.youtube.com/watch?v=5MjBMhlMq3s&list=PL49AE65994CB8A791
we need the unique id of this video, which is the part immediately after the ?v= up to, but not including, the &.
In this case: 5MjBMhlMq3s
Paste this into the YOUTUBE_VIDEO_ID.
Next wee need the playlist id, which is everything after the list=
So: PL49AE65994CB8A791
Paste this into the YOUTUBE_PLAYLIST.

```
static final String GOOGLE_API_KEY = "AIzaSyAutXe5s3Tv6D9WiTt-9qgs2NFD4lYcbfI";
static final String YOUTUBE_VIDEO_ID = "5MjBMhlMq3s";
static final String YOUTUBE_PLAYLIST = "PL49AE65994CB8A791";
```

Right.........lets code our app!


First thing to do is initialise (create an instance of) the youtube player. This is how we will give it our API key.
So, in the YouTubeActivity, onCreate method, just below where we add the playerView to the layout, add;

```
layout.addView(playerView);
playerView.initialize(GOOGLE_API_KEY, this);
```

Hopefully now you can see why we need the methods stipulated by our interface. One will contain instruction on what to do if the playerView fails to initialise, the other if initialisation is a success.
Lets look handling errors first;

```
@Override
public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    final int REQUEST_CODE = 1;
    if(youTubeInitializationResult.isUserRecoverableError()){
        youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();
    } else {
        String errorMessage = String.format("There was an error initialising youTube player (%1$s)", youTubeInitializationResult.toString());
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
```
So, if initialisation fails and is recoverable, we display the request_code on screen. If it is not recoverable, we create a Toast with a custome error message.

A Toast is a pop up which appears on screen to display a message to the user. In this case we have selected LENGTH_LONG which displays for longer than the alternative - LENGTH_SHORT. The toast disappears after a short period without the need for the user to click it.


Before we complete the 'success' method, lets give our application access to the internet.
In the AndroidManifest.xml;

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="example.codeclan.com.youtubeapp">
    <uses-permission android:name="android.permission.INTERNET"/>
```
If you run you app on the emulator now, you will that we get an error. We don't have the YouTubePlayer app on our emulator. We can't download it either, however, once we deploy our finished app to an android device we will be able to do both.


Let's make a start on the successfully initialised method.
First we need to declare another variable;
```
private static final String TAG = "YouTubeActivity";
```
Then, our onInitialize becomes......

```
@Override
public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
    Log.d(TAG, "onInitializationSuccess: provider is " + provider.getClass().toString());
    Toast.makeText(this, "Initialised YouTube Player successfully", Toast.LENGTH_LONG).show();
    if(!wasRestored){
        youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
    }
}
```

Here we are logging out information on the provider, using a toast to say the player launched correctly, and checking if the video we have selected was previously playing (so that it will resume from the same position).


Launch the YouTubeActivity on your Android device.
You should see the confirmation Toast and the video ready to play.


### Listeners.

We are going to implement the PlaybackEventListener interface on our YouTubePlayer instance. As this is an interface, there are a number of methods which we must implement. As usual, once you have written the top line of the method, android studio can create the required method headings for you;
Creating additional functionality inside these methods is not essential, but it is a nice to have way of seeing what is triggering when.
Inside YouTubeActivity;
```
private YouTubePlayer.PlaybackEventListener playBackEventListener = new YouTubePlayer.PlaybackEventListener(){


     @Override
     public void onPlaying() {
       Toast.makeText(YouTubeActivity.this, "Video is playing ok.", Toast.LENGTH_LONG).show();
     }

     @Override
     public void onPaused() {
       Toast.makeText(YouTubeActivity.this, "Video is paused.", Toast.LENGTH_LONG).show();
     }

     @Override
     public void onStopped() {

     }

     @Override
     public void onBuffering(boolean b) {

     }

     @Override
     public void onSeekTo(int i) {

     }
     ```

Next we create the state change listeners.
You will see that we now a different set of events to which we can react.

```
private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {
      Toast.makeText(YouTubeActivity.this, "Click the ad. The creator's gotta eat.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVideoStarted() {
      Toast.makeText(YouTubeActivity.this, "Video started.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVideoEnded() {
      Toast.makeText(YouTubeActivity.this, "Video completed.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }
};
```

You will notice that the name definition of our playbackEventListener and stateChangeListener are greyed out. Meaning they are not currently be used/called. To use them we need to add some code to the onInitializationSuccess method;
```
@Override
public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
    Log.d(TAG, "onInitializationSuccess: provider is " + provider.getClass().toString());
    Toast.makeText(this, "Initialised YouTube Player successfully", Toast.LENGTH_LONG).show();
    //Added
    youTubePlayer.setPlaybackEventListener(playBackEventListener);
    youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

    if(!wasRestored){
        youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
    }
}
```

Now when you re-run the app on your device, you should be able to see the toast messages when you play, pause, finish, etc.
Congrats! Our basic app is complete!


### Layouts & Intents.

Next up, lets make our app capable of handling playlists.
To do this we will create a launch activity from which we can choose to play a video or play a playlist.
By doing so, we will look at using intents to launch activities.
We're going to work on our MainActivity and create a layout called StandaloneActivity.
*Note: We will create this without using the new activity wizard
So, in the main_activity.xml, drag and drop 2 new buttons to the layout.
Constrain the top button to the left and top of the layout.
Constrain the bottom button to the left of the top button and to the bottom of the first button.
Change the top button id to 'btnPlaySingle' with a text field 'play a single video'.
Change the bottom button to 'btnStandalone' with a text field of 'standalone sub menu'.

Create a new layout.xml called activity_standalone.xml and add two buttons just as we did before.
Change the top button id to 'btnPlayVideo' with text field 'play video'.
Change the bottom button id to 'btnPlayList' with text field of 'play playlist'.

OK, we have our new activities/layouts. Lets add the code to make them work.
Since we manually added our new layout this time, we will need to manually add a corresponding java file. So, click on the package and select new java class. Call it 'StandaloneActivity', extends 'AppCompatActivity', implements 'View.OnClickListener'.
Inside our new class, press ctrl+o and select onCreate. The IDE will create this method for us.
As usual, we need to define our content view, then obtain our buttons and set onclickListeners on them.
```
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_standalone);

    Button btnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
    Button btnPlayList = (Button) findViewById(R.id.btnPlayList);

    btnPlayVideo.setOnClickListener(this);
    btnPlayList.setOnClickListener(this);
}
```

Below the code shown above, still inside our class, right click and select 'generate/implementMethods'
This will create the methods required by the interface. Now all we need to do is give our listener some instructions to execute when it is triggered.
We want to launch the standalone video player with an id of video or playlist to play.

```
@Override
public void onClick(View view) {
    Intent intent = null;
    switch(view.getId()){
        case R.id.btnPlayVideo:
            intent = YouTubeStandalonePlayer.createVideoIntent(this, YouTubeActivity.GOOGLE_API_KEY, YouTubeActivity.YOUTUBE_VIDEO_ID);
            break;
        case R.id.btnPlayList:
            intent = YouTubeStandalonePlayer.createVideoIntent(this, YouTubeActivity.GOOGLE_API_KEY, YouTubeActivity.YOUTUBE_PLAYLIST);
            break;
        default:
    }
    if(intent != null){
        startActivity(intent);
    }
}
```
Let's break this down a bit;
First we create a new Intent object (more on this later) called intent and assign it to null.
Next, we use a switch statement to determine which button was clicked (necessary since we are using the same listener for both). Once ascertained, we set up our intent.
We use the YouTubeStandalonePlayer which our API provides for us and run the createVideoIntent method on it. This takes in 3 params. First, the activity the intent is coming from (in this case, the current activity. I.e. this), then the API key and the id of the video to launch (these were set as static constants in the YouTubeActivity).
We will make our own intents a bit later, but for now, we don't need to.
Once we have out Intent object setup, we trigger it using the startActivity method.


### Intents.
Here's what the google docs say about intents;

An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int) to communicate with a background Service.

An Intent provides a facility for performing late runtime binding between the code in different applications. Its most significant use is in the launching of activities, where it can be thought of as the glue between activities. It is basically a passive data structure holding an abstract description of an action to be performed.

Simply put, an intent is used for launching activities or external apps (like email for example) and can also be used to carry data around. Intents bind activities together.
Alternatively, An intent is a messaging object you can use to request an action from another app component.
This page of the google docs is pretty well written and worth a read;
https://developer.android.com/guide/components/intents-filters.html


Ok, lets move over to our MainActivity, we'll set up the required buttons and make our own intents.
make the MainActivity implement onClickListener.
In the onCreate method, get hold of out layout buttons and assign an onClickListener just as we did before.
```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button btnSingle = (Button) findViewById(R.id.btnPlaySingle);
    Button btnStandalone = (Button) findViewById(R.id.btnStandAlone);

    btnSingle.setOnClickListener(this);
    btnStandalone.setOnClickListener(this);
}
```
Generate the method (onClick) required by our interface as we did in the previous activity.
Again, create an instance of an intent set to null.
Ascertain, which of our buttons has triggered and define the action.

```
@Override
public void onClick(View view) {
    Intent intent = null;
    switch(view.getId()){
        case R.id.btnPlaySingle:
            intent = new Intent(this, YouTubeActivity.class);
            break;
        case R.id.btnStandAlone:
            intent = new Intent(this, StandaloneActivity.class);
            break;
        default:
    }
    if(intent != null){
        startActivity(intent);
    }
}
```
You will notice this intent is slightly different from before. This is because this time we are creating our own.
The intent class has several constructors (also known as overloaded methods), meaning the intent object can be created using several different sets of parameters. Feel free to read the docs on this subject and investigate other ways of initialising an intent.
In this case, we pass in the activity starting the intent (this) and a class literal (YouTubeActivity.class).
In java if we need to pass a class to a method, we do so by using a 'class literal'.
A class literal is just a notation describing the name of a class.
For example;
```
String name = "Deirdre Barlow"
name.class => String.
```
So, in our intent constructor, we are effectively saying;
```
intent = new Intent(this, YouTubeActivity)
```


Finally, we are checking that the intent isn't null (it shouldn't be as we only have two buttons and both are covered by the same listener), then using the startActivity method to.......start our activity.

One last thing, since we didn't use the wizard when creating our StandaloneActivity, we will need to add it to our manifest.
In the projects pane, click manifests/AndroidManifest.

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="example.codeclan.com.youtubeapp">
    <uses-permission android:name="android.permission.INTERNET"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".YouTubeActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

You will notice we have activity tags for;
```
<activity android:name=".MainActivity">
<activity android:name=".YouTubeActivity">
```
In addition, because we checked the launch activity in the activity wizard when we created these activities, they also have this line (intent filter);
```
<category android:name="android.intent.category.LAUNCHER" />
```
Without these we would have been unable to launch the app in the manner we have been (i.e. launching the MainActivity or the YouTubeActivity).
Now that we are launching the YouTubeActivity using a button, we no longer require this intent filter and as such it can be removed.
Next add our new Activity in the same format as the YouTubeActivity.
The final xml file should look like this;
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="example.codeclan.com.youtubeapp">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name=".YouTubeActivity"
            android:label="YouTube Player">

        </activity>

        <activity
            android:name=".StandaloneActivity"
            android:label="Standalone Activity">

        </activity>
    </application>
</manifest>
```

###
All that remains is to run your app (selecting 'app' from the dropdown, no 'YouTubeActivity') and bask in the glory of shiny new video player!
