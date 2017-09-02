#Running the app

1. Create Docker network
```bash
docker network create --subnet=172.18.0.0/16 mynet
```
2. Run the solr on docker
```bash
docker run --name my_solr --net mynet --ip 172.18.0.22 -d -p 8983:8983 -t solr
```
3. Create solr Core
```bash
docker exec -it --user=solr my_solr bin/solr create_core -c userIndex
```
Or if you are using windows
```bash
winpty docker exec -it --user=solr my_solr bin/solr create_core -c userIndex
```
4.Build an application
```bash
./gradlew build buildDocker
```
On windows use
```bash
./gradlew.bat build buildDocker
```
4.Make sure that you have an Application image on docker
```bash
docker images
```
You should see the application in the list of the images:
```bash
REPOSITORY              TAG                 IMAGE ID            CREATED             SIZE
matmatch/matmatch-app   1.0-SNAPSHOT        de9706a8259c        2 days ago          742MB
```
5. Start the application using the same docker network
```bash
 docker run --name matmatch --net mynet --ip 172.18.0.23 -p 8080:8080 -t matmatch/matmatch-app:1.0-SNAPSHOT
```
