# HireHub - Online Job Portal

HireHub is a Java-based web application that connects job seekers and recruiters through an online job portal.

The project is developed using Core Java, Java Servlets, JDBC, MySQL, HTML, CSS and JavaScript.

## Features

### Job Seeker
- Register and Login
- View available jobs
- Search jobs
- View job details
- Apply for jobs
- View applications
- Withdraw applications
- View profile
- Edit profile
- Change password

### Recruiter
- Register and Login
- Post jobs
- View posted jobs
- Edit jobs
- Delete jobs
- View applicants
- Update application status
- View and edit profile
- Change password

## Technologies Used

- Java
- Java Servlets
- JDBC
- MySQL
- HTML5
- CSS3
- JavaScript
- Apache Tomcat
- VS Code

## Project Structure

```text
OnlineJobPortal/
│
├── frontend/
│   ├── index.html
│   ├── login.html
│   ├── register.html
│   ├── jobs.html
│   ├── job-details.html
│   ├── jobseeker-dashboard.html
│   ├── recruiter-dashboard.html
│   ├── my-applications.html
│   ├── my-jobs.html
│   ├── post-job.html
│   ├── edit-job.html
│   ├── applicants.html
│   ├── view-applicants.html
│   ├── update-status.html
│   ├── profile.html
│   ├── edit-profile.html
│   ├── change-password.html
│   ├── style.css
│   └── script.js
│
├── servlet/
│   ├── LoginServlet.java
│   ├── RegisterServlet.java
│   ├── JobsServlet.java
│   ├── SearchJobsServlet.java
│   ├── JobDetailsServlet.java
│   ├── ApplyJobServlet.java
│   ├── MyApplicationsServlet.java
│   ├── PostJobServlet.java
│   ├── MyJobsServlet.java
│   ├── ViewApplicantsServlet.java
│   ├── UpdateApplicationStatusServlet.java
│   ├── DeleteJobServlet.java
│   ├── EditJobServlet.java
│   ├── ProfileServlet.java
│   ├── EditProfileServlet.java
│   ├── ChangePasswordServlet.java
│   ├── LogoutServlet.java
│   └── DatabaseConnection.java
│
├── lib/
│   ├── mysql-connector-j-26.7.0.jar
│   └── servlet-api.jar
│
├── Main.java
├── Login.java
├── Register.java
├── DatabaseConnection.java
├── JobSeekerMenu.java
├── RecruiterMenu.java
├── ViewJobs.java
├── SearchJobs.java
├── JobDetails.java
├── ApplyJob.java
├── MyApplications.java
├── WithdrawApplication.java
├── Profile.java
├── EditProfile.java
├── EditRecruiterProfile.java
├── PostJob.java
├── MyJobs.java
├── ViewApplicants.java
├── UpdateApplicationStatus.java
├── DeleteJob.java
├── EditJob.java
├── .gitignore
└── README.md
