package taxipark

import kotlin.math.roundToInt

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    this.allDrivers.minus(
        this.trips.map { it.driver }).toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger>
    { var  a:Set<Passenger>
        if(minTrips > 0 ) {
            a = this.trips
                .flatMap { it.passengers }
                .groupingBy { it.name }
                .eachCount().filter { it.value >= minTrips }
                .keys.map { Passenger(it) }.toSet()
        }
        else {
            a = this.allPassengers
        }
        return a
    }

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    this.trips.filter { it.driver == driver }
        .flatMap { it.passengers }
        .groupingBy { it.name }.eachCount()
        .filter { it.value > 1 }
        .keys.map { Passenger(it) }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
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
        }.keys.map { Passenger(it) }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange?
     {
        var result:Int?
        var resultrange:IntRange?
        if (this.trips.count() == 0 ) return null
        var maxtrip:Int? = this.trips
            .map { it.duration }.max()
        var mapres: Map<Int,Int> = mutableMapOf()
        var i:Int = 0
        var j:Int = 0
        do {
            j = this.trips.map { it.duration }
                .filter { it in i..i+9 }.count()
            mapres=  mapres + Pair(j,i)
            i += 10
            if ( i > maxtrip!!) break
        }while (true)
        result = mapres.get( mapres.keys.max())
        resultrange = result!!..result!!+9
        return resultrange
    }


/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.count() == 0) return false
    var totaldriverscount:Int = this.allDrivers.sortedBy { it.name }.distinct().count()
    var totalincome:Double = this.trips.sumByDouble {
        it.cost
    }
    var superdriverscount:Int  = (totaldriverscount*20)/100
    var eightypercentincome:Double = (totalincome* 80)/100
    var incomeslist  = this.trips.groupingBy { it.driver.name }
        .aggregateTo(mutableMapOf()){
                key, accumulator: Double?, element, first ->
            if (first)
                element.cost
            else
                accumulator?.plus(element.cost)
        }
    var driverincomepares = listOf(Pair(incomeslist.values,incomeslist.keys))
    var superdriverstotal = driverincomepares.flatMap { it.first }
        .sortedByDescending { it }
        .filterIndexed { index, d ->  index < superdriverscount}
        .sumByDouble { it?:0.0 }
    return if(eightypercentincome - superdriverstotal > 0) false else true
}
