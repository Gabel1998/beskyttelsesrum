/**
 * Hovedscript til Beskyttelsesrum SPA.
 * Henter og viser kommuner og deres beskyttelsesrum.
 */
//Leaflet bruger
let map;
let markers = [];

//Statistikker bruger
let statsData = {
    kommunerMedRum: new Set(),
    totalRooms: 0,
    totalKapacitet: 0
};

//Søgefunktion bruger
let alleKommuner = [];


// ============ INITIALISERING ============
document.addEventListener("DOMContentLoaded", function () {
    initTabs();
    loadKommuner();
    loadKommunerDropdown();
    loadAllRooms();
    initForm();
    initMap();

    // Søgning med Enter-tast
    document.getElementById('searchInput').addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            searchKommune();
        }
    });
});


// ============ TAB NAVIGATION ============

function initTabs() {
    const tabsButtons = document.querySelectorAll('.tab-btn');
    tabsButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            //fjern active fra alle tabs
            tabsButtons.forEach(btn => btn.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

            //Tilføj active til den klikkede tab
            btn.classList.add('active');
            const tabId = btn.getAttribute('data-tab');
            document.getElementById(tabId).classList.add('active');

            //Refresh data baseret på tab
            switch (tabId) {
                case 'kommuner':
                    document.getElementById('kommunerMedRooms').innerHTML = '';
                    document.querySelector('#kapacitetTable tbody').innerHTML = '';
                    resetStats()
                    loadKommuner();
                    break;
                case 'kapacitet':
                    document.getElementById('#kapacitetTable tbody').innerHTML = '';
                    document.querySelector('#kapacitetTable tbody').innerHTML = '';
                    resetStats()
                    loadKommuner();
                    break;
                case 'kort':
                    setTimeout(() => map.invalidateSize(), 100);
                    loadAllRooms()
                    break;
                case 'admin':
                    loadAllRooms();
                    break;
                case 'vedligehold':
                    loadVedligeholdelser();
                    loadRoomsDropdown();
                    break;
            }
        })
    })
}


// ============ KOMMUNER VISNING ============

function loadKommuner() {
    fetch("/kommuner")
        .then(response => response.json())
        .then(kommuner => {
            alleKommuner = kommuner;
            kommuner.forEach(kommune => {
                loadRoomsForKommune(kommune);
            });
        })
        .catch(error => console.error('Fejl ved hentning af kommuner:', error));

}

function loadRoomsForKommune(kommune) {
    fetch(`/kommuner/${kommune.id}/rooms`)
        .then(response => response.json())
        .then(rooms => {
            renderKommuneMedRooms(kommune, rooms);
            renderKapacitetRow(kommune, rooms);
            updateStats(kommune.id, rooms);
        })
        .catch(error => console.error('Fejl ved hentning af beskyttelsesrum:', error));
}

function renderKommuneMedRooms(kommune, rooms) {
    const container = document.getElementById('kommunerMedRooms');
    const div = document.createElement('div');
    div.className = 'kommune-card';

    const roomsHtml = rooms.length > 0
        ? rooms.map(r => `${r.adresse}, ${r.postalCode} - Kapacitet: ${r.kapacitet}`).join('<br>')
        : 'Ingen beskyttelsesrum registreret';

    div.innerHTML = `
        <div class="kommune-header">${kommune.kode} - ${kommune.navn}</div>
        <div class="rooms-list">${roomsHtml}</div>
    `;
    container.appendChild(div);
}

function renderKapacitetRow(kommune, rooms) {
    const tbody = document.querySelector('#kapacitetTable tbody');
    const totalKapacitet = rooms.reduce((sum, r) => sum + r.kapacitet, 0);

    const row = document.createElement(`tr`)
    row.innerHTML = `
    <td>${kommune.kode}</td>
    <td>${kommune.navn}</td>
    <td>${rooms.length}</td>
    <td>${totalKapacitet}</td>
    `;
    tbody.appendChild(row);
}

// ============ ADMIN - CRUD ============

