import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {


    private static final int TAMANIO_ARREGLO = 50;
    private static String[] info = new String[TAMANIO_ARREGLO];
    private static Posicion posicion = new Posicion();
    private static ArrayList<HiloEscribe> hilosCreados = new ArrayList<>();
    private static int contadorInicios = 0;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        System.out.println("Bienvenido al Gestor de Hilos.");

        while (!salir) {
            mostrarMenu();
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcion) {
                    case 1:
                        crearYArrancarHilo();
                        break;
                    case 2:
                        pausarHilo(scanner); 
                        break;
                    case 3:
                        continuarHilo(scanner); 
                        break;
                    case 4:
                        terminarHilo(scanner);
                        break;
                    case 5:
                        mostrarArreglo();
                        break;
                    case 6:
                        mostrarEstadoHilos();
                        break;
                    case 7:
                        salir = true;
                        terminarTodosLosHilos();
                        System.out.println("Saliendo del programa. Adiós.");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                }
            } 
            catch (InputMismatchException e) {
                System.out.println("Error: Debes ingresar un número entero.");
                scanner.nextLine(); 
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error inesperado en el menú: " + e.getMessage());
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Crear y arrancar hilo");
        System.out.println("2. Pausar hilo (por ID)");
        System.out.println("3. Continuar hilo (por ID)");
        System.out.println("4. Terminar hilo (por ID)");
        System.out.println("5. Mostrar arreglo");
        System.out.println("6. Mostrar estado de todos los hilos");
        System.out.println("7. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void crearYArrancarHilo() {
        if ((contadorInicios + 5) > TAMANIO_ARREGLO) {
            System.out.println("Error: No se pueden crear más hilos, el arreglo está lleno.");
            return;
        }

        try {
            HiloEscribe nuevoHilo = new HiloEscribe(posicion);
            nuevoHilo.setInfo(info); 
            nuevoHilo.setIni(contadorInicios); 
            nuevoHilo.setTmp(1000 + (int)(Math.random() * 1500)); 
            
            hilosCreados.add(nuevoHilo);
            nuevoHilo.start(); 
            
            System.out.println("Hilo " + nuevoHilo.getName() + " (ID: " + nuevoHilo.getId() + ") creado y arrancado.");
            
            contadorInicios += 5;

        } catch (Exception e) {
            System.out.println("Error al intentar crear el hilo: " + e.getMessage());
        }
    }

    private static HiloEscribe encontrarHiloPorId(long id) {
        for (HiloEscribe hilo : hilosCreados) {
            if (hilo.getId() == id) {
                return hilo;
            }
        }
        return null;
    }

    private static void pausarHilo(Scanner scanner) {
        System.out.print("Ingresa el ID del hilo a PAUSAR: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            HiloEscribe hilo = encontrarHiloPorId(id);

            if (hilo != null && hilo.isAlive()) {
                hilo.pausar();
                System.out.println("Hilo " + id + " pausado de forma segura.");
                System.out.println("Estado actual: " + hilo.getState());
            } else {
                System.out.println("Hilo no encontrado o ya ha terminado.");
            }
        } catch (InputMismatchException e) {
            System.out.println("ID no válido. Debe ser un número.");
            scanner.nextLine();
        }
    }

    // --- MÉTODO MODIFICADO ---
    private static void continuarHilo(Scanner scanner) {
        System.out.print("Ingresa el ID del hilo a CONTINUAR: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            HiloEscribe hilo = encontrarHiloPorId(id);

            if (hilo != null) {
                // Se usa el nuevo método seguro
                hilo.reanudar();
                System.out.println("Hilo " + id + " reanudado.");
                System.out.println("Estado actual: " + hilo.getState());
            } else {
                System.out.println("Hilo no encontrado.");
            }
        } catch (InputMismatchException e) {
            System.out.println("ID no válido. Debe ser un número.");
            scanner.nextLine();
        }
    }

    private static void terminarHilo(Scanner scanner) {
        System.out.print("Ingresa el ID del hilo a TERMINAR (interrumpir): ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            HiloEscribe hilo = encontrarHiloPorId(id);

            if (hilo != null && hilo.isAlive()) {
                hilo.interrupt();
                System.out.println("Señal de interrupción enviada al hilo " + id + ".");
            } else {
                System.out.println("Hilo no encontrado o ya ha terminado.");
            }
        } catch (InputMismatchException e) {
            System.out.println("ID no válido. Debe ser un número.");
            scanner.nextLine();
        }
    }

    private static void mostrarArreglo() {
        System.out.println("--- Contenido del Arreglo (Posición actual: " + posicion.getP() + ") ---");
        int limite = Math.min(posicion.getP(), TAMANIO_ARREGLO); 
        
        if (limite == 0) {
            System.out.println("[Arreglo vacío]");
            return;
        }

        for (int i = 0; i < limite; i++) {
            System.out.println("Índice [" + i + "]: " + info[i]);
        }
        
        if (posicion.getP() >= TAMANIO_ARREGLO) {
             System.out.println("... (El arreglo ha llegado a su capacidad máxima) ...");
        }
    }

    private static void mostrarEstadoHilos() {
        System.out.println("--- Estado Actual de los Hilos ---");
        if (hilosCreados.isEmpty()) {
            System.out.println("No hay hilos creados.");
            return;
        }
        
        for (HiloEscribe hilo : hilosCreados) {
            Thread.State estado = hilo.getState();
            
            System.out.println("Hilo: " + hilo.getName() + " (ID: " + hilo.getId() + ")");
            System.out.println("  Estado: " + estado); 
            System.out.println("  ¿Está vivo?: " + (hilo.isAlive() ? "Sí" : "No"));
        }
    }
    
    private static void terminarTodosLosHilos() {
        System.out.println("Intentando terminar todos los hilos activos...");
        for (HiloEscribe hilo : hilosCreados) {
            if (hilo.isAlive()) {
                hilo.interrupt(); 
            }
        }
    }
}
