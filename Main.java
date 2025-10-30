import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        String[] info = new String[10];
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String opcion = SCANNER.nextLine();

            switch (opcion) {
                case "1":
                    ejecutarHilos(info);
                    break;
                case "2":
                    mostrarContenido(info);
                    break;
                case "3":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }

        System.out.println("Programa finalizado.");
        SCANNER.close();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("MENÚ PRINCIPAL");
        System.out.println("1. Ejecutar hilos");
        System.out.println("2. Mostrar contenido");
        System.out.println("3. Salir");
        System.out.print("Selecciona una opción: ");
    }

    private static void ejecutarHilos(String[] info) {
        Arrays.fill(info, null);

        Posicion posicion = new Posicion();

        HiloEscribe h1 = new HiloEscribe(posicion);
        HiloEscribe h2 = new HiloEscribe(posicion);

        h1.setInfo(info);
        h1.setIni(0);
        h1.setTmp(500);

        h2.setInfo(info);
        h2.setIni(5);
        h2.setTmp(1000);

        h1.start();
        h2.start();

        try {
            h1.join();
            h2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("INTERRUPCIÓN EN MAIN");
        }

        System.out.println("Hilos finalizados.");
    }

    private static void mostrarContenido(String[] info) {
        System.out.println("CONTENIDO DEL ARRAY");
        for (int i = 0; i < info.length; i++) {
            System.out.println(i + ": " + info[i]);
        }
    }
}