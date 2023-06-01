# Bachelor Compiler

- [Bachelor Compiler](#bachelor-compiler)
  - [Short intro and Why](#short-intro-and-why)
  - [Dependencies](#dependencies)
  - [Goals for this Project](#goals-for-this-project)
  - [How to compile \& run](#how-to-compile--run)
  - [Naming ideas - breh](#naming-ideas---breh)
- [Configuring the Compiler](#configuring-the-compiler)
  - [Adding another Token to the Lexer/Parser](#adding-another-token-to-the-lexerparser)
  - [Visitor Pattern](#visitor-pattern)
  - [Debugging](#debugging)
    - [AST\_Printer](#ast_printer)
  - [Language semantics and Syntax](#language-semantics-and-syntax)
    - [Declarations](#declarations)

---



## Short intro and Why
Welcome to my Bachelors Repo.
This Compiler Project uses two of my own tools, `Parsley` & `Regex`, which are used instead of the traditional tools: `Bison` & `Flex`.
Now why did I make my own tools instead of using freely-available tools?
Because this is a Project designed to teach myself as much about compilers as possible. This involves doing every step from scratch.

---


## Dependencies
Parsley and Regex were made in the fall of 2022 as a side project (I was bored), and I decided it would be fun to actually use the tools I had created for something.
Naturally this pointed me in the direction of Compilers.

---

## Goals for this Project
The Compiler will be written as a part of my Bachelors Project in the Spring of 2023.

My hope for this project is that it can be used as a teaching tool for the course DM552 Programming Languages at SDU.
The main goal is to provide different scoping techniques (Static Scoping and Dynamic Scoping) aswell as multiple parsing techniques such as, Parse by value, Parse by reference, Parse by Name, Parse by Value-Result. (Might have forgotton some...)

Other than the above, my aim is to have as much fun as possible while developing this compiler.

## How to compile & run
~~To be able to compile and run this compiler yourself (or for myself in the future when I have forgotten how to do it :S), you need to download the dependencies listed above (Parsley and Regex), and install them on your local machine with maven: `mvn clean install compile package`.~~

The above should no longer be needed. I think I have successfully published the dependencies as a package to github's maven repo. I might be wrong. Try cloning this repo, and run `mvn install`. It should work.

For this project I am using `Java 17`

To run the compiler, I have provided a bash file called `run.sh`, that should be able to execute the program. Afterwards call `comp.sh` which assembles the program, links it, and finally runs the program. 

---

## Naming ideas - breh
* Juhl
* Juhllang $\rightarrow$ .jl
* FUJ

So far I think I will go with JuhlLang with extension $\rightarrow$ `.jl`

---

# Configuring the Compiler

This section will try to explain how to add functionality to the compiler / language. This section is also a reference for myself to limit the amount of time wasted trying to remember every little detail. The goal is to be able to look up anything that is remotely difficult or complicated.

---

## Adding another Token to the Lexer/Parser
This section will explain how one can add another token/node to the language.
1. Goto Parser.Symbol.java and add the desired Token
   * It can be a **Terminal**, prefixed with a *t_*. The name is typically uppercased.
   * It can be a **Non-Terminal**, prefixed with a *n_*. The name is typically lowercased.
2. Depending on whether the added token is a *Terminal* or a *Non-Terminal* go to step *2a* and *2b* respectivly.
    **2a.** 
    1. Goto *Lexer.Tokens.Leaf* and add a new Java-Class representing this token. This Class will be instantiated by the Lexer in step 4. The new Java-Class should extend *Visitors.ASTNode.java* to enable the Visitor Pattern. Since the Token is a leaf, remember that the method `isLeaf()` should return `true` and the method `getChildrenCount()` should return `0`. The method `accept(VisitorBase visitor)` can simply call the super method `acceptNoChild(visitor);`. These settings are the default configuration for a leaf token. It ensures that the visitor functions correctly. 
    2. After this, Goto *Lexer.Tokens.TokenType.java* and add the token to the enum-list. 
    3. Goto *Lexer.Tokens.TokenConstructor.java* and add the enum identifier to the switch-statement. This Java-Class contains the rules for how each node should be created once the Lexer recognizes a specific Token.
    4. Now we can tell the Lexer how to recognize our newly created token. Goto *Lexer.Lexer.java* and add the regular expression to identify the Token. If you are experiencing difficulty in getting the Lexer to return the correct Leaf, ensure that the regular expression for the Token is unique. If this doesn't solve the problem, try adding a priority to it, the lower the number the bigger the chance for that constructor to be called. (This is a bug in my regex-compiler that I currently as of writing *(20.01.2023)* haven't found a solution to yet. The problem surfaces when combining multiple NFA-states into a single DFA-state. If the created DFA-state contains two or more NFA-states that are accept-states, then the constructor function that will be called is *currently* **undefined**... Hehe)

    **2b.**
        
    1. Goto *Parser.Nodes* and add a Java-Class representing the inner-node that you have defined. This Class should extend *Visitors.ASTNode.java* to enable the Visitor Pattern. Remember the method `isLeaf()` should return false, since it is not a leaf-node, and the method `getChildrenCount()` should return the amount of children this node contains. For example: The Print-Node contains a single child. The methods `getChild1()` upto `getChild4()` should return the child in the desired order. For example: The Assignment-Node returns first the Identifier to which the second child should be assigned to. 
    2. If this node contains **more** than 4 children, update the Java-Class *Visitors.ASTNode.java* to encompass this *big* node. The method `accept(VisitorBase)` can call the super method that corresponds to the amount of children this inner-node contains. If the node has 2 children, you can call the super method `accept2Child(visitor);`
    3. Now we can add the CFG for the new node. Goto *Parser.Parser.java* and add the rule. The rule can be added by called the method `addRuleWithReduceFunction` on the `Grammar` object currently called `g`. The first parameter is the left side of the CFG. The second parameter is a `List` of Tokens, can be either *Non-Terminals* or *Terminals*. The order in which they appear in the list matters. It goes from left to right.
 3. **Important**: The term in the created node/leaf **MUST** be the one created in the Java-Class *Parser.Symbol.java*. This term is used to ensure that the parser can correctly identify what term it is looking at. 
 
---

## Visitor Pattern

The visitor pattern have been updated to a more modular interface. To Be Written

---

## Debugging 

Currently the project has the following debug utilities:

* Enable/Disable printing of the Parsing Steps during parsing. `Settings.showParsingSteps = false|true`
* Enable/Disable printing the Grammar. `Settings.showGrammar = false|true`
* Enable/Disable printing the Parsing Table. `Settings.showParsingTable = false|true`
* Print the AST with the `ASTPrinter.java` class. Simply pass any node an object of type `ASTPrinter` and it will print the node and it's children.

### AST_Printer

The `AST_Printer` can be used to print the syntax tree during debugging or if one is just interested in seeing how the internal representation is handled. It should look something like this:

![Image of running the ast_printer][ast_printer]

## Language semantics and Syntax

The following will deine the language semantics and syntax of the language.

### Declarations

`let` defines that we are about to make a declaration. Variable, Function, Type.

To define a new variable start by typing `let` and follow it up with the name for the variable.

`let age`

Says that we are about to create a variable with name `age`. Now we should give it a type. Perhaps `int` sounds reasonable. We do this by following with a colon `:` and the desired type after the name of the variable. Every statement should end with a semicolon `;`.

`let age: int;`

To define a new type, we do almost the same as above. But between the `let` keyword and the name, type `type`, to indicate that we are about to declare a new type.

`let type Person`

Would define that we are about to declare a new type with the type_name of `Person`.
Now we need to give the type a type, we do this by typing a colon `:` followed by the desired type. What should it be? We could give the type `Person` the type `string`. This means that whenever we type `Person` it is the same type as `string`.

`let type Person: string;`

If we, however, want to make a compound type, a struct, we can do so by typing `{}`. Inside the curly braces we can declare what fields should be inside the struct.

`let type Person: {
    name: string;
    age: int;
}`

We can now declare a new variable of the type `Person`, by doing the following:

`let person: Person;`

Note: The keyword doesn't stop you from using `type` as a variable name. If you want to make a variable called type, do the following:

`let type: int;`


[ast_printer]: /assets/ast_printer.png