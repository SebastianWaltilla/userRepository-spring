# A application for a demo of a kafka consumer. 
Start with running ./start-servers.sh to run the containers for the kafka services.

Then start this application together with contest application.

Send a message from contest applications endpoint by POST to localhost:8080/kafka/show

In the consumerkafka applications log, a message showing thats been sent from contest.
