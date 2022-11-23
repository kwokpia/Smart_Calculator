fun addUser(userMap: Map<String, String>, login: String, password: String): MutableMap<String, String> {
    // write your code here
   return if (userMap.containsKey(login)){
       println("User with this login is already registered!")
       userMap.toMutableMap()
    }else{
       userMap.toMutableMap().apply { put(login,password) }
    }

}