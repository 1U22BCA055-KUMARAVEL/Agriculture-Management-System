$(document).ready(function() {
            $("#fertilizerForm").submit(function(event) {
                event.preventDefault(); // Prevent form from submitting the traditional way

                var currentCrop = $("#currentCrop").val();
                var soilType = $("#soilType").val();
                var fertilizerType = $("#fertilizerType").val();

                $.ajax({
                    url: "/Agriculture-Management-System/FertilizerServlet", // URL of the servlet
                    method: "POST",
                    data: {
                        currentCrop: currentCrop,
                        soilType: soilType,
                        fertilizerType: fertilizerType
                    },
                    success: function(response) {
                    	if (response.error) {
                            $("#fertilizerOutput").val("Error: " + response.error);
                            return;
                        }
                        var resultText = "";
                        if (fertilizerType === "natural") {
                            resultText = "Organic Matter: " + response.organicMatter;
                        } else if (fertilizerType === "man-made") {
                            resultText = "Nitrogen: " + response.nitrogen + "\n" +
                                         "Phosphorus: " + response.phosphorus + "\n" +
                                         "Potassium: " + response.potassium;
                        } else {
                            resultText = "Invalid fertilizer type selected.";
                        }

                        $("#fertilizerOutput").val(resultText);
                    },
                    error: function() {
                        alert("Error fetching fertilizer recommendations.");
                    }
                });
            });
        });