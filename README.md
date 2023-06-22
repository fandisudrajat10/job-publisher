# Job Publisher
 Job Publisher is job portal which connects company and expert freelancer/part-timer. It always keeps
 track of freelancer performance, and the more jobs the freelancer completes, the more
 benefits they gain.



## Requirement

- Java 8
- Maven
- MySQL Database

## Installation

1. Clone this repository to your local machine.
2. Open a terminal and navigate to the project directory.
3. Install MySQL and create a database. For example, create a database called `db_mx100`.
4. Create the necessary tables in the database by executing the following SQL queries:

    - Create the `users` table:

      ```sql
      CREATE TABLE `users` (
        `id` int NOT NULL AUTO_INCREMENT,
        `username` varchar(100) NOT NULL,
        `password` varchar(100) NOT NULL,
        `email` varchar(100) NOT NULL,
        `role` enum('EMPLOYER','FREELANCER') NOT NULL,
        PRIMARY KEY (`id`)
      ) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
      ```

    - Create the `jobs` table:

      ```sql
      CREATE TABLE `jobs` (
        `id` int NOT NULL AUTO_INCREMENT,
        `title` varchar(100) NOT NULL,
        `description` text NOT NULL,
        `status` enum('DRAFT','PUBLISHED') NOT NULL,
        `employer_id` int NOT NULL,
        `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
        `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (`id`),
        KEY `employer_id` (`employer_id`),
        CONSTRAINT `jobs_ibfk_1` FOREIGN KEY (`employer_id`) REFERENCES `users` (`id`)
      ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
      ```

    - Create the `proposals` table:

      ```sql
      CREATE TABLE `proposals` (
        `id` int NOT NULL AUTO_INCREMENT,
        `job_id` int NOT NULL,
        `freelancer_id` int NOT NULL,
        `proposal_text` text NOT NULL,
        `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`id`),
        KEY `job_id` (`job_id`),
        KEY `freelancer_id` (`freelancer_id`),
        CONSTRAINT `proposals_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`),
        CONSTRAINT `proposals_ibfk_2` FOREIGN KEY (`freelancer_id`) REFERENCES `users` (`id`)
      ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
      ```
5. Update the database connection settings in the `application.yml` file located in the `src/main/resources` directory. Modify the following section to match your MySQL database configuration:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://127.0.0.1:3306/db_mx100?allowPublicKeyRetrieval=true&useSSL=false
       driver-class-name: com.mysql.cj.jdbc.Driver
       username: root
       password: my-secret-pw 
    ```
6. Save the changes to application.yml.
7. Run the following command to compile and build the project:

   ```shell
   mvn clean install
   ```
8. After the build process is complete, you can start the application by running the following command:

   ```shell
   mvn spring-boot:run
   ```
   The application will be launched and accessible at localhost:8080.

Please note that the commands `mvn clean install` and `mvn spring-boot:run` assume that you have Maven installed and available in your system's PATH. Make sure you have Java 8 and Maven properly set up on your machine before running these commands.


## API Endpoints
Below is a list of available APIs in the application:

### Create User
API endpoint for creating a new user. The API requires authentication with an API Key provided in the request headers (`X-API-KEY`). The user can be created with the role of EMPLOYER or FREELANCER. Note: The value of the API Key can be changed in the application.yml file.
- Path: /api/v1/users
- Method: POST
- Headers : X-API-KEY:e489130a-fc47-11ed-be56-0242ac120002
- request:
```json
{
    "username":"user1",
    "password":"testpw",
    "email":"user1@expample.com",
    "role":"EMPLOYER" // enum EMPLOYER / FREELANCER

}
```
- response:
```json
{
    "data": "OK",
    "errors": null
}
```

### Login User
API endpoint for user authentication. This API is used to obtain a token that is required for accessing other APIs.
- Path: /api/v1/auth/login
- Method: POST
- request:
```json
{
    "username":"user1",
    "password":"testpw"
}
```
- response:
```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbXAxIiwicm9sZSI6IkVNUExPWUVSIiwiaWF0IjoxNjg1MTc4NTM3LCJleHAiOjE2ODUxODIxMzd9.fWCl0MEhb_gFDIiaGvQAPEEQR-Ad_ReZAukMXppSlKd-f_l1AmfMi-b4W5FimdnFCBL_DwztHNwK227oWkEJdA"
}
```

### Create Job
API endpoint for creating a new job. Only employers can create jobs using this API.
- Path: /api/v1/job
- Method: POST
- Headers : Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbXAxIiwicm9sZSI6IkVNUExPWUVSIiwiaWF0IjoxNjg1MTc3ODY3LCJleHAiOjE2ODUxODE0Njd9.1JrlJ_m0IeIcRzEMMnvh_i6C-u4eCjwt3sDG8o8FXKzO2KCdfwpydjfsewzHan8GXcnyvKhLM8xAh8sYRRYisg
- request:
```json
{
    "title":"job title 5",
    "description":"description job 5",
    "status":"PUBLISHED" // enum DRAFT / PUBLISHED
}
```
- response:
```json
{
    "data": {
        "id": 12,
        "title": "job title 5",
        "description": "description job 5",
        "status": "PUBLISHED"
    },
    "errors": null
}
```

