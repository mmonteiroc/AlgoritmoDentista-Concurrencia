import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.util.LinkedList;

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

class MyGame extends BasicGame {
    public MyGame() {
        super("El dentista");
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        DentistOffice.start();
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

        // Cuantos pacientes han pasado por nuestras ofis
        graphics.drawString("Numero de paceintes que han pasado por la oficina: " + (Patient.numeroPacientes - 1), 5, 5);


        // String pacientes en espera
        graphics.drawString("Pacientes en espera: ", 5, 30);
        drawPatientsWaiting(graphics);


        graphics.drawString("Pacientes operados: ", 5, 150);
        drawPatientsOperated(graphics);

        drawDoctor(graphics);


        graphics.drawString("Pacientes rechazados: ", 5, 450);
        drawRejected(graphics);
    }

    private void drawPatientsWaiting(Graphics graphics) {
        LinkedList<Patient> pacientesEsperando = WaitingRoom.getPatientsWaiting();
        Rectangle box = null;
        int BOX_SIZE = 30;
        int x = 20;
        int y = 70;
        draw(pacientesEsperando, x, y, graphics, BOX_SIZE);


    }

    private void drawPatientsOperated(Graphics graphics) {
        LinkedList<Patient> pacientesOperado = WaitingRoom.getPacientesOperados();
        int BOX_SIZE = 30;
        int x = 20;
        int y = 190;
        draw(pacientesOperado, x, y, graphics, BOX_SIZE);
    }

    private void drawDoctor(Graphics graphics) {
        int BOX_SIZE = 100;
        int x = 900;
        int y = 330;

        Rectangle box = new Rectangle(x, y, BOX_SIZE * 3, BOX_SIZE);
        box.setCenterX(x);
        box.setCenterY(y);
        graphics.draw(box);
        if (!Dentist.estaOperando) {
            graphics.drawString("Dentista no esta operando", x - 130, y + 10);
        } else {
            graphics.drawString("Dentista operando a: " + WaitingRoom.getOpertationId(), x - 120, y + 10);
        }

    }

    private void drawRejected(Graphics graphics) {
        LinkedList<Patient> rejected = WaitingRoom.getRejected();
        int BOX_SIZE = 30;
        int x = 20;
        int y = 490;
        draw(rejected, x, y, graphics, BOX_SIZE);
    }


    private void draw(LinkedList<Patient> pacientes, int x, int y, Graphics graphics, int BOX_SIZE) {
        Rectangle box = null;
        int yOriginal = y;
        for (int i = 0; i < pacientes.size(); i++) {
            Patient paciente = pacientes.get(i);
            synchronized (paciente) {
                box = new Rectangle(x, y, BOX_SIZE, BOX_SIZE);
                box.setCenterX(x);
                box.setCenterY(y);

                graphics.drawString(paciente.getId() + "", x - 10, y - 10);
                x += 40;
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
