package heath

object Main extends App {
  val health = new HealthAggregator
  health.SetResource("r1", true)
  health.SetResource("r6", true)
  health.SetResource("r2", true)
  health.SetResource("r3", true)
  health.SetResource("r4", true)
  health.SetResource("r5", true)
  health.SetResource("r6", false)
  health.SetResource("r7", true)
//  health.SetResource("r6", true)
//  health.SetResource("r2", false)
  
  println(health.IsHealthy())
}