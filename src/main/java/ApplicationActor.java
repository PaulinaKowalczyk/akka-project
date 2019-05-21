import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorRef;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;

import java.nio.charset.Charset;



public class ApplicationActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private boolean found = false;
    private int counter = 0;
    private int price = 0;

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    if (s.equals("hi")) {
                        System.out.println("hello");
                    } else if (s.startsWith("find")) {
                        // s.substring(5) -> tytul ksiazki
                        context().child("findActor").get().tell(s.substring(5), getSelf()); // send task to child
                    }else if (s.startsWith("order")) {
                        context().child("orderActor").get().tell(s.substring(6), getSelf()); // send task to child
                    } else if(s.startsWith("read")){
                        //System.out.println(s.substring(5));
                        context().child("readActor").get().tell(s.substring(5), getSelf());
                    } else{
                        System.out.println("Result from a child: " + s);              // result from child
                    }
                })
                .match(Integer.class, s-> {
                    counter ++;
                    if(s!=0){
                        found = true;
                        price = s;
                    }

                    if(counter==2 && found) {
                        System.out.println("Znaleziono ksiazke w bazie. Cena: " + price);
                        counter = 0;
                        found = false;
                    }else if(counter==2 && !found) {
                        System.out.println("Nie znaleziono ksiazki w bazie");
                        counter = 0;
                        found = false;
                    }
                })
                .match(Integer.class, s-> {
                    System.out.println(s);
                })
                .match(akka.util.ByteString.class, s-> {
                    //System.out.println("My bytestring " + s);
                    String b = s.decodeString("US-ASCII");
                    System.out.println(b);
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    // optional
    @Override
    public void preStart() throws Exception {
        context().actorOf(Props.create(FindActor.class), "findActor");
        context().actorOf(Props.create(OrderActor.class), "orderActor");
        context().actorOf(Props.create(ReadActor.class), "readActor");
    }

}
