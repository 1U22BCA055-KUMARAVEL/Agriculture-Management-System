document.addEventListener("DOMContentLoaded", function () {
    const landformSelect = document.getElementById("landform");
    const climateSelect = document.getElementById("climate");
    const soilTypeSelect = document.getElementById("soiltype");
    const majorCropSelect = document.getElementById("majorCrop");
    const calculateButton = document.getElementById("calculate");
    const resultDiv = document.getElementById("result");
    
    // Landform-Climate Mapping
    const landformClimateMap = {
        "Valley": "Humid & Fertile (Valley)",
        "Mountain Slopes": "Cool, Humid, Temperate (Mountain-Slopes)",
        "Plateau": "Semi-Arid, Dry Temperate (Plateau)",
        "Plains": "Temperate, Subtropical (Plains)",
        "Coastal": "Tropical, Humid (Coastal)",
        "Desert": "Arid, Hot, Dry (Desert)"
    };
    
    // Soil Type - Crop Mapping
    const soilCropMap = {
        "Alluvial Soil": ["Rice", "Sugarcane", "Jute", "Banana"],
        "Loamy Soil": ["Tea", "Apple", "Walnut", "Cardamom"],
        "Volcanic Soil": ["Coffee"],
        "Black Soil": ["Sorghum", "Cotton", "Finger Millet", "Groundnut"],
        "Laterite Soil": ["Cashew", "Rubber", "Tea"],
        "Clayey Soil": ["Wheat", "Maize", "Sunflower", "Barley"],
        "Sandy Soil": ["Coconut", "Cashew"],
        "Saline Soil": ["Salt-Tolerant Rice"],
        "Arid Soil": ["Date Palm", "Guar", "Pearl Millet", "Aloe Vera"]
    };
    
    // Function to validate landform-climate selection
    function validateLandformClimate() {
        const selectedLandform = landformSelect.value;
        const selectedClimate = climateSelect.value;
        
        return landformClimateMap[selectedLandform] === selectedClimate;
    }
    
    // Function to validate soil type - crop selection
    function validateSoilTypeCrop() {
        const selectedSoilType = soilTypeSelect.value;
        const selectedCrop = majorCropSelect.value.replace(/\s*\([^)]*\)/g, "").trim(); // Extract crop name
        return soilCropMap[selectedSoilType]?.includes(selectedCrop);
    }
    
    // Function to fetch crop periods
    function fetchCropPeriods(crop) {
		fetch(`/CropManagementServlet?action=getPeriods&crop=${encodeURIComponent(crop)}`)
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    resultDiv.innerHTML = `<p>Error: ${data.error}</p>`;
                } else {
                    resultDiv.innerHTML = `
                        <p><strong>Total Period:</strong> ${data.totalPeriod} days</p>
                        <p><strong>Growth Period:</strong> ${data.growthPeriod} days</p>
                        <p><strong>Productivity Period:</strong> ${data.productivityPeriod} days</p>
                    `;
                }
            })
            .catch(error => {
                resultDiv.innerHTML = `<p>Error fetching crop periods: ${error.message}</p>`;
            });
    }
    
    // Event Listener for Calculate Button
    calculateButton.addEventListener("click", function () {
        if (!validateLandformClimate()) {
            alert("Mismatch: Selected landform does not match the selected climate.");
            return;
        }
        
        if (!validateSoilTypeCrop()) {
            alert("Mismatch: Selected crop does not match the selected soil type.");
            return;
        }
        
        // Fetch and display crop periods if validation passes
        fetchCropPeriods(majorCropSelect.value.replace(/\s*\([^)]*\)/g, "").trim());
    });
});
