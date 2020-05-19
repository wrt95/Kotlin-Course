package taxipark

/*************** Task #1 ****************/
fun TaxiPark.findFakeDrivers1(): Set<Driver> =
    allDrivers.filter { d ->
        trips.none {
            it.driver == d
        }
    }.toSet()

// This one is slightly better:
fun TaxiPark.findFakeDrivers2(): Set<Driver> =
    allDrivers - ( trips.map { it.driver } )

/*************** Task #2 ****************/
fun TaxiPark.findFaithfulPassengers1(minTrips: Int): Set<Passenger> =
    trips
        .flatMap (Trip::passengers)
        .groupBy { passenger -> passenger }
        .filterValues { group -> group.size >= minTrips }
        .keys

fun TaxiPark.findFaithfulPassengers2(minTrips: Int): Set<Passenger> =
    allPassengers.filter { p ->
        trips.count {
            p in it.passengers
        } >= minTrips
    }.toSet()

/*************** Task #3 ****************/
fun TaxiPark.findFrequentPassengers1(driver: Driver): Set<Passenger> =
    trips
        .filter { trip -> trip.driver == driver }
        .flatMap (Trip::passengers)
        .groupBy { passenger -> passenger }
        .filterValues { group -> group.size > 1 }
        .keys

fun TaxiPark.findFrequentPassengers2(driver: Driver): Set<Passenger> =
    allPassengers.filter { p ->
        trips.count {
            it.driver == driver && p in it.passengers
        } > 1
    }.toSet()

/*************** Task #4 ****************/
fun TaxiPark.findSmartPassengers1(): Set<Passenger> {
    // Give meaningful names
    val (tripsWithDiscount, tripsWithoutDiscount) = trips.partition {
        // it.discount is Double basically checks for not null.
        // Better:
        it.discount != null
    }
    return allPassengers.filter { passenger ->
        tripsWithDiscount.count {
            passenger in it.passengers
        } >
        tripsWithoutDiscount.count {
            passenger in it.passengers
        }
    }.toSet()
}


fun TaxiPark.findSmartPassengers2(): Set<Passenger> =
    allPassengers
        .associate { p ->
            p to trips.filter { t ->
                p in t.passengers
            }
        }
        .filterValues { group ->
            val (withDiscount, withoutDiscount) = group.partition {
                it.discount != null
            }
            withDiscount.size > withoutDiscount.size
        }
        .keys


fun TaxiPark.findSmartPassengers3(): Set<Passenger> =
    allPassengers.filter { p ->
        trips.count { t ->
            p in t.passengers && t.discount != null
        } >
        trips.count { t ->
            p in t.passengers && t.discount == null
        }
    }.toSet()


/*************** Task #5 ****************/
fun TaxiPark.findTheMostFrequentTripDurationPeriod1(): IntRange? {
    return trips
        .groupBy {
            val start = it.duration / 10 * 10
            val end   = start + 9
            // group in range from start to end
            start .. end
        }
        // Convert map to list
        .toList()
        // only interested in the second value
        .maxBy { (_, group) -> group.size }
        ?.first
}



/*************** Task #6 ****************/

fun TaxiPark.checkParetoPrinciple1(): Boolean {
    if (trips.isEmpty())
        return false

    // Sum up all the cost of all the trips
    val totalIncome = trips.sumByDouble (Trip::cost)

    val sortedDriversIncome: List<Double> = trips
        // group trips by drivers to build a map from driver to all trips
        // by these drivers.
        .groupBy (Trip::driver)
        .map { (_, tripsByDriver) ->
            // sum up the income for all the trips made by this specific driver
            tripsByDriver.sumByDouble(Trip::cost)
        }
        .sortedDescending()

    val numberOfTopDrivers = (0.2 * allDrivers.size).toInt()
    val incomeByTopDrivers = sortedDriversIncome
        .take(numberOfTopDrivers)
        .sum()

     return incomeByTopDrivers >= 0.8 * totalIncome
}
