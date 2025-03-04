// Function to toggle the menu visibility
function toggleMenu() {
    var menu = document.getElementById('menu');
    menu.classList.toggle('active');
}

// Function to handle logout
function logout() {
    // Perform logout by calling the logout servlet using POST
    $.post('logout', function() {
        window.location.href = 'login.html'; // Redirect after logout
    }).fail(function() {
        alert('Error logging out. Please try again later.');
    });
}

// Dynamically set username if logged in
$(document).ready(function() {
    $.ajax({
        url: '/Agriculture-Management-System/getUsername',
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            if (response.loggedIn) {
                $('#username').text('Hello, ' + response.username);

                // Log activity using the actual username
                logUserActivity(response.username);
            }
        }
    });
});

// Function to log user activity
function logUserActivity(username) {
    var moduleName = window.location.pathname.split("/").pop().replace(".html", ""); // Get current page name

    $.post('/Agriculture-Management-System/logActivity', {username: username, module_name: moduleName}, function(response) {
        console.log(response); // Log success
    }).fail(function() {
        alert('Error logging module visit');
    });
}
