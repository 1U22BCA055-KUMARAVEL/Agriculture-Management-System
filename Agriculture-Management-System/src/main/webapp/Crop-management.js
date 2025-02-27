document.addEventListener("DOMContentLoaded", function () {
    const landformSelect = document.getElementById("landform");
    const climateSelect = document.getElementById("climate");
    const soilTypeSelect = document.getElementById("soiltype");
    const cropSelect = document.getElementById("majorCrop");
    const calculateButton = document.getElementById("calculate");

    function extractBracketValue(optionText) {
        const match = optionText.match(/\((.*?)\)/);
        return match ? match[1].trim() : null;
    }

    function validateSelection() {
        const selectedLandform = landformSelect.value;
        const selectedClimate = climateSelect.value;
        const selectedSoilType = soilTypeSelect.value;
        const selectedCrop = cropSelect.value;

        const landformBracketValue = extractBracketValue(climateSelect.options[climateSelect.selectedIndex].text);
        const climateBracketValue = extractBracketValue(soilTypeSelect.options[soilTypeSelect.selectedIndex].text);
        const soilBracketValue = extractBracketValue(cropSelect.options[cropSelect.selectedIndex].text);

        if (landformBracketValue !== selectedLandform) {
            alert("Error: Selected Climate does not match the expected Landform.");
            return null;
        }

        if (climateBracketValue !== selectedClimate) {
            alert("Error: Selected Soil Type does not match the expected Climate.");
            return null;
        }

        if (soilBracketValue !== selectedSoilType) {
            alert("Error: Selected Major Crop does not match the expected Soil Type.");
            return null;
        }

        return selectedCrop;
    }

    function fetchCropPeriods(crop) {
        if (!crop) {
            alert("Error: Invalid selection. Please check the dependencies between fields.");
            return;
        }
		else{
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
	}

    calculateButton.addEventListener("click", function () {
        const validCrop = validateSelection();
        if (validCrop) {
            fetchCropPeriods(validCrop);
        }
    });
});