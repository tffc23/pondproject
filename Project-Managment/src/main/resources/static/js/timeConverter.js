var timeDiff = 0;

setInterval(() => {

	const now = new Date();
	document.getElementById("time").innerHTML = now.toDateString() + " - " + now.toLocaleTimeString();

	now.setMinutes(now.getMinutes() + now.getTimezoneOffset() + parseInt(timeDiff));
	document.getElementById("select").innerHTML = now.toDateString() + " - " + now.toLocaleTimeString();


}, 1000);

function updateTimeZone() {

	const cities = document.getElementById("cities");
	const value = cities.options[cities.selectedIndex].value;
	const city = cities.options[cities.selectedIndex].text;

	timeDiff = value;

	document.getElementById("selected-time").style.display = "block";
	document.getElementById("location").innerHTML = city;
}

function hideTimeConverter() {

	const timeConverter = document.getElementById("timeConverter");

	timeConverter.style.display = timeConverter.style.display === "none" ? "block" : "none";

	const button = document.getElementById("view")
	button.innerHTML = button.innerHTML === "Hide" ? "Show" : "Hide";
}