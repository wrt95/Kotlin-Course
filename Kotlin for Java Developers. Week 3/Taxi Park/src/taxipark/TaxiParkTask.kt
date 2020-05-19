package taxipark

/**
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers.filter { theDriver: Driver ->
        // Get the drivers that are not in
        theDriver !in trips.map { trip: Trip ->
            trip.driver
        }
    // Make it to a set
    }.toSet()

/**
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { thePassenger: Passenger ->
        trips.filter {
            // Filter out the passengers in the trip
            trip: Trip -> thePassenger in trip.passengers
        // count the number of trips, and check that it is greater than or equal to the minimum trips
        }.count() >= minTrips
    // Make it to a set
    }.toSet()

/**
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    allPassengers.filter { thePassenger: Passenger ->
        trips.filter {
            // Filter when the passenger is in this trips passenger, and this trips
            // driver in the given driver
            trip: Trip -> thePassenger in trip.passengers && trip.driver == driver
        // Check that count is greater than 1
        }.count() > 1
    // Make a set of it
    }.toSet()

/**
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers.filter { thePassenger: Passenger ->
        // Get where passenger and discount exists
        trips.filter { theTrip: Trip ->
            // Filter when the passenger is the trips passenger, and where
            // the discount of this trip is not null
            thePassenger in theTrip.passengers && theTrip.discount != null
        // Check that it is greater than when the discount is null
        }.count() >
        trips.filter { theTrip: Trip ->
            // Get the passenger in the trip, and where the discount is null
            thePassenger in theTrip.passengers && theTrip.discount == null
        // Count the occasions
        }.count()
    // Make it to a set
    }.toSet()

/**
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    // Map all the different durations on the list of trip durations, this so we get
    // the correct minute periods
    return trips.map { theTrip: Trip ->
        theTrip.duration - (theTrip.duration%10)..(theTrip.duration+9-(theTrip.duration%10))
    // Group it by the range
    }.groupingBy {theRange: IntRange ->
        theRange
    // Count each element in the group, return the first largest value
    }.eachCount().maxBy { rangeMap: Map.Entry<IntRange, Int> ->
        rangeMap.value
    // Get the key or null if there is no value
    }?.key
}

/**
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    // The total cost: Sum up all the costs
    var totalIncome = trips.sumByDouble { trip: Trip -> trip.cost }

    // Count all the drivers, and get the top 20%
    var top20PercentDrivers = allDrivers.count() / 5

    // Get the driver income by mapping driver to cost.
    var driverIncome = this.trips.map { trip: Trip ->
        trip.driver to trip.cost
        // Group by the first pair
        }.groupBy { driverAndCost: Pair<Driver, Double> ->
        driverAndCost.first
        // Map the values, and get the sum
        }.mapValues {
            it.value.sumByDouble { driverAndCost: Pair<Driver, Double> ->
                driverAndCost.second
            }
        }
    // The driver income sorted descending
    val driverIncomeSorted = driverIncome.values.sortedDescending()

    // the 80% of the total income
    val income80percent = totalIncome * 0.8

    // If there is no trips, return false
    if (trips.isNullOrEmpty()) {
        return false
    }
    // If top 20%´s sum is greater or equal to the 80% of the total income, return true
    if (driverIncomeSorted.take(top20PercentDrivers).sum() >= income80percent) {
        return true
    }
    // If there is no match, return false
    return false
}