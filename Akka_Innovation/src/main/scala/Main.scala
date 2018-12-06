import akka.actor.{ActorSystem, PoisonPill, Props}

import scala.util.{Failure, Success}
import akka.pattern._

import scala.concurrent.duration._
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Main {

  def main(args: Array[String]): Unit ={

    implicit val system: ActorSystem = ActorSystem("myActorSystem");
    println(system)

//    tellPattern(system)
//    askPattern(system)
//    askPatternMapTo(system)
//    askPatternPipeTo(system)
//    findActor(system)
    actorPoisionPill(system)

    system.terminate.onComplete {
      case Success(result) => println("Successfully terminated my actor system")
      case Failure(e)     => println("Failed to terminate my actor system")
    }

    Thread.sleep(5000)

  }

  def tellPattern(system: ActorSystem): Unit = {
    val infoActor = system.actorOf(Props[InfoActor], name = "tellPatternActor")
    val res = infoActor ! Info("tpattern")
  }

  /*
    Ask Pattern: timeout required to get response from actor
   */

  def askPattern(system: ActorSystem): Unit = {
    implicit val timeout = Timeout(5 second)
    val infoActor = system.actorOf(Props[InfoActor], name = "askPatternActor")
    val res = infoActor ? Info("apattern")
  }

  def askPatternMapTo(system: ActorSystem): Unit = {
    implicit val timeout = Timeout(5 second)
    val infoActor = system.actorOf(Props[InfoActor], name = "askPatternMapToActor")
    val infoActorFound: Future[Boolean] = (infoActor ? Info("askPatternMapTo")).mapTo[Boolean]

    for {
      found <- infoActorFound
    } yield println(s"ask Pattern Map To Actor Found = $found")
  }

  def askPatternPipeTo(system: ActorSystem): Unit = {
    implicit val timeout = Timeout(5 second)
    val infoActor = system.actorOf(Props[InfoActor], name = "askPatternPipeToActor")
    val infoActorFound: Future[Int] = (infoActor ? Info("askPatternPipeTo")).mapTo[Int]

    for {
      found <- infoActorFound
    } yield println(s"ask Pattern Pipe To Actor Found = $found")
  }


  def findActor(system: ActorSystem): Unit = {
    system.actorSelection("/user/*") ! Info("chocolate")
  }

  def actorPoisionPill(system: ActorSystem): Unit = {
    val infoActor = system.actorOf(Props[InfoActor], name = "tellPatternActor")
    infoActor ! Info("tpattern")
    infoActor ! PoisonPill
    infoActor ! Info("plain")
  }


  /*def getserverStats(system: ActorSystem): Unit = {
    system.actorSelection("/user/IO-HTTP/listener-0").resolveOne().map {
      ref =>
        val statFuture = (ref ? Http.GetStats)
        statFuture.onComplete({
          case Success(stats) => println("stats are " + stats)
          case Failure(t) => t.printStackTrace
        })
    }
  } */

}
