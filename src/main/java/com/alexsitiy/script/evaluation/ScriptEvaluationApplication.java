package com.alexsitiy.script.evaluation;

import com.alexsitiy.script.evaluation.model.CyclicOutputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ScriptEvaluationApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ScriptEvaluationApplication.class, args);
        test();
    }

    public static void test() throws IOException {
        CyclicOutputStream cyclicOutputStream = new CyclicOutputStream(10);

        for (int i = 0; i < 20; i++) {
            cyclicOutputStream.write(i);
        }

        System.out.println(cyclicOutputStream);
    }
}
