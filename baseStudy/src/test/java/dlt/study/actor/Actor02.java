package dlt.study.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import dlt.study.log4j.Log;

/**
 * Created by denglt on 16/9/29.
 */
public class Actor02 extends UntypedActor {
    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof String)
            Log.info("2-------------->" + o);
        ActorRef sender = this.getSender();
        ActorRef self = this.getSelf();
        sender.tell("hell by Actor02",self);
    }
}
