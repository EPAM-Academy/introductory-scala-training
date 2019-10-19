object V1 {
  case class User(id: Long, name: String)

  object KekBook {

    def search(keyword: String, users: List[User]): List[User] = {
      users.filter(_.name.contains(keyword))
    }
  }

}

val users = List(
    V1.User(42, "Sait"),
    V1.User(123, "Volodymyr"),
    V1.User(353, "Wagdy"),
    V1.User(847, "Madi"),
    V1.User(928, "Khushbu")
  )

V1.KekBook.search("dy", users)