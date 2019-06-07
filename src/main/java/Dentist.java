/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Dentista
 */
public class Dentist implements Runnable {
    private WaitingRoom waitingRoom;
    public static boolean estaOperando = false;

    Dentist(WaitingRoom wr) {
        this.waitingRoom = wr;
    }

    @Override
    public void run() {
        System.out.println("Dentista entra a la oficina");
        try {
            while (true) {
                synchronized (this) {
                    while (!estaOperando && waitingRoom.roomIsEmpty()) {
                        System.out.println("Dentista espera a tener trabajo");
                        this.wait();
                    }
                }

                // significa que o se ha despertado por que ha entrado un cliente o sigue trabajando despues de operar;
                estaOperando = true;
                if (waitingRoom.sillaOperaciones != null) {
                    System.out.println("Me has despertado de mi siesta ca#@~€!lo");
                    // Tenemos un paciente que nos ha despertado
                    printText("Dentista operando a paciente: " + waitingRoom.sillaOperaciones.getId());

                    // Tiempo de operacion random
                    Thread.sleep(DentistOffice.random.nextInt(8000) + 3000);


                    printText("Paciente: " + waitingRoom.sillaOperaciones.getId() + " ya ha sido operado y se va a su casa");


                    // Sacamos al paciente y vaciamos la silla
                    waitingRoom.dispatchClient();
                } else if (!waitingRoom.roomIsEmpty()) {
                    // Elegimos que paciente operamos ahora
                    System.out.println("Siguiente paciente !!!!!!!!");
                    waitingRoom.pickPatient();

                    printText("Dentista operando a paciente: " + waitingRoom.sillaOperaciones.getId());

                    // Tiempo de operacion random
                    Thread.sleep(DentistOffice.random.nextInt(8000) + 3000);

                    printText("Paciente: " + waitingRoom.sillaOperaciones.getId() + " ya ha sido operado y se va a su casa");

                    waitingRoom.dispatchClient();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void printText(String text) {
        System.out.println();
        System.out.println("###############################");
        System.out.println("###############################");
        System.out.println(text);
        System.out.println("###############################");
        System.out.println("###############################");
        System.out.println();
    }
}