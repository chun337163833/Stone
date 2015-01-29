package com.stone.actor;

import com.stone.actor.call.IActorCall;
import com.stone.actor.call.IActorCallback;
import com.stone.actor.future.IActorFuture;
import com.stone.actor.id.IActorId;

/**
 * Actor接口;
 * 
 * @author crazyjohn
 *
 */
public interface IActor {
	/**
	 * 投递一个调用;
	 * 
	 * @param call
	 */
	public <T> IActorFuture<T> call(IActorCall<T> call);

	/**
	 * 投递一个调用以及一个回调, 以及处理回调的ActorId;
	 * 
	 * @param call
	 * @param callback
	 * @param source
	 */
	public void put(IActorCall<?> call, IActorCallback callback, IActorId source);

	/**
	 * the run method;
	 */
	public void run();

	/**
	 * 启动actor;
	 */
	public void start();

	/**
	 * 停止actor;
	 */
	public void stop();

	/**
	 * 投递回调;
	 * 
	 * @param callback
	 */
	public void put(IActorCallback callback);

	/**
	 * 获取ActorId;
	 * 
	 * @return
	 */
	public IActorId getActorId();

}
