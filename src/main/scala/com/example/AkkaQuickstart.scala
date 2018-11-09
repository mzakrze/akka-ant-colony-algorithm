package com.example

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{HttpOrigin, RawHeader}
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

object Greeter {
  def props(message: String, printerActor: ActorRef): Props = Props(new Greeter(message, printerActor))
  final case class WhoToGreet(who: String)
  case object Greet
}

class Greeter(message: String, printerActor: ActorRef) extends Actor {
  import Greeter._
  import Printer._

  var greeting = ""

  def receive = {
    case WhoToGreet(who) =>
      greeting = message + ", " + who
    case Greet           =>
      printerActor ! Greeting(greeting)
  }
}

object Printer {
  def props: Props = Props[Printer]
  final case class Greeting(greeting: String)
}

class Printer extends Actor with ActorLogging {
  import Printer._

  def receive = {
    case Greeting(greeting) =>
      log.info("Greeting received (from " + sender() + "): " + greeting)
  }
}

object AkkaQuickstart {

  def main(args: Array[String]): Unit = {
    import Greeter._

    implicit val system: ActorSystem = ActorSystem("helloAkka")

    val printer: ActorRef = system.actorOf(Printer.props, "printerActor")
    val howdyGreeter: ActorRef = system.actorOf(Greeter.props("Howdy", printer), "howdyGreeter")
    val helloGreeter: ActorRef = system.actorOf(Greeter.props("Hello", printer), "helloGreeter")
    val goodDayGreeter: ActorRef = system.actorOf(Greeter.props("Good day", printer), "goodDayGreeter")

    howdyGreeter ! WhoToGreet("Akka")
    howdyGreeter ! Greet

    howdyGreeter ! WhoToGreet("Lightbend")
    howdyGreeter ! Greet

    helloGreeter ! WhoToGreet("Scala")
    helloGreeter ! Greet

    goodDayGreeter ! WhoToGreet("Play")
    goodDayGreeter ! Greet

    println("xd")

    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val requestHandler: HttpRequest => HttpResponse = {
      case HttpRequest(GET, Uri.Path("/hello"), _, _, _) =>
        HttpResponse(entity = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          "<html><body>Hello world!</body></html>"))

      case HttpRequest(GET, Uri.Path("/api"), headers, _, _) =>
          HttpResponse(
            headers = scala.collection.immutable.Seq(RawHeader("Access-Control-Allow-Origin", "*")),
            entity = HttpEntity(ContentTypes.`application/json`, getWorldAsJson()))

      case HttpRequest(GET, Uri.Path("/admin"), _, _, _) =>
        HttpResponse(entity = "Not implemented !")

      case r: HttpRequest =>
        r.discardEntityBytes() // important to drain incoming HTTP Entity stream
        HttpResponse(404, entity = "Unknown resource!")
    }

    val bindingFuture = Http().bindAndHandleSync(requestHandler, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def getWorldAsJson(): String = {
    return "{\"ants\":[{\"x\":150,\"y\":150},{\"x\":150,\"y\":150},{\"x\":200,\"y\":200}],\"anthill\":{\"x\":100,\"y\":100},\"food\":[],\"pheromones\":[{\"x\":50,\"y\":50},{\"x\":50,\"y\":51},{\"x\":50,\"y\":52},{\"x\":50,\"y\":53},{\"x\":50,\"y\":54}]}"
  }




}
