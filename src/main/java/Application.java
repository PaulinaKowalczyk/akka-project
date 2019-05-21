import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Application {

    public static void main(String[] args) throws Exception {

        final ActorSystem system = ActorSystem.create("bookstore");

        final ActorRef applicationActor = system.actorOf(Props.create(ApplicationActor.class), "applicationActor");
        System.out.println("Started. Commands: 'hi', 'find [title]', 'order [title]', 'read [title]', 'q'");


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = br.readLine();
            if (line.equals("q")) {
                break;
            }
            applicationActor.tell(line, null);
        }

        system.terminate();
    }
}