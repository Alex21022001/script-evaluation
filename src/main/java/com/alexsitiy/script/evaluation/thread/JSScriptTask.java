package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.model.JSScript;
import org.graalvm.polyglot.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

public class JSScriptTask implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(JSScriptTask.class);

    private final JSScript jsScript;
    
    private Context context;

    public JSScriptTask(JSScript jsScript) {
        this.jsScript = jsScript;
    }

    @Override
    public void run() {
        // TODO: 18.08.2023  
    }
}
