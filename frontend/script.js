function login() {

    alert("Login page will open here.");

}


function register() {

    alert("Registration page will open here.");

}


function searchJobs() {

    let job = document.getElementById("jobSearch").value;
    let location = document.getElementById("locationSearch").value;

    if (job === "" && location === "") {

        alert("Please enter a job or location.");

        return;
    }

    alert(
        "Searching for:\nJob: " +
        job +
        "\nLocation: " +
        location
    );

}


function viewJob(jobName) {

    alert(
        "You selected: " +
        jobName +
        "\nJob listing page will open here."
    );

}