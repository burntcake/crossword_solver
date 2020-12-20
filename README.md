# crossword_solver
Given an empty 10x10 crossword and a list of words place the words in the crossword.

This crossword_solver uses backtracking.

After importing the crossword and the words through stdin, we sort the words by the number of other words that have the same number of letters ascending.
These words have less possible locations that they can be placed potentially reducing the number of branches that need to be searched.
In the case that all words have the same length this sorting procedure will not provide any benefits.

After this sort step the placement of words begins.

Pseudocode
crossword //populated by user input
def solve(locations, words, word_index)
  // once all our words are exhausted then the correct solution has been found
  if words_index == words.size
    return true
  for location in locations
    if location.matches(word)
      existing_chars = crossword.insert(word, location) // inserts word at a particular location and record any characters already there
      if solve(locations, words, word_index+1)
        return true
      crossword.revert  //reverts the crossword to the state before the insert using the information from existing chars
  return false


For every word, the solver explores every location the word could possibly be placed. If a branch reaches a state where there is no 
possible location to insert the word i.e. the word would overwrite letters that have been already placed by other words intersecting
that location. Then that branch is terminated and the crossword is reverted to a previous state. Terminated branches that cannot be correct early
reduces the states that need to be searched. Reverting the state of the crossword after a branch fails is done to avoid needing to give every branch
a seperate copy of the crossword.
Once all words are exhausted then we have found the solution since otherwise the algorithm would have terminated that branch early, the completed
crossword can then be displayed

## Time and Space complexity
We can model the structure of this algorithm with a tree where b, the branching factor is equal to the number of possible locations for a word 
and the depth d, of the tree corresponds to a different word.
Then our worst-case run time compexity is O(d^b) if we need to search every branch if the tree. This dominates the time required for the sort step.

There is only ever one copy of the crossword, but in order for this to be possible information from the previous state of the crossword must be stored so that the branch can be reverted 
if necessary. There only ever needs to be one of these states stored per level so the space complexity is linear O(d)






