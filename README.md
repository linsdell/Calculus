# Calculus
Java program I wrote in Grade 12 for a software course. The program parses an inputted function into a data tree, and is able to differentiate the function any number of times. Additionally, the program is able to produce a MacLaurin Polynomial Approximation for a given function up to 9 terms.

## Parser
The parser involves four different classes all corresponding to different elements of a function: variables, constants, operators, and sub-functions. 
It moves through the given function string and produces a data tree with nodes containing the above elements.

## Differentiation
Once the function has been parsed from a string into a data tree structure, the program is able to parse through and apply standard rules of differentiation to the function (i.e power rule, chain rule, quotient rule, etc.). These rules are hard coded into the Function class. 

## MacLaurin Polynomial Approximation
Using the ability to differentiate a passed function any number of times a MacLaurin Polynomial Approximation is able to be generated for a given function. Due to the limited computing power of my laptop I was only able to generate the apporximation for nine terms of the MacLaurin series of the function.
The reason for this limit is the data tree becomes too large after nine derivaitves of the function. To allow a greater accuracy (i.e. generating more terms) a prune function would have to be added to eleiminate redundent nodes in the tree.
