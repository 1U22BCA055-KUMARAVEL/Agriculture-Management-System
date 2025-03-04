document.addEventListener("DOMContentLoaded", function () {
  const landformSelect = document.getElementById("landform");
  const climateSelect = document.getElementById("climate");
  const soilTypeSelect = document.getElementById("soiltype");
  const cropSelect = document.getElementById("majorCrop");
  const calculateButton = document.getElementById("calculate");
  const resultDiv = document.getElementById("result");

  function validateSelection() {
    const landformId = landformSelect.value;
    const climateId = climateSelect.value;
    const soilTypeId = soilTypeSelect.value;
    const cropId = cropSelect.value;

    if (!landformId || !climateId || !soilTypeId || !cropId) {
      alert("Error: Please select all fields correctly.");
      return null;
    }

    return { landformId, climateId, soilTypeId, cropId };
  }

  function fetchCropPeriods(selection) {
    const { landformId, climateId, soilTypeId, cropId } = selection;

    fetch(`/Agriculture-Management-System/CropManagementServlet?landformId=${landformId}&climateId=${climateId}&soilTypeId=${soilTypeId}&cropId=${cropId}`)
      .then(response => response.json())
      .then(data => {
        if (!data.valid) {
          resultDiv.innerHTML = `<p class='error'>Error: ${data.error}</p>`;
        } else {
          displayCropData(data);
        }
      })
      .catch(error => {
        resultDiv.innerHTML = `<p class='error'>Error fetching crop data: ${error.message}</p>`;
      });
  }

  function displayCropData(data) {
    resultDiv.innerHTML = `
      <p><strong>Total Period:</strong> ${data.totalPeriod} days</p>
      <p><strong>Growth Period:</strong> ${data.growthPeriod} days</p>
      <p><strong>Productivity Period:</strong> ${data.productivityPeriod} days</p>
    `;
  }

  calculateButton.addEventListener("click", function () {
    const validSelection = validateSelection();
    if (validSelection) {
      fetchCropPeriods(validSelection);
    } else {
      resultDiv.innerHTML = "";
    }
  });
});
