import java.time.Instant

val now = Instant.now()

def timeNow = now

@scala.annotation.tailrec
  def window[T](xs: List[T])(f: T => Long): List[T] = xs match {
    case head :: tail if f(head) < timeNow.toEpochMilli => window(tail)(f)
    case _                                              => xs
  }

case class TimedEvent(event: String, timestamp: Instant)

val events = List(
    TimedEvent("past", now.minusMillis(500)),
    TimedEvent("now", now),
    TimedEvent("future", now.plusMillis(400))
  )

window(events)(_.timestamp.toEpochMilli)