import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OrderActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    order(s);
                    getSender().tell("Potwierdzenie zamowienia ksiazki " + s, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private void order(String title) throws IOException {
        FileWriter writer = new FileWriter("orders.txt", true);
        writer.write(title + "\n");
        writer.close();
    }
}
