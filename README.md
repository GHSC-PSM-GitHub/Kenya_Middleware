### ODK DHIS 2 INTEGRATION INSTALLATION

This web application is a Spring boot application. To build the war file required for installation, make sure you have Apache maven installed on your development machine. You've 2 installation options; docker or normal tomcat.

## Without Docker
Download Apache tomcat 9 and put it anywhere on your server. 
Install Postgres v9+ and create a database called `odkdhis` in the public schema.
Open `/src/main/resources/application.properties` and move to `DB Settings` section. This section looks like below:

==============================================================
= DB Settings  
==============================================================
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://postgres_db:5432/odkdhis
spring.datasource.username=username
spring.datasource.password=Password@123
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect


Replace `postgres_db` with hostname/IP address of the database on `spring.datasource.url`
Change the values of `spring.datasource.username=` (username) and `spring.datasource.password=` (password)

To build the war file go to terminal and change directory to the directory containing this file (odkdhis) and run `mvn clean install -Dmaven.test.skip=true`. Copy the war file in `target` folder to the `webapps` directory of your tomcat installation and start your tomcat.

## With Docker
Open `./docker/docker-compose.yml` in any editor to the below:

version: '3'

services:
  tomcat:
    container_name: odkdhis
    build: ./docker/tomcat
    networks:
      - datalink
    depends_on:
      - pgdb
    ports:
      - "8080:8080"
  pgdb:
    container_name: postgres_db
    image: postgres
    restart: always
    volumes:
      - /root/odkdhisdata:/var/lib/postgresql/data
    environment:
      POSTGRES_USER: username
      POSTGRES_PASSWORD: Password@123
      POSTGRES_DB: odkdhis
    ports:
      - 5430:5432
    networks:
      - datalink
  pgadmin:
    image: dpage/pgadmin4:latest
    environment:
      - "PGADMIN_DEFAULT_EMAIL=simiyuwdan@gmail.com"
      - "PGADMIN_DEFAULT_PASSWORD=simiyu@123"
    ports:
      - "8000:80"
    volumes:
      - /root/pgadmin:/var/lib/pgadmin
    depends_on:
      - pgdb
    networks:
      - datalink

networks:
  datalink:
    driver: bridge
    
    
Go to `pgdb` section and in its environment set postgres username/password and change `/root/odkdhisdata` if necessary to another directory (Postgres data will sit here). You can also change the port at which you can access the `tomcat` service. Now it's '8080:8080', you need to modify the first one. For example if you want to access this application on `8082`, change this to "8082:8080". This means you will be able to access the application at `http://<hostname/Ip address>:8082`

Open `/src/main/resources/application.properties` and move to `DB Settings` section. This section looks like below:

==============================================================
= DB Settings  
==============================================================
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://postgres_db:5432/odkdhis
spring.datasource.username=username
spring.datasource.password=Password@123
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect


Make sure `jdbc:postgresql://postgres_db:5432/odkdhis` is the value of `spring.datasource.url`
Change the values of `spring.datasource.username=` (username) and `spring.datasource.password=` (password) to match the ones you did edit on the `./docker/docker-compose.yml` above.

To build the war file go to terminal and change directory to the directory containing this file (odkdhis) and run `mvn clean install -Dmaven.test.skip=true`. Copy the war file in `target` folder to the    `./docker/docker/tomcat`.

At this point, you've made all updates as mentioned above or you are okay with what is already there. Let's now bring up the application. On terminal, change directory to the directory that contains this file(At time of writing this it was odkdhis).
Run `docker-compose up --build -d` to start the application. Go to `http://<hostname or IP>:PORT(if not 80)` and you should be able to see the application running.
To stop the web application, run `docker-compose down`


NOTE: Once you've started using the application , don't make the changes we did earlier and you may loose some data. But you can always change the PORT.
























































