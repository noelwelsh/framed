package framed

import scala.compiletime._
import scala.compiletime.ops.any

final case class Col[Key, Value](data: Array[Value])

sealed abstract class Frame:
  import Frame.Types._

  inline def contains[K]: Boolean

  inline def get[K]: Get[this.type, K]

  inline def indexOf[K]: Int

object Frame:
  final case class Cons[Key, Value, Tail <: Frame](col: Col[Key, Value], tail: Tail) extends Frame:
    import Types._

    inline def contains[K]: Boolean =
      inline if(constValue[any.==[Key, K]]) true
      else tail.contains[K]

    inline def get[K]: Get[this.type, K] =
      inline if(constValue[any.==[Key, K]]) col.data.asInstanceOf[Select[this.type, K]]
      else tail.select[K].asInstanceOf[Select[this.type, K]]

    inline def indexOf[K]: Int =
      inline if(constValue[any.==[Key, K]]) 0
      else 1 + tail.indexOf[K]


  case object Empty extends Frame:
    import Types._

    inline def contains[K]: Boolean = false

    inline def get[K]: Get[this.type, K] =
      error("This data frame does not contain a column with the given name.")

    inline def indexOf[K]: Int =
      error("This data frame does not contain a column with the given name.")


  object Types:
    type Get[F <: Frame, K] =
      F match {
        case Empty.type    => Nothing
        case Cons[K, v, _] => v
        case Cons[k, v, t] => Select[t, K]
      }
