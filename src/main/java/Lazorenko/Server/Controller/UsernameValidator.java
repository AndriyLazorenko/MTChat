package Lazorenko.Server.Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lazorenko on 07.07.2015.
 */
public class UsernameValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]{3,15}$";

    public UsernameValidator(){
        pattern = Pattern.compile(USERNAME_PATTERN);
    }

    public boolean validate(String username){

        matcher = pattern.matcher(username);
        return matcher.matches();

    }
}
