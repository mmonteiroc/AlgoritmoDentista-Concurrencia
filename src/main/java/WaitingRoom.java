import java.util.LinkedList;

/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Dentista
 */
public class WaitingRoom {
    //Atributos private
    private static int numberOfChairsAvaiable = 0;
    private static int numberOfChairs = 0;
    private static LinkedList<Patient> chairs = new LinkedList<Patient>();
    private static LinkedList<Patient> pacientesOperados = new LinkedList<Patient>();
    private static LinkedList<Patient> rejected = new LinkedList<Patient>();

    // Atributos package
    static Patient sillaOperaciones = null;
    static Dentist dentista;

    /**
     * Constructor que nos permite instanciar nuestra waiting room
     *
     * @param capacity Capacidad maxima de dicha waiting room
     */
    WaitingRoom(int capacity) {
        numberOfChairs = capacity;
        numberOfChairsAvaiable = capacity;
    }


    /**
     * Este metodo nos permite intenrar sentar un paciente en la
     * sala de espera y retornarle a dicho paciente si ha
     * podido sentarse o no
     *
     * @param patient Paciente que queremos que se siente
     * @return true/false si se ha podido sentar o no
     */
    public synchronized boolean seat(Patient patient) {
        if (numberOfChairsAvaiable == numberOfChairs && !Dentist.estaOperando) {
            // Me voy de cabeza a la silla de operaciones
            Dentist.estaOperando = true;
            sillaOperaciones = patient;
            patient.setTocaTurno(true);
            synchronized (dentista) {
                dentista.notifyAll();
            }
            return true;
        } else {
            // nos sentamos en una silla de espera
            if (numberOfChairsAvaiable > 0) {
                chairs.addLast(patient);

                System.out.println();
                System.out.println("-------------------");
                System.out.println("Paciente: " + patient.getId() + " sentado y esperando turno");
                System.out.println("Number of chairs avaiable: " + --numberOfChairsAvaiable + "/" + numberOfChairs);
                System.out.println("-------------------");
                System.out.println();
                return true;
            }
        }

        // No se ha podido sentar al final
        rejected.addLast(patient);
        return false;
    }


    /**
     * Este metodo retorna si la sala de espera esta vacia o no
     * @return true / false dependiendo si esta vacia o no
     */
    public synchronized boolean roomIsEmpty() {
        return numberOfChairsAvaiable == numberOfChairs;
    }


    /**
     * Este metodo simplemente selecciona al siguiente paciente y lo sienta en la silla
     * de operaciones, despues le asigna que le toca el turno y despues lo quita de las sillas de espera
     */
    public synchronized void pickPatient() {
        numberOfChairsAvaiable++;
        sillaOperaciones = chairs.getFirst();

        synchronized (sillaOperaciones) {
            sillaOperaciones.setTocaTurno(true);
            sillaOperaciones.notifyAll();
        }
        chairs.removeFirst();
    }

    /**
     * En este metodo lo que hacemos es despachar
     * el cliente y vaciar nuestra silla de operaciones
     */
    public synchronized void dispatchClient() {
        synchronized (dentista) {
            sillaOperaciones.operado = true;
            synchronized (sillaOperaciones) {
                sillaOperaciones.notifyAll();
                pacientesOperados.addLast(sillaOperaciones);
                sillaOperaciones = null;
            }
            Dentist.estaOperando = false;
        }
    }

    /**
     * Este metodo nos sirve para
     * asignar un dentista a nuestra waiting room
     *
     * @param dentista Pasamos que dentista queremos que sea el que asignamos
     */
    public void setDentista(Dentist dentista) {
        this.dentista = dentista;
    }

    /**
     * Este metodo simplemente nos retorna una
     * lista con todos los pacientes que estan a la espera
     *
     * @return lista de pacientes
     */
    public static LinkedList<Patient> getPatientsWaiting() {
        synchronized (chairs) {
            return chairs;
        }
    }

    /**
     * Este metodo nos retorna una lista con todos los pacientes ya operados
     *
     * @return lista de pacientes
     */
    public static LinkedList<Patient> getPacientesOperados() {
        synchronized (pacientesOperados) {
            return pacientesOperados;
        }
    }

    /**
     * Este metodo nos retorna el ID del paciente que actualmente estamos operando
     *
     * @return Int ID
     */
    public synchronized static int getOpertationId() {
        synchronized (dentista) {
            if (sillaOperaciones == null) {
                return 0;
            }
            return sillaOperaciones.getId();
        }
    }

    /**
     * Este metodo nos retorna una lista de todos los pacientes
     * a los que hemos rechazado la entrada porque no
     * hay sillas disponibles
     *
     * @return lista de pacientes
     */
    public static LinkedList<Patient> getRejected() {
        synchronized (rejected) {
            return rejected;
        }
    }
}