### Search Job
API endpoint for retrieving a list of jobs. If the token used has the role of EMPLOYER, all jobs with the status of DRAFT or PUBLISHED will be returned. If the token has the role of FREELANCER, only published jobs will be returned. Optional query parameters can be used to search for jobs based on their title or description.
- Path: /api/v1/job/list
- Method: GET
- Headers : Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbXAxIiwicm9sZSI6IkVNUExPWUVSIiwiaWF0IjoxNjg1MTc3ODY3LCJleHAiOjE2ODUxODE0Njd9.1JrlJ_m0IeIcRzEMMnvh_i6C-u4eCjwt3sDG8o8FXKzO2KCdfwpydjfsewzHan8GXcnyvKhLM8xAh8sYRRYisg
- Query Param :
```query
- title: title //optional for search
- description: description //optional for search
``` 
- response:
```json
{
    "data": [
        {
            "id": 7,
            "title": "job title 1",
            "description": "description job 1",
            "status": "PUBLISHED"
        },
        {
            "id": 8,
            "title": "job title 2",
            "description": "description job 2",
            "status": "DRAFT"
        }
    ],
    "errors": null
}
```

### Publish Job
API endpoint for publishing a job that was previously in the DRAFT status. Only employers can publish jobs using this API.
- Path: /api/v1/job/publish/{jobId}
- Method: PATCH
- Headers : Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbXAxIiwicm9sZSI6IkVNUExPWUVSIiwiaWF0IjoxNjg1MTc3ODY3LCJleHAiOjE2ODUxODE0Njd9.1JrlJ_m0IeIcRzEMMnvh_i6C-u4eCjwt3sDG8o8FXKzO2KCdfwpydjfsewzHan8GXcnyvKhLM8xAh8sYRRYisg
- response:
```json
{
    "data": "OK",
    "errors": null
}
```

### Submit Proposal
API endpoint for freelancers to submit a proposal for a job. Each freelancer can only submit one proposal for a job.
- Path: /api/v1/proposal
- Method: POST
- Headers : Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbXAxIiwicm9sZSI6IkVNUExPWUVSIiwiaWF0IjoxNjg1MTc3ODY3LCJleHAiOjE2ODUxODE0Njd9.1JrlJ_m0IeIcRzEMMnvh_i6C-u4eCjwt3sDG8o8FXKzO2KCdfwpydjfsewzHan8GXcnyvKhLM8xAh8sYRRYisg
- request:
```json
{
    "jobId": 2,
    "proposalText":"test text proposal"
}
```
- response:
```json
{
    "data": "OK",
    "errors": null
}
```

### Get My Proposal
API endpoint for freelancers to retrieve their submitted proposal details for a job.
- Path: /api/v1/proposal/my-proposal
- Method: GET
- Headers : Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbXAxIiwicm9sZSI6IkVNUExPWUVSIiwiaWF0IjoxNjg1MTc3ODY3LCJleHAiOjE2ODUxODE0Njd9.1JrlJ_m0IeIcRzEMMnvh_i6C-u4eCjwt3sDG8o8FXKzO2KCdfwpydjfsewzHan8GXcnyvKhLM8xAh8sYRRYisg
- response:
```json
{
    "data": {
        "id": 7,
        "jobTitle": "job title 1",
        "jobDescription": "description job 1",
        "proposalText": "test text proposal",
        "employerUsername": "emp1"
    },
    "errors": null
}
```

### Get My Job Proposal
API endpoint for employers to retrieve a list of jobs that have received proposals from freelancers.
- Path: /api/v1/proposal/my-jobs
- Method: GET
- Headers : Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbXAxIiwicm9sZSI6IkVNUExPWUVSIiwiaWF0IjoxNjg1MTc3ODY3LCJleHAiOjE2ODUxODE0Njd9.1JrlJ_m0IeIcRzEMMnvh_i6C-u4eCjwt3sDG8o8FXKzO2KCdfwpydjfsewzHan8GXcnyvKhLM8xAh8sYRRYisg
- response:
```json
{
    "data": [
        {
            "id": 7,
            "jobTitle": "job title 1",
            "jobDescription": "description job 1",
            "freelancerUsername": "freelancer2",
            "freelancerEmail": "freelancerxx1@expample.com",
            "proposalText": "test text proposal"
        },
        {
            "id": 8,
            "jobTitle": "job title 1",
            "jobDescription": "description job 1",
            "freelancerUsername": "freelancer3",
            "freelancerEmail": "freelancerxx3@expample.com",
            "proposalText": "test text proposal"
        }
    ],
    "errors": null
}
```
