# Ranpak


To deploy the back-end website locally use backend3 and go into application.yml
comment out all of the active code except for aws s3bucketName and then uncomment the code below it
application.yml file

click on docker compose which will spin up a database
go into mysql workbench and connect to that database
go into folder db and run the latest version of the database to create tables
go into folder db -> scripts and copy the sql statement in appreciate_client_provisioning to initialize oauth 2 and basic authorization
you will need my aws accessKeyId and secretAccessKey in order to perform storage actions
so please ask and do not submit them anywhere



