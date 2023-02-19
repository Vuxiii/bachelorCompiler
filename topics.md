# Topics

1. Basic counting problem (Pigeon hole, generalized permutations and combinations) 
   * **[Rosen chapter 6]**
   * Pigeon Hole: [Rosen p. 420]
   * Generalized Permutations & Combinations: [Rosen p. 428, 445, 457]
2. Inclusion-Exclusion with applications (derivation of the generalized formula, number of onto-functions, the hatcheck problem)
   * **[Rosen chapter 8]**
   * *Inclusion-Exclusion*: [Rosen p. 412, 579]
     * Proof _principle of inclusion-exclusion_: [Rosen: p. 583] 
   * Application of Inclusion-Exclusion: [Rosen p. 585]
     * Number of Onto-Functions (Rosen p. 587, 588)
     * Finding primes (Rosen p. 586)
     * Hatcheck problem (Rosen p. 588)
3. Discrete probability, random variables and bounds (expected value, variance, Bayes formula, Markov's inequality, Chebyshev's inequality and Chernoff bounds)
   * **[Rosen chapter 7]**
   * Finite Probability: [Rosen p. 470]
   * Bernoulli Trials / Binomial Distribution: [Rosen p. 484]
   * Random Variables: [Rosen p. 485]
     * Birthday Problem: (Rosen p. 486)
     * Hashing function collision: (Rosen p. 487)
   * Monte Carlo Algorithm: [Rosen p. 488]
   * Probabilistic Method: [Rosen p. 490]
   * Bayes' Theorem: [Rosen p. 494]
     * Generalizing: [Rosen p. 497]
   * Expected value and Variance: [Rosen p. 503]
     * Fair coin example: (Rosen p. 504)
     * Hatcheck problem: (Rosen p. 507)
     * Number of inversions problem: (Rosen p. 507)
   * Linearity of Expectation: [Rosen p. 506]
   * Average Case Computational Complexity: [Rosen p. 508]
   * Geometric Distribution: [Rosen p. 510]
   * Independent Random Variables: [Rosen p. 511]
   * Variance: [Rosen p. 513]
   * Chebyshev's Inequality: [Rosen p. 517]
   * Markov's Inequality: [Rosen p. 519 opg 37, Weekly Notes week 4]
   * Chernoff bounds: [Tardos p. 758]
   
4. Randomized algorithms (Quicksort, median finding and selection, min-cut in graphs, generating random permutation, majority element ...)
   * **[Rosen Chapter ]**
   * Quicksort: [Tardos p. 727]
   * Median finding and Selection [Tardos p. 727]
   * Min-cut graphs: [Tardos p. 714]
   * Random Permutation: [Cormen p. 99]
   * Majority element

5. Probabilistic analysis (using indicator random variables, coupon selector, expected running time of Quicksort and selection, randomized selection for max K-SAT)
   * **[Cormen Chapter 5]**
   * Coupon selector: [Tardos p. 722]
   * max K-SAT: [Tardos p. 724]
   * Quicksort: [Tardos p. 727]
   * Indicator Random Variable: [Cormen p. 108]
     * Birthday Problem: (Cormen p. 106)
6. Examples of applications of indicator random variables
   * **[Cormen Chapter 5.4]**
   * Birthday: [Cormen p. 106]
   * Balls and Bins: [Cormen p. 109]
   * Streaks: [Cormen p. 110]
7. Universal hashing (universal hash function, perfect hashing (2-level hashing), count-min sketch)
   * **[Cormen Chapter 11.3]**
   * Dictionary (Hashing): [Tardos p. 734]
   * Universal Hash: [Tardos p. 738] [Cormen p. 232]
   * Perfect Hashing: [Cormen p. 245]
   * Min-Cut: [Tardos p. 346]
8. String matching (naive algorithm, The Rabin-Karp algorithm, Finite automaton based string matching)
   * **[Cormen Chapter 32]**
   * Naive method: [Cormen p. 909]
   * Rabin Karp: [Cormen p. 911]
   * Finite Automaton: [Cormen p. 916]
9.  Maximum flows (Definitions, Ford Fulkerson algorithm, Max-flow-min-cut theorem, Edmonds-Karp Algorithm, biparte matching, integrality theorem)
    * **[Cormen Chapter 26]**
    * Flow Networks: [Cormen p. 644]
    * Ford Fulkerson: [Cormen p. 651]
    * Edmonds-Karp: [Cormen p. 660]
    * Biparte Matching: [Cormen p. 664]
10. The min-cut problem (randomized algorithm, solution via flows solution via max-back orderings) 
    * ???

# Notes


---

## 1: Basic counting problem (Pigeon hole, generalized permutations and combinations) 

### The basics

#### Product rule

If there are n_i different tasks in a procedure, and $n_j$ denotes the work for that task. The total amount of work for the procedure is $n_1 \cdot n_2 \cdot ... \cdot n_i$

#### Sum rule

