function addCandidate() {
    let schoolId = document.getElementById("schoolId").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let grade = document.getElementById("grade").value;

    let candidate = {
        schoolId: schoolId,
        firstName: firstName,
        lastName: lastName,
        grade: grade
    };

    fetch('/api/candidates', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(candidate),
    })
        .then(getCandidates)
        .catch(error => console.error('Error:', error));

    document.getElementById("schoolId").value = "";
    document.getElementById("firstName").value = "";
    document.getElementById("lastName").value = "";
    document.getElementById("grade").value = "";
}

function getCandidates() {
    fetch('/api/candidates')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok.');
            }
            return response.json();
        })
        .then(data => {
            let candidatesList = document.getElementById("candidatesList");
            candidatesList.innerHTML = "";

            data.forEach(candidate => {
                let li = document.createElement("li");
                li.appendChild(document.createTextNode("SchoolId: " + candidate.schoolId + " - Name: " + candidate.firstName + " " + candidate.lastName + " -  Klasse: " + candidate.grade));
                candidatesList.appendChild(li);
            });
        })
        .catch(error => console.error('Error:', error));
}

getCandidates();