package com.stone.core.akka.compute;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
// Util now, i don't know what the router use for??
//import akka.routing.RoundRobinRouter;

public class MasterActor extends UntypedActor {
	ActorRef mapActor;
	ActorRef reduceActor;
	ActorRef aggregateActor;

	public MasterActor() {
		this.mapActor = this.getContext().actorOf(Props.create(MapActor.class), "map");
		this.reduceActor = this.getContext().actorOf(Props.create(ReduceActor.class), "reduce");
		this.aggregateActor = this.getContext().actorOf(Props.create(AggregateActor.class), "aggregate");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			mapActor.tell(message, getSelf());
		} else if (message instanceof MapData) {
			reduceActor.tell(message, getSelf());
		} else if (message instanceof ReduceData) {
			aggregateActor.tell(message, getSelf());
		} else if (message instanceof Result) {
			aggregateActor.forward(message, getContext());
		} else {
			unhandled(message);
		}
	} 

}
