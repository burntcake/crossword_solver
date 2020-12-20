import scala.collection.mutable
import scala.io.StdIn.readLine
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Queue

/**
 * Given a crossword and a list of words, solves the crossword and displays it to stdout
 */
object Solution {
  var crossword: Array[Array[Char]] = Array.ofDim[Char](10,10)
  var wordLengthMap = mutable.HashMap.empty[Int,Int] // Key Number of Characters Value: Number of words with that number of characters

  /** displays the crossword in stdout */
  def display(): Unit ={
    println()
    crossword.foreach( row => println(row.mkString) )
  }

  /**
   * Inserts word at specified location in the crossword
   * @param word
   * @param l location
   * @return ListBuffer[(Int, Int, Char)]: contains a list of letters filled in by previous inserts
   */
  def insert_word(word: String, l: Location): ListBuffer[(Int, Int, Char)]= {
    var prevchars = new ListBuffer[(Int, Int, Char)]
    if (l.horizontal) {
      for (i <- l.x until l.x + l.length) {
        if(crossword(l.y)(i)!= '-') {
          prevchars.addOne(l.y,i,crossword(l.y)(i))
        }
        crossword(l.y)(i) = word(i - l.x)
      }
    }else {
      for (i <- l.y until l.y + l.length) {
        if(crossword(i)(l.x)!= '-') {
          prevchars.addOne(i,l.x,crossword(i)(l.x))
        }
        crossword(i)(l.x) = word(i - l.y)
      }
    }
    prevchars
  }

  /** reverts the crossword to a previous state so it can be used by other branches of solve */
  def backtrack(l: Location, prevchars:ListBuffer[(Int, Int, Char)]): Unit = {
    if (l.horizontal) {
      for (i <- l.x until l.x + l.length) {
        crossword(l.y)(i) = '-'
      }
    }else {
      for (i <- l.y until l.y + l.length) {
        crossword(i)(l.x) = '-'
      }
    }
    //move the characters that were there before the last insert back into place
    for (prevchar <- prevchars) {
      crossword(prevchar._2)(prevchar._1) = prevchar._3
    }
  }

  /**
   * Main body of the algorithm, will recursively find and place words into empty locations until
   * all words or empty locations are exhausted
   * @param locations
   * @param words
   * @param word_index
   * @return
   */
  def solve(locations: ListBuffer[Location], words: Queue[String], word_index: Int = 0): Boolean = {
    if (word_index == words.size) {
      return true
    }
    var word = words(word_index)
    for (location <- locations) {
      if (location.matching_length(word)) {
        if (location.matches(word, crossword)) {
          val prevchars = insert_word(word, location)
          val correct = solve(locations, words, word_index+1)
          if (correct) {
            return correct
          }
          backtrack(location, prevchars)
        }
      }
    }
    false

  }
  /**sort by number of words with the same number of characters, then by word length*/
  def sortBysharedLength(s1: String, s2: String):Boolean ={
    if (wordLengthMap(s1.length()) == wordLengthMap(s2.length())) {
      return s1.length() < s2.length()
    }
    wordLengthMap(s1.length()) < wordLengthMap(s2.length())
  }
  def main(args: Array[String]): Unit ={
    var empty_locations = new ListBuffer[Location]()
    println("Please enter your crossword and solutions.")

    // Construct crossword matrix and find the location of horizontal words
    for(i <- 0 to 9) {
      val line = readLine()
      var horizontalLength = 1

      for (j <- 0 to 9) {
        crossword(i)(j) = line(j)
        if (line(j) == '-') {
          horizontalLength+=1
        }else {
          if (horizontalLength > 0) {
            empty_locations+= Location(j - horizontalLength, i, horizontalLength, true)
          }
          horizontalLength = 0
        }
      }
      //take care of case where empty space reaches till end of line
      if (horizontalLength > 0) {
        empty_locations+= Location(10 - horizontalLength, i, horizontalLength, true)
      }
      horizontalLength = 0

    }
    // add vertical words
    for (i <- 0 to 9) {
      var verticalLength = 0
      for (j <- 0 to 9) {
        if (crossword(j)(i) == '-') {
          verticalLength += 1
        }else {
          if (verticalLength > 0) {
            empty_locations+= Location( i, j - verticalLength,verticalLength, false)
          }
          verticalLength = 0
        }
      }
      if (verticalLength > 0) {
        empty_locations+= Location( i, 10 - verticalLength,verticalLength, false)
      }
      verticalLength = 0
    }

    val words = readLine().split(";").to(collection.mutable.Queue)
    for (word <- words) {
      wordLengthMap.updateWith(word.length()) ({
        case Some(count) => Some(count + 1)
        case None         => Some(1)
      })
    }
    // sort by number of other words sharing the same length
    // this can reduce the number of branches that need to be searched
    words.sortWith(sortBysharedLength)
    solve(empty_locations, words)
    display()
  }

}
