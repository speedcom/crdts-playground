package mm

case class UpdateCounter(c: Int) extends AnyVal
case class Counter(c: Int) extends AnyVal
object Counter {
  val zero = Counter(0)
}
case class GCounterUid(uid: Int) extends AnyVal
case class GCounterState(uid: GCounterUid, counter: Counter)

/**
  * The state-based increase-only counter
  */
case class StateBasedGCounter(uid: GCounterUid, counter: Counter, otherCounters: Map[GCounterUid, Counter] = Map()) {

  lazy val value: Int = counter.c + otherCounters.values.map(_.c).sum

  def update(change: UpdateCounter): (StateBasedGCounter, GCounterState) = {
    val newValue = Counter(counter.c + change.c)
    (new StateBasedGCounter(uid, newValue, otherCounters), GCounterState(uid, newValue))
  }

  def merge(other: GCounterState): StateBasedGCounter = {
    val newValue = Counter(other.counter.c max otherCounters.getOrElse(other.uid, Counter(0)).c)
    val others = otherCounters + (uid -> newValue)

    new StateBasedGCounter(uid, newValue, others)
  }

}

object StateBasedGCounterApp extends App {

  val sbc1 = new StateBasedGCounter(GCounterUid(1), Counter.zero)
  val sbc2 = new StateBasedGCounter(GCounterUid(2), Counter.zero)
  val sbc3 = new StateBasedGCounter(GCounterUid(3), Counter.zero)

  println("update first counter by client")
  val (sbc1_1, sbc1_1_counter) = sbc1.update(UpdateCounter(2))

  println("update msg is sent from sbc1 to sbc2 counter")
  val (sbc2_1) = sbc2.merge(sbc1_1_counter)

  println("update second counter by client")
  val (sbc2_2, sbc2_2_counter) = sbc2_1.update(UpdateCounter(10))

  println("update first counter after second was updated by client")
  val sbc1_2 = sbc1_1.merge(sbc2_2_counter)

  println("Final resolution:")
  println("1. counter: " + sbc1_2)
  println("2. counter: " + sbc2_2)

}
