package mm

case class UpdateCounter(c: Int) extends AnyVal
case class Counter(c: Int) extends AnyVal
case class GCounterUid(uid: Int) extends AnyVal
case class GCounterState(uid: GCounterUid, counter: Counter)

/**
  * The state-based increase-only counter
  */
class StateBasedGCounter(uid: GCounterUid, counter: Counter, otherCounters: Map[GCounterUid, Counter]) {

  lazy val value: Int = counter.c + otherCounters.values.map(_.c).sum

  def update(change: UpdateCounter): (StateBasedGCounter, GCounterState) = {
    (new StateBasedGCounter(uid, Counter(counter.c + change.c), otherCounters), GCounterState(uid, counter))
  }

  def merge(other: GCounterState): StateBasedGCounter = {
    val newValue = Counter(other.counter.c max otherCounters.getOrElse(other.uid, Counter(0)).c)
    val others = otherCounters + (uid -> newValue)

    new StateBasedGCounter(uid, newValue, others)
  }

}
