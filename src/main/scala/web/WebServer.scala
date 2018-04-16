package web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.Extraction
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods.render
import web.dto.Event

import scala.util.{Failure, Success}
import scala.concurrent.{ExecutionContextExecutor, Future}

trait JsonSupport extends Json4sSupport {

  implicit val serialization = org.json4s.native.Serialization
  implicit val json4sFormats = org.json4s.DefaultFormats
}

class WebServer(implicit system: ActorSystem, materializer: ActorMaterializer,
                executionContext: ExecutionContextExecutor)
  extends Directives with JsonSupport {

  // set up a demo route using the #render method of json4s.jackson
  // this allows us to send native Scala code back as Json
  val getHome = get {

    complete(render("message" -> "welcome to the fun house"))
  }

  // match on the post request
  val postHome = post {
    // Unmarshalls the requests entity and yields to inner block
    entity(as[Event]) { jsonReq => {

      complete(

        // here's an example of how to respond with a combination of json fields
        // using json4s Extraction
        render("message" -> "welcome to the fun house")
      )
    }}
  }

  val restfulRoutes: Route = path("home") {
    getHome ~ postHome
  }


  def myUserPassAuthenticator(credentials: Credentials): Future[Option[String]] =
    credentials match {
      case p @ Credentials.Provided(id) =>
        Future {
          // potentially
          if (p.verify("p4ssw0rd")) Some(id)
          else None
        }
      case _ => Future.successful(None)
    }







  val bindingFuture = Http().bindAndHandle(restfulRoutes, "0.0.0.0", 8089)

  bindingFuture.onComplete {
    case Success(binding) ⇒
      println(s"Webserver is listening on localhost:8089")
    case Failure(e) ⇒
      println(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }
}

object Main {

  def main(args: Array[String]) {
    println("Hello from Akka-Http starter pack!")

    implicit val system = ActorSystem("demo-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext:ExecutionContextExecutor = system.dispatcher

    val webServer = new WebServer

  }

}
