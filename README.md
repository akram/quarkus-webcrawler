# quarkus-webcrawler

## Compile, build, test & run
```
mvn compile quarkus:dev
mvn test
```

## Building docker image
```
mvn package -Pnative -Dnative-image.docker-build=true
docker build -f src/main/docker/Dockerfile -t quarkus/quarkus-webcrawler .
```

## Run docker image
```
docker run -p 8080:8080 quarkus/quarkus-webcrawler
```


