package fit5046.test.touchme;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

public class GlobalClass extends Application {
    public Fragment currentFragment;
    public String serverURL="";
    public boolean isOfflineMode;
    public int pxOf250dp;

    public Context globalContext;
    public ViewGroup globalViewGroup;
}
