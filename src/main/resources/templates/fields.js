function addfields() {
    var p = d.createElement('p'),
        cloneField = field.cloneNode();
    cloneField.value = '';
    p.appendChild(cloneField);
    newFields.appendChild(p);
    return false;
}
var d = document,
    myForm = d.getElementById('myform'),
    newFields = myForm.querySelector('#new_fields'),
    field = myForm.querySelector('input[type=file]'),
    butAdd = d.getElementById('addfields');
butAdd.addEventListener('click', addfields, false);