package com.conversor;

public class App {
    public static void main(String[] args) {
        // Cria uma instância da classe FrontEnd
        FrontEnd frontEnd = new FrontEnd();
        // Cria uma instância da classe BackEnd
        BackEnd backEnd = new BackEnd();

        // Define a instância da classe BackEnd no FrontEnd
        frontEnd.setBackEnd(backEnd);
        // Define a instância da classe FrontEnd no BackEnd
        backEnd.setFrontEnd(frontEnd);
    }

}