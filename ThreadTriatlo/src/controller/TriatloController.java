package controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class TriatloController extends Thread {
	private static int pontuacao = 250;
	private int pontuacaoTotal = 0;
	private int permissoes;
	public static Map<Integer,Integer> competidores = new HashMap<>();
	Semaphore semaforo = new Semaphore(permissoes);
	private int id;
	public TriatloController(int id, int permissoes, Semaphore semaforo) {
		this.permissoes = permissoes;
		this.semaforo = semaforo;
		this.id = id;
	}

	public void run() {

		int distanciaCorrida = 3000;
		int distanciaBicicleta = 5000;

		corrida(distanciaCorrida);
		try{
			semaforo.acquire();
			pontuacaoTotal += tiro();
		}catch(Exception e) {
			e.printStackTrace();
		}finally{semaforo.release();
		}
		bicicleta(distanciaBicicleta);
		pontuacaoTotal += pontuacao;
		pontuacao-=10;
		System.out.println("Pontuação total do competidor "+id+": "+pontuacaoTotal);
		competidor(id,pontuacaoTotal);
	}

	public void corrida( int distanciaTotal) {
		int distanciaAtual = 0;
		while (distanciaAtual < distanciaTotal) {
			try {
				Thread.sleep(30);
				System.out.println("Competidor "+id+ " já andou "+distanciaAtual+" metros");
				int velocidade = (int) ((Math.random() * 6) + 20);
				distanciaAtual += velocidade;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Competidor "+id+ " terminou a corrida");
	}
	public int tiro() {
		int pontuacaoTotal=0;
		for(int i = 0;i<3;i++) {
			try {
				int sleep = (int) ((Math.random() * 2501) + 500);
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int pontuacaoTiro = (int) ((Math.random() * 11));
			pontuacaoTotal += pontuacaoTiro;
			System.out.println("Pontuação do competidor: "+pontuacaoTiro);
		}
		System.out.println("Pontuação total do competidor "+id+" no tiro ao alvo: "+pontuacaoTotal);
		return pontuacaoTotal;
	}
	public void bicicleta( int distanciaBicicleta) {
		int distanciaAtual = 0;
		while (distanciaAtual < distanciaBicicleta) {
			try {
				Thread.sleep(40);
				int velocidadeBicicleta = (int) ((Math.random() * 11) + 30);
				System.out.println("Competidor "+id+ " já andou de bicicleta "+distanciaAtual+" metros");
				distanciaAtual += velocidadeBicicleta;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Competidor "+id+" terminou a corrida de bicicleta");

	}
	public void competidor(int id, int pontuacaoTotal) {
		synchronized (competidores) {
	        competidores.put(id, pontuacaoTotal);
	        competidores = competidores.entrySet()
	                .stream()
	                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
	                .collect(Collectors.toMap(
	                        Map.Entry::getKey,
	                        Map.Entry::getValue,
	                        (e1, e2) -> e2,
	                        LinkedHashMap::new));
	    }
	}
	public static void printCompetidores() {
		System.out.println("Ranking: "+competidores);
	}
}
