#Install & Run the application:
1. Copy the SmartPortables folder and paste it into the webapps folder of tomcat directory.
2. All the servlets are mentioned in the deployment descriptor file web.xml
3. Start the MongoDb Server first. Then start the MySql server. After both the database servers are started,
then start the Tomcat server.
4. Run the application by writing localhost/SmartPortables in the browser.

------------------

#All new code added for the deals match feature is placed in a class DealMatches.java.

#The Python script to connect and fetch tweets is present in SmartPortables folder.

#All twitter app credentials is in credentials.txt file.

#All twitter deals matching tweets is written to DealMatches.txt file in SmartPortables folder.

#All the code for MySQL is in the class MySqlDataStoreUtilities.

#All the code for MongoDB is in the class MongoDBDataStoreUtilities.