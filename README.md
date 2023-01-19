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
To be able to compile and run this compiler yourself (or for myself in the future when I have forgotten how to do it :S), you need to download the dependencies listed above (Parsley and Regex), and install them on your local machine with maven: `mvn clean install compile package`. 
For this project I am using `Java 17`

---

## Naming ideas - breh
* Juhl
* Juhllang $\rightarrow$ .jl
* Fischer Juhl
* Fischer Uldall Juhl
* FUJ
* JUF

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
Each ASTToken in the AST utilizes the visitor pattern. Each Node will accept a visitor in the following order:
1. visitor.visit( this );
2. visitor.preVisit( this );
3. visitor.midVisit( this );
4. visitor.postVisit( this );

The following will explain how the visitor is passed to children:
* 0 children:
    ```Java
    @Override
    public void accept(VisitorBase visitor) {
        visitor.visit( this );

        visitor.preVisit( this );
        visitor.midVisit( this );
        visitor.postVisit( this );
    }
    ```
* 1 child:
    ```Java
    @Override
    public void accept(VisitorBase visitor) {
        visitor.visit( this );

        visitor.preVisit( this );
        child1.accept( visitor );
        visitor.midVisit( this );
        visitor.postVisit( this );
    }
    ```
* 2 children:
    ```Java
    @Override
    public void accept(VisitorBase visitor) {
        visitor.visit( this );

        visitor.preVisit( this );
        child1.accept( visitor );
        visitor.midVisit( this );
        child2.accept( visitor );
        visitor.postVisit( this );
    }
    ```
* 3 children:
    ```Java
    @Override
    public void accept(VisitorBase visitor) {
        visitor.visit( this );

        visitor.preVisit( this );
        child1.accept( visitor );
        visitor.midVisit( this );
        child2.accept( visitor );
        child3.accept( visitor );
        visitor.postVisit( this );
    }
    ```

---

## Debugging 

Currently the project has the following debug utilities:

* Enable/Disable printing of the Parsing Steps during parsing. `Settings.showParsingSteps = false|true`
* Enable/Disable printing the Grammar. `Settings.showGrammar = false|true`
* Enable/Disable printing the Parsing Table. `Settings.showParsingTable = false|true`
* Print the AST with the `ASTPrinter.java` class. Simply pass any node an object of type `ASTPrinter` and it will print the node and it's children.