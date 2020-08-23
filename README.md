# JobScheduler
A RESTful service which allows you to run multiple recurring jobs with specified start and end times. Jobs which can be run include:
- Jobs to print supplied content
- Jobs to perform supplied system calls
- Jobs to write supplied content to a file

## Running the Project
Clone the changes: `git clone https://github.com/corey2604/JobScheduler.git`

Install Maven (if you do not already have maven installed):
- Download Maven from https://maven.apache.org/download.cgi
- Follow the instructions at https://mkyong.com/maven/install-maven-on-mac-osx/ to install

Run the project by running `mvn spring-boot:run` from the project root. This will run the project on localhost:8080



## Job Scheduler Instructions
The project has several endpoints. Below I have outlined each endpoint, its expected request format and its usage:

---

### USER UTILITIES


### POST localhost:8080/users/signUp
Creates a user with the specified username and password if the user does not already exist. It will also login as this user (if the sign up is successful). The expected request body format is shown below:
```
{
    "username": "test",
    "password": "test100"
}
```

### POST localhost:8080/users/logIn
Logs in as the user with the specified username and password if the user exists. The expected request body format is shown below:
```
{
    "username": "test",
    "password": "test100"
}
```

### GET localhost:8080/users/current
Returns the username of the currently logged in user.


### GET localhost:8080/users/current/logOut
Logs out the current user.

---

### JOB SCHEDULER UTILITIES


### POST localhost:8080/jobs
Here you can create a job to be run by the scheduler. The expected request body format is shown below:
```
{
    "name": "testJob5",
    "type": "PRINT",
    "content": "test end",
    "startTime": "2020-08-15 10:36:00",
    "endTime": "2020-08-16 15:38:00"
}
```
*KEY:*

"name": The name used to uniquely identify jobs

"type": The type of job to be run, expected types are:
- PRINT: The job will print the content you've supplied every 5 seconds
- SYSTEM_CALL: The job will perform the system call supplied in the content of the request every 5 seconds
- WRITE_TO_FILE: The job will write the supplied content to a file called `jobSchedulerFile.txt` every 5 seconds

"content": The content to be used as part of the job e.g. to be printed or written to a file

"startTime": The time at which a job should be executed. Supplied in the format of "yyyy-MM-dd HH:mm:ss"

"endTime": The time at which a job should cease execution. Supplied in the format of "yyyy-MM-dd HH:mm:ss"

---

### GET localhost:8080/jobs
Retrieves details on all jobs.

### PATCH localhost:8080/jobs/{job name}/stop
Pause execution of the job with the supplied job name.

### PATCH localhost:8080/jobs/{job name}/resume
Resume execution of the job with the supplied job name.

### DELETE localhost:8080/jobs/{job name}
Deletes the job matching the supplied job name outright


## Further Improvements
The following areas could be improved/implemented:
- Expand testing coverage
- Encapsulating error handling within services, especially schedulerService
- More detailed error responses for bad requests
- Data persistence - particularly for user data
- Migrate models to builder pattern
- Increased security around user input/requests (and improved parsing in general, e.g. around dates)
- Allowing users to specify a time interval for jobs
- Allowing for further job coverage, potentially allowing users to execute their own code
- Adding a UI
