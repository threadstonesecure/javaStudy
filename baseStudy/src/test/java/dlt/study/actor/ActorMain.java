package dlt.study.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import scala.concurrent.Future;

/**
 * Created by denglt on 16/9/29.
 */
public class ActorMain {
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("ActorMain");
        ActorRef actor1 = system.actorOf(Props.create(Actor01.class));
        ActorRef actor2 = system.actorOf(Props.create(Actor02.class));
        actor1.tell("hello akka!!", actor2);

        System.in.read();
        Future<Terminated> terminatedFuture = system.terminate();
        if (terminatedFuture.isCompleted())
            System.out.println("completed");
        else

            System.out.println("not completed");

    }
}
