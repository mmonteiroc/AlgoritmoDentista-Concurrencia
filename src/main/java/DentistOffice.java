import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Dentista
 */
public class DentistOffice {
    // Atributos
    static Random random = null;
    static int tiempoEntrePacientes = 1300;
    static int tiempoOperaciones = 8000;
    static int NUMBER_CHAIRS = 7;
    static WaitingRoom waitingRoom;


    /**
     * Metodo que inicia nuestro programa
     */
    static void start() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                random = new Random();
                // CREAMOS LA WAITING ROOM Y EL DENTISTA
                waitingRoom = new WaitingRoom(NUMBER_CHAIRS);
                Dentist dent = new Dentist(waitingRoom);
                waitingRoom.setDentista(dent);

                ExecutorService es = Executors.newCachedThreadPool();
                // iniciamos el proceso dentista
                es.execute(dent);

                // Esperamos un random hasta tener nuestro primer paciente
                try {
                    Thread.sleep(random.nextInt(4000) + 300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Creamos pacietes (Hilos)
                while (Patient.numeroPacientes <= 10000) {
                    es.execute(new Patient(waitingRoom));
                    try {
                        Thread.sleep(random.nextInt(tiempoEntrePacientes) + 300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        hilo.start();
    }
}
