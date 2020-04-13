package com.github.stanislawtokarski.codenames.service;

import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CountdownService {

    private final EventLog eventLog;

    private Timer timer = new Timer();
    private AtomicBoolean inProgress = new AtomicBoolean(false);
    //from 0.0 to 1.0
    private AtomicDouble progress = new AtomicDouble(0.0);
    private AtomicInteger period = new AtomicInteger(60);

    public CountdownService(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    public boolean getInProgress() {
        return inProgress.get();
    }

    public double getProgress() {
        return progress.get();
    }

    public int getPeriod() {
        return period.get();
    }

    public void setPeriod(int period) {
        this.period.set(period);
    }

    public void startCountdown(double period) {
        var step = 1.0 / period;
        inProgress.set(true);
        timer.schedule(new CountdownTask(step), 0, 500);
    }

    public void resetCountdown() {
        timer.cancel();
        eventLog.addEvent("The countdown has ended!");
        timer = new Timer();
        inProgress.set(false);
        progress.set(0);
    }

    private final class CountdownTask extends TimerTask {

        private final double step;

        private CountdownTask(double step) {
            this.step = step;
        }

        @Override
        public void run() {
            if (!(progress.get() < 1)) {
                resetCountdown();
            } else {
                progress.getAndAdd(0.5 * step);
            }
        }
    }
}
