package fit5046.test.touchme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class FQuiz extends Fragment {
    private View fQuiz;
    private ImageButton ibAnswer1;
    private ImageButton ibAnswer2;
    private ImageButton ibAnswer3;
    private ImageButton ibAnswer4;


    private ImageView ivAnswer1;
    private ImageView ivAnswer2;
    private ImageView ivAnswer3;
    private ImageView ivAnswer4;
    private ImageView ivQuestion;
    private ImageView ivTeller;
    private TextView tvQuestion;

    private Animation animMoveRight;
    private Animation animFadeIn;
    private Animation animSlideDown;
    private GlobalClass globalClass;
    private ArrayList<QuizData> OfflineQuiz = new ArrayList<QuizData>();
    private int indexQuiz = 0;
    private boolean isCorrect = false;

    private int correct = 0;
    private ImageButton ib_option;

    private MyDialogBuilder alertDialogBuilder;
    private AlertDialog alertDialog;

    Timer timerCloseEmotion = new Timer("timer",false);
    TimerTask tickCloseEmotion = new TimerTask() {
        @Override
        public void run() {
            handlerCloseEmotion.sendEmptyMessage(0);
        }
    };
    final Handler handlerCloseEmotion = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (alertDialog.isShowing())
                alertDialog.dismiss();
            return true;
        }
    });
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(fQuiz == null)
            fQuiz = inflater.inflate(R.layout.fragment_quiz, container, false);
        globalClass = (GlobalClass)getContext().getApplicationContext();
        ibAnswer1 = (ImageButton)fQuiz.findViewById(R.id.ib_answer1);
        ibAnswer2 = (ImageButton)fQuiz.findViewById(R.id.ib_answer2);
        ibAnswer3 = (ImageButton)fQuiz.findViewById(R.id.ib_answer3);
        ibAnswer4 = (ImageButton)fQuiz.findViewById(R.id.ib_answer4);

        ivAnswer1 = (ImageView)fQuiz.findViewById(R.id.iv_answer1);
        ivAnswer2 = (ImageView)fQuiz.findViewById(R.id.iv_answer2);
        ivAnswer3 = (ImageView)fQuiz.findViewById(R.id.iv_answer3);
        ivAnswer4 = (ImageView)fQuiz.findViewById(R.id.iv_answer4);

        ivQuestion = (ImageView)fQuiz.findViewById(R.id.iv_conversation_bubble);
        ivTeller = (ImageView)fQuiz.findViewById(R.id.iv_teller);



        tvQuestion = (TextView)fQuiz.findViewById(R.id.tv_quiz);
        final FragmentManager fragmentManager = getFragmentManager();
        ib_option = (ImageButton)fQuiz.findViewById(R.id.ib_option);
        ib_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalClass.currentFragment = new FWelcome();
                fragmentManager.beginTransaction().replace(R.id.fl_main, globalClass.currentFragment).commit();
            }
        });

        animMoveRight = AnimationUtils.loadAnimation(container.getContext(), R.anim.move_right);
        animFadeIn = AnimationUtils.loadAnimation(container.getContext(), R.anim.fade_in);
        animSlideDown = AnimationUtils.loadAnimation(container.getContext(), R.anim.slide_down);

        ibAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllCheck();
                ivAnswer1.setVisibility(View.VISIBLE);
                if (correct == 0 || correct ==-1)
                    showResponse(true);
                else
                    showResponse(false);

            }
        });

        ibAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllCheck();
                ivAnswer2.setVisibility(View.VISIBLE);
                if (correct == 1 || correct ==-1)
                    showResponse(true);
                else
                    showResponse(false);
            }
        });
        ibAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllCheck();
                ivAnswer3.setVisibility(View.VISIBLE);
                if (correct == 2 || correct ==-1)
                    showResponse(true);
                else
                    showResponse(false);
            }
        });

        ibAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllCheck();
                ivAnswer4.setVisibility(View.VISIBLE);
                if (correct == 3 || correct ==-1)
                    showResponse(true);
                else
                    showResponse(false);
            }
        });

        hideAllCheck();
        hideAllOption();
        ivTeller.startAnimation(animMoveRight);
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
                        QuizData temp = OfflineQuiz.get(0);
                        startNewQuestion(temp.getQuestion(),temp.options);
                        indexQuiz=1;
                        correct = temp.correctIndex;
                    }
                });
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        QuizData temp1 = new QuizData();
        temp1.setQuestion("What breeds of dogs have you recently seen the most?");
        ArrayList<Integer> dogBreads = new ArrayList();
        dogBreads.add(R.drawable.staffy);
        dogBreads.add(R.drawable.malteseshihtzu);
        dogBreads.add(R.drawable.labrador);
        dogBreads.add(R.drawable.jackrussel);
        dogBreads.add(R.drawable.deerhound);
        dogBreads.add(R.drawable.bullmastiff);
        dogBreads.add(R.drawable.briard);
        dogBreads.add(R.drawable.bassetthound);
        Collections.shuffle(dogBreads);
        ArrayList<Integer> aa = new ArrayList<>();
        for (int i = 0; i< 4;i++){
            aa.add(dogBreads.get(i));
        }
        temp1.setOptions(aa);
        //Random random = new Random();
        temp1.setCorrectIndex(-1);
        OfflineQuiz.add(temp1);

        QuizData temp2 = new QuizData();
        temp2.setQuestion("which dog do you least see most?");
        Collections.shuffle(dogBreads);
        ArrayList<Integer> bb = new ArrayList<>();
        for (int i = 0; i< 4;i++){
            bb.add(dogBreads.get(i));
        }
        temp2.setOptions(bb);
        temp2.setCorrectIndex(-1);
        OfflineQuiz.add(temp2);

        QuizData temp3 = new QuizData();
        temp3.setQuestion("Which dog look HAPPY?");
        ArrayList<Integer> cc = new ArrayList<>();

        cc.add(R.drawable.angry_th);
        cc.add(R.drawable.cute_th);
        cc.add(R.drawable.happy_th);
        cc.add(R.drawable.mad_th);
        temp3.setOptions(cc);
        temp3.setCorrectIndex(2);
        OfflineQuiz.add(temp3);


        QuizData temp4 = new QuizData();
        temp4.setQuestion("Which dog looks MAD?");
        ArrayList<Integer> dd = new ArrayList<>();
        dd.add(R.drawable.mad_fo);
        dd.add(R.drawable.cute_fo);
        dd.add(R.drawable.happy_fo);

        dd.add(R.drawable.stare_fo);
        temp4.setCorrectIndex(0);
        temp4.setOptions(dd);
        OfflineQuiz.add(temp4);

        QuizData temp5 = new QuizData();
        temp5.setQuestion("What food CAN you feed to dogs");
        ArrayList<Integer> ee = new ArrayList<>();

        ee.add(R.drawable.alcohol_fi);
        ee.add(R.drawable.chocolate_fi);
        ee.add(R.drawable.onions_fi);
        ee.add(R.drawable.corn_fi);
        temp5.setCorrectIndex(3);
        temp5.setOptions(ee);
        OfflineQuiz.add(temp5);



        /*
        ArrayList<String> food = new ArrayList<>();
        food.add("CHOCOLATE");
        food.add("CANDY");
        food.add("AVOCADO");

        food.add("CARROT");
        food.add("WHITE RICE");
        food.add("MILK");

        Collections.shuffle(food);
        QuizData temp6 = new QuizData();
        temp6.setQuestion("Can we feed dog with "+food.get(0));
        ArrayList<Integer> ff = new ArrayList<>();

        ff.add(R.drawable.no);
        ff.add(R.drawable.yes);

        temp6.setOptions(ff);
        if (food.get(0).equals("CARROT") || food.get(0).equals("WHITE RICE")||food.get(0).equals("MILK"))
            temp6.setCorrectIndex(2);
        else
            temp6.setCorrectIndex(1);
        OfflineQuiz.add(temp6);*/

        Collections.shuffle(OfflineQuiz);


        alertDialogBuilder = new MyDialogBuilder(getContext(),container,MyDialogBuilder.TYPE_YES);
        alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setYesOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(isCorrect){
                    if (indexQuiz<4){
                        QuizData temp = OfflineQuiz.get(indexQuiz);
                        startNewQuestion(temp.getQuestion(),temp.options);
                        indexQuiz+=1;
                        correct = temp.correctIndex;
                    } else {
                        globalClass.currentFragment = new FGame();
                        fragmentManager.beginTransaction().replace(R.id.fl_main, globalClass.currentFragment).commit();
                    }
                }
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        return fQuiz;
    }

    private void startNewQuestion(String question, ArrayList<Integer> imageUrl){
        isCorrect = false;
        tvQuestion.setText(question);
        tvQuestion.startAnimation(animFadeIn);
        hideAllCheck();
        hideAllOption();
        if(imageUrl.size() == 4){
            ibAnswer1.setVisibility(View.VISIBLE);
            ibAnswer2.setVisibility(View.VISIBLE);
            ibAnswer3.setVisibility(View.VISIBLE);
            ibAnswer4.setVisibility(View.VISIBLE);

            ibAnswer1.setImageResource(imageUrl.get(0));
            ibAnswer2.setImageResource(imageUrl.get(1));
            ibAnswer3.setImageResource(imageUrl.get(2));
            ibAnswer4.setImageResource(imageUrl.get(3));


            ibAnswer1.startAnimation(animSlideDown);
            ibAnswer2.startAnimation(animSlideDown);
            ibAnswer3.startAnimation(animSlideDown);
            ibAnswer4.startAnimation(animSlideDown);
        } else if(imageUrl.size() == 2){
            ibAnswer2.setVisibility(View.VISIBLE);
            ibAnswer3.setVisibility(View.VISIBLE);

            ibAnswer2.setImageResource(imageUrl.get(0));
            ibAnswer3.setImageResource(imageUrl.get(1));

            ibAnswer2.startAnimation(animSlideDown);
            ibAnswer3.startAnimation(animSlideDown);
        }
    }

    private void hideAllCheck(){
        ivAnswer1.setVisibility(View.GONE);
        ivAnswer2.setVisibility(View.GONE);
        ivAnswer3.setVisibility(View.GONE);
        ivAnswer4.setVisibility(View.GONE);
    }

    private void hideAllOption(){

        ibAnswer1.setVisibility(View.INVISIBLE);
        ibAnswer2.setVisibility(View.INVISIBLE);
        ibAnswer3.setVisibility(View.INVISIBLE);
        ibAnswer4.setVisibility(View.INVISIBLE);

        ibAnswer1.clearAnimation();
        ibAnswer2.clearAnimation();
        ibAnswer3.clearAnimation();
        ibAnswer4.clearAnimation();
    }

    private void showResponse(boolean isCorrect){
        timerCloseEmotion.cancel();
        tickCloseEmotion.cancel();
        timerCloseEmotion = new Timer("timer",false);
        tickCloseEmotion = new TimerTask() {
            @Override
            public void run() {
                handlerCloseEmotion.sendEmptyMessage(0);
            }
        };
        if (isCorrect){
            this.isCorrect = true;
            alertDialogBuilder.setImageTeller(R.drawable.answer_correct);
            alertDialogBuilder.setMessage(getString(R.string.answer_correct));
        }

        else{
            this.isCorrect = false;
            alertDialogBuilder.setImageTeller(R.drawable.answer_wrong);
            alertDialogBuilder.setMessage(getString(R.string.answer_wrong));
        }
        timerCloseEmotion.schedule(tickCloseEmotion,4000);
        alertDialog.show();
    }

}