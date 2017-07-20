package com.example.puchoo.mapmaterial.Exceptions;

/**
 * Created by Agustin on 01/26/2017.
 */

public class FileSaverException extends Exception {
    public FileSaverException(String msg){
        super(msg);
    }
    public FileSaverException(String msg,String cause){
        super(msg);
    }
}
