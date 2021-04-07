addClick = () => {
   var table = document.getElementById('employeesTable');
   var tbody = table.lastElementChild;
   var lastElementId;
   if(tbody.lastElementChild == null){
        lastElementId = 0;
   } else {
        lastElementId = tbody.lastElementChild.getAttribute('id');
   }
   var newElementId = parseInt(lastElementId) + 1;

   var newElementDeleteButton = getDeleteButton(newElementId);
   var newRecord = getNewRecord(newElementId, newElementDeleteButton);

   tbody.innerHTML += newRecord;
}

deleteClick = () => {
    var target = event.target;
    var targetButtonId;
    if(target.tagName == 'I'){
        targetButtonId = target.parentElement.getAttribute('id');
    } else if(target.tagName == 'A'){
        targetButtonId = target.getAttribute('id');
    }
    var targetElementId = targetButtonId.split("#")[1];
    document.getElementById(targetElementId).remove();
}

getDeleteButton = (id) => {
    return `<a id="deleteEmployee#${id}"
   onclick="deleteClick()"
   role="button"
   class="btn btn-outline-danger"
   data-bs-toggle="modal"
   data-bs-target="#deleteModalWindow"
   data-bs-deleteId="${id}">
   <i class="fas fa-trash"></i>
</a>`;
}

getNewRecord = (newRecordId, deleteButton) => {
    return `<tr id="${newRecordId}">
                <td scope="row" class="col">
                    <input class="form-control" list="employeeOptions" placeholder="Ivan Ivanov">
                </td>
                <td scope="row" class="col-1 text-end">
                    ${deleteButton}
                </td>
            </tr>`;
}