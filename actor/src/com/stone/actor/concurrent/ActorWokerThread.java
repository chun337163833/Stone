package com.stone.actor.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ActorWokerThread extends Thread implements IActorWorkerThread {
	protected BlockingQueue<IActorRunnable> runnableQueue = new LinkedBlockingQueue<IActorRunnable>();
	protected volatile boolean stop = true;

	@Override
	public void submit(IActorRunnable iActorRunnable) {
		try {
			runnableQueue.put(iActorRunnable);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!stop) {
			try {
				IActorRunnable runnable = this.runnableQueue.take();
				runnable.run();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// FIXME: crazyjohn log
			}
		}
	}

	@Override
	public void startWorker() {
		stop = false;
	}

	@Override
	public void stopWorker() {
		stop = true;
	}

}