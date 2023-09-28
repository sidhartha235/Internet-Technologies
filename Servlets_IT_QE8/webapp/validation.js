const date = new Date();
const year = date.getFullYear();
const month = (date.getMonth() + 1).toString().padStart(2, '0');
const day = date.getDate().toString().padStart(2, '0');
const dateString = year + "-" + month + "-" + day;
	
document.getElementById("dateOfBirth").setAttribute("max", dateString);
document.getElementById("joiningDate").setAttribute("max", dateString);

change = () => {
	document.getElementById("departmentId1Error").innerHTML = "";
	document.getElementById("departmentNameError").innerHTML = "";
	document.getElementById("departmentLocationError").innerHTML = "";
	
	document.getElementById("employeeIdError").innerHTML = "";
	document.getElementById("employeeNameError").innerHTML = "";
	document.getElementById("jobTitleError").innerHTML = "";
    document.getElementById("dateOfBirthError").innerHTML = "";
    document.getElementById("joiningDateError").innerHTML = "";
    document.getElementById("salaryError").innerHTML = "";
    document.getElementById("departmentId2Error").innerHTML = "";
    
    document.forms["populateForm"]["departmentId1"].value = "";
    document.forms["populateForm"]["departmentName"].value = "";
    document.forms["populateForm"]["departmentLocation"].value = "";
    
    document.forms["populateForm"]["employeeId"].value = "";
    document.forms["populateForm"]["employeeName"].value = "";
    document.forms["populateForm"]["jobTitle"].value = "";
    document.forms["populateForm"]["dateOfBirth"].value = "";
    document.forms["populateForm"]["joiningDate"].value = "";
    document.forms["populateForm"]["salary"].value = "";
    document.forms["populateForm"]["departmentId2"].value = "";
	
	let dropdownList = document.getElementById("whichTable");
	let selectedTable = dropdownList.options[dropdownList.selectedIndex].value;
	
	console.log("Selected Table: " + selectedTable);
	
	if(selectedTable == "Department"){
		document.getElementById("input1").style.display = "block";
		document.getElementById("input2").style.display = "none";
	}
	else if(selectedTable == "Employee"){
		document.getElementById("input1").style.display = "none";
		document.getElementById("input2").style.display = "block";
	}
}

validateDepartment = () => {
	let valid = true;
	let departmentId = test_input(document.forms["populateForm"]["departmentId1"].value);
	let departmentName = test_input(document.forms["populateForm"]["departmentName"].value);
	let departmentLocation = test_input(document.forms["populateForm"]["departmentLocation"].value);
	
	document.getElementById("departmentId1Error").innerHTML = "";
	document.getElementById("departmentNameError").innerHTML = "";
	document.getElementById("departmentLocationError").innerHTML = "";
	
	if(departmentId == "" || departmentName == "" || departmentLocation == "" || (!/^[1-9][0-9][0-9]$/.test(departmentId)) || (!/^[a-zA-Z ]*$/.test(departmentName)) || (!/^[a-zA-Z ]*$/.test(departmentLocation))) {
		if(departmentId == "") {
			document.getElementById("departmentId1Error").innerHTML = "Please fill this field!";
		}
		else if(!/^[1-9][0-9][0-9]$/.test(departmentId)) {
			document.getElementById("departmentId1Error").innerHTML = "Department ID is a 3-digit integer!";
		}
		
		if(departmentName == "") {
			document.getElementById("departmentNameError").innerHTML = "Please fill this field!";
		}
		else if(!/^[a-zA-Z ]*$/.test(departmentName)) {
			document.getElementById("departmentNameError").innerHTML = "Only alphabets are allowed!";
		}
		
		if(departmentLocation == "") {
			document.getElementById("departmentLocationError").innerHTML = "Please fill this field!";
		}
		else if(!/^[a-zA-Z ]*$/.test(departmentLocation)) {
			document.getElementById("departmentLocationError").innerHTML = "Only alphabets are allowed!";
		}
		
		valid = false;
	}
	submitForm(valid);
}

