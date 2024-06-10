package com.conversor;

/**
 * Classe principal do programa
 * @author Grupo four-people-una
 * @version 1.0
 */
public class App {

    /**
     * Método construtor da classe App
     * @param args recebe os argumentos passados pela linha de comando
     * @author Grupo four-people-una
     * @version 1.0
     */
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
