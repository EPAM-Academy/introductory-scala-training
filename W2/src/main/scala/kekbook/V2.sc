object V2 {
  case class User(id: Long, name: String, isPrivate: Boolean = false)

  object TestData {
    val Sait      = User(42, "Sait")
    val Volodymyr = User(123, "Volodymyr")
    val Wagdy     = User(353, "Wagdy", isPrivate = true)
    val Madi      = User(847, "Madi")
    val Khushbu   = User(928, "Khushbu")
  }

  class Connection {
    println("A connection created")

    // some thread pool logic

    // runs your query and returns result
    def query(q: String) = {
      println(s"Query run: $q")
      ()
    }
  }

  class Database(connection: => Connection) {

    import TestData._

    def users: List[User] = {
      connection.query("select * from users")
      List(Sait, Volodymyr, Wagdy, Madi, Khushbu)
    }

    def blacklist: Map[User, User] = {
      connection.query("select * from blacklist")
      Map(Volodymyr -> Sait)
    }
  }

  object SearchManager {

    def filterOutPrivate(users: List[User]): List[User] = users.filterNot(_.isPrivate)

    def filterOutBlacklisted(requester: User, blacklist: Map[User, User], users: List[User]): List[User] = {
      val banExclusion = blacklist.collect { case (actor, banned) if actor == banned => actor }.toSet

      users.filterNot(banExclusion.contains)
    }
  }

  object KekBook {
    import SearchManager._

    val db = new Database(new Connection())

    // Varargs
    def search(requester: User, keywords: String*): List[User] = {

      val publicUsers      = filterOutPrivate(db.users)
      val publicAndAllowed = filterOutBlacklisted(requester, db.blacklist, publicUsers)

      publicAndAllowed.filter(u => keywords.exists(u.name.contains))
    }
  }
}

import V2.TestData._

V2.KekBook.search(Sait, "Ma", "Volod")
