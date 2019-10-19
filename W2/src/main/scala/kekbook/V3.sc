object V3 {
  case class User(id: Long, name: String, isPrivate: Boolean)

  object TestData {
    val Sait      = User(42, "Sait", isPrivate = false)
    val Volodymyr = User(123, "Volodymyr", isPrivate = false)
    val Wagdy     = User(353, "Wagdy", isPrivate = true)
    val Madi      = User(847, "Madi", isPrivate = false)
    val Khushbu   = User(928, "Khushbu", isPrivate = false)
  }

  object SearchAlgorithms {

    // Partial function that is defined if name contains the given keyword
    def nameContains(keywords: List[String]): PartialFunction[User, User] = {
      case u if keywords.map(_.trim.toLowerCase).exists(u.name.trim.toLowerCase.contains) => u
    }
  }

  object SearchManager {

    def publicProfiles: List[User] => List[User] = { users =>
      users.filterNot(_.isPrivate)
    }

    def withoutSelf(requester: User): List[User] => List[User] = { users =>
      users.filter(_ != requester)
    }

    def searchKeyword(keywords: String*): List[User] => List[User] = { users =>
      val algorithm = SearchAlgorithms.nameContains(keywords.toList)

      users.collect(algorithm)
    }

    def allowedUsers(requester: User, blacklist: Map[User, User]): List[User] => List[User] = { users =>
      // Closure which depends to requester out of the block
      def whomBannedRequester: PartialFunction[(User, User), User] = {
        case (actor, banned) if banned == requester => actor
      }

      val bannedBy = blacklist.collect(whomBannedRequester).toList

      users.diff(bannedBy)
    }
  }

  class Connection {
    println("A connection created")
    // some thread pool logic

    // runs your query and returns result
    def query(q: String) = Some(println(s"Query run: $q"))
  }

  class Database(connection: => Connection) {

    import TestData._

    def users: List[User] =
      connection
        .query("select * from users")
        .map(_ => List(Sait, Volodymyr, Wagdy, Madi, Khushbu))
        .getOrElse(List.empty)

    def blacklist: Map[User, User] =
      connection
        .query("select * from blacklist")
        .map(_ => Map(Volodymyr -> Sait))
        .getOrElse(Map.empty)
  }

  object KekBook {
    import SearchManager._

    val db = new Database(new Connection())

    // Curried function which blacklist is provided
    val allowedUsersWithKnownBlacklist: User => List[User] => List[User] = allowedUsers(_, db.blacklist)

    def search(requester: User, keywords: String*): List[User] = {
      // Compose desired functions
      val queryFunction = publicProfiles
        .andThen(allowedUsersWithKnownBlacklist(requester))
        .andThen(searchKeyword(keywords: _*))
        .andThen(withoutSelf(requester))

      queryFunction(db.users)
    }
  }

}

import V3.TestData._

V3.KekBook.search(Sait, "mad", "khush ", "vol", "wagdy")
