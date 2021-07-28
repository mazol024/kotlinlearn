package nicestring

fun main(){
    val testlist = listOf<String>("bac","aza","abaca","baaa","aaab")
    testlist.forEach { println( "The string $it is nice ->  ${it.isNice()} ") }
}

fun String.isNice(): Boolean {
    println(" subs->  ${noSubs(this)}, vowels -> ${vowelS(this)}, dubs -> ${dubS(this)}")
    return noSubs(this) + vowelS(this) + dubS(this) >=2
}

fun noSubs(a:String):Int{
    var aa = 1
    if (a.contains("bu")
        || a.contains("ba")
        || a.contains("be")) {
        aa = 0
    }
    return aa
}
fun vowelS(a:String): Int {
    var aa = 0
    val vv = "aeiou"
    val counter = a.toCharArray().count {vv.contains(it)}
    if(counter >=3 ) {
        aa = 1
    }
    return aa
}
fun dubS(a:String):Int{
    var aa = 0
    val bb = a.toCharArray().toList().zipWithNext().count { it.first == it.second }
    if (bb>=1) { aa = 1 }
    return aa
}