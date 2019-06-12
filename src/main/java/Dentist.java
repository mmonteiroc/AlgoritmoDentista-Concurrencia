/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Dentista
 *
 * Esta clase nos permite representar
 * el dentista en nuestro programa
 */
public class Dentist implements Runnable {

    // Atributos privados
    private WaitingRoom waitingRoom;

    // Atributos publicos
    public static boolean estaOperando = false;


    /**
     * Este constructor nos permitira instanciar
     * un dentista asignandole una waitingroom
     *
     * @param wr waiting room donde el dentista
     *           tendra que trabajar
     */
    Dentist(WaitingRoom wr) {
        this.waitingRoom = wr;
    }

    /**
     * Metodo que ejecutara nuestro proceso de dicho dentista
     * Metodo implementado de la interfaz runnable
     */
    @Override
    public void run() {
        System.out.println();
        System.out.println("Dentista entra a la oficina");
        System.out.println();
        try {

            // Bucle de trabajo infinito
            while (true) {
                synchronized (this) {
                    // Miramos si el dentista no tiene trabajo y se puede dormir
                    while (!estaOperando && waitingRoom.roomIsEmpty()) {
                        System.out.println();
                        System.out.println("Dentista se hecha una siesta mientras que no tiene trabajo");
                        System.out.println();
                        this.wait();
                    }
                }

                // significa que o se ha despertado por que ha entrado un cliente o sigue trabajando despues de operar;
                estaOperando = true;
                if (WaitingRoom.sillaOperaciones != null) {
                    System.out.println("Me has despertado de mi siesta ca#@~€!lo");
                    // Tenemos un paciente que nos ha despertado
                    printText("Dentista operando a paciente: " + WaitingRoom.sillaOperaciones.getId());

                    // Tiempo de operacion random
                    Thread.sleep(DentistOffice.random.nextInt(DentistOffice.tiempoOperaciones) + 3000);


                    printText("Paciente: " + WaitingRoom.sillaOperaciones.getId() + " ya ha sido operado y se va a su casa");


                    // Sacamos al paciente y vaciamos la silla
                    waitingRoom.dispatchClient();
                } else if (!waitingRoom.roomIsEmpty()) {
                    // Elegimos que paciente operamos ahora
                    System.out.println("Siguiente paciente !!!!!!!!");
                    waitingRoom.pickPatient();

                    printText("Dentista operando a paciente: " + WaitingRoom.sillaOperaciones.getId());

                    // Tiempo de operacion random
                    Thread.sleep(DentistOffice.random.nextInt(DentistOffice.tiempoOperaciones) + 1000);

                    printText("Paciente: " + WaitingRoom.sillaOperaciones.getId() + " ya ha sido operado y se va a su casa");

                    waitingRoom.dispatchClient();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Este metodo nos permite imprimir en el terminal
     * texto que queramos con barras separadoras
     *
     * @param text texto que queremos imprimir entre barras
     */
    private void printText(String text) {
        String barra = "";
        String barra2 = "";
        for (int i = 0; i < text.length(); i++) {
            barra += "#";
            barra2 += "~";
        }
        System.out.println(barra);
        System.out.println(barra2);
        System.out.println(text);
        System.out.println(barra2);
        System.out.println(barra);
        System.out.println();
    }
}