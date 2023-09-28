const date = new Date();
const year = date.getFullYear();
const month = (date.getMonth() + 1).toString().padStart(2, '0');
const day = date.getDate().toString().padStart(2, '0');
const dateString = year + "-" + month + "-" + day;
	
document.getElementById("joiningDate").setAttribute("max", dateString);

const validate = () => {
//	let valid = true;
	let joiningDate = test_input(document.forms["queryForm"]["joiningDate"].value);
	let age = test_input(document.forms["queryForm"]["age"].value);
	
	document.getElementById("joiningDateError").innerHTML = "";
	document.getElementById("ageError").innerHTML = "";
	
	if( joiningDate == "" || age == "" || (!/^[1-2][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]$/.test(joiningDate)) || (!/^[0-9]+$/.test(age)) ) {
		if(joiningDate == "") {
			document.getElementById("joiningDateError").innerHTML = "Please fill this field!";
		}
		else if(!/^[1-2][0-9][0-9][0-9]-[0-9][0-9]-[0-3][0-9]$/.test(joiningDate)) {
			document.getElementById("joiningDateError").innerHTML = "Enter a valid date!";
		}
		
		if(age == ""){
			document.getElementById("ageError").innerHTML = "Please fill this field!";
		}
		else if(!/^[0-9]+$/.test(age)) {
			document.getElementById("ageError").innerHTML = "Enter a valid age!";
		}
		
		return false;
	}
	
	document.forms["queryForm"]["joiningDate"].value = joiningDate;
	document.forms["queryForm"]["age"].value = age;
	
	return true;
	
//	return submitForm(valid);
}

const test_input = (value) => {
	value = value.trim();
	return value;
}

//const submitForm = (isValid) => {
//	console.log(isValid);
//	let form = document.getElementById("queryForm");
//	form.addEventListener("submit", function(event){
//		event.preventDefault();
//		if(isValid) {
//			form.submit();
//		}
//	});
//}
