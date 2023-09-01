package com.alexsitiy.script.evaluation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.springframework.stereotype.Component;

/**
 * This class is a validator for JavaScript code that implements {@link ConstraintValidator}
 * and works alongside {@link CheckScript}.
 * Utilizes {@link Context} for parsing a given String.
 */
@Component
public class ScriptValidator implements ConstraintValidator<CheckScript, String> {

    private final Context context = Context.newBuilder()
            .engine(Engine.newBuilder()
                    .option("engine.WarnInterpreterOnly", "false")
                    .build())
            .build();

    /**
     * Parses a give string as a JavaScript code to verify that it doesn't have
     * any syntax errors.
     *
     * @see Context
     */
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
