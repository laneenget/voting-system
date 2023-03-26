# Project 1 README

## Steps for Compilation
Please navigate to the repo-Team18/Project1/VotingSystem/src directory.  
Next, to compile type "javac VotingSystem/*.java"  
Next, to run the Voting System program, type "java VotingSystem.Main" (if Java17)  
For older versions, you may need to type "java VotingSystem/Main" instead.  
You may also pass along the name of the input file if you wish.  

**Important**: Next, as our test files with the code for the tests are currently located in the src/VotingSystem directory, 
please note that if there is a compilation error, it may be due to not being able to find JUnit.  
This can be solved by moving the test files with the code to the Testing folder while you run the program.  


## Notes on Testing
Please note that for our final product, many of the methods that are used in tests are private, primarily in IR, CPL, etc. as there is no reason to use them
externally. This may further affect the tests as the tests were primarily used in development when these methods were not private. Please note
that if the tests show you an error when you run them it may be due to the method being private now. You may be able to change them back to being public
if you wish to run a specific test on that method. It may be easier to change all private methods to public to avoid going back and forth.
All test resources (files) may be found in repo-Team18/Project1/VotingSystem/Testing  
If you wish to run the tests, we strongly recommend using IntelliJ.
We have noticed that different IDEs may set up the classpath, configuration, etc. differently.  
This may cause trouble in configuring the tests such that they are runnable on your IDE.
Next, please ensure that the user's directory is set such that the working directory is the one
that contains the testing files. Occasionally, a person's default may be set to be repo-Team18 instead
depending on their IDE. This may show an error that makes it seem like a test file cannot be accessed. As long as the user's
directory is set to the one that holds the test file, it should be able to find the test file. You may check the user directory by
typing in "String path = System.getProperty("user.dir");" and then checking that path to make sure it's not just repo-Team18.
Next, the program can only find JUnit and run tests involving JUnit if the test file
is located in whichever directory is marked as the test sources root for IDEs like IntelliJ. Please ensure that if JUnit is giving an error that
the test file can access JUnit in its directory.  
Lastly, please note that the src files located in src will not allow access for the tests to certain
protected fields or private fields, methods, etc. When running a test and if it gives an error depending on your setup,
please ensure that it is not due to accessing a private or protected field that is not accessible in the test sources root directory.
We have left getters that should provide access to these fields in their respective classes, which are currently commented out.
Please uncomment them if you wish to run tests that involve accessing private or protected fields.
Many of our tests currently rely on using these auto-generated getters.

For instance, in IRtests, it is imperative to uncomment the getters and setters in the IR Class, Tree Class, Node Class.
In RunElectionTests, it is important to uncomment the setters in RunElection.
All of these are located at the bottom of their respective classes.
It is easiest to re-publicize all methods in the beginning and comment back in related getters and setters for the class the tests
are testing.

