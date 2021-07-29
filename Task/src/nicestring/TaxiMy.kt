package taxipark

fun main(){
    var tp = taxiPark(0..6, 0..9,
        trip(2, listOf(9), duration = 9, distance = 36.0),
        trip(1, listOf(0,5), duration = 15, distance = 28.0),
        trip(2, listOf(1), duration = 37, distance = 30.0),
        trip(0, listOf(5,9), duration = 24, distance = 10.0),
        trip(5, listOf(1,4,7,9), duration = 24, distance = 10.0),
        trip(1, listOf(2), duration = 1, distance = 6.0),
        trip(0, listOf(0, 9), duration = 9, distance = 7.0),
        trip(2, listOf(3, 2, 8), duration = 18, distance = 39.0, discount = 0.1),
        trip(1, listOf(9, 4), duration = 19, distance = 1.0, discount = 0.2),
        trip(1, listOf(3), duration = 16, distance = 23.0),
        trip(2, listOf(5,7), duration = 10, distance = 31.0, discount = 0.2))


    println("Drivers fake test  ${tp.findFake()}")
    println("Passenger test  ${tp.findFaith(2)}")
}

//fun
fun TaxiPark.findFake(): Set<Driver> =
    this.allDrivers.minus(
        this.trips.map { it.driver }).toSet()

fun TaxiPark.findFaith(minTrips: Int) =
    this.trips.flatMap{ it.passengers }
        .groupingBy{ it.name }.eachCount().filter { it.value >2 }
