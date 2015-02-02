package com.stone.actor.system;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stone.actor.IActor;
import com.stone.actor.call.IActorCall;
import com.stone.actor.call.IActorCallback;
import com.stone.actor.concurrent.ActorWokerMonster;
import com.stone.actor.concurrent.IActorRunnable;
import com.stone.actor.concurrent.IActorWorkerMonster;
import com.stone.actor.id.IActorId;
import com.stone.core.annotation.GuardedByUnit;
import com.stone.core.annotation.ThreadSafeUnit;

/**
 * 基础的ActorSystem实现;
 * 
 * @author crazyjohn
 *
 */
@ThreadSafeUnit
public class ActorSystem implements IActorSystem, Runnable {
	/** prefix */
	private static final String MONSTER_PREFIX = "ActorWokerMonster-";
	private static final long SLEEP_INTERVAL = 100L;
	/** hash index */
	protected Map<IActorId, IActor> actors = new ConcurrentHashMap<IActorId, IActor>();
	/** work monsters */
	protected IActorWorkerMonster[] workerThreads;
	@GuardedByUnit(whoCareMe = "use volatile procted to mem sync")
	protected volatile boolean stop = true;
	private int workerNum;
	@GuardedByUnit(whoCareMe = "use class lock")
	private static IActorSystem instance = new ActorSystem();
	/** log */
	private Logger logger = LoggerFactory.getLogger(ActorSystem.class);
	/** executor */
	private Executor executor = Executors.newSingleThreadExecutor();

	/**
	 * private
	 */
	private ActorSystem() {

	}

	/**
	 * 初始化ActorSystem;
	 * 
	 * @param threadNum
	 */
	@Override
	public void initSystem(int threadNum) {// init worker thread
		logger.info("Begin to init the ActorSystem...");
		workerNum = threadNum;
		workerThreads = new IActorWorkerMonster[threadNum];
		for (int i = 0; i < threadNum; i++) {
			workerThreads[i] = new ActorWokerMonster();
			workerThreads[i].setMonsterName(MONSTER_PREFIX + i);
		}
		logger.info("Init the ActorSystem finished.");
	}

	@Override
	public void dispatch(IActorId actorId, IActorCallback<?> callback, Object result) {
		IActor actor = this.actors.get(actorId);
		if (actor == null) {
			return;
		}
		actor.put(callback, result);
	}

	@Override
	public void dispatch(IActorId actorId, IActorCall<?> call) {
		IActor actor = this.actors.get(actorId);
		if (actor == null) {
			return;
		}
		actor.put(call);
	}

	@Override
	public void run() {
		while (!stop) {
			boolean haveARest = true;
			for (final Map.Entry<IActorId, IActor> eachActorEntry : this.actors.entrySet()) {
				final IActor actor = eachActorEntry.getValue();
				if (!actor.hasAnyWorkToDo()) {
					continue;
				}
				haveARest = false;
				IActorWorkerMonster workThread = getActorWorkerMonster(eachActorEntry.getKey());
				if (workThread != null) {
					workThread.submit(new IActorRunnable() {
						@Override
						public void run() {
							actor.run();
						}

					});
				} else {
					// FIXME: crazyjohn log
				}
			}
			if (haveARest) {
				try {
					Thread.sleep(SLEEP_INTERVAL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private IActorWorkerMonster getActorWorkerMonster(IActorId actorId) {
		int workerIndex = actorId.getWorkerMonsterIndex(this.workerNum);
		return workerThreads[workerIndex];
	}

	@Override
	public void start() {
		if (!stop) {
			return;
		}
		logger.info("Begin to start the ActorSystem...");
		stop = false;
		for (IActorWorkerMonster eachMonster : this.workerThreads) {
			eachMonster.startWorker();
		}
		// executor self
		executor.execute(this);
		logger.info("Start the ActorSystem finished.");
	}

	@Override
	public void stop() {
		stop = true;
	}

	public static synchronized IActorSystem getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IActor> T getActor(IActorId actorId) {
		return (T) actors.get(actorId);
	}

	@Override
	public void registerActor(IActor actor) {
		this.actors.put(actor.getActorId(), actor);
	}

}