If a procedure can be done by choosing either task from a set, then the total number of ways to finish the procedure is the sum of each task. $n_1 + n_2 + ... + n_i$

#### Subtraction rule (Inclusion-Exclusion for Two Sets)

If a task can be done in either $n_1$ ways or $n_2$ ways, then the total number of ways this task can be done, is the sum of $n_1$ and $n_2$ minus what they have in common. $n_1 + n_2 - (n_1 \cap n_2)$

#### Division rule

There are n/d ways to do a task if it can be done using a procedure that can be carried out in n ways, and for every way w, exactly d of the n ways correspond to way w. (Seating 4 people around a round table example)

### Pigeon hole

Let k be a positive integer and $k+1$ or more objects are placed into k boxes, then there is at least one box containing two or more objects.

**Proof**: (Contraposition). Suppose that none of the k boxes contain more than one object. Then the total number of objects would be at most k. This is a contradiction, because there are at least $k + 1$ objects.

**Corollary 1**: A function _f_ from a set with k + 1 or more elements to a set with k elements is **not** one-to-one.

**Proof**: Suppose that for each element *y* in the codomain of *f* we have a box that contain all the elements *x* of the domain of *f* such that $f(x) = y$. Because the domain contains $k + 1$ or more elements and the codomain contains only *k* elements, the pigeonhole principle tells us that one of these boxes contain two or more elements *x* of the domain. This means that *f* cannot be one-to-one.

#### Generalized Pigeon hole principle

If N  objects are placed into k boxes, then there is at least one box containing at least $\lceil\frac{N}{k}\rceil$ objects.

**Proof**: (Contraposition). Suppose that none of the boxes contain more than $\lceil\frac{N}{k}\rceil - 1$ objects. Then, the total number of objects is at most:

\[k \lparen\lceil \frac{N}{k}\rceil -1\rparen < k\lparen\lparen\frac{N}{k}+1\rparen - 1\rparen=N,\]

where the inequality $\lceil\frac{N}{k}\rceil<\frac{N}{k}+1 $ has been used. Thus, the total number of objects is less than N. Done.

**Example 6**: *Rosen p. 422*
What is the minimum number of students required in a class to be sure that at least six will receive the same grade, if there are five possible grades, A, B, C, D and F?
Use the formula
\[ N>k(r-1) \]
We want to solve for N, number of students. r is the minimum number of students that must receive the same grade. k is the number of available grades.
\[ N > 5(6-1) = 25 \Rightarrow N = 25+1=26\] 
So the answer is 26 students.

**Theorem 3**: Every sequence of $n^2+1$ distinct real numbers contain a subsequence of length $n+1$ that is either strictly increasing or strictly decreasing

**Proof:** Let $a_1,a_2,...,a_{n^2+1}$ be a sequence of $n^2+1$ distinct real numbers. Associate an ordered pair with each term of the sequence, namely, associate $(i_k,d_k)$ to term $a_k$, where $i_k$ is the length of the longest increasing sequence starting at $a_k$, and $d_k$ is the length of the longest decreasing sequence starting at $a_k$.
Suppose that there are no increasing or decresing subsequence of length $n+1$. Then $i_k$ and $d_k$ are both positive integers less than or equal to $n$, for $k=1,2,...,n^2+1$. Hence, by the product rule there are $n^2$ possible ordered pairs for $(i_k,d_k)$. By The pigeonhole principle, two of these $n^2 +1$ ordered pairs are equal. In other words, there exist terms $a_s$ and $a_t$, with $s<t$ such that $i_s=i_t$ and $d_s=d_t$. We will show that this is impossible. Because the terms of the sequence are distinct, either $a_s<a_t$ or $a_s>a_t$, then, because $i_s=i_t$, and increasing subsequence of length $i_t+1$ can be built starting at $a_s$, by taking $a_s$ followed by an increasing subsequence of length $i_t$ beginning at $a_t$. This is a contradiction. Similarly, if $a_s>a_t$, the same reasoning shows that $d_s$ must be greater than $d_t$, which is a contradiction.

### Generalized Permutations

   $P(n,r)$ denotes an r-permutation of a set with $n$ elements.

   We want to select $r$ elements where the order matters.

   > **Theorem 1:** If $n$ is a positive integer and $r$ is an integer with $1\le r\le n$, then there are
   \[P(n,r)=n(n-1)(n-2)\dots (n-r+1)\]
   r-permutations of a set with $n$ distinct elements.

   > **Corollary 1:** If $n$ nd $r$ are integers with $0\le r\le n$, then $P(n,r)=\frac{n!}{(n-r)!}$



### Generalized Combinations

   $n$ choose $r$. Also called **Binomial Coefficient**.
   \[{n \choose r}\]

   > **Theorem 2:** The number of r-combinations of a set with $n$ elements, where $n$ is a nonnegative integer and $r$ is an integer with $0\le r \le n$, equals
   \[C(n,r)=\frac{n!}{r!(n-r)!} \]

   ***proof on page 432***

   > **Corollary 2:** Let $n$ and $r$ be nonnegative integers with $r \le n$. Then \[C(n,r)=c(n,n-r)\]

