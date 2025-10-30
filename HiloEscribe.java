public class HiloEscribe extends Thread {
    private String info[];
    private int ini;
    private int tmp;
    private Posicion p;

    
    private volatile boolean pausado = false;

    private final Object candadoPausa = new Object();

    public HiloEscribe(Posicion x) {
        this.p = x;
    }

    public void setTmp(int tmp) {
        this.tmp = tmp;
    }

    @Override
    public void run() {
        for (int i = ini; i < ini + 5; i++) {
            try {
                

                synchronized (candadoPausa) {
                    while (pausado) {

                        candadoPausa.wait();
                    }
                }

                synchronized (p) {
                    info[p.getP()] = getName() + i + "-" + p.getP();
                    p.incrementa();
                }
                sleep(tmp); 

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("NO HAY ESPACIO");
                i = ini + 5;
            } catch (InterruptedException e) {
              
                System.out.println("Hilo " + getName() + " interrumpido y terminado.");
                i = ini + 5; 
            }
        }
        System.out.println("Hilo " + getName() + " ha finalizado su trabajo.");
    }

    public void setIni(int x) {
        ini = x;
    }

    public void setInfo(String x[]) {
        info = x;
    }

   
    public void pausar() {
        pausado = true;
    }

    public void reanudar() {
        pausado = false;
 
        synchronized (candadoPausa) {
            candadoPausa.notify();
        }
    }

    
}
