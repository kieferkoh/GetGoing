data class User(
    var name: String,
    var phone: String,
    var password: String,
    var friends: ArrayList<String>? = null,
    var friendreqs: ArrayList<String>? = null,
    var groups: ArrayList<String>? = null,
    var location: String? = null
)
