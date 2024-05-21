package com.xebia.com.xebia.intro

import arrow.core.andThen
import arrow.core.compose
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*

/**
 * In functional programming, composition allows the creation of a pipeline of functions, where each function takes
 * the output of the preceding one as its input. By doing so, we can build more complex functions from simpler ones.
 */

/**
 * In this exercise, we will implement a function that generates a table containing the royalties generated by
 * a song according to the number of plays. To build this function, we will use three simple tasks that perform
 * a specific action:
 *   1. Given a song, return its artist.
 *   2. Given an artist, return the fee paid per song play
 *   3. Based on the fee, generate a table with the royalties for song plays from 100,000 to 1,000,000
 *      in intervals of 100,000.
 */

fun getSongArtist(song: Song): Artist = song.artist

fun getFeePerPlay(artist: Artist): Double = artist.feePerSongPlayed

fun estimateRoyalties(fee: Double): Map<Int, Double> =
    (100_000..1_000_000 step 100_000).associateWith {
        it * fee
    }

/**
 * Using Kotlin Standard library, we could implement this composed functions by using
 * two different approaches:
 * 1. Chaining all the functions in one step
 * 2. Executing the functions in several steps and storing intermediate results in values
 */
fun generateRoyaltiesEstimationForSong(song: Song): Map<Int, Double> {
    // Approach 1:
    estimateRoyalties(getFeePerPlay(getSongArtist(song)))

    // Approach 2:
    val artist = getSongArtist(song)
    val feePerPlay = getFeePerPlay(artist)
    return estimateRoyalties(feePerPlay)
}

/**
 * Solve the exercise by using the [andThen] function provided by the Arrow Core Functions module
 */
val generateRoyaltiesEstimationForSongUsingAndThen: (Song) -> Map<Int, Double> = TODO()

/**
 * In this case, you should use the [compose] function. The implementation will be slightly
 * different than the one used in the function above
 */
val generateRoyaltiesEstimationForSongUsingCompose: (Song) -> Map<Int, Double> = TODO()

fun main() {
    val artist = Artist(name = "Foo Fighters", feePerSongPlayed = 3.50)
    val song = Song(
        title = "Shame Shame",
        artist = artist,
        genre = Genre.Rock,
        releaseDate = LocalDate.of(2020, 11, 7)
    )

    val royaltiesEstimationUsingAndThen = generateRoyaltiesEstimationForSongUsingAndThen(song)
    val royaltiesEstimationUsingCompose = generateRoyaltiesEstimationForSongUsingCompose(song)
    assert(royaltiesEstimationUsingAndThen == royaltiesEstimationUsingCompose)

    val currencyFormatter = NumberFormat.getCurrencyInstance()
    currencyFormatter.setMaximumFractionDigits(0)
    currencyFormatter.currency = Currency.getInstance("EUR")

    println("# plays -> Royalties to pay")
    royaltiesEstimationUsingAndThen.forEach { (plays, royalty) ->
        println("$plays -> ${currencyFormatter.format(royalty)}")
    }
}