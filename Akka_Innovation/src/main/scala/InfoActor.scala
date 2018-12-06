import akka.actor.{Actor, ActorLogging, Props}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InfoActor extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("prestart")

  override def postStop(): Unit = log.info("postStop")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = log.info("preRestart")

  override def postRestart(reason: Throwable): Unit = log.info("postRestart")

  /*def receive = {
    case Info(name) =>
      log.info(s"Found $name")
      getDatabaseQueryRes(name) //.pipeTo(sender)
  }*/

  private def getDatabaseQueryRes(name: String): Future[Int] = Future {
    // assume a long running database operation to find stock for the given donut
    100
  }

  /*def receive = {
    case Info(name) if name == "dev" =>
      log.info(s"Found valid $name")
      sender ! true

    case Info(name) =>
      log.info(s"$name is not supported")
      sender ! false
  }*/


  val childActor = context.actorOf(Props[ChildActor], name = "ChildActor")

  def receive = {
    case msg @ Info(name) =>
      log.info(s"Found $name")
      childActor forward msg
  }
}

class ChildActor extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("prestart")

  override def postStop(): Unit = log.info("postStop")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = log.info("preRestart")

  override def postRestart(reason: Throwable): Unit = log.info("postRestart")

  def receive = {
    case Info(name) =>
      log.info(s"ChildActor $name")
  }
}