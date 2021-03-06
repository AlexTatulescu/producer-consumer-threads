package ace.ucv.ro;

public class Consumer extends Thread {
	private int consumerId;

	public Consumer(int consumerId) {
		this.consumerId = consumerId;
	}

	@Override
	public void run() {
		try {
			System.out.println("Consumer " + consumerId + " tries to consume " + consume());
			Thread.sleep(300);
		} catch (InterruptedException ie) {
			ie.getMessage();
		}
	}

	@SuppressWarnings("finally")
	private int consume() {
		int value = 0;
		Simulation.lock.lock();
		try {
			while (Simulation.queue.isEmpty()) {
				System.out.println("Queue is empty. Waiting for notEmpty condition " + consumerId);
				Simulation.notEmpty.await();
			}
			value = Simulation.queue.remove();
			Simulation.notFull.signal();
		} catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		} finally {
			Simulation.lock.unlock();
			return value;
		}
	}
}
