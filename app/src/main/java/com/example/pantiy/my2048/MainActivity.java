package com.example.pantiy.my2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mShowScore;
    private Button mLevelButton;
    private Button mRestartButton;
    private int mScore = 0;
    private static MainActivity mainActivity = null;
    private static boolean easyGame = false;

    public MainActivity(){
        mainActivity = this;
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShowScore = (TextView)findViewById(R.id.show_score);
        mLevelButton = (Button)findViewById(R.id.level_button);
        mRestartButton = (Button)findViewById(R.id.restart_button);

        mLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.getMainActivity());

                dialog.setTitle(R.string.dialog_title);
                dialog.setMessage(R.string.changeLevel_dialog_message);
                dialog.setPositiveButton(R.string.normalLevel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!easyGame)
                        {
                            Toast.makeText(MainActivity.getMainActivity(),R.string.notEasyGame,Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            easyGame = false;
                            GameView.startGame();
                            Toast.makeText(MainActivity.getMainActivity(),R.string.levelChange_text,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.setNegativeButton(R.string.easyLevel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (easyGame)
                        {
                            Toast.makeText(MainActivity.getMainActivity(),R.string.isEasyGame,Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            easyGame = true;
                            GameView.startGame();
                            Toast.makeText(MainActivity.getMainActivity(),R.string.levelChange_text,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.create().show();
            }
        });

        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.getMainActivity());

                dialog.setTitle(R.string.dialog_title);
                dialog.setMessage(R.string.restart_dialog_message);
                dialog.setPositiveButton(R.string.restart_dialog_pButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameView.startGame();
                        Toast.makeText(MainActivity.getMainActivity(),R.string.game_begin,Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton(R.string.restart_dialog_nButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.create().show();
            }
        });
    }

    public void clearScore(){
        mScore = 0;
        showScore();
    }

    public void addScore(int num){
        mScore += num;
        showScore();
    }

    public void showScore(){
        mShowScore.setText(mScore + "");
    }

    public boolean getEasyGame(){
        return easyGame;
    }
}
