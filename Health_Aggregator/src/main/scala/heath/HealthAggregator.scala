package heath;
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import java.util.concurrent.TimeoutException
import akka.actor.Actor

/*case class SetHealthResource(name: String, status: Boolean)
case object GetHealthResource*/

class HealthAggregator {

  val system = ActorSystem("HealthSystem")
  val healthActor = system.actorOf(Props[Health], name = "healthActor")

  def SetResource(resourceName: String, isHealthy: Boolean) = {
    healthActor ! (resourceName, isHealthy)
  }

  def IsHealthy(): Boolean = {
    try {
      implicit val timeout = Timeout(30 seconds)
      val future = healthActor ? "GetHealthResource"
      Await.result(future, timeout.duration).asInstanceOf[Boolean]
    } catch {
      case ex: TimeoutException =>
        ex.printStackTrace()
        false
    }
  }
}

class Health extends Actor {

  var healthReports = Map[String, Boolean]()

  def receive = {
    case (name: String, status: Boolean) => healthReports += (name -> status)
    case "GetHealthResource" => {
      val helthStatus = healthReports.values.toList
      sender ! reduceList(helthStatus)
    }
    case _ => println("pattern not found")
  }

  def reduceList(ls: List[Boolean]): Boolean = {
    def f(res: Boolean, ls: List[Boolean]): Boolean = ls match {
      case Nil     => res
      case x :: xs => f(res && x, xs)
    }
    f(true, ls)
  }
}

