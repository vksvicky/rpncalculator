import java.lang.Double.parseDouble

import scala.collection.mutable
import scala.io.Source

/**
 * Created by vivek on 03/07/2014.
 */
object sRPNCalculator {
  // Maps an operator to a function .
  val ops = Map("+" -> ((_: Double) + (_: Double)),
    "-" -> (-(_: Double) + (_: Double)),
    "*" -> ((_: Double) * (_: Double)),
    "/" -> (1 / (_: Double) * (_: Double)))

  def main(args: Array[String]) = {
    // Read line by line from stdin + tokenize line + evaluates line
    Source.fromInputStream(System.in).getLines.foreach(l =>
      System.out.printf("Expression: %.2f", "%.2f".format(evalTokens(l.split(" ")))))
  }

  // Evaluate RPN expr (given as string of tokens)
  def evalTokens(tokens: Array[String]): Double = {
    val stack = new mutable.Stack[Double]
    tokens.foreach(tok => {
      if (ops.contains(tok)) stack.push(ops(tok)(stack.pop, stack.pop))
      else stack.push(parseDouble(tok))
    })
    stack.pop
  }
}