validateEmployee = () => {
	let valid = true;
	let employeeId = test_input(document.forms["populateForm"]["employeeId"].value);
	let employeeName = test_input(document.forms["populateForm"]["employeeName"].value);
	let jobTitle = test_input(document.forms["populateForm"]["jobTitle"].value);
	let dateOfBirth = test_input(document.forms["populateForm"]["dateOfBirth"].value);
	let joiningDate = test_input(document.forms["populateForm"]["joiningDate"].value);
	let salary = test_input(document.forms["populateForm"]["salary"].value);
	let departmentId = test_input(document.forms["populateForm"]["departmentId2"].value);
	
	document.getElementById("employeeIdError").innerHTML = "";
	document.getElementById("employeeNameError").innerHTML = "";
	document.getElementById("jobTitleError").innerHTML = "";
    document.getElementById("dateOfBirthError").innerHTML = "";
    document.getElementById("joiningDateError").innerHTML = "";
    document.getElementById("salaryError").innerHTML = "";
    document.getElementById("departmentId2Error").innerHTML = "";

    if( employeeId == "" || employeeName == "" || jobTitle == "" || dateOfBirth == "" || joiningDate == "" || salary == "" || departmentId == "" ||
        (!/^[1-9][0-9][0-9][0-9][0-9]$/.test(employeeId)) || (!/^[a-zA-Z ]*$/.test(employeeName)) || (!/^[a-zA-Z ]*$/.test(jobTitle)) || (!/^[1-2][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]$/.test(dateOfBirth)) || (!/^[1-2][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]$/.test(joiningDate)) || (!/^[0-9]+.?[0-9]*$/.test(salary)) || (!/^[1-9][0-9][0-9]$/.test(departmentId)) ) {
        if(employeeId == "") {
            document.getElementById("employeeIdError").innerHTML = "Please fill this field!";
        }
        else if(!/^[1-9][0-9][0-9][0-9][0-9]$/.test(employeeId)) {
            document.getElementById("employeeIdError").innerHTML = "Employee ID is a 5-digit integer!";
        }

        if(employeeName == "") {
            document.getElementById("employeeNameError").innerHTML = "Please fill this field!";
        }
        else if(!/^[a-zA-Z ]*$/.test(employeeName)) {
            document.getElementById("employeeNameError").innerHTML = "Only alphabets are allowed!";
        }

        if(jobTitle == "") {
            document.getElementById("jobTitleError").innerHTML = "Please fill this field!";
        }
        else if(!/^[a-zA-Z ]*$/.test(jobTitle)){
			document.getElementById("jobTitleError").innerHTML = "Only alphabets are allowed!";
		}

        if(dateOfBirth == "") {
            document.getElementById("dateOfBirthError").innerHTML = "Please fill this field!";
        }
        else if(!/^[1-2][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]$/.test(dateOfBirth)){
			document.getElementById("dateOfBirthError").innerHTML = "Enter a valid date!";
		}

        if(joiningDate == "") {
            document.getElementById("joiningDateError").innerHTML = "Please fill this field!";
        }
        else if(!/^[1-2][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]$/.test(joiningDate)){
			document.getElementById("joiningDateError").innerHTML = "Enter a valid date!";
		}

        if(salary == "") {
            document.getElementById("salaryError").innerHTML = "Please fill this field!";
        }
        else if(!/^[0-9]+.?[0-9]*$/.test(salary)){
			document.getElementById("salaryError").innerHTML = "Only a Real number is allowed!";
		}

        if(departmentId == "") {
            document.getElementById("departmentId2Error").innerHTML = "Please fill this field!";
        }
        else if(!/^[1-9][0-9][0-9]$/.test(departmentId)){
			document.getElementById("departmentId2Error").innerHTML = "Department ID is a 3-digit number!";
		}
		
		valid = false;
	}
	submitForm(valid);
}

test_input = (value) => {
	value = value.trim();
	return value;
}

submitForm = (isValid) => {
	// console.log(isValid);
	let form = document.getElementById("populateForm");
	form.addEventListener("submit", function(event){
		event.preventDefault();
		if(isValid){
			form.submit();
		}
	});
}
