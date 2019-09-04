package fit5046.test.touchme;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class FGame extends Fragment {
    private View fGame;
    private final FragmentManager fragmentManager = getFragmentManager();


    //private Button btNext;

    private ImageView ivQuestion;
    private ImageView ivTeller;
    private TextView tvQuestion;

    private Animation animMoveRight;
    private Animation animFadeIn;
    private Animation animSlideDown;
    private ImageView ivMainChar;
    private GifImageView givEmotion;
    private VelocityTracker mVelocityTracker = null;
    private TextView tvXY;
    private CharactorPoint mainCharactorPoint;
    private GlobalClass globalClass;
    List<SquareArea.TouchPoint> touchPoints;
    private ImageButton ib_option;

    final Handler handlerTickBoring = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            givEmotion.setImageResource(R.drawable.boring);
            givEmotion.setVisibility(View.VISIBLE);
            return true;
        }
    });
    final Handler handlerCloseEmotion = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            givEmotion.setVisibility(View.GONE);
            return true;
        }
    });
    Timer timerBoring = new Timer("timerBoring",false);
    TimerTask tickBoring = new TimerTask() {
        @Override
        public void run() {
            handlerTickBoring.sendEmptyMessage(0);
        }
    };
    Timer timerCloseEmotion = new Timer("timerCloseEmotion",false);
    TimerTask tickCloseEmotion = new TimerTask() {
        @Override
        public void run() {
          handlerCloseEmotion.sendEmptyMessage(0);
        }
    };
    Handler startTimer = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            timerBoring.cancel();
            timerCloseEmotion.cancel();
            tickCloseEmotion.cancel();
            tickBoring.cancel();
            timerBoring = new Timer("timerBoring",false);
            tickBoring = new TimerTask() {
                @Override
                public void run() {
                    handlerTickBoring.sendEmptyMessage(0);
                }
            };
            timerCloseEmotion = new Timer("timerCloseEmotion",false);
            tickCloseEmotion = new TimerTask() {
                @Override
                public void run() {
                    handlerCloseEmotion.sendEmptyMessage(0);
                }
            };
            timerBoring.schedule(tickBoring,5500);
            timerCloseEmotion.schedule(tickCloseEmotion,2000);
            return true;
        }
    });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(fGame == null)
            fGame = inflater.inflate(R.layout.fragment_game, container, false);

        givEmotion = (GifImageView) fGame.findViewById(R.id.iv_emotion);
        givEmotion.setVisibility(View.GONE);
        ivQuestion = (ImageView)fGame.findViewById(R.id.iv_game_conversation_bubble);
        ivTeller = (ImageView)fGame.findViewById(R.id.iv_game_teller);

        tvQuestion = (TextView)fGame.findViewById(R.id.tv_game_info);

        ivMainChar = (ImageView)fGame.findViewById(R.id.iv_game);
        ivMainChar.setVisibility(View.GONE);

        animMoveRight = AnimationUtils.loadAnimation(container.getContext(), R.anim.move_right);
        animFadeIn = AnimationUtils.loadAnimation(container.getContext(), R.anim.fade_in);
        animSlideDown = AnimationUtils.loadAnimation(container.getContext(), R.anim.slide_down);
        globalClass = (GlobalClass)getContext().getApplicationContext();
        ivTeller.startAnimation(animMoveRight);
        ib_option = (ImageButton)fGame.findViewById(R.id.ib_option);
        ib_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalClass.currentFragment = new FWelcome();
                fragmentManager.beginTransaction().replace(R.id.fl_main, globalClass.currentFragment).commit();
            }
        });


        animMoveRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivQuestion.post(new Runnable() {
                    @Override
                    public void run() {
                        ivQuestion.setVisibility(View.VISIBLE);
                        tvQuestion.setVisibility(View.VISIBLE);
                        ivQuestion.startAnimation(animSlideDown);
                        tvQuestion.setText(R.string.intro_game);
                        tvQuestion.startAnimation(animFadeIn);
                    }
                });
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final View.OnTouchListener screenTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ivMainChar.post(new Runnable() {

                    @Override
                    public void run() {
                        ivMainChar.setVisibility(View.VISIBLE);
                        ivMainChar.startAnimation(animFadeIn);
                    }
                });
                tvQuestion.post(new Runnable() {
                    @Override
                    public void run() {
                        tvQuestion.setVisibility(View.GONE);
                    }
                });
                ivQuestion.post(new Runnable() {
                    @Override
                    public void run() {
                        ivQuestion.setVisibility(View.GONE);
                    }
                });

                ivTeller.post(new Runnable() {
                    @Override
                    public void run() {
                        ivTeller.clearAnimation();
                        ivTeller.setVisibility(View.GONE);
                    }
                });

                fGame.setOnTouchListener(null);
                return true;
            }
        };
        fGame.setOnTouchListener(screenTouch);
        touchPoints = new ArrayList<SquareArea.TouchPoint>();
        //test
        tvXY = (TextView)fGame.findViewById(R.id.tv_xy);
        //tvXY.setVisibility(View.VISIBLE);
        ivMainChar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int index = event.getActionIndex();
                int action = event.getActionMasked();
                int TouchPointerId = event.getPointerId(index);
                float x = event.getX();
                float y = event.getY();
                String temp ="";
                //timerBoring.cancel();
                //timerCloseEmotion.cancel();
                switch (action){
                    case MotionEvent.ACTION_DOWN:

                        temp = "X:"+ x + " Y:"+y;
                        tvXY.setText(temp);
                        touchPoints.add(new SquareArea.TouchPoint(x,y));

                        if(mVelocityTracker == null) {
                            mVelocityTracker = VelocityTracker.obtain();
                        }
                        else {
                            mVelocityTracker.clear();
                        }
                        mVelocityTracker.addMovement(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                         mVelocityTracker.addMovement(event);
                         mVelocityTracker.computeCurrentVelocity(1000);
                         temp = "X velocity: " + mVelocityTracker.getXVelocity(TouchPointerId)
                                 + "Y velocity: " + mVelocityTracker.getYVelocity(TouchPointerId);
                         tvXY.setText(temp);
                         x = x + mVelocityTracker.getXVelocity(TouchPointerId);
                         y = y + mVelocityTracker.getYVelocity(TouchPointerId);
                         touchPoints.add(new SquareArea.TouchPoint(x,y));
                         break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if(touchPoints.size()<10){
                            int animationID = 0;
                            if (mainCharactorPoint.getBack().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.happy1;
                            if (mainCharactorPoint.getBody().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.happy2;
                            if (mainCharactorPoint.getEyes().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.hurt;
                            if (mainCharactorPoint.getLeftEar().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.happy1;
                            if (mainCharactorPoint.getLeftFrontLeg().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.happy2;
                            if (mainCharactorPoint.getLeftRearLeg().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.annoyed;
                            if (mainCharactorPoint.getMouth().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.angry;
                            if (mainCharactorPoint.getRightEar().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.happy1;
                            if (mainCharactorPoint.getRightFrontLeg().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.happy2;
                            if (mainCharactorPoint.getChest().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.happy1;
                            if (mainCharactorPoint.getRightRearLeg().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.annoyed;
                            if (mainCharactorPoint.getTail().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.angry;
                            if (mainCharactorPoint.getTopHead().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.lovely;
                            if (mainCharactorPoint.getStomach().checkIncludePoint(touchPoints.get(0)))
                                animationID = R.drawable.annoyed;
                            final int id = animationID;

                            if (id != 0){
                                givEmotion.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        givEmotion.setVisibility(View.VISIBLE);
                                        givEmotion.setImageResource(id);
                                    }
                                });
                                startTimer.sendEmptyMessage(0);
                            }


                        } else {

                        }
                        touchPoints.clear();
                        break;
                }
                return true;
            }
        });

        //todo crete a function to read the TouchPoint in raw file
        int cuurentPx = globalClass.pxOf250dp;
        mainCharactorPoint = new CharactorPoint();
        mainCharactorPoint.setBack(new SquareArea(new SquareArea.TouchPoint(380,230,cuurentPx),new SquareArea.TouchPoint(570,325,cuurentPx)));
        mainCharactorPoint.setBody(new SquareArea(new SquareArea.TouchPoint(380,325,cuurentPx),new SquareArea.TouchPoint(570,425,cuurentPx)));
        mainCharactorPoint.setChest(new SquareArea(new SquareArea.TouchPoint(100,385,cuurentPx),new SquareArea.TouchPoint(290,460,cuurentPx)));
        mainCharactorPoint.setEyes(new SquareArea(new SquareArea.TouchPoint(135,220,cuurentPx),new SquareArea.TouchPoint(345,260,cuurentPx)));
        mainCharactorPoint.setRightEar(new SquareArea(new SquareArea.TouchPoint(53,100,cuurentPx),new SquareArea.TouchPoint(135,220,cuurentPx)));
        mainCharactorPoint.setLeftFrontLeg(new SquareArea(new SquareArea.TouchPoint(30,460,cuurentPx),new SquareArea.TouchPoint(260,660,cuurentPx)));
        mainCharactorPoint.setLeftRearLeg(new SquareArea(new SquareArea.TouchPoint(525,425,cuurentPx),new SquareArea.TouchPoint(715,485,cuurentPx)));

        mainCharactorPoint.setMouth(new SquareArea(new SquareArea.TouchPoint(100,260,cuurentPx),new SquareArea.TouchPoint(330,385,cuurentPx)));
        mainCharactorPoint.setLeftEar(new SquareArea(new SquareArea.TouchPoint(255,75,cuurentPx),new SquareArea.TouchPoint(330,186,cuurentPx)));
        mainCharactorPoint.setRightFrontLeg(new SquareArea(new SquareArea.TouchPoint(30,460,cuurentPx),new SquareArea.TouchPoint(260,660,cuurentPx)));
        mainCharactorPoint.setRightRearLeg(new SquareArea(new SquareArea.TouchPoint(525,425,cuurentPx),new SquareArea.TouchPoint(715,485,cuurentPx)));
        mainCharactorPoint.setTail(new SquareArea(new SquareArea.TouchPoint(570,239,cuurentPx),new SquareArea.TouchPoint(730,335,cuurentPx)));
        mainCharactorPoint.setTopHead(new SquareArea(new SquareArea.TouchPoint(135,145,cuurentPx),new SquareArea.TouchPoint(245,220,cuurentPx)));
        mainCharactorPoint.setStomach(new SquareArea(new SquareArea.TouchPoint(300,425,cuurentPx),new SquareArea.TouchPoint(515,451,cuurentPx)));




        return fGame;
    }
    
}