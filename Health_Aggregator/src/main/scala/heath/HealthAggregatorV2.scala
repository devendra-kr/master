package heath

import java.util.concurrent.locks.ReentrantReadWriteLock
import scala.collection.mutable

class HealthAggregatorV2 {

  private[this] val healthReports = mutable.Map[String, Boolean]()
  private[this] val rwLock = new ReentrantReadWriteLock()

  def SetResource(resourceName: String, isHealthy: Boolean) = {
    withWriteLock(rwLock) {
      healthReports += (resourceName -> isHealthy)
    }
  }

  def IsHealthy(): Boolean = {
    withReadLock(rwLock) {
      healthReports.values.reduce(_ && _)
    }
  }

  def withWriteLock[B](rwLock: ReentrantReadWriteLock)(fn: => B): B = {
    rwLock.writeLock().lock()
    try {
      fn
    } finally {
      rwLock.writeLock().unlock()
    }
  }

  def withReadLock[B](rwLock: ReentrantReadWriteLock)(fn: => B): B = {
    rwLock.readLock().lock()
    try {
      fn
    } finally {
      rwLock.readLock().unlock()
    }
  }
}