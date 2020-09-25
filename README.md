# Reboarding to the office
Accenture competition second round<br>
- [First round description](first_round_descripton.md)<br>
- [Second round description](second_round_descripton.md)<br>
## Running in docker
Build the image:  
`docker image build -t reboarding .`

Run the docker image:  
`docker run -p 8080:8080 -d reboarding [ARGS]`

To start the application basic configuration shall be provided. This includes the dates where the required distance 
between reservable seats decreases. These dates provided in the following format: 'YYYY-MM-DD'. Image file of the 
office layout are stored in the resources. Seat coordinates mapped in advance by hand and stored in 
`seat-coordinates.json`.

**List of arguments:**  
--date.stepTo1  
--date.stepTo2  
--date.stepTo3  
--date.stepTo5  
--date.endOfPeriod 
--numberInWaitnigListBeforeNotification

## Distribute seats
To pack the most people to the office with the given distance criteria, the problem can be represented as a graph. 
The nodes are connected when they are further apart from each other than the given distance. The graphs maximal clique 
is when the most seats can be reserved, and they all compile to the rules. This is find by an implementation of the
Bron-Kerbosch algorithm. With this approach the application finds the optimal seating distribution in about 2 minutes.

## Testing locally

For local testing dummy data is provided in the `application.properties` file, so it can be started without any 
arguments or configuration.

## API testing

API testing is done by Postman Script. For these scripts to run correctly, the current date needs to have a corresponding
capacity. For this reason the application can be started with `apitest` argument for local testing.

```
docker run -p 8080:8080 -d reboarding apitest
newman run postman/postman_collection.json -g postman/postman_globals.json   
```

## CI/CD

Unit tests and build run by Github Action, after every commit or pull request to master.

## Endpoints

HTTP Method | URL | Description
----------- | --- | -----------
POST | /api/reboarding/register?userId={userId}&day={day} | Register a user to a given date
POST | /api/reboarding/entry/{userId} | Check in a user for today
POST | /api/reboarding/exit/{userId} | Check out a user for today
GET | /api/reboarding/status?userId={userId}&day={day} | Get the status of the user for a given day
GET | /api/reboarding/layout?day={day} | Get the status of all seats for a given day (image)
GET | /api/reboarding/layout?userId={userId}&day={day} | Get the location of the reserved seat on a given day (image)

 
