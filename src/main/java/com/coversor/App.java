package com.coversor;

public class App {
    public static void main(String[] args) {
        FrontEnd frontEnd = new FrontEnd();
        BackEnd backEnd = new BackEnd();

        frontEnd.setBackEnd(backEnd);
        backEnd.setFrontEnd(frontEnd);
    }
}