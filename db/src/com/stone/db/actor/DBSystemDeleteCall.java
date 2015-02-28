package com.stone.db.actor;

import com.stone.actor.data.IActorDBService;
import com.stone.actor.system.AbstractActorSystemCall;
import com.stone.actor.system.IActorSystem;
import com.stone.core.entity.IEntity;

/**
 * delete call;
 * 
 * @author crazyjohn
 *
 */
public class DBSystemDeleteCall extends AbstractActorSystemCall<IEntity<?>> {
	private IEntity<?> entity;

	public DBSystemDeleteCall(IActorSystem callerSystem, long callerActorId, IEntity<?> entity) {
		super(callerSystem, callerActorId);
		this.entity = entity;
	}

	@Override
	public IEntity<?> execute(IActorDBService dbService) {
		// TODO Auto-generated method stub
		return null;
	}

	public IEntity<?> getEntity() {
		return entity;
	}

}
