import java.util.LinkedList;

/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Dentista
 */
public class WaitingRoom {
    private static int numberOfChairsAvaiable = 0;
    private static int numberOfChairs = 0;
    private static LinkedList<Patient> chairs = new LinkedList<Patient>();
    Patient sillaOperaciones = null;
    Dentist dentista;

    WaitingRoom(int capacity) {
        numberOfChairs = capacity;
        numberOfChairsAvaiable = capacity;
    }


    public synchronized boolean seat(Patient patient) {
        if (numberOfChairsAvaiable == numberOfChairs && !Dentist.estaOperando) {
            // Me voy de cabeza
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
        return false;
    }


    public synchronized boolean roomIsEmpty() {
        return numberOfChairsAvaiable == numberOfChairs;
    }


    public synchronized void pickPatient() {
        numberOfChairsAvaiable++;
        sillaOperaciones = chairs.getFirst();
        chairs.removeFirst();
    }

    public synchronized void dispatchClient() {
        sillaOperaciones.operado = true;
        synchronized (sillaOperaciones) {
            sillaOperaciones.notifyAll();
        }
        sillaOperaciones = null;
        Dentist.estaOperando = false;
    }

    public void setDentista(Dentist dentista) {
        this.dentista = dentista;
    }

}


