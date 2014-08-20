package com.example.prakharsriv.colorphun;

import android.app.AlertDialog;
import android.content.Context;

public class GameOverPopup extends AlertDialog {
    protected GameOverPopup(Context context) {
        super(context);

        this.setContentView(R.layout.game_over_dialog);
    }
}
