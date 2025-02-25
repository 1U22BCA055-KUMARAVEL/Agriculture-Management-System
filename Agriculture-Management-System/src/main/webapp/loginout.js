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

                // Check if the username is 'admin'
                if (response.username === 'admin') {
                    $('#create-report-btn').show(); // Show the Create Report button for admin
                }
            } else {
                $('#logout-btn').hide(); // Hide logout button if not logged in
                $('#login-btn').show();  // Show login button
            }
        },
        error: function() {
            alert('Error checking session. Please try again later.');
        }
    });

    // Log page visits and module access when the page is loaded
    var moduleName = 'module_name_here'; // Set this dynamically based on the page/module the user is visiting
    var username = 'user_name_here'; // Fetch the logged-in user's username dynamically

    $.post('logActivity', {username: username, module_name: moduleName}, function(response) {
        console.log(response); // Log successful access
    }).fail(function() {
        alert('Error logging module visit');
    });

    // Handle Create Report button click
    $('#create-report-btn').click(function() {
        // Open a new tab to display the report
        var reportWindow = window.open('logActivity', '_blank');
    });
	
});