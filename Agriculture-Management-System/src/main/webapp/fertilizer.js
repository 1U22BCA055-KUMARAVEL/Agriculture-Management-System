$(document).ready(function() {
    $("#fertilizerForm").submit(function(event) {
        event.preventDefault();

        var previousCrop = $("#previousCrop").val();
        var currentCrop = $("#currentCrop").val();
        var soilType = $("#soilType").val();
        var fertilizerType = $("#fertilizerType").val();

        $.ajax({
            url: "/Agriculture-Management-System/FertilizerServlet",
            method: "POST",
            data: {
                previousCrop: previousCrop,
                currentCrop: currentCrop,
                soilType: soilType,
                fertilizerType: fertilizerType
            },
            success: function(response) {
                if (response.error) {
                    $("#fertilizerOutput").val("Error: " + response.error);
                    return;
                }
                
                var nitrogen = response.nitrogen ? parseFloat(response.nitrogen) : 0;
                var phosphorus = response.phosphorus ? parseFloat(response.phosphorus) : 0;
                var potassium = response.potassium ? parseFloat(response.potassium) : 0;
                var organicMatter = response.organicMatter ? response.organicMatter : "";

                if (response.nitrogenReduction) {
                    nitrogen -= parseFloat(response.nitrogenReduction);
                }
                if (response.phosphorusReduction) {
                    phosphorus -= parseFloat(response.phosphorusReduction);
                }
                if (response.potassiumReduction) {
                    potassium -= parseFloat(response.potassiumReduction);
                }
                
                var resultText = "";
                if (fertilizerType === "natural") {
                    resultText = "Organic Matter: " + organicMatter;
                } else if (fertilizerType === "man-made") {
                    resultText = "Nitrogen: " + nitrogen.toFixed(2) + " kg per hectare\n" +
                                 "Phosphorus: " + phosphorus.toFixed(2) + " kg per hectare\n" +
                                 "Potassium: " + potassium.toFixed(2) + " kg per hectare";
                }

                $("#fertilizerOutput").val(resultText);
            },
            error: function() {
                alert("Error fetching fertilizer recommendations.");
            }
        });
    });
});