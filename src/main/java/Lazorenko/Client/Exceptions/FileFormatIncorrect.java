package Lazorenko.Client.Exceptions;

/**
 * Created by Lazorenko on 08.07.2015.
 */
public class FileFormatIncorrect extends Exception {
    @Override
    public String getMessage() {
        return "This is not an image. Insert a filepath for file, which is an image "+super.getMessage();
    }
}
