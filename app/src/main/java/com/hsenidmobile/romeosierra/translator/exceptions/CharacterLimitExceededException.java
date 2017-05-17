package com.hsenidmobile.romeosierra.translator.exceptions;

/**
 * Created by kanchana on 5/17/17.
 */

public class CharacterLimitExceededException extends Exception{
    public CharacterLimitExceededException(String message) {
        super(message);
    }
}
