package server;
import java.util.LinkedList;


public class Trådpool {
	private Buffer<Runnable> buffer = new Buffer<Runnable>();
	private LinkedList<Worker> workers;
	private int Threads;
	
	public Trådpool(int Threads) {
		this.Threads = Threads;
	}

	/*
	 * Trådarna måste instantieras och startas. Instansvariabeln n håller antalet trådar.
	 */

	public synchronized void start() {
		Worker worker;
		if(workers==null) {
			workers = new LinkedList<Worker>();	
			for(int i=0; i<Threads; i++) {
				worker = new Worker();
				worker.start();
				workers.add(worker);
			}
		}
	}

	/* Trådarna bör kunna avslutas – Detta kan ske omedelbart (eventuella objekt i
	 *bufferten exekveras ej) eller då bufferten är tömd (se alternativ stop-metod).
	 */

	public synchronized void stop() {	
		if(workers!=null) {
			buffer.clear();
			for(Worker worker : workers) {
				worker.interrupt();
			}
			workers = null;
		}

	}


	public synchronized void execute(Runnable runnable) {
		buffer.put(runnable);
	}

	private class Buffer<T>{
		private LinkedList<T> buffer = new LinkedList<T>();
		/*
		 * Lägger till ett objekt i bufferten
		 */
		public synchronized void put(T obj) {
			buffer.addLast(obj);
			notifyAll();
		}

		/*
		 * Returnar första elementet i bufferten samt tar bort den sedan
		 */

		public synchronized T get() throws InterruptedException {
			while(buffer.isEmpty()) {
				wait();
			}
			return buffer.removeFirst();
		}

		/*
		 * Returnar första elementet i bufferten samt tar bort den sedan
		 */

		public synchronized void clear() {
			buffer.clear();
		}
	}


	/*
	 * Hämtar buffer och kör den då thread inte är intrrupted.
	 */

	private class Worker extends Thread {
		public void run() {
			while(!Thread.interrupted()) {
				try {
					buffer.get().run();
				} catch(InterruptedException e) {
					System.out.println(e);
					break;
				}
			}
		}
	}
}
