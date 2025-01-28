// Function to toggle the menu visibility
function toggleMenu() {
    var menu = document.getElementById('menu');
    menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
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
        url: 'getUsername', // Servlet to check if session is active
        type: 'GET',
        success: function(response) {
            if (response.loggedIn) {
                $('#username').text('Hello, ' + response.username); // Set username if logged in
                $('#logout-btn').show(); // Show logout button
                $('#login-btn').hide();  // Hide login button
            } else {
                $('#logout-btn').hide(); // Hide logout button if not logged in
                $('#login-btn').show();  // Show login button
            }

            // Check if the username is 'admin'
            if (response.username === 'admin') {
                // Show the "Create Report" button in the bottom-right corner if the user is admin
                $('#create-report-btn').show(); // Show "Create Report" button
            } else {
                $('#create-report-btn').hide(); // Hide "Create Report" button if the user is not admin
            }
        },
        error: function() {
            alert('Error checking session. Please try again later.');
        }
    });

    // Handle Create Report button click
    $('#create-report-btn').click(function() {
        var moduleName = 'some_module'; // The name of the module being accessed (you can dynamically pass it based on user actions)

        // Log the module access
        $.post('logActivity', {username: 'admin', module_name: moduleName}, function(response) {
            console.log(response); // Log successful access
        }).fail(function() {
            alert('Error logging module visit');
        });

        // Open a new tab to display the report
        var reportWindow = window.open('logActivity', '_blank');
    });
});
