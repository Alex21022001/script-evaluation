package com.alexsitiy.script.evaluation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.springframework.stereotype.Component;

@Component
public class ScriptValidator implements ConstraintValidator<CheckScript, String> {

    private final Context context = Context.newBuilder()
            .engine(Engine.newBuilder()
                    .option("engine.WarnInterpreterOnly", "false")
                    .build())
            .build();

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            context.parse("js", s);
            return true;
        } catch (PolyglotException e) {
            return false;
        }
    }
}
