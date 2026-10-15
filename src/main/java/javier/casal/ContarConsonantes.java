package javier.casal;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ContarConsonantes {

    // Contador total de apariciones
    static int total = 0;

    // Clase de hilo encargado de contar una consonante
    static class Contador extends Thread {

        // Texto en el que se buscan las consonantes
        private String texto;

        // Consonante que hay que encontrar
        private char consonante;

        // Numero de veces que aparece la consonante en el texto
        private int contador = 0;

        // Constructor
        public Contador(String texto, char consonante) {
            this.texto = texto;
            this.consonante = consonante;
        }

        // Metodo que inicia el hilo en start()
        @Override
        public void run() {

            // Recorre todos los caracteres del texto
            for (int i = 0; i < texto.length(); i++) {

                // Compara los caracteres en minusculas para que no importe si son mayusculas o minusculas
                if (Character.toLowerCase(texto.charAt(i)) == Character.toLowerCase(consonante)) {

                    // Aumenta el contador si encuentra la consonante
                    contador++;
                }
            }

            // Sincroniza el acceso para evitar problemas al contador total
            synchronized (ContarConsonantes.class) {

                // Suma las veces que aparece la consonante al contador total
                total += contador;
            }

            // Muestra la cantidad de veces que aparece la consonante
            System.out.println("Hilo " + consonante + ": " + contador + " apariciones.");
        }
    }

    // Metodo principal del programa
    // Declara las excepciones
    public static void main(String[] args)
            throws InterruptedException, FileNotFoundException {

        // Lee las consonantes introducidas por teclado
        Scanner teclado = new Scanner(System.in);

        // Lee el archivo que contiene el texto
        Scanner fichero = new Scanner(new File("src/main/java/javier/casal/Lorem.txt"));

        // Variable donde se almacena el contenido del archivo
        String texto = "";

        // Lee el archivo linea por linea hasta llegar al final
        while (fichero.hasNextLine()) {

            // Añade cada linea a la variable texto, conservando los saltos de línea
            texto += fichero.nextLine() + "\n";
        }

        // Cierra el Scanner del archivo
        fichero.close();

        // Repite el contenido del texto 2000 veces para aumentar el trabajo
        texto = texto.repeat(2000);

        // Crea un array para almacenar los cinco hilos
        Contador[] hilos = new Contador[5];

        // Crea un array para almacenar las cinco consonantes
        char[] consonantes = new char[5];

        // Pide las cinco consonantes al usuario y las guarda en el array consonantes
        for (int i = 0; i < 5; i++) {
            System.out.print("Introduce la consonante " + (i + 1) + ": ");

            String letra = teclado.next();

            // Comprueba que la letra introducida es una consonante válida
            if (letra.length() == 1 && Character.isLetter(letra.charAt(0)) && !"aeiouAEIOU".contains(letra)) {
                consonantes[i] = letra.charAt(0);

            } else {
                System.out.println("Por favor, introduce una consonante válida.");
                i--;
            }
        }

        // Linea divisora
        System.out.println();

        // Crea e inicia los cinco hilos
        for (int i = 0; i < 5; i++) {

            // Crea un hilo con el texto y la consonante correspondiente
            hilos[i] = new Contador(texto, consonantes[i]);

            // Inicia la ejecución del hilo
            hilos[i].start();
        }

        // Espera a que terminen los cinco hilos
        for (int i = 0; i < 5; i++) {

            // El hilo principal espera hasta que este hilo finalice
            hilos[i].join();
        }

        // Linea divisora
        System.out.println();

        // Muestra el total de apariciones sumadas por todos los hilos
        System.out.println("Total de apariciones: " + total);

        // Cierra el Scanner utilizado para leer por teclado
        teclado.close();
    }
}