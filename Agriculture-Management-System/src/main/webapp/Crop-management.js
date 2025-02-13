$(document).ready(function () {
    // When the landform is selected, update climate and country options
    $("#landform").change(function () {
        var selectedLandform = $(this).val();
        if (!selectedLandform) return;

        $.ajax({
            url: "Agriculture-Management-System/CropManagementServlet",
            type: "GET",
            data: { action: "getCountriesByLandform", landform: selectedLandform },
            dataType: "json",
            success: function (data) {
                var countryDropdown = $("#country");
                countryDropdown.empty();
                countryDropdown.append('<option value="">Select a Country</option>');
                $.each(data.countries, function (index, country) {
                    countryDropdown.append('<option value="' + country + '">' + country + '</option>');
                });
            },
            error: function (xhr, status, error) {
                console.error("Error fetching countries:", error);
            }
        });
    });

    // When the country is selected, update crop options
    $("#country").change(function () {
        var selectedCountry = $(this).val();
        if (!selectedCountry) return;

        $.ajax({
            url: "Agriculture-Management-System/CropManagementServlet",
            type: "GET",
            data: { action: "getCropsByCountry", country: selectedCountry },
            dataType: "json",
            success: function (data) {
                var cropDropdown = $("#majorCrop");
                cropDropdown.empty();
                cropDropdown.append('<option value="">Select a Crop</option>');
                $.each(data.crops, function (index, crop) {
                    cropDropdown.append('<option value="' + crop + '">' + crop + '</option>');
                });
            },
            error: function (xhr, status, error) {
                console.error("Error fetching crops:", error);
            }
        });
    });

    // Fetch crop periods when the user clicks Calculate
    $("#calculate").click(function () {
        var selectedCrop = $("#majorCrop").val();
        if (!selectedCrop) {
            alert("Please select a crop.");
            return;
        }

        $.ajax({
            url: "Agriculture-Management-System/CropManagementServlet",
            type: "GET",
            data: { action: "getPeriods", crop: selectedCrop },
            dataType: "json",
            success: function (data) {
                if (data.error) {
                    alert("Error: " + data.error);
                } else {
                    $("#totalPeriod").val(data.totalPeriod);
                    $("#growthPeriod").val(data.growthPeriod);
                    $("#productivityPeriod").val(data.productivityPeriod);
                }
            },
            error: function (xhr, status, error) {
                alert("AJAX Error: " + error);
            }
        });
    });
});
