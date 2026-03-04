 console.log("JS Loaded");

    let chart;

    function upload() {
        let file = document.getElementById("fileInput").files[0];
        uploadFile(file);
    }

    function uploadFile(file) {

        let formData = new FormData();
        formData.append("file", file);

        fetch("/api/logs/upload", {
            method: "POST",
            body: formData
        })
        .then(res => res.json())
        .then(data => {

          showResult(data);
    })

     .catch(err => {
    alert("File too large or processing error.");
});
}

    function showResult(data) {


       document.getElementById("summary").innerHTML = `
    <div class="card-container">
        <div class="card">⏱ Processing Time<br><b>${data.processingTimeMs} ms</b></div>
        <div class="card">📦 Total Requests<br><b>${data.totalRequests}</b></div>
        <div class="card">💾 Total Bytes<br><b>${data.totalBytes}</b></div>
    </div>
`;

        let statusLabels = Object.keys(data.statusCount).map(code => {
            if (code == 200) return "200 Success";
            if (code == 302) return "302 Redirect";
            if (code == 404) return "404 Not Found";
            if (code == 500) return "500 Server Error";
            return code;
        });

        let statusValues = Object.values(data.statusCount);

        if (chart) chart.destroy();

        chart = new Chart(document.getElementById("statusChart"), {
    type: "bar",
    data: {
        labels: statusLabels,
        datasets: [{
            label: "Status Codes",
            data: statusValues,
            backgroundColor: "#6366f1"
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {
                labels: {
                    color: "#000"
                }
            }
        },
        scales: {
            x: {
                ticks: { color: "#000" }
            },
            y: {
                ticks: { color: "#000" }
            }
        }
    }
});

        let topUrlsList = document.getElementById("topUrls");
        topUrlsList.innerHTML = "";

        for (let url in data.topUrls) {
            let li = document.createElement("li");
            li.textContent = url + " → " + data.topUrls[url];
            topUrlsList.appendChild(li);
        }
    }

    window.addEventListener("DOMContentLoaded", function () {

        let dropArea = document.getElementById("dropArea");

        dropArea.addEventListener("dragover", function (e) {
    e.preventDefault();
    dropArea.style.borderColor = "#22d3ee";
    dropArea.style.background = "rgba(34, 211, 238, 0.1)";
});

        dropArea.addEventListener("dragleave", function () {
            dropArea.style.background = "";
        });

           dropArea.addEventListener("drop", function (e) {
            e.preventDefault();
            dropArea.style.background = "";

            let file = e.dataTransfer.files[0];
            if (file) {
            document.getElementById("fileName").innerText =
                 "Uploaded File: " + file.name;
                uploadFile(file);
            }
        });

    });