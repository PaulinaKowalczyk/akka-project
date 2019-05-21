import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FindActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    @Override
    public Receive createReceive() {
            return receiveBuilder()
                    .match(String.class, s -> {
                        context().child("findChild1").get().tell("books1.txt" + s, getSelf());
                        context().child("findChild2").get().tell("books2.txt" + s, getSelf());
                    })
                    .match(Integer.class, s->{
                        context().parent().tell( s, getSelf());
                    })
                    .matchAny(o -> log.info("received unknown message"))
                    .build();
    }

    @Override
    public void preStart() throws Exception {
        context().actorOf(Props.create(FindChild.class), "findChild1");
        context().actorOf(Props.create(FindChild.class), "findChild2");
    }

}
