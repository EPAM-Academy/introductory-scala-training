def safeDivide: PartialFunction[(Int, Int), Double] = {
    case (dividend, divisor) if divisor != 0 => dividend.toDouble / divisor
  }

safeDivide(8, 2)
safeDivide(5, 2)

safeDivide(5, 0)

// Since generic types used in the same parameter group scala compiler
// having difficulties with inferring the type. Something currying can help with
safeDivide.applyOrElse((5, 0), (failed: (Int, Int)) => 0d)

val fiveOverZero = safeDivide.isDefinedAt(5, 0)
val fiveOverOne = safeDivide.isDefinedAt(5, 1)

def divide(dividend: Int, divisor: Int): Option[Double] = {
    if (safeDivide.isDefinedAt(dividend, divisor))
      Some(safeDivide(dividend, divisor))
    else
      None
  }

divide(5, 5)
divide(5, 0)
