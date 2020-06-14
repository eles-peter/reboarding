# Reboarding to the office
Accenture competition first round

## Running from docker
Build the image:  
`docker image build -t reboarding .`

Run the docker image:  
`docker run -p 8080:8080 -d reboarding [args]`

## Running Postman api test with newman

```
newman run postman/postman_collection.json -g postman/postman_globals.json   
```
