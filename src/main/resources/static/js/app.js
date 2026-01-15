/**
 * Hovedscript til Beskyttelsesrum SPA.
 * Henter og viser kommuner og deres beskyttelsesrum.
 */
let map;
let markers = [];

// ============ INITIALISERING ============
document.addEventListener("DOMContentLoaded", function () {
    initTabs();
    loadKommuner();
    loadKommunerDropdown();
    loadAllRooms();
    initForm();
    initMap();
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

            //Refresh kort når tab åbnes
            if (tabId === 'kort' && map) {
                setTimeout(() => map.invalidateSize(), 100)
            }
        })
    })
}


// ============ KOMMUNER VISNING ============

function loadKommuner() {
    fetch("/kommuner")
        .then(response => response.json())
        .then(kommuner => {
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
        })
        .catch(error => console.error('Fejl ved hentning af beskyttelsesrum:', error));
}

function renderKommuneMedRooms(kommune, rooms) {
    const container = document.getElementById('kommunerMedRooms');
    const div = document.createElement('div');

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
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(() => {
        closeModal();
        loadAllRooms();
    })
    .catch(error => console.error('Fejl ved oprettelse:', error));
}

function updateRoom(id, data) {
    fetch(`/rooms/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(() => {
        closeModal();
        loadAllRooms();
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
            loadAllRooms();
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