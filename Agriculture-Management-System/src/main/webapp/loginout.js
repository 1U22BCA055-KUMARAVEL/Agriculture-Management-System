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
        },
        error: function() {
            alert('Error checking session. Please try again later.');
        }
    });
});

 function validateCrop() {
        const cropName = document.getElementById("cropName").value;
        const notification = document.getElementById("notification");

        if (cropName.trim() === "") {
            notification.textContent = "Please enter a crop name.";
            notification.className = "notification error";
            return;
        }

        // AJAX request to check crop validity
        fetch("CropValidationServlet", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `cropName=${encodeURIComponent(cropName)}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.valid) {
                document.getElementById("totalPeriod").value = data.totalPeriod;
                document.getElementById("growthPeriod").value = data.growthPeriod;
                document.getElementById("productivityPeriod").value = data.productivityPeriod;
                notification.textContent = "Crop validated successfully!";
                notification.className = "notification success";
            } else {
                notification.textContent = "Invalid crop name. Please try again.";
                notification.className = "notification error";
            }
        })
        .catch(error => {
            notification.textContent = "An error occurred: " + error.message;
            notification.className = "notification error";
        });
    }

