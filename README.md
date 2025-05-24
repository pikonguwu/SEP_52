# BuckBrainAI Financial Tracking System

BuckBrainAI is a personal financial tracking and management application based on Java Swing, providing functions such as transaction records, account management, analysis and AI assistants.

System Requirements

- JDK 21 (Supporting preview features)
Maven 3.6+
- Operating System: Windows/macOS/Linux

## Project Structure

Main directory structure:
```
src/
├── main/
│   └── java/
│ ├── Analysis/ - Financial data analysis function
│ ├── Controller/ - Controller component
│ ├── Entity/ - Data entity class
│ ├── components/ - Custom UI components
│ ├── constants/ - Apply constants
│ ├── data/ - Data storage and processing
│ ├── services/ - Business Service layer
│ ├── ui/ - User interface
│ └── views/ - View component
├── test/ - Test code
└── photo/ - Image resource
```

## Environment Settings and Installation

1. Install JDK 21

Make sure that JDK 21 is installed on your system:

- access [Oracle JDK download page] (https://www.oracle.com/java/technologies/downloads/) or [its] (https://jdk.java.net/21/)
Download and install JDK 21 that suits your operating system
Set the 'JAVA_HOME' environment variable to point to the JDK installation directory
Add '%JAVA_HOME%\bin' to the system 'PATH' variable

Verify the successful installation:
```
java -version
```

Version 21 should be displayed.

2. Install Maven

- access/Apache Maven website (https://maven.apache.org/download.cgi) to download the latest version
- Extract to the directory of your choice
Set the 'MAVEN_HOME' environment variable to point to the Maven installation directory
Add '%MAVEN_HOME%\bin' to the system 'PATH' variable

Verify Maven installation:
```
mvn -version
```

3. Clone/Download the project

```
git clone [Project Repository URL]
cd SEP_52
```

Build and run applications

Build the project using Maven

Execute in the project root directory:

```
mvn clean install
```

Run the application

Method 1: Use the exec plugin of Maven:
```
mvn exec:java
```

Method 2: Run the JAR file directly (after construction) :
```
java -- enable-preview-jar target/my-finance-tracker-1.0-SNAPSHOT.jar
```

Application functions

After the application is launched, you will see the login interface:

1. "Login/Registration" : An account needs to be registered when used for the first time
2. "Main Interface Functions" :
- Dashboard: Overview of Financial Status
- Transaction records: Manage income and expenditure
- Accounts: Manage different financial accounts
- Analysis: Charts and financial analysis tools
- Credit Card: Credit card management
- Currency conversion: Currency exchange calculation
- BuckBrainAI Chat: AI Financial Assistant

Configuration options

The application data is stored in the local file:
- User data: 'users.txt' (automatically created in the project root directory)
- Financial data: Stored in the corresponding directory by username

## Development Guide

Add new features

1. Create a new class in the corresponding directory
2. Follow the existing code structure and naming conventions
3. Update the UI components to integrate new functions

Run the test

```
mvn test
```

Troubleshooting

### Common Questions

1. Startup error "--enable-preview"
Make sure to use JDK 21 and the '--enable-preview' flag

2. Cannot find the main class 
Confirm that the mainClass setting in the Maven configuration is correct (currently it is' ui.Main ')

3. UI Display Problem
Check whether the picture resources are in the correct location (the 'bin\photo\' directory).



# Additional-info: SEP_52
This is the work of Group 52.

## The product backlog
The product backlog is made by Weijing He and Jiayue Ma.

## The prototype
The prototype is made by Zhimu Zhou and Chen Chen.

## Front-end interface and large model integration
The front-end interface and large model integration are completed by Weiguang Wang.

## AI generated report and transaction records
The AI-generated report function, AI chat history, and transaction record export function are completed by Chen Chen.

## Page integration and Import function adding
The login and register page writing and CSV file support are completed by Zecheng Zuo.

## Interface visualization synchronization
The interface visualization synchronization is completed by Weiguang Wang.

## function: Mulple cards, data visualization, data sync, AI classification, English report
Made by Zhimu Zhou

## Currency setting and Data protection 
Currency setting is made by Weijin He,Front-end page beautification is made by mjy,data protection is made by Jiayue Ma.

## Beautify the displayed page
Uniform font and backgroud colour are changed by Weijin He, sidebar colour and button size and Front-end page beautification are changed by Jiayue Ma.

## Complete Junit test of function
Maded by Jiayue Ma and Weijin He.

## Update Junit test version and fix the version conflict
Made by Zecheng Zuo

## Modify the Chinese display on some pages
Made by Jiayue Ma

## Add Javadocs for the added code snippets
Made by Jiayue Ma,Weijin He and Zhimu Zhou

## Write the report and complete UML class diagram and User menu
Made by Jiayue Ma and Weijin He

## Write readme file to instruct how to set up or configure and run sofeware
Made by Jiayue Ma and Weijin He
