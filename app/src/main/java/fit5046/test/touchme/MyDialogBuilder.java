package fit5046.test.touchme;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class MyDialogBuilder extends AlertDialog.Builder {
    protected static final int TYPE_YES_NO = 0;
    protected static final int TYPE_INFO = 1;
    protected static final int TYPE_YES= 2;

    private ImageButton btYes,btNo;
    private TextView tvInfo;
    private GifImageView ivTeller;
    private ViewGroup viewGroup;

    protected MyDialogBuilder(Context context, ViewGroup viewGroup, int type) {
        super(context);
        this.viewGroup = viewGroup;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_custom, viewGroup, false);

        tvInfo = (TextView)dialogView.findViewById(R.id.tv_dialog);

        btYes = (ImageButton)dialogView.findViewById(R.id.bt_yes);
        btNo = (ImageButton)dialogView.findViewById(R.id.bt_no);
        ivTeller = (GifImageView)dialogView.findViewById(R.id.teller_dialog);

        switch (type){
            case TYPE_INFO:
                btYes.setVisibility(View.GONE);
                btNo.setVisibility(View.GONE);
                break;
            case TYPE_YES_NO:
                btYes.setVisibility(View.VISIBLE);
                btNo.setVisibility(View.VISIBLE);
                break;
            case TYPE_YES:
                btYes.setVisibility(View.VISIBLE);
                btNo.setVisibility(View.GONE);
                break;
        }
        this.setView(dialogView);
    }

    public AlertDialog.Builder setYesOnClickListener (View.OnClickListener onClickListener){
        btYes.setOnClickListener(onClickListener);
        return this;
    }

    public AlertDialog.Builder setNoOnClickListener (View.OnClickListener onClickListener){
        btNo.setOnClickListener(onClickListener);
        return this;
    }
    @Override
    public AlertDialog.Builder setMessage(CharSequence mess){
        tvInfo.setText(mess);
        return this;
    }

    public AlertDialog.Builder setImageTeller(int id){
        ivTeller.setImageResource(id);
        return this;
    }

}
