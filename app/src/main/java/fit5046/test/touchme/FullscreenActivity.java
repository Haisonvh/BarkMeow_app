package fit5046.test.touchme;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private boolean mIsBound = false;
    private boolean isMusic = true;
    private MusicService mServ;
    private Intent music;
    private ImageButton btMusic;
    private ImageButton btSeting;
    private ImageButton btAbout;
    private LinearLayout llSetting;

    private GlobalClass globalClass;
    private Context context;
    private boolean isShowSetting = false;
    private ServiceConnection Scon = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen);
        btMusic = (ImageButton) findViewById(R.id.bt_music);
        btSeting = (ImageButton) findViewById(R.id.bt_setting);
        btAbout = (ImageButton) findViewById(R.id.bt_about);
        llSetting = (LinearLayout)findViewById(R.id.ll_setting);
        globalClass = (GlobalClass)getApplicationContext();
        context = this;
        Resources r = this.getResources();
        AlertDialog alertDialog;
        int  px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 250, r.getDisplayMetrics()));
        globalClass.pxOf250dp = px;

        float temp = r.getDisplayMetrics().density;
        globalClass.globalViewGroup = findViewById(android.R.id.content);
        globalClass.globalContext = context;
        if (globalClass.currentFragment == null)
            globalClass.currentFragment = new FWelcome();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,globalClass.currentFragment).commit();

        btMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btMusic.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isMusic){
                            btMusic.setBackgroundResource(R.drawable.nomusic);
                            isMusic = false;
                            if (mServ!=null){
                                mServ.stopMusic();
                            }
                        }
                        else {
                            btMusic.setBackgroundResource(R.drawable.music);
                            isMusic = true;
                            if (mServ!=null){
                                mServ.startMusic();
                            }
                        }

                    }
                });
            }
        });

        btSeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSetting.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isShowSetting){
                            llSetting.setVisibility(View.GONE);
                            isShowSetting = false;
                        } else{
                            llSetting.setVisibility(View.VISIBLE);
                            isShowSetting = true;
                        }

                    }
                });
            }
        });

        btAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog.Builder builder = new AlertDialog.Builder(context);

                MyDialogBuilder builder = new MyDialogBuilder(globalClass.globalContext,globalClass.globalViewGroup,MyDialogBuilder.TYPE_INFO);
                builder.setMessage(getString(R.string.image_copyright)+"\n"+getString(R.string.music_copyright));
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog.show();
            }
        });
        music = new Intent();
        music.setClass(this, MusicService.class);
        doBindService();
        startService(music);
    }



    void doBindService(){
        bindService(music,Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mServ != null) {
            mServ.stopMusic();
            mServ.startMusic();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mServ != null) {
            mServ.stopMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        stopService(music);

    }

    @Override
    public void onBackPressed() {
        //todo show dialog confirm to exit
    }
}
