package mm

/**
  * the grow-only set
  */
case class AddElement[A](a: A)
case class GSetState[A](set: Set[A])

class StateBasedGSet[A](val set: Set[A]) {

  def contains(a: A): Boolean = set(a)

  def update(change: AddElement[A]): (StateBasedGSet[A], GSetState[A]) = {
    val newSet = new StateBasedGSet(set + change.a)
    (newSet, GSetState(newSet.set))
  }

  def merge(other: GSetState[A]): StateBasedGSet[A] = {
    new StateBasedGSet(set ++ other.set)
  }
}
