package view;

import java.util.concurrent.Semaphore;

import controller.TriatloController;

public class Main {

	public static void main(String[] args) {
		int permissoes = 5;
		Semaphore semaforo = new Semaphore(permissoes);
		int competidores = 25;
		TriatloController[] triatloControllers = new TriatloController[competidores];

		for(int i = 0; i<competidores;i++) {
			triatloControllers[i] = new TriatloController(i+1,permissoes, semaforo);
			triatloControllers[i].start();
		}

		for (int i = 0; i < competidores; i++) {
			try {
				triatloControllers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		TriatloController.printCompetidores();
	}
}
