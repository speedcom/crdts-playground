package mm

/**
  * The operation-based increase-only counter
  */
class OperationBasedCounter(count: Int) {

  def value = count

  def update(change: UpdateCounter): (OperationBasedCounter, UpdateCounter) = {
    (new OperationBasedCounter(count + change.c), change)
  }

  def merge(operation: UpdateCounter): OperationBasedCounter = update(operation)._1

}
