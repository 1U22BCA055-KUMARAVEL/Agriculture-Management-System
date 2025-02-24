document.addEventListener("DOMContentLoaded", function () {
    const cropSelect = document.getElementById("majorCrop");
    const calculateButton = document.getElementById("calculate");
    const resultDiv = document.getElementById("result");

    if (!cropSelect || !calculateButton || !resultDiv) {
        console.error("Missing required DOM elements.");
        return;
    }

    function fetchCropPeriods(crop) {
        if (!crop) {
            alert("Please select a valid crop.");
            return;
        }

        const fetchUrl = `/Agriculture-Management-System/CropManagementServlet?crop=${encodeURIComponent(crop)}`;
        console.log("Fetching crop data from:", fetchUrl);

        fetch(fetchUrl)
            .then(response => {
                if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    resultDiv.innerHTML = `<p style="color:red;">Error: ${data.error}</p>`;
                } else {
                    resultDiv.innerHTML = `
                        <p><strong>Total Period:</strong> ${data.totalPeriod} days</p>
                        <p><strong>Growth Period:</strong> ${data.growthPeriod} days</p>
                        <p><strong>Productivity Period:</strong> ${data.productivityPeriod} days</p>
                    `;
                }
            })
            .catch(error => {
                console.error("Error fetching crop periods:", error);
                resultDiv.innerHTML = `<p style="color:red;">Error fetching crop periods: ${error.message}</p>`;
            });
    }

    calculateButton.addEventListener("click", function () {
        fetchCropPeriods(cropSelect.value.trim());
    });
});
