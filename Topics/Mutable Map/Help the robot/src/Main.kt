fun helpingTheRobot(purchases: Map<String, Int>, addition: Map<String, Int>) : MutableMap<String, Int> {
    //write your code here
    val purchasesMutable = purchases.toMutableMap()
    for ((k,v) in addition){
        if (purchasesMutable.containsKey(k)){
            purchasesMutable[k] = purchasesMutable[k]!! + v
        }else{
            purchasesMutable[k] = v
        }
    }
    return  purchasesMutable
}

