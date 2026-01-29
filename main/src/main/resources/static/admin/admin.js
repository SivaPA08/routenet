
async function loadRoutes() {
	const res = await fetch("/admin/display");
	if (!res.ok) return;

	const data = await res.json();
	const body = document.getElementById("routesBody");
	body.innerHTML = "";

	data.forEach(r => {
		body.innerHTML += `
        <tr>
            <td>${r.id}</td>
            <td>${r.urlPattern}</td>
            <td>${r.httpMethod}</td>
            <td>${r.targetUrl}</td>
            <td>${r.authRequired}</td>
            <td>${r.role ?? ""}</td>
            <td>
                <button class="delete-btn"
                        onclick="deleteRoute(${r.id})">❌</button>
            </td>
        </tr>`;
	});
}

async function saveRoute() {
	const payload = {
		id: document.getElementById("id").value || null,
		urlPattern: document.getElementById("urlPattern").value,
		httpMethod: document.getElementById("httpMethod").value,
		targetUrl: document.getElementById("targetUrl").value,
		authRequired: document.getElementById("authRequired").checked,
		role: document.getElementById("role").value
	};

	const res = await fetch("/admin/routes/save", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(payload)
	});

	const status = document.getElementById("status");

	if (res.ok) {
		status.innerText = "OK";
		loadRoutes();
	} else {
		status.innerText = "ERROR";
	}
}

async function deleteRoute(id) {
	if (!confirm("Delete this route?")) return;

	const res = await fetch(`/admin/routes/delete/${id}`, {
		method: "DELETE"
	});

	if (res.ok) {
		loadRoutes();
	} else {
		alert("Delete failed");
	}
}

window.onload = loadRoutes;
