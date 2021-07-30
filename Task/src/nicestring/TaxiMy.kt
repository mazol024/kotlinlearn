package taxipark


fun main(){
    var tp1 =  taxiPark(0..6, 0..9,
        trip(2, listOf(9), duration = 9, distance = 36.0),
        trip(1, listOf(0,7), duration = 15, distance = 28.0),
        trip(2, listOf(1), duration = 37, distance = 30.0),
        trip(0, listOf(9), duration = 24, distance = 10.0),
        trip(1, listOf(2), duration = 1, distance = 6.0),
        trip(0, listOf(0, 9), duration = 9, distance = 7.0),
        trip(2, listOf(1,2,3,7,8), duration = 18, distance = 39.0, discount = 0.1),
        trip(1, listOf(1,3,9,4), duration = 19, distance = 1.0, discount = 0.2),
        trip(1, listOf(3), duration = 16, distance = 23.0),
        trip(5, listOf(3,5,2), duration = 16, distance = 123.0),
        trip(2, listOf(3,7), duration = 10, distance = 31.0, discount = 0.2))
    println(" test 1 ${tp1.findFake()}")
    println(" test 2 ${tp1.findFaith(3)}")
    println(" test 3 ${tp1.findFrequentPass(Driver("D-1"))}")
    println(" test 4 ${tp1.findSmartPass()}")
    println(" test 5 ${tp1.findTheMostFrequentTripDuration()}")

}
fun TaxiPark.findFake(): Set<Driver> =
    this.allDrivers.minus(
        this.trips.map { it.driver }).toSet()

fun TaxiPark.findFaith(minTrips: Int): List<Passenger> =
    this.trips.flatMap { it.passengers }
        .groupingBy{ it.name }.eachCount().filter { it.value >2 }
        .keys.map { Passenger(it) }

fun TaxiPark.findFrequentPass(driver: Driver):List<Passenger> =
    this.trips.filter { it.driver == driver }
        .flatMap { it.passengers }
        .groupingBy { it.name }.eachCount()
        .filter { it.value > 1 }
        .keys.map { Passenger(it) }

fun TaxiPark.findSmartPass(): Collection<Passenger> =
    this.trips.filter { it.discount != null  }
        .flatMap { it.passengers }
        .groupingBy { it.name }
        .eachCount()
        .filter { it1 ->
            this
                .trips
                .filter { it.discount == null  }
                .flatMap { it.passengers }
                .groupingBy { it.name }
                .eachCount().getOrDefault(it1.key,0) < it1.value
        }.keys.map { Passenger(it) }

fun TaxiPark.findTheMostFrequentTripDuration(): IntRange? =
    this.trips.map { it.duration }
        .sorted()
        .groupingBy { it -> when {
            it in 0..9 -> "one"
            it in 10..19 -> "two"
            it in 20..29 -> "three"
            it in 30..39 -> "four"
            else -> "out"
        } }
        .eachCount()
        .maxBy { it.value }
        .let { it -> when {
            it?.key == "one" -> 0..9
            it?.key == "two" -> 10..19
            it?.key == "three" -> 20..29
            it?.key == "four" -> 30..39
            else -> 40..99
        }
             }