function loadKommunerDropdown() {
    fetch('/kommuner')
        .then(response => response.json())
        .then(kommuner => {
            const select = document.getElementById('kommuneId');
            kommuner.forEach(k => {
                const option = document.createElement('option');
                option.value = k.id;
                option.textContent = `${k.kode} - ${k.navn}`;
                select.appendChild(option);
            });
        });
}

function loadAllRooms() {
    fetch('/rooms')
        .then(response => response.json())
        .then(rooms => {
            renderRoomsTable(rooms);
            updateMapMarkers(rooms);
        })
        .catch(error => console.error('Fejl ved hentning af beskyttelsesrum:', error));

}

function renderRoomsTable(rooms) {
    const tbody = document.querySelector('#roomsTable tbody');
    tbody.innerHTML = ``;

    rooms.forEach(room => {
        const row = document.createElement('tr');
        row.innerHTML = `
                <td>${room.adresse}</td>
                <td>${room.postalCode}</td>
                <td>${room.kapacitet}</td>
                <td>${room.kommuneNavn}</td>
                <td>
                    <button class="btn-edit" onclick="editRoom(${room.id})">Rediger</button>
                    <button class="btn-delete" onclick="deleteRoom(${room.id})">Slet</button>
                </td>
            `;
        tbody.appendChild(row);
    });
}

function initForm() {
    const form = document.getElementById('roomForm');
    form.addEventListener(`submit`, handleSubmit);
}

function handleSubmit(e) {
    e.preventDefault();

    const roomId = document.getElementById(`roomId`).value;
    const data = {
        adresse: document.getElementById(`adresse`).value,
        postalCode: document.getElementById(`postalCode`).value,
        kapacitet: parseInt(document.getElementById(`kapacitetInput`).value),
        latitude: parseFloat(document.getElementById(`latitude`).value),
        longitude: parseFloat(document.getElementById(`longitude`).value),
        kommuneId: document.getElementById(`kommuneId`).value
    };

    if (roomId) {
        updateRoom(roomId, data);
    } else {
        createRoom(data);
    }
}

