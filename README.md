# Bachelor Compiler

## Short intro and Why
Welcome to my Bachelors Repo.
This Compiler Project uses two of my own tools, `Parsley` & `Regex`, which are used instead of the traditional tools: Bison & Flex.
Now why did I make my own tools instead of using freely-available tools?
Because this is a Project designed to teach myself as much about compilers as possible. This involves doing every step from scratch.

## Dependencies
Parsley and Regex were made in the fall of 2022 as a side project (I was bored), and I decided it would be fun to actually use the tools i had created for something.
Naturally this pointed me in the direction of Compilers.

## Goals for this Project
The Compiler will be written as a part of my Bachelors Project in the Spring of 2023.

My hope for this project is that it can be used as a teaching tool for the course DM552 Programming Languages at SDU.
The main goal is to provide different scoping techniques (Static Scoping and Dynamic Scoping) aswell as multiple parsing techniques such as, Parse by value, Parse by reference, Parse by Name, Parse by Value-Result. (Might have forgotton some...)

Other than the above, my aim is to have as much fun as possible while developing this compiler.

To be able to compile and run this compiler yourself (or for myself in the future when I have forgotten how to do it :S), you need to download the dependencies listed above (Parsley and Regex), and install them on your local machine with maven: `mvn clean install compile package`. 
For this project I am using `Java 17`