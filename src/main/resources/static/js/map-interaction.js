function renderEvents(events, menuDiv) {
    /*
    *   Used by the fetch call / get request to /api/events when a marker is clicked.
    *
    *   @events - list of TicketingEvent returned by the api call
    *   @menuDiv - the div where the data should be added
    * */

    menuDiv.innerHTML = ''; // Clear previous content

    if (events.length === 0) {
        menuDiv.innerHTML = '<p>No events found for this marker.</p>';
        return;
    }

    events.forEach(event => {
        const eventDiv = document.createElement('div');
        eventDiv.classList.add('event');

        const title = document.createElement('h5');
        title.textContent = event.title;

        const desc = document.createElement('p');
        desc.textContent = event.description;

        const button = document.createElement('button');
        button.textContent = 'View Details';
        button.onclick = () => {
            alert(`Event ID: ${event.id}`);
        };

        eventDiv.appendChild(title);
        eventDiv.appendChild(desc);
        eventDiv.appendChild(button);

        menuDiv.appendChild(eventDiv);
    });
}

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

    const menuItems = document.querySelector('#menuItems');
    const selectedLatLngLabel = document.querySelector('#selectedCoords');

    let addingEvent = false;
    let sideMenuOpen = false;
    let selectedLatLng = null;
    let currentMarker = null;

    map.eachLayer(function(layer) {
        if (layer instanceof L.Marker) {
            layer.on('click', function () {
                // Load the events at selected marker through api call
                const markerId = layer.options.markerId;

                console.log(markerId);

                // Make GET call to Spring Boot API
                fetch(`/api/events?markerId=${markerId}`)
                    .then(response => response.json())
                    .then(data => {
                        console.log(data)
                        renderEvents(data, menuItems);
                    })
                    .catch(error => {
                        console.error('Error fetching events:', error);
                        menuItems.innerHTML = '<p>Error loading events.</p>';
                    });

                if (!sideMenuOpen) {
                    sideMenuOpen = true;
                    sideMenu.classList.toggle('active');
                    mapControls.classList.toggle('shifted');
                    body.classList.toggle('menu-open');
                }
            });
        }
    });

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

        const marker = L.marker([selectedLatLng.lat, selectedLatLng.lng])
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
                        id: 0,
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
                console.log(resp);
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
