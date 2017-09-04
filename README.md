### Running instrictions

Create Docker network
```bash
docker network create --subnet=172.18.0.0/16 mynet
```

Run the solr on docker
```bash
docker run --name my_solr --net mynet --ip 172.18.0.22 -d -p 8983:8983 -t solr
```

Make sure the Solr is running. Try to open an admin console http://localhost:8983/solr

Create solr Core for the application. The name should be the userIndex
```bash
docker exec -it --user=solr my_solr bin/solr create_core -c userIndex
```
Or if you are using windows
```bash
winpty docker exec -it --user=solr my_solr bin/solr create_core -c userIndex
```

Checkout or download the code and build an application
```bash
./gradlew build buildDocker
```
On windows use
```bash
gradlew.bat build buildDocker
```

Make sure that you have an Application image on docker
```bash
docker images
```
You should see the application in the list of the images:
```bash
REPOSITORY              TAG                 IMAGE ID            CREATED             SIZE
matmatch/matmatch-app   1.0-SNAPSHOT        de9706a8259c        2 days ago          742MB
```

Start the application using the same docker network
```bash
 docker run --name matmatch --net mynet --ip 172.18.0.23 -p 8080:8080 -t matmatch/matmatch-app:1.0-SNAPSHOT
```

### API documentation.
The API documentation in swagger format can be found under the following URL:
http://localhost:8080/swagger-ui.html

Or in case you run the app on different host, you should replace the host name with the one where app is actually running.

If something isn't working or you have any questions, feel free to contact me.
