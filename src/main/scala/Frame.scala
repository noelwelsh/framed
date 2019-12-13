sealed trait Col[Name,Value]


sealed abstract class Frame extends Product with Serializable {
  import Frame._
  def ::[K,V]: Pair[K,V,this.type] =
    Pair(this)

  def select[Name] given (w: FrameTypeFunctions.Select[this.type, Name] >:> Nothing): FrameTypeFunctions.Select[this.type,Name] =
    ???
}
object Frame {
  final case class Pair[K,V,Tail <: Frame](tail: Tail) extends Frame
  case object Empty extends Frame

  def empty: Empty.type = Empty
}
// enum Frame {
//   case Empty
//   case Pair[K,V,Tail <: Frame](tail: Tail)

//   def ::[K,V]: Pair[K,V,this.type] = {
//     new Pair[K,V,this.type](this)
//   }

//   def select[Name]: FrameTypeFunctions.Select[this.type,Name] =
//     ???
// }
object FrameExamples {
  val empty: Frame.Empty.type = Frame.Empty

  val name = empty.::["name",String]
  val select = name.select["name"]
  empty.select["name"]
}

object FrameTypeFunctions {
  import Frame._

  type Select[A <: Frame,Name] <: Frame =
    A match {
      case Pair[Name,v,t] => Pair[Name,v, Select[t,Name]]
      case Pair[_,_,t]    => Select[t,Name]
      case Empty.type     => Empty.type
    }
}

// /**
//  * A data frame contains a description of operations that should be performed on
//  * data.
//  *
//  * Implementation is just proof of concept for now
//  */
// enum Frame[A <: Tuple] {
//   import FrameTypeFunctions._

//   case Pure()

//   def add[Name,Value]: Frame[Col[Name,Value] *: A] =
//     Pure()

//   def select[Name]: Frame[Select[A,Name]] =
//     Pure[Select[A,Name]]()
// }
