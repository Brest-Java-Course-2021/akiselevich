addEmployeeClick = () => {
	var employeeName = document.getElementById('employeesDataList').value;
	var record = `	<div class="row justify-content-between mb-2">
						<label for="employeeName" class="col-2 col-form-label">Employee:</label>
						<div class="col-5">
							<input type="text" readonly class="form-control-plaintext" id="employeeName" value="${employeeName}">
						</div>						
					</div>`;
	var addedEmployees = document.getElementById('addedEmployees');
	addedEmployees.innerHTML += record;
}