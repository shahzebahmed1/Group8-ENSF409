ENSF 409 Final Project - Group 8

Team Members: Praveen De Silva, Dainielle Puno, Salma Infelas, Shahzeb Ahmed

For our final project in ENSF 409, we developed a Java-based program that interacts with a database to manage and retrieve furniture inventory. The program allows users to input their database credentials, select a type of furniture (e.g., chair, lamp), specify the furniture type, and indicate the quantity needed. It handles user inputs in a case-insensitive manner, trimming any excess whitespace, and guides the user through corrections when necessary by providing feedback for invalid entries.

To run the program, compile all the Java files and execute the Main class. The program will continuously prompt for valid inputs until correct entries are provided. Additionally, the program utilizes the mySQL-connector for database interaction, along with JUnit and Hamcrest libraries for unit testing, which are included in the project's lib folder.

The program is designed to interface with a specific database structure, ensuring compatibility with the inventory.sql file provided on D2L. Make sure to compile and run the provided unit tests to validate the program's functionality.
