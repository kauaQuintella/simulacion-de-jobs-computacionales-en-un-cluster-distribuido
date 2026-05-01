import java.util.Random;

public class ValidatorThread implements Runnable {

    private Cluster cluster;
    private Random random;

    public ValidatorThread(Cluster cluster) {
        this.cluster = cluster;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Job job = cluster.tomarJobEnCola(); // espera si no hay

                if (job == null) {

                    if (cluster.isSchedulerTerminado() && cluster.getTotalFinal() >= 500) {
                        break; // ya terminó TODO
                    }

                    Thread.sleep(50); // espera antes de reintentar
                    continue;
                }

                Nodo nodo = job.getNodoAsignado();

                int prob = random.nextInt(100);

                if (prob < 85) {
                    // valido
                    nodo.liberar();
                    cluster.moverAEjecucion(job);

                } else {
                    // invalido
                    nodo.marcarFueraDeServicio();
                    cluster.moverAFallidos(job);
                }

                // Simula tiempo de validación
                Thread.sleep(80);


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }

    }
}