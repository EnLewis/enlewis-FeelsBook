package com.example.erikn.enlewis_feelsbook;


public class NoteTooLongException extends Exception {

    NoteTooLongException(){
        super("Your note was too long! Please keep it under 100 chars or it hurt my head.");
    }
}
