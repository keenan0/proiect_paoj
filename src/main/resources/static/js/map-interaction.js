document.addEventListener('mapReady', function (event) {
    console.log("Map is ready.");

    const mapId = document.body.dataset.mapId;
    const map = window['leaf' + mapId];

    const addEventBtn = document.querySelector('#addEventBtn');
    const submitEventBtn = document.querySelector('#submitEventBtn');
    const cancelEventBtn = document.querySelector('#cancelEventBtn');

    const selectedLatLngLabel = document.querySelector('#selectedCoords');

    let addingEvent = false;
    let selectedLatLng = null;
    let currentMarker = null;

    addEventBtn.addEventListener('click', () => {
        addingEvent = true;
        selectedLatLng = null;
        selectedLatLngLabel.innerText = 'Location: ❌';
        document.getElementById('eventForm').style.display = 'flex';
    });

    map.on('click', function (e) {
        if (addingEvent) {
            selectedLatLng = e.latlng;

            if(currentMarker) {
                map.removeLayer(currentMarker);
            }

            currentMarker = L.marker([selectedLatLng.lat, selectedLatLng.lng]).addTo(map);

            selectedLatLngLabel.innerText = `Location: (${selectedLatLng.lat.toFixed(4)},${selectedLatLng.lng.toFixed(4)})`;
        }
    });

    submitEventBtn.addEventListener('click', () => {
        if (!selectedLatLng) {
            selectedLatLngLabel.innerText = `You haven't selected anything yet.`;
            return;
        }

        const title = document.getElementById('eventTitle').value;
        const desc = document.getElementById('eventDesc').value;

        if (!title || !desc) {
            // Title or desc is not selected
            return;
        }

        const popupHtml = `
            <strong>${title}</strong><br>
            ${desc}<br>
        `;

        L.marker([selectedLatLng.lat, selectedLatLng.lng])
            .addTo(map)
            .bindPopup(popupHtml)
            .openPopup();

        fetch('/api/events', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                title,
                description: desc,
                marker: {
                    latitude: selectedLatLng.lat,
                    longitude: selectedLatLng.lng,
                    popUp: popupHtml
                }
            })
        }).then(resp => {
            if (resp.ok) {
                alert("Event added!");
            } else {
                alert("Failed to add event.");
            }

            // Reset the values
            document.getElementById('eventTitle').value = '';
            document.getElementById('eventDesc').value = '';
            document.getElementById('selectedCoords').innerText = 'Location: ❌';

            document.getElementById('eventForm').style.display = 'none';
            addingEvent = false;
        });
    });

    cancelEventBtn.addEventListener('click', () => {
        document.getElementById('eventForm').style.display = 'none';
        addingEvent = false;
    });
});
