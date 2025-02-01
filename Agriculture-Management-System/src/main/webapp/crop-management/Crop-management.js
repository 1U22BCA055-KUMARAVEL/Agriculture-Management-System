$(document).ready(function(){
  $("#calculate").click(function(){
    var selectedCrop = $("#majorCrop").val();
    if (!selectedCrop) {
      alert("Please select a crop.");
      return;
    }
    
    $.ajax({
      url: "Agriculture-Management-System/CropManagementServlet",
      type: "POST",
      data: { action: "getPeriods", crop: selectedCrop },
      dataType: "json",
      success: function(data) {
        if (data.error) {
          console.error("Error:", data.error);
          alert("Error: " + data.error);
        } else {
          $("#totalPeriod").val(data.totalPeriod);
          $("#growthPeriod").val(data.growthPeriod);
          $("#productivityPeriod").val(data.productivityPeriod);
        }
      },
      error: function(xhr, status, error) {
        console.error("AJAX Error:", status, error);
        alert("AJAX Error: " + error);
      }
    });
  });
});
