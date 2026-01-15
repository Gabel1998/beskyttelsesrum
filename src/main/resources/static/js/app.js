/**
 * Hovedscript til Beskyttelsesrum SPA.
 * Henter og viser kommuner og deres beskyttelsesrum.
 */

document.addEventListener("DOMContentLoaded", function () {
    loadKommuner();
});

/**
 * Henter alle kommuner fra API'et.
 */
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

/**
 * Henter beskyttelsesrum for en kommune og opdaterer begge visninger.
 */
function loadRoomsForKommune(kommune) {
    fetch(`/kommuner/${kommune.id}/rooms`)
        .then(response => response.json())
        .then(rooms => {
            renderKommuneMedRooms(kommune, rooms);
            renderKapacitetRow(kommune, rooms);
        })
        .catch(error => console.error('Fejl ved hentning af beskyttelsesrum:', error));
}

/**
 * Viser kommune med tilhørende beskyttelsesrum.
 */
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

/**
 * Tilføjer række til kapacitetstabellen.
 */
function renderKapacitetRow(kommune, rooms) {
    const tbody = document.querySelector('#kapacitetTable tbody');
    const totalKapacitet = rooms.reduce((sum, r) => sum + r.kapacitet, 0);

    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${kommune.kode}</td>
        <td>${kommune.navn}</td>
        <td>${rooms.length}</td>
        <td>${totalKapacitet}</td>
    `;
    tbody.appendChild(row);
}