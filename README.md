# akka-http-quickstart-scala

### About the project
The purpose of this project was to implement REST API for quadratic equation.
<br>More information on quadratic equation can be found at https://en.wikipedia.org/wiki/Quadratic_equation.
<br>Akka HTTP was selected for this project.

This project is an extension of Akka HTTP's quickstart project available on Lightbend's website at https://developer.lightbend.com/guides/akka-http-quickstart-scala/.
<br>
<br>Without affecting the existing functionality, new functionality was added.
This decision was made in order to demonstrate ability to add functionliity to existing projects
without breaking the existing functionality, which is a more often scenario in real life. 

### How to start the application
To try the application, you can load the project into Intellij Idea, right-click on QuickStartApp.scala and select "Run".
<br>The application will run as an HTTP service on localhost using port 8080.

### How to send HTTP requests and receive responses
To see how the app works, you can send HTTP requests running cURL on the same machine.

There are some examples below explaining the purpose, showing the commands and the expected results.   

```
Example 1:
    Purpose: Calculate roots for quadratic equation passing three coefficients as a comma separated list
    Command: curl localhost:8080/quadeq/1,-6,-7
    Expected result: {"roots":[7.0,-1.0]}

Example 2:
    Puropse: Confirm that there are no roots if the coeffients are not properly formatted
    Command: curl localhost:8080/quadeq/some_string_that_cannot_be_parsed
    Expected result: {"roots":[]}

Example 3:
    Purpose: Calculate roots for quadratic equation passing a, b, and c coeeficients using POST
    Command: curl -H "Content-type: application/json" -X POST http://localhost:8080/quadeq -d "{\"a\": 1, \"b\": -7, \"c\": 10}"
    Expected result: {"roots":[5.0,2.0]}

Example 4:
    Purpose: see usage
    Command: curl localhost:8080/quadeq
    Expected result: usage info will be displayed as plain text
```

<br> <br> <br>
Thank you for reading :-)

<br> <br> <br>
Best wishes,
<br>-igor

<br> <br> <br>
