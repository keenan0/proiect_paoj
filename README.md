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

*AccessCode* este o clasa abstracta care contine un cod de acces pentru fiecare eveniment. Acesta are doua derivate momentan: *QrAccessCode* si *CustomAccessCode*, care sunt folosite pentru a verifica daca un utilizator poate participa la un eveniment. 

* [x] Un set care retine markerele randate in LeafletMapService

### Actiuni/interogari:
* Randeze dinamic cod javascript specific bibliotecii alese (metodele din LeafletMapService)
* Sa permita operatii CRUD pentru evenimente (metodele din EventController)
* Sa existe o modalitate de a configura in mod dinamic harta (metodele din MapConfig + MapConfigBuilder)
* Fiecare eveniment are trei optiuni:
  * Fara intrare
  * Intrare generata cu cod QR
  * Intrare generata cu un cod personalizat de catre organizator

### 2. Implementare

- [x] clase simple cu atribute private / protected si metode de acces; (clase + derivate)
- [x] cel putin 2 colectii diferite capabile sa gestioneze obiectele definite anterior (eg: List, Set, Map, etc.) dintre care cel putin una sa fie sortata.
- [x] utilizare mostenire pentru crearea de clase aditionale si utilizarea lor în cadrul colectiilor; (AccessCode + derivate)
- [x] cel putin o clasa serviciu care sa expuna operatiile sistemului (LeafletMapService)
- [x] o clasa Main din care sunt facute apeluri catre servici (TicketingApplication)

# Etapa II

### TODO:
- [x] Implementarea unei baze de date folosind JDBC si H2
- [x] Integrare user authentication
- [x] Construirea unei baze de date
- [x] Servicii CRUD pentru 4 clase
- [x] Servicii singleton pentru r/w in baza de date (Orice + Service)
- [ ] Serviciu de audit CSV pentru fiecare actiune definita la etapa I

### Misc:
- [x] Cand un eveniment este highlighted, vor aparea detalii despre acesta in #sideMenu
- [ ] Un nou meniu pentru a vizualiza toate evenimentele active
- [ ] In #sideMenu un search bar pentru a cauta evenimente dupa nume, locatie, etc.
- [ ] Un nou meniu pentru vizualizarea profilului (dinamic/pagina proprie)

### Detalii implementare user

* Un user poate avea un singur eveniment activ (ca si host)
  * Daca nu are niciun eveniment, poate adauga unul
  * Daca are un eveniment activ, cand apasa pe butonul "+" acesta va fi informat de limita evenimentelor active
* Un user poate participa la oricate evenimente doreste (ca si client)
* Un user poate sa isi modifice datele profilului