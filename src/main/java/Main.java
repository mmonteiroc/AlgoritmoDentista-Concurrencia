import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete PACKAGE_NAME
 * Proyecto Dentista
 */
public class Main {
    private static final int MAX_FPS = 60;

    public static void main(String[] args) throws SlickException {
        MyGame app = new MyGame();
        AppGameContainer ventana = new AppGameContainer(app, 1300, 750, false);
        ventana.setShowFPS(false);
        ventana.setTargetFrameRate(MAX_FPS);
        ventana.start();
    }

}

/**
 * Clase que nos permetira representar
 * la parte visual de nuestro codigo
 */
class MyGame extends BasicGame {


    /**
     * constructor de dicha clase
     */
    public MyGame() {
        super("El dentista");
    }

    /**
     * Este metodo nos permite inicializar todo
     * lo que necesitemos para nuestro programa
     *
     * @param gameContainer
     * @throws SlickException Posible excepcion que se podria lanzar durante nuestro programa
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        DentistOffice.start();
    }

    /**
     * Este metodo se llama 60 veces por segundo
     * y nos permite ir actualizando nuestro
     * juego a tiempo real
     *
     * @param gameContainer
     * @param i
     * @throws SlickException Posible excepcion que se podria lanzar durante nuestro programa
     */
    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        Input tecla = gameContainer.getInput();

        // CONTROL DE TECLAS PARA MODIFICAR EL JUEGO
        if (tecla.isKeyDown(Input.KEY_A)) {
            // subimos el tiempo de pacientes
            DentistOffice.tiempoEntrePacientes += 100;
        }
        if (tecla.isKeyDown(Input.KEY_Z)) {
            // bajamos el tiempo de pacientes
            if (DentistOffice.tiempoEntrePacientes >= 200) {
                DentistOffice.tiempoEntrePacientes -= 100;
            }
        }

        if (tecla.isKeyDown(Input.KEY_S)) {
            // subimos el tiempo de operaciones
            DentistOffice.tiempoOperaciones += 100;
        }
        if (tecla.isKeyDown(Input.KEY_X)) {
            // bajamos teimpo de operaciones
            if (DentistOffice.tiempoOperaciones >= 200) {
                DentistOffice.tiempoOperaciones -= 100;
            }
        }


        // Aqui añadimos de golpe 80 pacientes mas
        if (tecla.isKeyPressed(Input.KEY_ESCAPE)) {
            ExecutorService es = Executors.newCachedThreadPool();
            for (int j = 0; j < 80; j++) {
                es.execute(new Patient(DentistOffice.waitingRoom));
            }
        }

    }

    /**
     * Este metodo nos permite renderizar
     * visualmente a tiempo real nuestro codigo
     *
     * @param gameContainer
     * @param graphics
     * @throws SlickException Posible excepcion que se podria lanzar durante nuestro programa
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

        // Cuantos pacientes han pasado por nuestras ofis
        graphics.drawString("Numero de paceintes que han pasado por la oficina: " + (Patient.numeroPacientes - 1), 5, 5);


        // String pacientes en espera
        graphics.drawString("Pacientes en espera: ", 5, 30);
        drawPatientsWaiting(graphics);

        // Pacientes operados
        graphics.drawString("Pacientes operados: ", 5, 150);
        drawPatientsOperated(graphics);

        // Pintamos el doctor y al que esta operando
        drawDoctor(graphics);

        // Pintamos los pacientes rechazados
        graphics.drawString("Pacientes rechazados: ", 5, 450);
        drawRejected(graphics);

        // Velocidad creacion
        graphics.drawString("(a/z to modify) Tiempo maximo entre pacientes llegando: " + ((double) DentistOffice.tiempoEntrePacientes / 1000) + "s", 750, 5);
        graphics.drawString("(s/x to modify) Tiempo maximo entre operacion: " + ((double) DentistOffice.tiempoOperaciones / 1000) + "s", 830, 25);
    }

    /**
     * Este metodo lo que hacemos es recoger la lista
     * de pacientes que estan esperando, definimos
     * donde queremos empezar a dibujar y llamamos
     * a draw pasando los parametros que queramos
     *
     * @param graphics Donde dibujaremos
     */
    private void drawPatientsWaiting(Graphics graphics) {
        LinkedList<Patient> pacientesEsperando = WaitingRoom.getPatientsWaiting();
        int BOX_SIZE = 35;
        int x = 20;
        int y = 70;
        draw(pacientesEsperando, x, y, graphics, BOX_SIZE);
    }

    /**
     * Este metodo lo que hacemos es recoger la lista
     * de pacientes que estan operados, definimos
     * donde queremos empezar a dibujar y llamamos
     * a draw pasando los parametros que queramos
     *
     * @param graphics Donde dibujaremos
     */
    private void drawPatientsOperated(Graphics graphics) {
        LinkedList<Patient> pacientesOperado = WaitingRoom.getPacientesOperados();
        int BOX_SIZE = 35;
        int x = 20;
        int y = 190;
        draw(pacientesOperado, x, y, graphics, BOX_SIZE);
    }


    /**
     * Este metodo lo que hacemos es recoger la lista
     * de pacientes que han estado rechazados, definimos
     * donde queremos empezar a dibujar y llamamos
     * a draw pasando los parametros que queramos
     *
     * @param graphics Donde dibujaremos
     */
    private void drawRejected(Graphics graphics) {
        LinkedList<Patient> rejected = WaitingRoom.getRejected();
        int BOX_SIZE = 35;
        int x = 20;
        int y = 490;
        draw(rejected, x, y, graphics, BOX_SIZE);
    }

    /**
     * Este metodo nos permite dibujar nuestro doctor
     *
     * @param graphics Donde dibujaremos
     */
    private void drawDoctor(Graphics graphics) {
        int BOX_SIZE = 100;
        int x = 800;
        int y = 120;

        Rectangle box = new Rectangle(x, y, BOX_SIZE * 3, BOX_SIZE);
        box.setCenterX(x);
        box.setCenterY(y);
        graphics.draw(box);

        synchronized (WaitingRoom.dentista) {
            if (!Dentist.estaOperando) {
                graphics.drawString("Dentista no esta operando", x - 130, y + 10);
            } else {
                if (WaitingRoom.sillaOperaciones != null) {
                    graphics.drawString("Dentista operando a: " + WaitingRoom.getOpertationId(), x - 120, y + 10);
                }
            }
        }

    }


    /**
     * Este metodo recibe una lista y la representa en nuestra pantalla
     *
     * @param pacientes Lista de pacientes a pintar
     * @param x         donde queremos pintar en X
     * @param y         donde queremos pintar en Y
     * @param graphics  donde queremos pintar
     * @param BOX_SIZE  Tamaño de la caja que quiere vender
     */
    private void draw(LinkedList<Patient> pacientes, int x, int y, Graphics graphics, int BOX_SIZE) {
        Rectangle box = null;
        int yOriginal = y;

        int dondeEmpezar;
        if (pacientes.size() > 140) {
            dondeEmpezar = pacientes.size() - 140;
        } else {
            dondeEmpezar = 0;
        }
        for (int i = dondeEmpezar; i < pacientes.size(); i++) {
            Patient paciente = pacientes.get(i);
            synchronized (paciente) {
                box = new Rectangle(x, y, BOX_SIZE, BOX_SIZE);
                box.setCenterX(x);
                box.setCenterY(y);

                graphics.drawString(paciente.getId() + "", x - 10, y - 10);
                x += 45;
                //y+=40;
                graphics.draw(box);
            }
            if (x >= 1250) {
                x = 20;
                y += 40;
            }
        }
    }
}