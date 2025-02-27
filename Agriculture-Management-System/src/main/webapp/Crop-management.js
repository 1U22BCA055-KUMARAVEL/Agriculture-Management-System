document.addEventListener("DOMContentLoaded", function () {
    const landformSelect = document.getElementById("landform");
    const climateSelect = document.getElementById("climate");
    const soilTypeSelect = document.getElementById("soiltype");
    const cropSelect = document.getElementById("majorCrop");
    const calculateButton = document.getElementById("calculate");
	document.addEventListener("DOMContentLoaded", function () {
	    console.log("DOM Loaded");
	    console.log("landform:", document.getElementById("landform"));
	    console.log("climate:", document.getElementById("climate"));
	    console.log("soiltype:", document.getElementById("soiltype"));
	    console.log("majorCrop:", document.getElementById("majorCrop"));
	    console.log("calculate:", document.getElementById("calculate"));
	});

    const climateMapping = {
        "Valley": "Humid & Fertile (Valley)",
        "Mountain Slopes": "Cool, Humid, Temperate (Mountain-Slopes)",
        "Plateau": "Semi-Arid, Dry Temperate (Plateau)",
        "Plains": "Temperate, Subtropical (Plains)",
        "Coastal": "Tropical, Humid (Coastal)",
        "Desert": "Arid, Hot, Dry (Desert)"
    };

    const cropSoilMapping = {
        "Rice": "Alluvial Soil", "Sugarcane": "Alluvial Soil", "Jute": "Alluvial Soil",
        "Banana": "Alluvial Soil", "Tea": "Loamy Soil", "Apple": "Loamy Soil",
        "Walnut": "Loamy Soil", "Cardamom": "Loamy Soil", "Coffee": "Volcanic Soil",
        "Sorghum": "Black Soil", "Cotton": "Black Soil", "Finger Millet": "Black Soil",
        "Groundnut": "Black Soil", "Cashew": "Laterite Soil", "Rubber": "Laterite Soil",
        "Wheat": "Clayey Soil", "Maize": "Clayey Soil", "Sunflower": "Clayey Soil",
        "Barley": "Clayey Soil", "Coconut": "Sandy Soil", "Salt-Tolerant Rice": "Saline Soil",
        "Date Palm": "Arid Soil", "Guar": "Arid Soil", "Pearl Millet": "Arid Soil",
        "Aloe Vera": "Arid Soil"
    };

    function validateSelection() {
        const selectedLandform = landformSelect.value;
        const selectedClimate = climateSelect.value;
        const selectedSoil = soilTypeSelect.value;
        const selectedCropText = cropSelect.options[cropSelect.selectedIndex].text;
        const selectedCrop = cropSelect.value;

        if (climateMapping[selectedLandform] !== selectedClimate) {
            alert("Selected Climate does not match the Landform.");
            return false;
        }

        if (cropSoilMapping[selectedCrop] !== selectedSoil) {
            alert("Selected Soil Type does not match the Major Crop.");
            return false;
        }

        return selectedCrop;
    }

    function fetchCropPeriods(crop) {
        const fetchUrl = `/Agriculture-Management-System/CropManagementServlet?crop=${encodeURIComponent(crop)}`;
        
        fetch(fetchUrl)
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    alert("Error: " + data.error);
                } else {
                    document.getElementById("totalPeriod").value = data.totalPeriod;
                    document.getElementById("growthPeriod").value = data.growthPeriod;
                    document.getElementById("productivityPeriod").value = data.productivityPeriod;
                }
            })
            .catch(error => alert("Error fetching crop data: " + error.message));
    }

    calculateButton.addEventListener("click", function () {
        const validCrop = validateSelection();
        if (validCrop) {
            fetchCropPeriods(validCrop);
        }
    });
});
