package fit5046.test.touchme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FWelcome extends Fragment {
    private View fWelcome;

    //private ImageButton btDogFace;
    private GlobalClass globalClass;

    private LocationManager locationManager;

    private boolean isGPSEnabled,isNetworkEnabled, canGetLocation;
    private Location myLocation;
    private MyDialogBuilder alertDialogBuilder;
    private AlertDialog alertDialog;
    //private PaintView paintView;
    private LinearLayout ll_dog_behavior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fWelcome == null){
            fWelcome = inflater.inflate(R.layout.fragment_welcome, container, false);
        }
        globalClass = (GlobalClass) getContext().getApplicationContext();

        ll_dog_behavior = fWelcome.findViewById(R.id.ll_dog_behavior);
        final FragmentManager fragmentManager = getFragmentManager();
        ll_dog_behavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //globalClass.currentFragment = new FQuiz();
                //fragmentManager.beginTransaction().replace(R.id.fl_main, globalClass.currentFragment).commit();
                getLocation();
            }
        });
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        alertDialogBuilder = new MyDialogBuilder(getContext(),container,MyDialogBuilder.TYPE_YES);
        alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setYesOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalClass.isOfflineMode){
                    globalClass.currentFragment = new FQuiz();
                    fragmentManager.beginTransaction().replace(R.id.fl_main, globalClass.currentFragment).commit();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCancelable(false);

        globalClass.isOfflineMode = true;

        //paintView = (PaintView)fWelcome.findViewById(R.id.pv_dog_face);
        //paintView.setColor(getResources().getColor(R.color.colorAccent));
       // paintView.setTextSize(12f);
        //paintView.setText("Dogs Behavior");

        return fWelcome;
    }

    public void getLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (isGPSEnabled){
                    myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (isNetworkEnabled) {
                    myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    globalClass.isOfflineMode = true;
                    alertDialogBuilder.setMessage(getString(R.string.error_no_gps));
                    alertDialog.show();
                }
            } else {
                globalClass.isOfflineMode = true;
                alertDialogBuilder.setMessage(getString(R.string.error_no_gps));
                alertDialog.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
