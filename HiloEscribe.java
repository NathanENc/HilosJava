public class HiloEscribe extends Thread{
    private String info[];
    private int ini;
    private int tmp;
    private Posicion p;

    public HiloEscribe(Posicion x){
        this.p=x;
    }
    
    public void setTmp(int tmp) {
        this.tmp = tmp;
    }
    @Override
    public void run(){
        for(int i=ini;i<ini+5;i++)
        {
           try{
                synchronized(p){
                    info[p.getP()]=getName()+i+"-"+p.getP();
                    p.incrementa();
                }
                sleep(tmp);
           }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println("NO HAY ESPACIO");
                i=ini+5;
            }
           catch(InterruptedException e){
                System.out.println("INTERRUPCIÓN");
            }
        }
    }
    
    public void setIni(int x){
        ini=x;
    }
    
    public void setInfo(String x[]){
        info=x;
    }
    
}