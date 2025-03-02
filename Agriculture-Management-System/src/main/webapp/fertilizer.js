$(document).ready(function() {
    const nitrogenFixingAgents = ["Groundnut", "Guar", "Pea", "Lentil", "Soybean"];
    const phosphorusFixingAgents = ["Sunflower", "Alfalfa", "Chickpea"];
    const potassiumFixingAgents = ["Banana", "Coconut", "Papaya"];

    $("#fertilizerForm").submit(function(event) {
        event.preventDefault();

        var previousCrop = $("#previousCrop").val();
        var currentCrop = $("#currentCrop").val();
        var fertilizerType = $("#fertilizerType").val();

        $.ajax({
            url: "/Agriculture-Management-System/FertilizerServlet",
            method: "POST",
            data: { previousCrop, currentCrop, fertilizerType },
            success: function(response) {
                if (response.error) {
                    $("#fertilizerOutput").val("Error: " + response.error);
                    return;
                }

                let nitrogen = parseFloat(response.nitrogen || 0);
                let phosphorus = parseFloat(response.phosphorus || 0);
                let potassium = parseFloat(response.potassium || 0);
                let organicMatter = response.organicMatter || "";

                if (nitrogenFixingAgents.includes(previousCrop)) nitrogen -= parseFloat(response.nitrogenReduction || 0);
                if (phosphorusFixingAgents.includes(previousCrop)) phosphorus -= parseFloat(response.phosphorusReduction || 0);
                if (potassiumFixingAgents.includes(previousCrop)) potassium -= parseFloat(response.potassiumReduction || 0);

                let resultText = fertilizerType === "natural" ? `Organic Matter: ${organicMatter}` : 
                    `Nitrogen: ${nitrogen.toFixed(2)} kg/ha\nPhosphorus: ${phosphorus.toFixed(2)} kg/ha\nPotassium: ${potassium.toFixed(2)} kg/ha`;

                $("#fertilizerOutput").val(resultText);
            },
            error: function() {
                alert("Error fetching fertilizer recommendations.");
            }
        });
    });
});
