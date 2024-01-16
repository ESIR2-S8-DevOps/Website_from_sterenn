package org.acme;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/mail")
public class MailResource {

    @Inject
    Mailer mailer;

    @GET
    @Blocking
    public void sendEmail() {
        mailer.send(Mail.withText("your-destination-email@quarkus.io", "Ahoy from Quarkus", "A simple email sent from a Quarkus application."));
    }

    @Incoming("mail")
    public void accountCreated(JsonObject obj){ // Quand un utilisateur s'inscrit

        String email = obj.getString("data");

        System.out.println("Email : " + email);

        Thread t = new Thread(() -> {
            System.out.println("Sending mail to " + email);
            mailer.send(
                    Mail.withText(
                            email,
                            "Création de compte", "Vous venez de créer un compte "
                    )
            );
        });
        t.start();
    }
    @Inject
    ReactiveMailer reactiveMailer;

    @GET
    @Path("/reactive")
    public Uni<Void> sendEmailUsingReactiveMailer() {
        return reactiveMailer.send(                         // <4>
                Mail.withText("clement.escoffier@redhat.com",
                        "Ahoy from Quarkus",
                        "A simple email sent from a Quarkus application using the reactive API."
                )
        );
    }

}