var modalWindow = document.getElementById('deleteModalWindow')
var rowId = 0;
modalWindow.addEventListener('show.bs.modal', function (event) {
  
  // Button that triggered the modal
  var button = event.relatedTarget

  // Extract info from data-bs-* attributes
  rowId = button.getAttribute('data-bs-deleteId')
})

deleteRecordConfirm = ()=>{
  document.getElementById(`r${rowId}`).remove();
  var evt = document.createEvent("MouseEvents");
  evt.initMouseEvent("click", true, true, window,0, 0, 0, 0, 0, false, false, false, false, 0, null);
  var cb = document.getElementById("modalCancelButton"); 
  cb.dispatchEvent(evt);
}