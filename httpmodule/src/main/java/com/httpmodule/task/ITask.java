package com.httpmodule.task;


/**
 * Created by apple on 16/6/30.
 */

public interface ITask {
    enum State{
        STOP,
        RUNNING,
        PAUSE,
    }
    public void start();

    public void stop();

    public void cancle();

    public void pause();

    public Object getTag();
}
