import akka.Done;
import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.protobuf.ByteString;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.*;
import com.google.protobuf.Internal;
import scala.concurrent.duration.Duration;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.google.protobuf.Internal.toStringUtf8;

public class ReadActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                   // dostaje tytul i streamuje z pliku


                    final Materializer materializer = ActorMaterializer.create(getContext());
                    final ActorRef actor = getContext().getSender();

                    final Path file = Paths.get("inwokacja.txt");
                    Source source = FileIO.fromPath(file);//.via(Framing.delimiter(ByteString("\n"), 256, true));

                    String d= "done";
                    Sink sinkPrint = Sink.actorRef(actor,d);

                    source.throttle
                            (1, Duration.create(1, "seconds"), 1, ThrottleMode.shaping())
                            .runWith(sinkPrint, materializer);

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
