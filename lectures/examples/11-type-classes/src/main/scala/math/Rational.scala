package math

import scala.annotation.tailrec
import scala.language.implicitConversions

class Rational(n: Int, d: Int = 1) extends Ordered[Rational] {
  require(d != 0)

  val (numer, denom) = {
    val div = gcd(n.abs, d.abs)

    (d.signum * n / div, d.abs / div)
  }

  @tailrec
  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

  def unary_- = new Rational(-numer, denom)
  def inverse = new Rational(denom, numer)

  def +(that: Rational) = new Rational(
    numer * that.denom + that.numer * denom,
    denom * that.denom
  )
  def -(that: Rational) = this + (-that)
  def *(that: Rational) = new Rational(
    numer * that.numer,
    denom * that.denom
  )
  def /(that: Rational) = this * that.inverse

  override def equals(other: Any): Boolean = other match {
    case that: Rational => numer == that.numer && denom == that.denom
    case _ => false
  }
  override def hashCode(): Int = (numer, denom).##

  override def toString: String = s"$numer/$denom"

  override def compare(that: Rational): Int = (this - that).numer
}

object Rational {
  def apply(n: Int, d: Int = 1) = new Rational(n, d)

  implicit def intToRational(n: Int): Rational = new Rational(n)

  implicit val rationalAdditiveMonoid = new Monoid[Rational] {
    def op(a: Rational, b: Rational): Rational = a + b

    val identity: Rational = 0
  }

  val rationalMultiplicativeMonoid = new Monoid[Rational] {
    def op(a: Rational, b: Rational): Rational = a * b

    val identity: Rational = 1
  }
}