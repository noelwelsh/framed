// type IsFrame[T <: Tuple] =
//   T match {
//     case Unit => Unit
//     case Col[k, v] *: cols => Col[k, v] *: cols
//   }
import scala.compiletime._
import scala.compiletime.ops.any

final case class Col[Key, Value](data: Array[Value]) 

object Frame {
  object Types {
    type IndexOf[F <: Tuple, Key] <: Int =
      F match {
        case Col[Key, v] *: cols => 0
        case Col[k, v] *: cols => S[IndexOf[cols, Key]]
      }

    type Get[F <: Tuple, Key] = 
      F match {
        case Col[Key, v] *: cols => v
        case Col[k, v] *: cols => Get[cols, Key]
        case _ => Nothing
      }
      // Tuple.Elem[F, IndexOf[F, Key]]
  }

  inline def get[Key, F <: Tuple](frame: F): Array[Types.Get[F, Key]] =
    // frame.apply(constValue[Types.IndexOf[F, Key]])
    inline frame match {
      case Unit => error("The frame did not have an element with the given key.")
      case (c: Col[k, v]) *: cols => 
        inline if(constValue[any.==[Key, k]]) c.data.asInstanceOf[Array[Types.Get[F, Key]]]
               else get[Key, cols.type](cols).asInstanceOf[Array[Types.Get[F, Key]]]
    }

  val frame: Col["firstName", String] *: Unit = (Col["firstName", String](Array("Dotty")) *: ())
  get[Key="firstName"](frame)
}
// sealed trait Frame {
//   import Frame._

//   def ::[Key,V](data: Array[V]): Pair[Key,V,this.type] =
//     new Pair(data, this)

//   /** 
//     * Given a key get the data associated with that key.
//     */
//   inline def get[Key]: Types.Get[this.type, Key] =
//     inline this match {
//       case Empty => error("There is no column in this data frame with the given key.")
//       case p: Pair[k, v, _] => 
//         inline if(constValue[any.==[Key, k]]) p.data.asInstanceOf[Types.Get[this.type, Key]]
//         else p.tail.get[Key].asInstanceOf[Types.Get[p.tail.type, Key]]
//     }

//   // inline def select[Keys <: Tuple] <: Frame =
//   //   inline erasedValue[Keys] match {
//   //     case () => Empty
//   //     case _: *:[k,ks] => select[ks].::[Key=k](get[k])
//   //   }
// }
// object Frame {
//   val empty: Empty.type = Empty

//   final case class Pair[Key,V, Tail <: Frame](data: Array[V], tail: Tail) extends Frame
//   case object Empty extends Frame

//   object Types {
//     type Get[F <: Frame, Key] =
//       F match {
//         case Pair[Key, v, t] => Array[v]
//         case Pair[k, _, t] => Get[t, Key]
//       }
//   }
// }

// object FrameExamples {
//   val name = Frame.empty.::[Key="firstName"](Array("John")).::[Key="lastName"](Array("Doe"))
//   val firstName: Array[String] = name.get["firstName"]
//   // val empty: Frame.Empty.type = name.select["Age"]
// }
