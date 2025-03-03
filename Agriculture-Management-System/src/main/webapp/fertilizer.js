$(document).ready(function() {
    // Setup for custom select dropdowns
    function setupCustomSelect(inputId, selectId) {
        const $input = $(inputId);
        const $select = $(selectId);
        
        // Hide the original select element
        $select.css('display', 'none');
        
        // Show/hide the select when input is focused/blurred
        $input.on('focus', function() {
            $select.show();
            filterOptions($(this).val().toLowerCase(), $select);
        });
        
        // When clicking outside the custom select area, hide the dropdown
        $(document).on('click', function(event) {
            if (!$(event.target).closest('.custom-select').length) {
                $select.hide();
            }
        });
        
        // When typing in the input, filter the options
        $input.on('keyup', function() {
            filterOptions($(this).val().toLowerCase(), $select);
        });
        
        // When selecting an option, update the input and hide the select
        $select.on('change', function() {
            $input.val($(this).find('option:selected').text());
            $select.hide();
        });
        
        // When clicking on an option, update the input and hide the select
        $select.on('click', 'option', function() {
            $input.val($(this).text());
            $select.val($(this).val()).change();
            $select.hide();
        });
        
        // Set initial value
        if ($select.val()) {
            $input.val($select.find('option:selected').text());
        }
    }
    
    // Filter options in the dropdown
    function filterOptions(searchText, $select) {
        $select.find('option').each(function() {
            const optionText = $(this).text().toLowerCase();
            const isMatch = optionText.includes(searchText);
            $(this).toggle(isMatch);
        });
    }
    
    // Set up both custom select dropdowns
    setupCustomSelect('#previousCropInput', '#previousCrop');
    setupCustomSelect('#currentCropInput', '#currentCrop');
    
    // Handle form submission
    $("#fertilizerForm").submit(function(event) {
        event.preventDefault();
        $("#fertilizerOutput").val("Loading recommendations...");
        
        // Get values from the select elements
        const previousCrop = $("#previousCrop").val();
        const currentCrop = $("#currentCrop").val();
        const fertilizerType = $("#fertilizerType").val();
        
        // Validate inputs
        if (!previousCrop || !currentCrop) {
            $("#fertilizerOutput").val("Please select both previous and current crops.");
            return;
        }
        
        $.ajax({
            url: "/Agriculture-Management-System/FertilizerServlet",
            method: "POST",
            data: { previousCrop, currentCrop, fertilizerType },
            success: function(response) {
                if (response.error) {
                    $("#fertilizerOutput").val("Error: " + response.error);
                    return;
                }

                let resultText = "";
                if (fertilizerType === "natural") {
                    resultText = `Organic Matter Recommendation:\n${response.organicMatter}`;
                } else {
                    resultText = `Fertilizer Recommendation (NPK):\n` +
                                `Nitrogen: ${parseFloat(response.nitrogen || 0).toFixed(2)} kg/ha\n` +
                                `Phosphorus: ${parseFloat(response.phosphorus || 0).toFixed(2)} kg/ha\n` +
                                `Potassium: ${parseFloat(response.potassium || 0).toFixed(2)} kg/ha`;
                    
                    // Add note if previous crop was a nitrogen fixer
                    if (parseFloat(response.nitrogenReduction || 0) > 0) {
                        resultText += `\n\nNote: Fertilizer amounts have been reduced due to nitrogen fixation from the previous ${previousCrop} crop.`;
                    }
                }

                $("#fertilizerOutput").val(resultText);
            },
            error: function(xhr, status, error) {
                $("#fertilizerOutput").val("Error fetching fertilizer recommendations. Please try again.");
                console.error("AJAX Error:", status, error);
            }
        });
    });
});