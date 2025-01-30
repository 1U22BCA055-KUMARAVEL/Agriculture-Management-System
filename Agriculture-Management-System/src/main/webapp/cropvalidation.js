

function validateCrop() {
    const cropName = document.getElementById("cropName").value;
    const notification = document.getElementById("notification");

    if (cropName.trim() === "") {
        notification.textContent = "Please enter a crop name.";
        notification.className = "notification error";
        return;
    }

	fetch("/CropValidationServlet", {
	    method: "GET",
	    headers: { "Content-Type": "application/x-www-form-urlencoded" },
	    body: `cropName=${encodeURIComponent(cropName)}`
	})
	.then(response => {
	    if (!response.ok) {
	        throw new Error("HTTP error! Status: " + response.status);
	    }
	    return response.text(); // Use text() first to check the response
	})
	.then(text => {
	    try {
	        const data = JSON.parse(text); // Now try to parse JSON
	        if (data.valid) {
	            document.getElementById("soilType").value = data.soilType;
	            document.getElementById("totalPeriod").value = data.totalPeriod;
	            document.getElementById("growthPeriod").value = data.growthPeriod;
	            document.getElementById("productivityPeriod").value = data.productivityPeriod;
	            notification.textContent = "Crop validated successfully!";
	            notification.className = "notification success";
	        } else {
	            notification.textContent = data.message || "Invalid crop name.";
	            notification.className = "notification error";
	        }
	    } catch (error) {
	        console.error("Invalid JSON:", text);
	        notification.textContent = "Invalid server response.";
	        notification.className = "notification error";
	    }
	})
	.catch(error => {
	    notification.textContent = "An error occurred: " + error.message;
	    notification.className = "notification error";
	});
}