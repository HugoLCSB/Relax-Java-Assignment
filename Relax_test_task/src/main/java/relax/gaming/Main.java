package relax.gaming;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        System.out.println();
        Engine engine = new Engine();
        engine.init();
        engine.doSpin(/*Long.parseLong("-7817790225496238942")*/0, 10);
    }
}