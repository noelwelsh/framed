# Framed: Typed Dataframe for Scala 3

It's clearly possible to implement a dataframe library in Scala---Spark has one, for example. What's not clear is if it's possible to implement one that uses the type system to prevent common errors. For example, in Spark it's a runtime error to select a column that doesn't exist (Spark has typed dataframes but in practice they are unusable; all real systems I know of use the untyped dataframes). Can this be prevented at compile time? If so, it seems it could be a significant UX improvement over current systems.

This project is an experiment in creating a typed dataframe in Scala 3. Scala 3's compile-time metaprogramming features should make this possible in a way that was not achievable in Scala 2 with reasonable UX. 

There are two goals for this project:

1. Show it's possible to implement a typed dataframe in Scala 3
2. Show that such a dataframe is usable---compile times are reasonable and error messages are not incomprehensible


## Basic Design

The key idea is to represent a dataframe as a tuple or heterogeneous list. Each element in the tuple is a column in the dataframe. At the type level we store, for each column, the type of the data in the column and the name of the column as a [String type literal](https://docs.scala-lang.org/sips/42.type.html). Operations on dataframes can then be expressed as structural recursion at the type level over this representation, probably using [match types](https://docs.scala-lang.org/scala3/reference/new-types/match-types.html).

To prove this can actually work it should be sufficient to implement a few core methods:

- selecting a column by name
- selecting several columns by name
- adding a column
- dropping a column
- joining two dataframes (maybe just inner join to start with?)

If these methods can be implemented I'm confident that it will be possible to implement other methods using the same techniques. This meets goal 1.


## API Design

To meet goal 2 requires careful API design. It's not clear what the final API should be, nor what techniques will be necessary to present it a way that will be usable by data scientists (who are usually not Scala experts.)

Should look to the literature for API design. See the reading below.


## Reading

I'm not really familiar with the literature on typed dataframes. Presumably it exists---it's such an obvious thing to look at---but I haven't found much. Here are some papers that look interesting:

- [Types for Tables: A Language Design Benchmark](http://cs.brown.edu/~sk/Publications/Papers/Published/lgk-b2t2/). I've started on this and I'm pretty sure it will be essential reading.

- [Is a Dataframe Just a Table?](https://drops.dagstuhl.de/opus/volltexte/2020/11960/)

- [Categorical Data Structures for Technical Computing](https://arxiv.org/abs/2106.04703). Abstract looks interesting, but will I actually be able to understand it? There is code accompanying it, so there is hope if the Latex gets too involved.

- [Towards Scalable Dataframe Systems](https://arxiv.org/abs/2001.00888). I like the idea of defining an algebra for dataframes.
