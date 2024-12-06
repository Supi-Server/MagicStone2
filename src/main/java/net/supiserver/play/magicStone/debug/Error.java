package net.supiserver.play.magicStone.debug;

import java.util.ArrayList;
import java.util.List;

public class Error {
    private static List<String> errors = new ArrayList<>();

    public static boolean puts(String error){
        return errors.add(error);
    }

    public static void reset(){
        errors = new ArrayList<>();
    }

    public static List<String> get(){
        return errors;
    }
}
