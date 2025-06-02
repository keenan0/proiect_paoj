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
        button.textContent = 'Join Event';
        button.onclick = () => {
            // User with id (whatever user id the current user has) will own an accesscode in the db

            console.log(event);

            fetch('/api/access-code', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    event: event,
                    accessCode: event.accessCode
                })
            }).then(resp => {
                if (resp.ok) {
                    alert(`You joined ${event.title}!`);
                } else {
                    alert("Failed to join event.");
                    console.log(resp);
                }
            });
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
    const eventsOpenMenuBtn = document.querySelector('#eventsMenu');

    const eventsCloseMenuBtn = document.querySelector('#eventsCloseMenu');
    const closeMenuBtn = document.querySelector('#closeMenu');
    const submitEventBtn = document.querySelector('#submitEventBtn');
    const cancelEventBtn = document.querySelector('#cancelEventBtn');

    const sideMenu = document.querySelector('#sideMenu');
    const eventsSideMenu = document.querySelector('#eventsSideMenu');
    const eventForm = document.querySelector('#eventForm');

    const menuItems = document.querySelector('#menuItems');
    const eventsMenuItems = document.querySelector('#eventsMenuItems');
    const selectedLatLngLabel = document.querySelector('#selectedCoords');

    let addingEvent = false;
    let sideMenuOpen = false;
    let eventsSideMenuOpen = false;

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

    eventsOpenMenuBtn.addEventListener('click', function () {
        if(sideMenuOpen) {
            sideMenu.classList.toggle('active');
            sideMenuOpen = false;
        } else {
            mapControls.classList.toggle('shifted');
            body.classList.toggle('menu-open');
        }

        eventsSideMenu.classList.toggle('active');
        eventsSideMenuOpen = !eventsSideMenuOpen;
    })

    openMenuBtn.addEventListener('click', function (e) {
        if(eventsSideMenuOpen) {
            eventsSideMenu.classList.toggle('active');
            eventsSideMenuOpen = false;
        } else {
            mapControls.classList.toggle('shifted');
            body.classList.toggle('menu-open');
        }

        sideMenu.classList.toggle('active');
        sideMenuOpen = !sideMenuOpen;
    })

    eventsCloseMenuBtn.addEventListener('click', function(e) {
        if(sideMenuOpen) {
            sideMenu.classList.toggle('active');
            sideMenuOpen = false;
        }

        eventsSideMenu.classList.toggle('active');
        eventsSideMenuOpen = !eventsSideMenuOpen;

        mapControls.classList.toggle('shifted');
        body.classList.toggle('menu-open');
    })

    closeMenuBtn.addEventListener('click', function (e) {
        if(eventsSideMenuOpen) {
            eventsSideMenu.classList.toggle('active');
            sideMenuOpen = false;
        }

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

            currentMarker = L.marker([selectedLatLng.lat, selectedLatLng.lng], { markerId: 1 }).addTo(map);

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

        let returned_marker = undefined;

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
        })
            .then(resp => {
                if (!resp.ok) {
                    alert("Failed to add event.");
                    console.log(resp);
                    throw new Error('Network response was not ok');
                }
                return resp.json();
            })
            .then(marker => {
                returned_marker = marker;
                alert("Event added! Marker ID: " + marker.id);
                console.log("Marker primit:", marker);
            })
            .catch(error => {
                console.error('Error:', error);
            })
            .finally(() => {
                document.getElementById('eventTitle').value = '';
                document.getElementById('eventDesc').value = '';
                document.getElementById('selectedCoords').innerText = 'Location: ❌';

                addingEvent = false;

                eventForm.classList.toggle('active');
                body.classList.toggle('form-open');
            });

        L.marker([selectedLatLng.lat, selectedLatLng.lng])
            .addTo(map)
            .bindPopup(popupHtml)
            .openPopup()
            .on('click', (e) => {
                // Load the events at selected marker through api call
                const markerId = returned_marker.id;
                const lat = returned_marker.latitude;
                const lng = returned_marker.longitude;

                console.log(markerId);
                console.log(lat);
                console.log(lng);

                if(addingEvent) {
                    selectedLatLng = {
                        lat: lat,
                        lng: lng
                    };

                    return;
                }

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
            })
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
