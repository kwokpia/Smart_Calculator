fun summator(map: Map<Int, Int>): Int {
    // put your code here
  return  map.entries.sumOf { if (it.key%2 == 0) it.value else 0 }
}