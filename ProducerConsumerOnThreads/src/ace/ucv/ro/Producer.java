package ace.ucv.ro;

public class Producer extends Thread {

	private int producerId;

	public Producer(int producerId) {
		this.producerId = producerId;
	}

	@Override
	public void run() {
		try {
			System.out.println("Producer " + producerId + " tries to produce " + producerId);
			produce();
			Thread.sleep(100);
		} catch (InterruptedException ie) {
			ie.getMessage();
		}
	}

	private void produce() {
		Simulation.lock.lock();
		try {
			while (Simulation.queue.size() == Simulation.CAPACITY) {
				System.out.println("Queue is full. Wait for notFull condition " + producerId);
				Simulation.notFull.await();
			}
			Simulation.queue.offer(producerId);
			Simulation.notEmpty.signal();
		} catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		} finally {
			Simulation.lock.unlock();
		}
	}
}
