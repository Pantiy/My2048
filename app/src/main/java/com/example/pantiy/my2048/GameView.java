package com.example.pantiy.my2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantiy on 2016/9/11.
 */
public class GameView extends GridLayout{

    private static Card[][] cardMap = new Card[4][4];
    private static List<Point> emptyPoint = new ArrayList<>();
    private static boolean gameSuccess = false;
    private static boolean gameFailed = true;
    private static boolean gameStillRun = false;


    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    private void initGameView(){

        setColumnCount(4);
        setBackgroundColor(0xffbbada0);

        setOnTouchListener(new View.OnTouchListener(){

            private float startX,startY,endX,endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        performClick();
                        endX = event.getX() - startX;
                        endY = event.getY() - startY;
                        if (Math.abs(endX) > Math.abs(endY))
                        {
                            if (endX < -5)
                                goLeft();
                            else if (endX > 5)
                                goRight();
                        }
                        else if(Math.abs(endX) < Math.abs(endY))
                        {
                            if(endY > 5)
                                goDown();
                            else if(endY < -5)
                                goUp();
                        }
                        break;
                }

                return true;
            }
        });

        int cardWidth = getCardWidth();
        addCard(cardWidth, cardWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startGame();
    }

    protected static void startGame(){

        for (int y = 0;y < 4;y++)
            for (int x = 0;x < 4;x++)
                cardMap[x][y].setNum(0);

        MainActivity.getMainActivity().clearScore();
        gameSuccess = false;
        gameFailed = true;
        gameStillRun = false;

        addRandomNum();
        addRandomNum();
    }

    private void addCard(int cardWidth,int cardHeight) {

        Card c;

        for (int y = 0;y < 4;y++)
            for (int x = 0;x < 4;x++)
            {
                c = new Card(getContext());
                c.setNum(0);
                cardMap[x][y] = c;
                addView(c, cardWidth, cardHeight);
            }
    }

    private static void addRandomNum(){

        emptyPoint.clear();

        for (int y = 0;y < 4;y++)
            for (int x = 0;x < 4;x++)
                if(cardMap[x][y].getNum() <= 0)
                    emptyPoint.add(new Point(x,y));

        Point p = emptyPoint.remove((int)(Math.random()*emptyPoint.size()));
        if (MainActivity.getMainActivity().getEasyGame())
            cardMap[p.x][p.y].setNum((Math.random()>0.1)?8:16);
        else
            cardMap[p.x][p.y].setNum((Math.random()>0.1)?2:4);
    }

    private void checkGameOver(){

        gameFailed = true;

        All:
        for (int y = 0;y < 4;y++)
            for (int x = 0;x < 4;x++)
            {
                if (cardMap[x][y].getNum() == 2048 && !gameStillRun)
                {
                    gameSuccess = true;
                    break All;
                }

                if (gameFailed && (cardMap[x][y].getNum() == 0 ||
                        (x > 0 && cardMap[x][y].equals(cardMap[x-1][y]))||
                        (x < 3 && cardMap[x][y].equals(cardMap[x+1][y]))||
                        (y > 0 && cardMap[x][y].equals(cardMap[x][y-1]))||
                        (y < 3 && cardMap[x][y].equals(cardMap[x][y+1]))))
                    gameFailed = false;
            }

        if (gameFailed)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

            dialog.setTitle(R.string.dialog_title);
            dialog.setMessage(R.string.gameFailed_dialog_message);
            dialog.setPositiveButton(R.string.gameFailed_dialog_pButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                    Toast.makeText(getContext(),R.string.game_begin,Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setNegativeButton(R.string.gameFailed_dialog_nButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(),R.string.toast_gameFailed,Toast.LENGTH_SHORT).show();
                }
            });

            dialog.create().show();
        }

        if (gameSuccess)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

            dialog.setTitle(R.string.dialog_title);
            dialog.setMessage(R.string.gameSuccess_dialog_message);
            dialog.setPositiveButton(R.string.gameSuccess_dialog_pButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                    Toast.makeText(getContext(),R.string.game_begin,Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setNegativeButton(R.string.gameSuccess_dialog_nButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gameStillRun = true;
                    gameSuccess = false;
                    Toast.makeText(getContext(),R.string.toast_gameSuccess,Toast.LENGTH_SHORT).show();
                }
            });

            dialog.create().show();
        }

    }

    private void goLeft(){

        boolean addNewCard = false;

        for (int y = 0;y < 4;y++)
            for (int x = 0;x < 4;x++)
                for (int x1 = x + 1;x1 < 4;x1++)
                {
                    if (cardMap[x1][y].getNum() > 0)
                    {
                        if (cardMap[x][y].getNum() <= 0)
                        {
                            cardMap[x][y].setNum(cardMap[x1][y].getNum());
                            cardMap[x1][y].setNum(0);
                            x--;
                            addNewCard = true;
                        }
                        else if (cardMap[x][y].equals(cardMap[x1][y]))
                        {
                            cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
                            cardMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            addNewCard = true;
                        }
                        break;
                    }
                }

        if(addNewCard)
        {
            addRandomNum();
            checkGameOver();
        }
    }

    private void goRight(){

        boolean addNewCard = false;

        for (int y = 0;y < 4;y++)
            for (int x = 3;x >= 0;x--)
                for (int x1 = x - 1;x1 >= 0;x1--)
                {
                    if (cardMap[x1][y].getNum() > 0)
                    {
                        if (cardMap[x][y].getNum() <= 0)
                        {
                            cardMap[x][y].setNum(cardMap[x1][y].getNum());
                            cardMap[x1][y].setNum(0);
                            x++;
                            addNewCard = true;
                        }
                        else if (cardMap[x][y].equals(cardMap[x1][y]))
                        {
                            cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
                            cardMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            addNewCard = true;
                        }
                        break;
                    }
                }

        if(addNewCard)
        {
            addRandomNum();
            checkGameOver();
        }
    }

    private void goUp(){

        boolean addNewCard = false;

        for (int x = 0;x < 4;x++)
            for (int y = 0;y < 4;y++)
                for (int y1 = y + 1;y1 < 4;y1++)
                {
                    if (cardMap[x][y1].getNum() > 0)
                    {
                        if (cardMap[x][y].getNum() <= 0)
                        {
                            cardMap[x][y].setNum(cardMap[x][y1].getNum());
                            cardMap[x][y1].setNum(0);
                            y--;
                            addNewCard = true;
                        }
                        else if (cardMap[x][y].equals(cardMap[x][y1]))
                        {
                            cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
                            cardMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            addNewCard = true;
                        }
                        break;
                    }
                }

        if(addNewCard)
        {
            addRandomNum();
            checkGameOver();
        }
    }

    private void goDown(){

        boolean addNewCard = false;

        for (int x = 0;x < 4;x++)
            for (int y = 3;y >= 0;y--)
                for (int y1 = y - 1;y1 >= 0;y1--)
                {
                    if (cardMap[x][y1].getNum() > 0)
                    {
                        if (cardMap[x][y].getNum() <= 0)
                        {
                            cardMap[x][y].setNum(cardMap[x][y1].getNum());
                            cardMap[x][y1].setNum(0);
                            y++;
                            addNewCard = true;
                        }
                        else if (cardMap[x][y].equals(cardMap[x][y1]))
                        {
                            cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
                            cardMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            addNewCard = true;
                        }
                        break;
                    }
                }

        if(addNewCard)
        {
            addRandomNum();
            checkGameOver();
        }
    }

    private int getCardWidth() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (displayMetrics.widthPixels - 10) / 4;
    }

}
