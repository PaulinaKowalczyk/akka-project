import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FindChild extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    String fileName = s.substring(0,10);
                    String title = s.substring(10);
                    Integer price = search(fileName,title);
                    getSender().tell( price, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


    private Integer search (String fileName, String title) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName)); // plik na zewnatrz, nie w katoalogu src->com.company, ale w katalogu glownym: searchinafile
        String line;
        while ((line = br.readLine()) != null) {
            if(line.contains(title)){
                return Integer.parseInt(line.substring(line.lastIndexOf("\"") + 2));
            }
        }
        return 0;
    }
}
