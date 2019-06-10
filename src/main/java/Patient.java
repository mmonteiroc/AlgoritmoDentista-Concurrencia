import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Dentista
 */
public class Patient implements Runnable {
    private WaitingRoom waitingRoom;
    private int id;
    static int numeroPacientes = 1;
    private boolean tocaTurno = false;
    boolean operado = false;

    Patient(WaitingRoom wr) {
        this.waitingRoom = wr;
        this.id = numeroPacientes++;
    }


    @Override
    public void run() {
        try {
            if (waitingRoom.seat(this)) {
                // El proceso se ha sentado
                synchronized (this) {
                    while (!this.tocaTurno) this.wait();
                }
                System.out.println("Paciente: " + this.id + " le toca turno de operar");
                synchronized (this) {
                    while (!operado) this.wait();
                }
                System.out.println("Paciente: " + this.id + " -- Muchas gracias !! Chao ! ;D");

            } else {
                System.out.println("Paciente: " + this.id + " no se ha podido sentar y se va a su casa");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public int getId() {
        return this.id;
    }

    public void setTocaTurno(boolean tocaTurno) {
        this.tocaTurno = tocaTurno;
    }

}
