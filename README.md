# Aplicatie e-Ticketing

## Etapa I

### 1. Definirea sistemului

Proiectul este construit folosind *Spring Boot* si foloseste arhitectura MVC, ceea ce inseamna ca vom avea nevoie de cate o clasa Controller pentru majoritatea modelelor.

In acest proiect vor exista cateva clase de baza precum:

* TicketingEvent
* Marker
* TileLayer

Altele, sunt folosite pentru a asigura functionarea corecta a backend-ului:

* Controllere:
  * EventController
  * MapController

* Configurari: 
  * MapConfig
  * MapConfigBuilder

* Servicii:
  * LeafletMapService

Pentru randarea hartii vom folosi Leaflet, o biblioteca javascript.

*LeafletMapService* se ocupa de generarea dinamica a codului in javascript pentru Leaflet.

*MapConfig* contine informatiile necesare hartii, precum:
* ArrayList de evenimente
* latitudinea si longitudine de start a hartii
* zoomLevel initial al hartii
* tileLayer-ul cu care ar trebui randata harta
* un id unic pentru fiecare harta

*MapConfigBuilder* urmeaza design pattern-ul Builder pentru a construi mai usor o configuratie dinamica. Ea se va modifica pe parcursul sesiunii utilizatorului.

*Orice + Controller* asigura o functionare corecta a site-ului, adaugand validari si alte procesari pentru fiecare ruta a site-ului.

*TicketingEvent* contine informatii specifice unui eveniment, precum titlu, descriere, un marker asociat, etc.

*Marker* reprezinta un marker ce urmeaza sa fie randat pe harta de *LeafletMapService*.

*TileLayer* reprezinta o configurare a modului de randare al hartii.

### Actiuni/interogari:
* Randeze dinamic cod javascript specific bibliotecii alese (metodele din LeafletMapService)
* Sa permita operatii CRUD pentru evenimente (metodele din EventController)
* Sa existe o modalitate de a configura in mod dinamic harta (metodele din MapConfig + MapConfigBuilder)

### 2. Implementare

- [ ] clase simple cu atribute private / protected si metode de acces;
- [ ] cel putin 2 colectii diferite capabile sa gestioneze obiectele definite anterior (eg: List, Set, Map, etc.) dintre care cel putin una sa fie sortata.
- [ ] utilizare mostenire pentru crearea de clase aditionale si utilizarea lor în cadrul colectiilor;
- [x] cel putin o clasa serviciu care sa expuna operatiile sistemului (LeafletMapService)
- [x] o clasa Main din care sunt facute apeluri catre servici (TicketingApplication)