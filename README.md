# Reboarding to the office
Accenture competition first round

## Running in docker
Build the image:  
`docker image build -t reboarding .`

Run the docker image:  
`docker run -p 8080:8080 -d reboarding [ARGS]`

To start the application basic configuration shall be provided. This includes the offices full capacity, and the dates
where the number of people who can enter increases. These dates provided in the following format: 'YYYY-MM-DD'.

**List of arguments:**  
--date.stepTo10  
--date.stepTo20  
--date.stepTo30  
--date.stepTo50  
--date.stepTo100  
--fullCapacity

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

Github actions are defined to build the application, run unit tests and API tests and build a docker image. After every
commit or pull request to master these are run automatically. Pushing the docker image is yet to be implemented.

## Endpoints

HTTP Method | URL | Description
----------- | --- | -----------
POST | /api/reboarding/register?userId={userId}&day={day} | Register a user to a given date
POST | /api/reboarding/entry/{userId} | Check in a user for today
POST | /api/reboarding/exit/{userId} | Check out a user for today
GET | /api/reboarding/status?userId={userId}&day={day} | Get the status of the user for a given day
 
