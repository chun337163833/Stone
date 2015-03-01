package com.stone.game.player;

import com.stone.game.msg.ProtobufMessage;

import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * The palyer actor;
 * 
 * @author crazyjohn
 *
 */
public class PlayerActor extends UntypedActor {
	protected final Player player;

	public PlayerActor(Player player) {
		this.player = player;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof ProtobufMessage) {

		} else {
			unhandled(msg);
		}
	}

	public static Props props() {
		return Props.create(PlayerActor.class, new Player());
	}

}