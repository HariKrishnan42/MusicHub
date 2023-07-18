package com.example.musichub.Models;

import com.example.musichub.Interfaces.CheckProgress;
import com.example.musichub.Services.MyService;

public class ProgressThread implements Runnable {

    private CheckProgress checkProgress;

    public ProgressThread(MyService myService) {
        this.checkProgress = myService;
    }

    @Override
    public void run() {
        checkProgress.onProgress(PlayerController.player.getCurrentPosition());
    }
}
