addEmployeeClick = () => {
	var employeeName = document.getElementById('employeesDataList').value;
	var employeeRole = document.getElementById('rolesDataList').value;
	var record = `	<div class="row justify-content-between mb-2">
						<label for="employeeName" class="col-2 col-form-label">Employee:</label>
						<div class="col-5">
							<input type="text" readonly class="form-control-plaintext" id="employeeName" value="${employeeName}">
						</div>
						
						<label for="rolesDataList" class="col-1 col-form-label">Role:</label>
						<div class="col-4">
							<input type="text" readonly class="form-control-plaintext" id="employeeRole" value="${employeeRole}">
						</div>
					</div>`;
	var addedEmployees = document.getElementById('addedEmployees');
	addedEmployees.innerHTML += record;
	//document.getElementById('addedEmployees').innerHTML = addedEmployees + record;
}