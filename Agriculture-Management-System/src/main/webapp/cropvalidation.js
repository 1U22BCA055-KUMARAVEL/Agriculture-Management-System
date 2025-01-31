function validateCrop() {
    let cropName = document.getElementById("cropName").value.trim();
    let notification = document.getElementById("notification");

    if (cropName === "") {
        notification.innerHTML = "Please enter a crop name.";
        notification.className = "notification error";
        return;
    }

    $.ajax({
        url: "/Agriculture-Management-System/CropValidationServlet",
        type: "POST",
        data: { cropName: cropName },
        dataType: "json",
        success: function(response) {
            if (response.error) {
                notification.innerHTML = response.error;
                notification.className = "notification error";
            } else {
                document.getElementById("soilType").value = response.soilType;
                document.getElementById("totalPeriod").value = response.totalPeriod;
                document.getElementById("growthPeriod").value = response.growthPeriod;
                document.getElementById("productivityPeriod").value = response.productivityPeriod;
                notification.innerHTML = "Crop data retrieved successfully!";
                notification.className = "notification success";
            }
        },
        error: function() {
            notification.innerHTML = "Error fetching crop data.";
            notification.className = "notification error";
        }
    });
}
