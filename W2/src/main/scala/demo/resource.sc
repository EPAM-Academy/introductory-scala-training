case class Connection(number: Int) {
  println(s"Connection created with number $number")
  def execute()   = println(s"Something executed with connection $number")
  def close: Unit = println(s"Connection $number is closed")
}

def resource[T](allocate: => T)(deallocate: T => Unit)(run: T => Unit): Unit = {
    val res = allocate
    run(res)
    deallocate(res)
  }

resource(Connection(5))(_.close) { connection =>
    // do something with connection
    connection.execute()
  }