function createRoom(data) {
    fetch('/rooms', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(() => {
            closeModal();
            refreshAllData();
        })
        .catch(error => console.error('Fejl ved oprettelse:', error));
}

function updateRoom(id, data) {
    fetch(`/rooms/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(() => {
            closeModal();
            refreshAllData();
        })
        .catch(error => console.error('Fejl ved opdatering:', error));
}


function editRoom(id) {
    openModal(id);
}

function deleteRoom(id) {
    if (!confirm('Er du sikker på du vil slette dette beskyttelsesrum?')) {
        return;
    }
    fetch(`/rooms/${id}`, {method: 'DELETE'})
        .then(() => {
            refreshAllData();
            alert(`Beskyttelsesrum med id ${id} slettet`);
        })
        .catch(error => console.error('Fejl ved sletning af beskyttelsesrum:', error));
}

function clearForm() {
    document.getElementById(`roomForm`).reset();
    document.getElementById(`roomId`).value = ``;
}


// ============ LEAFLET KORT ============

function initMap() {
    map = L.map(`map`).setView([56.0, 10.5], 7);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);
}

function updateMapMarkers(rooms) {
    //Fjern eksisterende markers
    markers.forEach(marker => map.removeLayer(marker));
    markers = [];

    //Tilføj nye markers
    rooms.forEach(room => {
        if (room.latitude && room.longitude) {
            const marker = L.marker([room.latitude, room.longitude])
                .addTo(map)
                .bindPopup(`
                    <strong>${room.adresse}</strong><br></br>
                    ${room.postalCode}<br>
                    Kapacitet: ${room.kapacitet}<br>
                    Kommune: ${room.kommuneNavn}
                `);
            markers.push(marker);
        }
    });
}

// ============ MODAL ============

function openModal(id = null) {
    const modal = document.getElementById('modal');
    const title = document.getElementById('modalTitle');

    if (id) {
        title.textContent = 'Rediger beskyttelsesrum';
        loadRoomIntoForm(id);
    } else {
        title.textContent = 'Opret beskyttelsesrum';
        clearForm();
    }

    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    clearForm();
}

function loadRoomIntoForm(id) {
    fetch(`/rooms/${id}`)
        .then(response => response.json())
        .then(room => {
            document.getElementById('roomId').value = room.id;
            document.getElementById('adresse').value = room.adresse;
            document.getElementById('postalCode').value = room.postalCode;
            document.getElementById('kapacitetInput').value = room.kapacitet;
            document.getElementById('latitude').value = room.latitude || '';
            document.getElementById('longitude').value = room.longitude || '';
            document.getElementById('kommuneId').value = room.kommuneId;
        });
}

// ============ REFRESH DATA ============

function refreshAllData() {
    // Ryd eksisterende data
    document.getElementById('kommunerMedRooms').innerHTML = '';
    document.querySelector('#kapacitetTable tbody').innerHTML = '';
    resetStats();

    // Genindlæs alt
    loadKommuner();
    loadAllRooms();
}

// ============ STATISTIK ============

function resetStats() {
    statsData = {
        kommunerMedRum: new Set(),
        totalRooms: 0,
        totalKapacitet: 0
    };
}

function updateStats(kommuneId, rooms) {
    if (rooms.length > 0) {
        statsData.kommunerMedRum.add(kommuneId);
    }
    statsData.totalRooms += rooms.length;
    statsData.totalKapacitet += rooms.reduce((sum, r) => sum + r.kapacitet, 0);

    renderStats();
}

function renderStats() {
    document.getElementById('statKommuner').textContent = statsData.kommunerMedRum.size;
    document.getElementById('statRooms').textContent = statsData.totalRooms;
    document.getElementById('statKapacitet').textContent = statsData.totalKapacitet.toLocaleString('da-DK');
}

// ============ SØGEFUNKTION ============

function searchKommune() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase().trim();

    if (!searchTerm) {
        alert('Indtast en kommune at søge efter');
        return;
    }

    // Find matchende kommuner
    const matches = alleKommuner.filter(k =>
        k.navn.toLowerCase().includes(searchTerm) ||
        k.kode.includes(searchTerm)
    );

    if (matches.length === 0) {
        alert('Ingen kommuner fundet');
        return;
    }

    // Ryd og vis kun matchende kommuner
    document.getElementById('kommunerMedRooms').innerHTML = '';
    document.querySelector('#kapacitetTable tbody').innerHTML = '';
    resetStats();

    matches.forEach(kommune => {
        loadRoomsForKommune(kommune);
    });

    // Zoom til første match på kortet
    zoomToKommune(matches[0]);
}

function zoomToKommune(kommune) {
    fetch(`/kommuner/${kommune.id}/rooms`)
        .then(response => response.json())
        .then(rooms => {
            const roomsWithCoords = rooms.filter(r => r.latitude && r.longitude);

            if (roomsWithCoords.length > 0) {
                // Zoom til første beskyttelsesrum i kommunen
                const firstRoom = roomsWithCoords[0];
                map.setView([firstRoom.latitude, firstRoom.longitude], 13);

                // Åbn popup
                markers.forEach(marker => {
                    const latLng = marker.getLatLng();
                    if (latLng.lat === firstRoom.latitude && latLng.lng === firstRoom.longitude) {
                        marker.openPopup();
                    }
                });

                // Skift til kort-tab
                document.querySelector('[data-tab="kort"]').click();
            } else {
                alert(`Ingen beskyttelsesrum med koordinater i ${kommune.navn}`);
            }
        });
}

function resetSearch() {
    document.getElementById('searchInput').value = '';
    refreshAllData();
    map.setView([56.0, 10.5], 7);
}


// ============ VEDLIGEHOLDELSE ============

function loadVedligeholdelser() {
    fetch('/vedligehold')
        .then(response => response.json())
        .then(data => renderVedligeholdTable(data))
        .catch(error => console.error('Fejl ved hentning af vedligeholdelser:', error));
}

function renderVedligeholdTable(vedligeholdelser) {
    const tbody = document.querySelector('#vedligeholdTable tbody');
    tbody.innerHTML = '';

    vedligeholdelser.forEach(v => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${v.dato || ''}</td>
            <td>${v.beskyttelsesrumAdresse || ''}</td>
            <td>${v.beskrivelse || ''}</td>
            <td>${v.udfoertAf || ''}</td>
            <td><span class="status-badge status-${v.status}">${formatStatus(v.status)}</span></td>
            <td>
                <button class="btn-edit" onclick="editVedligehold(${v.id})">Rediger</button>
                <button class="btn-delete" onclick="deleteVedligehold(${v.id})">Slet</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function formatStatus(status) {
    const statusMap = {
        'PLANLAGT': 'Planlagt',
        'IGANGVAERENDE': 'Igangværende',
        'UDFOERT': 'Udført',
        'KLARGJORT': 'Klargjort'
    };
    return statusMap[status] || status;
}

function loadRoomsDropdown() {
    fetch('/rooms')
        .then(response => response.json())
        .then(rooms => {
            const select = document.getElementById('vedligeholdBeskyttelsesrumId');
            select.innerHTML = '<option value="">Vælg beskyttelsesrum...</option>';
            rooms.forEach(r => {
                const option = document.createElement('option');
                option.value = r.id;
                option.textContent = `${r.adresse}, ${r.postalCode}`;
                select.appendChild(option);
            });
        });
}

function openVedligeholdModal(id = null) {
    const modal = document.getElementById('vedligeholdModal');
    const title = document.getElementById('vedligeholdModalTitle');

    loadRoomsDropdown();

    if (id) {
        title.textContent = 'Rediger vedligeholdelse';
        loadVedligeholdIntoForm(id);
    } else {
        title.textContent = 'Opret vedligeholdelse';
        clearVedligeholdForm();
        document.getElementById('vedligeholdDato').value = new Date().toISOString().split('T')[0];
    }

    modal.classList.add('active');
}

function closeVedligeholdModal() {
    document.getElementById('vedligeholdModal').classList.remove('active');
    clearVedligeholdForm();
}

function clearVedligeholdForm() {
    document.getElementById('vedligeholdForm').reset();
    document.getElementById('vedligeholdId').value = '';
}

function loadVedligeholdIntoForm(id) {
    fetch(`/vedligehold/${id}`)
        .then(response => response.json())
        .then(v => {
            document.getElementById('vedligeholdId').value = v.id;
            document.getElementById('vedligeholdBeskyttelsesrumId').value = v.beskyttelsesrumId;
            document.getElementById('vedligeholdBeskrivelse').value = v.beskrivelse || '';
            document.getElementById('vedligeholdDato').value = v.dato || '';
            document.getElementById('vedligeholdStatus').value = v.status || 'PLANLAGT';
            document.getElementById('vedligeholdUdfoertAf').value = v.udfoertAf || '';
        });
}

function editVedligehold(id) {
    openVedligeholdModal(id);
}

function deleteVedligehold(id) {
    if (!confirm('Er du sikker på du vil slette denne vedligeholdelse?')) {
        return;
    }
    fetch(`/vedligehold/${id}`, {method: 'DELETE'})
        .then(() => {
            loadVedligeholdelser();
            alert('Vedligeholdelse slettet');
        })
        .catch(error => console.error('Fejl ved sletning:', error));
}

// Init vedligehold form
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('vedligeholdForm');
    if (form) {
        form.addEventListener('submit', handleVedligeholdSubmit);
    }
});

function handleVedligeholdSubmit(e) {
    e.preventDefault();

    const id = document.getElementById('vedligeholdId').value;
    const data = {
        beskyttelsesrumId: document.getElementById('vedligeholdBeskyttelsesrumId').value,
        beskrivelse: document.getElementById('vedligeholdBeskrivelse').value,
        dato: document.getElementById('vedligeholdDato').value,
        status: document.getElementById('vedligeholdStatus').value,
        udfoertAf: document.getElementById('vedligeholdUdfoertAf').value
    };

    if (id) {
        fetch(`/vedligehold/${id}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        })
            .then(() => {
                closeVedligeholdModal();
                loadVedligeholdelser();
            })
            .catch(error => console.error('Fejl ved opdatering:', error));
    } else {
        fetch('/vedligehold', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        })
            .then(() => {
                closeVedligeholdModal();
                loadVedligeholdelser();
            })
            .catch(error => console.error('Fejl ved oprettelse:', error));
    }
}