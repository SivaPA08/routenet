async function loadRoutes() {
	const res = await fetch("/admin/display");
	if (!res.ok) return;
	const data = await res.json();
	const body = document.getElementById("routesBody");
	body.innerHTML = "";

	if (data.length === 0) {
		body.innerHTML = `
			<tr>
				<td colspan="7" style="text-align: center; padding: 30px; color: #666666;">
					No routes configured. Add your first route above.
				</td>
			</tr>`;
		return;
	}

	data.forEach(r => {
		const authBadge = r.authRequired
			? '<span style="color: #6db33f; font-weight: 600;">YES</span>'
			: '<span style="color: #666666;">NO</span>';

		body.innerHTML += `
        <tr>
            <td><strong>${r.id}</strong></td>
            <td><code style="background: #0a0a0a; padding: 4px 8px; border: 1px solid #333333; color: #6db33f;">${r.urlPattern}</code></td>
            <td><span style="background: #6db33f; color: #000000; padding: 4px 10px; font-size: 12px; font-weight: 600;">${r.httpMethod}</span></td>
            <td style="max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="${r.targetUrl}">${r.targetUrl}</td>
            <td>${authBadge}</td>
            <td>${r.role ? `<span style="background: #333333; color: #6db33f; padding: 4px 8px; border: 1px solid #6db33f; font-size: 12px; font-weight: 600;">${r.role}</span>` : '<span style="color: #666666;">—</span>'}</td>
            <td>
                <button class="delete-btn"
                        onclick="deleteRoute(${r.id})"
                        title="Delete this route">DELETE</button>
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

	// Validation
	if (!payload.urlPattern || !payload.targetUrl) {
		const status = document.getElementById("status");
		status.innerHTML = '<span style="color: #ff4444; background: #1a1a1a; padding: 12px; border: 2px solid #ff4444; display: block;">ERROR: URL Pattern and Target URL are required!</span>';
		return;
	}

	const res = await fetch("/admin/routes/save", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(payload)
	});

	const status = document.getElementById("status");
	if (res.ok) {
		status.innerHTML = '<span style="color: #6db33f; background: #1a1a1a; padding: 12px; border: 2px solid #6db33f; display: block;">SUCCESS: Route saved successfully</span>';

		// Clear form
		document.getElementById("id").value = '';
		document.getElementById("urlPattern").value = '';
		document.getElementById("targetUrl").value = '';
		document.getElementById("authRequired").checked = false;
		document.getElementById("role").value = '';

		loadRoutes();

		// Clear status after 3 seconds
		setTimeout(() => {
			status.innerHTML = '';
		}, 3000);
	} else {
		status.innerHTML = '<span style="color: #ff4444; background: #1a1a1a; padding: 12px; border: 2px solid #ff4444; display: block;">ERROR: Failed to save route</span>';
	}
}

async function deleteRoute(id) {
	if (!confirm("Are you sure you want to delete this route?")) return;

	// Fixed syntax error: was using backticks incorrectly
	const res = await fetch(`/admin/routes/delete/${id}`, {
		method: "DELETE"
	});

	if (res.ok) {
		loadRoutes();

		// Show success message briefly
		const status = document.getElementById("status");
		status.innerHTML = '<span style="color: #6db33f; background: #1a1a1a; padding: 12px; border: 2px solid #6db33f; display: block;">SUCCESS: Route deleted successfully</span>';
		setTimeout(() => {
			status.innerHTML = '';
		}, 3000);
	} else {
		alert("ERROR: Delete failed");
	}
}

window.onload = loadRoutes;
