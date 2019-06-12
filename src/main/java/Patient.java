/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Dentista
 *
 * Esta clase la usaremos para representar un paciente en nuestro programa
 */
public class Patient implements Runnable {

    // Atributos privados
    private WaitingRoom waitingRoom;
    private int id;
    // Atributos package
    static int numeroPacientes = 1;
    private boolean tocaTurno = false;
    boolean operado = false;

    /**
     * Este Constructor nos permite instanciar un paciente
     *
     * @param wr Este parametro representara la sala de espera de
     *           dicho dentista
     */
    Patient(WaitingRoom wr) {
        this.waitingRoom = wr;
        this.id = numeroPacientes++;
    }


    /**
     * Este metodo es el que implementamos de la interface Runnable,
     * nos permite definir que sera el codigo que tendra que
     * ejecutar nuestro Thread
     */
    @Override
    public void run() {
        try {
            // Probamos si nos podemos sentar
            if (waitingRoom.seat(this)) {
                // El proceso se ha podido sentar
                synchronized (this) {
                    // Esperamos hasta que nos toque el turno
                    while (!this.tocaTurno) this.wait();
                }
                System.out.println("Paciente: " + this.id + " le toca turno de operar");
                synchronized (this) {
                    // Esperamos hasta que hayamos sido operados
                    while (!operado) this.wait();
                }
                System.out.println("Paciente: " + this.id + " -- Muchas gracias !! Chao ! ;D");

            } else {
                // El proceso no se ha podido sentar
                System.out.println("Paciente: " + this.id + " no se ha podido sentar y se va a su casa");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Este metodo es el getter de Id
     *
     * @return retorna el id de dicho paciente
     */
    public int getId() {
        return this.id;
    }

    /**
     * Este metodo es el setter de TocaTurno
     *
     * @param tocaTurno recibimos un booleano que representara
     *                  si toca el turno a un paciente o no
     */
    public void setTocaTurno(boolean tocaTurno) {
        this.tocaTurno = tocaTurno;
    }
}