---

## 2: Inclusion-Exclusion with applications (derivation of the generalized formula, number of onto-functions, the hatcheck problem)

   **Inclustion-Exclusion** is used when a task can be done in multiple ways, *but* some of the ways of completing a task share a common subtask. 

### Derivation of the Formula

   > **Theorem 1:** The principle of inclusion-exclision. Let $A_1,A_2,\dots,A_n$ be finite sets. Then
   $$|A_1 \cup A_2 \cup \dots A_n| = \sum_{1\le i\le n}|A_i| - \sum_{1\le i < j\le n}|A_i \cap A_j| $$
   $$+ \sum_{1\le i < j < k\le n}|A_i \cap A_j \cap A_k| - \dots +(-1)^{n+1}|A_1 \cap A_2 \cap \dots \cap A_n|$$

   **Proof by tegning!**

   Eller

   >**Proof:** We will probe the formula by showing that an element in the union is counted exactly once by the right-hand side of the equation. Suppose that $a$ is a member of exactly $r$ of the sets $A_1,A_2,\dots,A_n$ where $1\le r\le n$. This element is counted $C(r,1)$ times by $\sum|A_i|$. It is counted $C(r,2)$ times by $\sum|A_i \cap A_j|$. In general, it is counted $C(r,m)$ times by the summation involving $m$ of the sets $A_i$. Thus, this element is counted exaclty
   $$ C(r,1) - C(r,2) + C(r,3) - \dots +(-1)^{r+1}C(r,r) $$

### Number of Onto-functions

   > **Theorem 1:** Let $m$ and $n$ be positive integers with $m \ge n$. Then, there are
   $$ n^m-C(n,1)(n-1)^m+C(n,2)(n-2)^m-\dots +(-1)^{n-1}C(n,n-1)\cdot 1^m $$
   onto functions from a set with $m$ elements to a set with $n$ elements.

   > **Example:** Suppose that the elements in the codomain are $b_1,b_2,$ and $b_3$. Let $P_1,P_2,$ and $P_3$ be the properties that $b_1,b_2,$ and $b_3$ are not in the range of the function, respectively. Note that a function is onto if and only if it has none of the poperties $P_1,P_2,$ or $P_3$. By the inclusion-exclusion principle it follows that the number of onto functions from a set with six elements to a set with three elements is 
   $$ N(P_1'P_2'P_3')=N-[N(P_1)+N(P_2)+N(P_3)] + [N(P_1P_2) + N(P_1P_3) + N(P_2P_3)] - N(P_1P_2P_3),$$
   where N is the total number of functions from a set with siz elements to one with three elements. We will evaluate each of the terms on the right-hans side of this equation.
   From Example 6 of Section 6.1, it follows that $N=3^6$. Note that $N(P_i)$ is the number of functions that do not have $b_i$ in their range. hence, there are two choices for the value of the function at each element of the domain. Therefore, $N(P_i)=2^6$. Furthermre, there are $C(3,1)$ terms of this kind. Note that $N(P_iP_j)$ is the number of functions that do not have $b_i$ and $b_j$ in their range. Hende, there is only one choice for the value of the function at each element of the domain. Therefore, $N(P_iP_j)=1^6=1$. Furthermore, there are $C(3,2)$ terms of this kind. Also, note that $N(P_1P_2P_3)=0$, because this term is te number of functions that have none of $b_1, b_2,$ and $b_3$ in their range. Clearly, there are no such funcions, so the number of onto functions from a set with siz elements to one with three elements is 
   $$  $$ 

### Hatcheck Problem


---

## 3: Discrete probability, random variables and bounds (expected value, variance, Bayes formula, Markov's inequality, Chebyshev's inequality and Chernoff bounds)

### Discrete Probability

### Random Variables

### Expected value

### Variance

### Baye's Formula

### Markov's Inequiality

### Chebyshev's Inequality

### Chernoff Bounds

---

## 4: Randomized algorithms (Quicksort, median finding and selection, min-cut in graphs, generating random permutation, majority element)


---

## 5: Probabilistic analysis (using indicator random variables, coupon selector, expected running time of Quicksort and selection, randomized selection for max K-SAT)


---

## 6: Examples of applications of indicator random variables


---

## 7: Universal hashing (universal hash function, perfect hashing (2-level hashing), count-min sketch)


---

## 8: String matching (naive algorithm, The Rabin-Karp algorithm, Finite automaton based string matching)


---

## 9: Maximum flows (Definitions, Ford Fulkerson algorithm, Max-flow-min-cut theorem, Edmonds-Karp Algorithm, biparte matching, integrality theorem)


---

## 10: The min-cut problem (randomized algorithm, solution via flows solution via max-back orderings) 