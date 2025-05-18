document.addEventListener('mapReady', function (event) {
    console.log("Map is ready.");

    const mapId = document.body.dataset.mapId;
    const map = window['leaf' + mapId];

    const body = document.getElementsByTagName('body')[0];
    const mapControls = document.querySelector('.mapControls');

    const addEventBtn = document.querySelector('#addEventBtn');
    const openMenuBtn = document.querySelector('#openMenu');
    const closeMenuBtn = document.querySelector('#closeMenu');
    const submitEventBtn = document.querySelector('#submitEventBtn');
    const cancelEventBtn = document.querySelector('#cancelEventBtn');

    const sideMenu = document.querySelector('#sideMenu');
    const eventForm = document.querySelector('#eventForm');

    const selectedLatLngLabel = document.querySelector('#selectedCoords');

    let addingEvent = false;
    let sideMenuOpen = false;
    let selectedLatLng = null;
    let currentMarker = null;

    openMenuBtn.addEventListener('click', function (e) {
        sideMenu.classList.toggle('active');
        sideMenuOpen = !sideMenuOpen;
        mapControls.classList.toggle('shifted');
        body.classList.toggle('menu-open');
    })

    closeMenuBtn.addEventListener('click', function (e) {
        sideMenu.classList.toggle('active');
        sideMenuOpen = !sideMenuOpen;
        mapControls.classList.toggle('shifted');
        body.classList.toggle('menu-open');
    })

    addEventBtn.addEventListener('click', () => {
        eventForm.classList.toggle('active');
        body.classList.toggle('form-open');

        if (addingEvent) {
            addingEvent = false;

            if(currentMarker) {
                map.removeLayer(currentMarker);
            }

            selectedLatLng = null;
        } else {
            addingEvent = true;
            selectedLatLng = null;
            selectedLatLngLabel.innerText = 'Location: ❌';
        }
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
            selectedLatLngLabel.innerText = `Title or description is empty.`;
            return;
        }

        const accessCode = document.getElementById('accessCodeType').value;

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
                event: {
                    title: title,
                    description: desc,
                    marker: {
                        latitude: selectedLatLng.lat,
                        longitude: selectedLatLng.lng,
                        popUp: popupHtml
                    }
                },
                accessCode: accessCode
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

            addingEvent = false;

            eventForm.classList.toggle('active');
            body.classList.toggle('form-open');
        });
    });

    cancelEventBtn.addEventListener('click', () => {
        eventForm.classList.toggle('active');
        body.classList.toggle('form-open');

        selectedLatLng = null;

        if(currentMarker) {
            map.removeLayer(currentMarker);
        }

        addingEvent = false;
    });
});
