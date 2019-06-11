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
    static Random random = null;
    static int tiempoEntrePacientes = 1300;
    static int tiempoOperaciones = 8000;
    static int NUMBER_CHAIRS = 7;
    static WaitingRoom waitingRoom;
    static void start() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                random = new Random();
                // CREAMOS LA WAITING ROOM
                waitingRoom = new WaitingRoom(NUMBER_CHAIRS);
                Dentist dent = new Dentist(waitingRoom);
                waitingRoom.setDentista(dent);

                ExecutorService es = Executors.newCachedThreadPool();
                es.execute(dent);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
