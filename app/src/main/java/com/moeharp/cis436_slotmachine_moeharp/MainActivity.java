/*
* Author: Mohamed Harp
* Create Date: 2/2/18
* Course: CIS 436
* Last Modified: 2/4/18
* Instructor: JP Baugh
* Purpose: A simple android slot machine program that pics 3 random numbers and delivers payout
* based on outcome of numbers.
*/
package com.moeharp.cis436_slotmachine_moeharp;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener {

    // Widgets
    private TextView lblBank;
    private Button btnPull;
    private Button btnNewGame;
    private Button btnVerify;
    private EditText txtAmount;
    private EditText rand1;
    private EditText rand2;
    private EditText rand3;
    private TextView lblMessage; // static label
    private TextView lblMsg; // displays wins

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        // Sets app icon in the top acton bar and transitions when overview button is clicked (square button on bottom)
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setLogo(R.drawable.ic_launcher);
        getActionBar().setDisplayUseLogoEnabled(true);

        // connect to activity
        btnVerify = findViewById(R.id.btnSetValue);
        txtAmount = findViewById(R.id.txtAmount);
        lblBank = findViewById(R.id.lblBank);
        btnPull = findViewById(R.id.btnPull);
        rand1 = findViewById(R.id.txtRand1);
        rand2 = findViewById(R.id.txtRand2);
        rand3 = findViewById(R.id.txtRand3);
        btnNewGame = findViewById(R.id.btnNewGame);
        lblMessage = findViewById(R.id.lblMessage);
        lblMsg = findViewById(R.id.lblMsg);

        //onclick events
        btnVerify.setOnClickListener(this);
        btnPull.setOnClickListener(this);
        btnNewGame.setOnClickListener(this);

        //disable or hide objects
        btnPull.setEnabled(false);
        lblMessage.setVisibility(View.INVISIBLE);

        /*
        // Tried getting the app to accept the soft keyboard enter button... couldn't get it to work.
        txtAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE)
                {
                    btnVerify.performClick();
                    return true;
                }
                return false;
            }
        });*/
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnSetValue: // set amount
                String amnt = txtAmount.getText().toString();
                if(!amnt.matches("")) // checks to make sure amount isn't empty string
                {
                    int amount = Integer.parseInt(amnt);
                    if (amount >= 100 && amount <= 500)
                    {
                        // unlocks the random number gen and locks the set value steps
                        lblBank.setText(Integer.toString(amount));
                        txtAmount.getText().clear();
                        txtAmount.setEnabled(false);
                        btnVerify.setEnabled(false);
                        btnPull.setEnabled(true);
                        lblMessage.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        //Error 2
                        Toast.makeText(getApplicationContext(), "Error: Amount must be between $100 and $500", Toast.LENGTH_LONG).show();
                        txtAmount.getText().clear();
                        txtAmount.requestFocus();
                    }
                }
                else
                {
                    // Error 1
                    Toast.makeText(getApplicationContext(), "Error: No Starting Value Was Found.", Toast.LENGTH_LONG).show();
                    txtAmount.getText().clear();
                    txtAmount.requestFocus();
                }
                break;
            case R.id.btnPull: // generate 3 random numbers between 1 and 9, and processes win amounts
                int bank = Integer.parseInt(lblBank.getText().toString());
                if(bank > 0 && bank < 1000)
                {
                    bank -= 5;
                    int random1, random2, random3;

                    //generates 3 random numbers
                    random1 = new Random().nextInt(9) + 1; // 1 - 9
                    rand1.setText(Integer.toString(random1));
                    random2 = new Random().nextInt(9) + 1; // 1 - 9
                    rand2.setText(Integer.toString(random2));
                    random3 = new Random().nextInt(9) + 1; // 1 - 9
                    rand3.setText(Integer.toString(random3));

                    // Calculate wins and shows it in bank
                    bank = ProcessWins(random1, random2, random3, bank);
                    lblBank.setText(Integer.toString(bank));
                }
                else
                {
                    if(bank >= 1000)
                    {
                        Toast.makeText(getApplicationContext(), "You've Exceed $1,000! We've Cashed You Out! Game Reset.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Unfortunately You're Out Of Money. Game Reset.", Toast.LENGTH_LONG).show();
                    }
                    ResetGame();
                }
                break;
            case R.id.btnNewGame: // resets game
                Toast.makeText(getApplicationContext(), "Game Reset.", Toast.LENGTH_LONG).show();
                ResetGame();
                break;
        }
    }

    // Pre: 3 random numbers between 1 and 9, and a bank value between 0 and 1000
    // Post: a new bank value calculated based on random number matches
    private int ProcessWins(int random1, int random2, int random3, int bank)
    {
        String message;
        if(random1 == 9 && random2 == 9 && random3 == 9) // jackpot scenario
        {
            bank += 1000;
            Toast.makeText(getApplicationContext(), "You Cleared Out The Machine!", Toast.LENGTH_LONG).show();
            message = " Jackpot! $1,000 Added To The Bank!";
            btnPull.setText("Cash Out!".toString());
        }
        else if (random1 == random2 && random2 == random3) // 3 matches
        {
            if(random1 >= 5) // 5 - 8 matches
            {
                bank += 100;
                message = " Nice! $100 Added To The Bank!";
            }
            else // 1 - 4 matches
            {
                bank += 40;
                message = " Cool! $40 Added To The Bank!";
            }
        }
        else if (random1 == random2 || random1 == random3 || random2 == random3) // 2 matches
        {
            bank += 10;
            message = " Sweet! $10 Added To The Bank!";
        }
        else // no wins / 3 unique numbers
        {
            message = " Good Try, But You Didn't Win Anything!";
        }
        lblMsg.setText(message); // displays wins to user
        return bank;
    }

    // Pre: Form must be filled with other data
    // Post: resets everything to default
    private void ResetGame()
    {
        rand1.getText().clear();
        rand2.getText().clear();
        rand3.getText().clear();
        btnPull.setEnabled(false);
        btnVerify.setEnabled(true);
        lblBank.setText(R.string.lblBank);
        txtAmount.setEnabled(true);
        btnPull.setText(R.string.btnPull);
        lblMessage.setVisibility(View.INVISIBLE);
        lblMsg.setText(R.string.lblMsg);
    }
}