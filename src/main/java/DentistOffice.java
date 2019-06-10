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

    static void start() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                random = new Random();
                // CREAMOS LA WAITING ROOM
                final Integer NUMBER_CHAIRS = 7;
                WaitingRoom waitingRoom = new WaitingRoom(NUMBER_CHAIRS);
                Dentist dent = new Dentist(waitingRoom);
                waitingRoom.setDentista(dent);

                ExecutorService es = Executors.newCachedThreadPool();
                es.execute(dent);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (Patient.numeroPacientes <= 100) {
                    es.execute(new Patient(waitingRoom));
                    try {
                        Thread.sleep(random.nextInt(1300) + 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        hilo.start();
    }
}
