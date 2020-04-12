## Number Server
Hi there! Thanks for taking the time to review this code sample.

### Getting Up And Running
Short examples and snippits that should help you get a version of this server up and running locally.

#### Requirements
This Project was built and is run with the following:
- Java 14 (can be installed [here](https://www.java.com/en/download/))
- Gradle 6.3 (can be installed [here](https://gradle.org/install/))

#### Build
Building this project with gradle is super easy. From the root of the repository, just run `./gradlew init` and then `./gradlew build`. This should compile and pull in all the necessary packages.

#### Test
Running the tests is just as easy as building: `./gradlew test`. You'll see a Build Successful if all tests have passed and a Build Failed otherwise. 

#### Starting The Server
To start the server, simply run `./gradlew run`. This will spin up the main server. If you wish to connect to it, there are a couple of different ways you can do so:

##### netcat
netcat comes natively installed on most mac computers and is super easy to connect with. Just run `nc 127.0.0.1 4000` and it will connect you. You can then start entering lines into the terminal. To run a file against the server, you can run `nc 127.0.0.1 4000 < {fileName}`.

##### telnet
telnet comes natively installed on most windows machines (though it may need to be turned on). Connecting with telnet is just as easy as netcat, simply run `telnet 127.0.0.1 4000`. Note: I do not think it is possible to upload files using telnet.

