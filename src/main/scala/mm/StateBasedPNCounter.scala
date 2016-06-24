package mm

/**
  * The state-based balance counter
  */
class StateBasedPNCounter private (incCounter: StateBasedGCounter, decCounter: StateBasedGCounter) {

  def value: Int = incCounter.value - decCounter.value

  def update(change: UpdateCounter): (StateBasedPNCounter, PNCounter) = {
    val (newIncCounter, newDecCounter, pnCounter) = {
      change match {
        case UpdateCounter(v) if v >= 0 =>
          val (iC, iState) = incCounter.update(change)
          val dState = GCounterState(decCounter.uid, Counter(decCounter.value))
          (iC, decCounter, PNCounter(iState, dState))
        case UpdateCounter(v) if v < 0 =>
          val (dC, dState) = decCounter.update(change)
          val iState = GCounterState(incCounter.uid, Counter(incCounter.value))
          (incCounter, dC, PNCounter(iState, dState))
      }
    }
    (new StateBasedPNCounter(newIncCounter, newDecCounter), pnCounter)
  }

  def merge(other: PNCounter): StateBasedPNCounter = {
    new StateBasedPNCounter(
      incCounter.merge(other.incState),
      decCounter.merge(other.decState)
    )
  }

}

case class PNCounter(incState: GCounterState, decState: GCounterState)