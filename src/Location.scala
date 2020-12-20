/**
 * Models possible locations where words can be placed for Solution
 * @param x axis value
 * @param y axis value
 * @param length of word
 * @param horizontal true if location for horizontal word, false if vertical
 */
case class Location(val x: Int, val y: Int, val length: Int, val horizontal: Boolean){
  /** checks if word is of correct length for location */
  def matching_length( word: String): Boolean = length == word.length()
  /**checks if word can fit in location taking into account letters from other words intersecting the location*/
  def matches(word: String, crossword: Array[Array[Char]]): Boolean= {
    if (horizontal) {
      for (i <- x until x + length) {
        if (crossword(y)(i) != '-') {
          if (crossword(y)(i) != word(i - x)) {
            return false
          }
        }
      }
    }else {
      for (i <- y until y + length) {
        if(crossword(i)(x) != '-') {
          if (crossword(i)(x) != word(i - y)) {
            return false
          }
        }
      }
    }
    return true
  }
}
