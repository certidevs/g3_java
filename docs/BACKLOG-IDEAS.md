# Backlog de Ideas — Alquiler (Grupo 3)

Documento orientativo. El proyecto se entrega TAL CUAL: las ideas recogidas aqui NO se van a
implementar dentro del curso. Su objetivo es puramente didactico: ayudar a entender el ciclo de
vida del software, es decir, que pasa con una aplicacion DESPUES de la primera entrega, como se
detecta deuda tecnica real sobre el codigo que ya existe y como se prioriza la evolucion de un
producto. Todo lo que sigue esta fundamentado en clases, controllers, services y plantillas
concretas de este repositorio (`com.demo.*`), no en recomendaciones genericas.

## 1. Resumen del proyecto

- Dominio: plataforma de alquiler de alojamientos tipo Airbnb (casas, apartamentos y habitaciones).
  Un anfitrion (`ROLE_ADMIN`) publica `House` y un huesped (`ROLE_USER`) crea reservas (`Booking`),
  deja resenas (`Review`), marca favoritos (`Favorite`) y recomienda casas a otros usuarios
  (`HouseRecommended`).

- Stack (versiones reales del `pom.xml`):
  - Spring Boot 4.0.5 (parent), Java 25.
  - Spring MVC (`spring-boot-starter-webmvc`) + Thymeleaf (`spring-boot-starter-thymeleaf`) con
    `thymeleaf-extras-springsecurity6`.
  - Spring Data JPA (`spring-boot-starter-data-jpa`) sobre H2 en memoria (`jdbc:h2:mem:g3_db`,
    `ddl-auto=create-drop`, consola H2 activada).
  - Spring Security (`spring-boot-starter-security`, BCrypt).
  - Lombok, Bootstrap 5.3.8 y Font Awesome 7.2.0 via WebJars.
  - Dependencias de test presentes (`webmvc-test`, `data-jpa-test`, `thymeleaf-test`,
    `security-test`) pero practicamente sin uso (ver seccion 3).

- Entidades principales y relaciones (`com.demo.model`):
  - `User` (implementa `UserDetails`): username/email unicos, `password`, `role` (`Role`),
    `active`, y `tokenforRecommended` para compartir recomendaciones.
  - `House`: `title`, `description`, `pricePerNight`, `location`, `province` (`Province`),
    `maxGuests`, `houseType` (`HouseType`), `imageUrl`, `active`, `reserve` (`StatusReserva`),
    `@ManyToOne host`, `@ManyToMany amenities` (`Amenity`), mas dos campos `tokenFrom`/`tokenTo`
    sin uso real.
  - `Booking`: `@ManyToOne` a `User` (`userBooking`) y a `House` (`userHouse`), fechas estimadas y
    reales (`estimatedCheckin/out`, `checkin/out`), `numberNights`, `totalPrice`,
    `statusbooking` (`StatusBooking`) y datos de tarjeta en texto plano.
  - `Review`: `title`, `rating`, `comment`, `createdAt`, `@ManyToOne` a `House` y `User`.
  - `Favorite`: par `@ManyToOne` `User` + `House`.
  - `HouseRecommended`: `message`, `timeRecommended`, `viewed`, `@ManyToOne` a `House`,
    `userFrom`, `userTo`.
  - `Amenity`: `name`, `description`, `icon`, `@ManyToMany(mappedBy)` a `House`.
  - Enums: `HouseType`, `Province`, `Role`, y DOS enums de estado solapados, `StatusBooking`
    (PENDING/CONFIRMED/CANCELLED/COMPLETED) y `StatusReserva` (DISPONIBLE/NO_DISPONIBLE/RESERVADA).

- Que se puede hacer hoy (funcionalidad actual):
  - Publico / anonimo: ver la portada (`IndexController` `/`), listar casas activas
    (`HouseController` `/houses`) con un catalogo muy completo de filtros (provincia, tipo,
    precio maximo, rating minimo, huespedes, amenities, favoritos, alquiladas), ver el detalle de
    una casa activa y sus resenas, y registrarse/iniciar sesion (`AuthController`).
  - Usuario (`ROLE_USER`): crear reservas (`OrderController` `/orders/new` +
    `BookingController` `POST /booking`), "finalizar" una reserva con tarjeta
    (`/booking/{id}/finish`), gestionar favoritos (`FavoriteController`), escribir resenas
    (`ReviewController`), recomendar casas a otros usuarios por token o email
    (`RecommendedController`), ver su panel de huesped (`ControlPanelController`
    `/panel-control/{userId}`) y su agenda de usuarios (`/agenda`).
  - Admin / anfitrion (`ROLE_ADMIN`): crear y editar casas con subida de imagen
    (`HouseController` + `FileService`), activar/desactivar casas, gestionar usuarios
    (`UserController` `/users`), y administrar reservas de sus casas por estado
    (`/host/pending|confirmed|cancelled|completed/{id}`).

## 2. Features (nuevas funcionalidades)

Ideas de funcionalidad que la app NO tiene todavia y que aportarian valor de negocio. Todas son de
backend Spring + Thymeleaf, coherentes con la filosofia del curso (renderizado en servidor, sin
frameworks JS de cliente).

### F-01 · Comprobacion de disponibilidad y bloqueo de solapamiento de reservas
- Que: al crear una reserva, comprobar que la casa no tiene ya otra reserva PENDING/CONFIRMED que
  solape el rango de fechas solicitado, y mostrar las fechas ya ocupadas en el detalle de la casa.
- Por que aporta: es el corazon de un Airbnb; hoy se pueden crear dos reservas de la misma casa para
  las mismas fechas sin ningun control.
- Que habria que tocar: `BookingRepository` (nueva query de solapamiento por `userHouse.id`,
  estado y rango de fechas), `BookingService.validateDates(...)` (ampliarlo), `BookingController`
  `POST /booking`, plantilla `house/house-detail.html` para pintar un calendario/listado de fechas
  ocupadas.
- Dificultad: Alta

### F-02 · Listado y detalle propio de reservas para el huesped desde el panel
- Que: que `/panel-control/{userId}` permita al huesped abrir cada una de sus reservas y ver su
  historico completo de forma navegable, no solo el agregado por estado.
- Por que aporta: hoy `ControlPanelController` ya calcula `listBookingGuest`, pero la navegacion al
  detalle (`/booking/{id}`) no esta protegida ni integrada; falta una vista coherente "Mis reservas".
- Que habria que tocar: `ControlPanelController`, `BookingService.getGuestBookings(...)`,
  plantillas `user/panel-control.html` y `booking/booking-list.html`.
- Dificultad: Media

### F-03 · Edicion del rango de fechas por el huesped antes de confirmar
- Que: permitir al huesped modificar las fechas de una reserva en estado PENDING (hoy solo el flujo
  de edicion existe orientado a admin en `booking/edit/{id}`).
- Por que aporta: flexibiliza el flujo real de reserva sin tener que cancelar y volver a crear.
- Que habria que tocar: `BookingController` (`POST booking/update-dates` ya existe pero asume rol
  host/admin), `BookingService.recalculateBooking(...)`, plantilla `booking/booking-form.html`.
- Dificultad: Media

### F-04 · Galeria de imagenes por casa (no solo una portada)
- Que: permitir varias imagenes por `House` en lugar de un unico `imageUrl`.
- Por que aporta: una sola foto limita mucho la ficha del alojamiento; en alquiler vacacional la
  galeria es decisiva.
- Que habria que tocar: nueva entidad `HouseImage` (`@ManyToOne House`) o `@ElementCollection`,
  `FileService.store(...)` (ya soporta multipart), `HouseController` `POST /houses`, plantillas
  `house/house-form.html` y `house/house-detail.html`.
- Dificultad: Media

### F-05 · Respuesta del anfitrion a las resenas
- Que: que el `host` de una casa pueda responder publicamente a cada `Review`.
- Por que aporta: cierra el ciclo de reputacion; es estandar en plataformas de alquiler.
- Que habria que tocar: campo `hostReply` (+ fecha) en `Review` o entidad `ReviewReply`,
  `ReviewService`, `ReviewController`, plantillas `review/review-detail.html` y
  `house/house-detail.html`.
- Dificultad: Media

### F-06 · Resena solo para quien ha completado una estancia
- Que: permitir publicar `Review` unicamente si el usuario tiene un `Booking` COMPLETED en esa casa.
- Por que aporta: evita resenas falsas; ya existe `BookingService.hasUserVisitedHouse(...)` que lo
  hace casi posible, pero `ReviewController.saveReview(...)` no lo comprueba.
- Que habria que tocar: `ReviewService` (validacion), `ReviewController` `POST /reviews`,
  `BookingService.hasUserVisitedHouse(...)`, plantilla `house/house-detail.html`.
- Dificultad: Baja

### F-07 · Bandeja de recomendaciones recibidas con marca de leido integrada
- Que: convertir `recommended-show/{idUsuario}` en una verdadera bandeja de entrada, con contador de
  no leidas y boton de marcar todas como vistas.
- Por que aporta: ya existe el indicador `hasUnreadRecommendations` en el navbar
  (`GlobalModelAttributes`) y el endpoint `POST /recommended/{id}/view`, pero la experiencia esta a
  medias.
- Que habria que tocar: `RecommendedController`, `RecommendedService`, `HouseRecommendedRepository`,
  plantilla `recommended/recommended-list.html`.
- Dificultad: Baja

### F-08 · Perfil de anfitrion publico
- Que: pagina publica por `host` con sus casas activas y su valoracion media agregada.
- Por que aporta: da confianza al huesped; hoy el `host` solo se ve indirectamente.
- Que habria que tocar: `HouseRepository` (query por `host.id` + medias), `HouseService`, nuevo
  metodo en `UserController` o `HouseController`, nueva plantilla.
- Dificultad: Media

### F-09 · Politica de minimo y maximo de noches por casa
- Que: respetar `minimumNights` / `maxNights` por alojamiento (hay TODOs explicitos en `House.java`,
  lineas 34-35).
- Por que aporta: regla de negocio habitual; ahora cualquier numero de noches >= 1 vale.
- Que habria que tocar: campos nuevos en `House`, `BookingService.validateDates(...)`,
  `BookingController`, `house/house-form.html` y `booking/booking-form.html`.
- Dificultad: Baja

### F-10 · Busqueda de casas por rango de precio (min y max)
- Que: anadir filtro por precio minimo ademas del maximo ya existente.
- Por que aporta: el repositorio ya soporta `:price` como tope superior en `findByReserveStats`;
  falta el inferior. Hay incluso una derived query comentada (`findByPricePerNightBetween`) en
  `HouseRepository`.
- Que habria que tocar: `HouseRepository.findByReserveStats(...)`, `HouseService.getHousesForCatalog(...)`,
  `HouseController`, plantilla `house/house-list.html`.
- Dificultad: Baja

### F-11 · Estadisticas de anfitrion en el panel de control
- Que: mostrar al admin/host metricas: ingresos estimados (suma de `totalPrice`), nº de reservas por
  estado, rating medio de sus casas.
- Por que aporta: valor analitico real; ya existe el DTO `HouseStatsDto` y agregados de rating.
- Que habria que tocar: `BookingRepository`/`HouseRepository` (nuevas agregaciones), `BookingService`,
  `ControlPanelController`, plantilla `user/panel-control.html`.
- Dificultad: Media

### F-12 · Wishlist/favoritos con vista propia
- Que: pantalla dedicada "Mis favoritos" (hoy favoritos solo se usan como filtro en `/houses`).
- Por que aporta: mejora la usabilidad; `FavoriteService.getFavoriteHouseIds(...)` ya esta listo.
- Que habria que tocar: `FavoriteController` (nuevo GET), `FavoriteService`/`FavoriteRepository`,
  nueva plantilla reutilizando tarjetas de `house/house-list.html`.
- Dificultad: Baja

### F-13 · Cancelacion con politica y motivo
- Que: registrar motivo de cancelacion y aplicar una politica simple (p.ej. no cancelable a < N dias
  del check-in).
- Por que aporta: trazabilidad y reglas de negocio; hoy cancelar es un simple cambio de estado por
  GET sin contexto.
- Que habria que tocar: campos en `Booking`, metodo en `BookingService`, endpoints
  `/booking/from-*-to-cancelled*` de `BookingController`, plantillas de booking.
- Dificultad: Media

### F-14 · Notificaciones in-app de cambios de estado de reserva
- Que: avisar al huesped cuando su reserva pasa a CONFIRMED/CANCELLED, reutilizando el patron de
  "no leidas" que ya existe para recomendaciones.
- Por que aporta: feedback al usuario; el patron ya esta probado en `HouseRecommended.viewed`.
- Que habria que tocar: entidad de notificacion o flag en `Booking`, `BookingService`,
  `GlobalModelAttributes` (badge navbar), plantillas de layout.
- Dificultad: Media

### F-15 · Gestion CRUD de amenities desde el panel admin
- Que: alta/edicion/baja de `Amenity` desde la UI (hoy solo se siembran en `DataInitializer`).
- Por que aporta: las amenities son un eje de filtrado importante y no se pueden mantener sin tocar
  codigo.
- Que habria que tocar: `AmenityRepository` (hoy vacio), nuevo `AmenityService` y `AmenityController`,
  nuevas plantillas.
- Dificultad: Baja

### F-16 · Idempotencia y consistencia del estado de la casa (`StatusReserva`) derivado de las reservas
- Que: que la disponibilidad `House.reserve` se calcule a partir de las reservas vigentes en vez de
  fijarse a mano en varios puntos.
- Por que aporta: hoy `BookingController` escribe `StatusReserva.RESERVADA/DISPONIBLE` manualmente en
  varios metodos y es facil que se descuadre (relacionado con B-02).
- Que habria que tocar: `BookingService` (metodo que recalcule estado de la casa), `HouseService`,
  todos los endpoints de transicion en `BookingController`.
- Dificultad: Media

## 3. Fixes (correcciones y deuda tecnica)

Problemas, riesgos y deuda detectados en el codigo ACTUAL.

### B-01 · IDOR: cualquier usuario autenticado puede operar reservas ajenas
- Problema: los endpoints de transicion de estado y el detalle de reserva reciben solo el `id` y no
  comprueban que el usuario actual sea el huesped o el anfitrion de esa reserva.
- Donde: `BookingController` metodos `getBookingById` (`/booking/{id}`),
  `actionFromPendingToConfirmed`, `actionFromPendingToCancelled`, `actionFromConfirmedToCancelled`,
  `actionFromConfirmedToCompleted`, `actionFromPendingToCancelledGuest`, y `finish`
  (`/booking/{id}/finish`). En `SecurityConfig` estas rutas caen en `.anyRequest().authenticated()`.
- Impacto: un usuario puede confirmar, cancelar, completar o "pagar" la reserva de otro conociendo
  el id (vulnerabilidad de control de acceso a nivel de objeto).
- Propuesta: inyectar `@AuthenticationPrincipal User` y validar propiedad (huesped o host de la casa)
  en `BookingService` antes de mutar; devolver 403 si no procede.
- Severidad: Alta

### B-02 · Doble enum de estado (`StatusBooking` vs `StatusReserva`) sincronizado a mano
- Problema: el estado de la reserva (`Booking.statusbooking`) y la disponibilidad de la casa
  (`House.reserve`) son dos enums distintos que hay que mantener coherentes manualmente.
- Donde: `model/enums/StatusBooking.java`, `model/enums/StatusReserva.java`; sincronizacion dispersa
  en `BookingController.createBooking(...)` y `actionFromConfirmedToCompleted(...)`.
- Impacto: alto riesgo de descuadre (una casa marcada RESERVADA sin reserva activa o viceversa);
  fuente clasica de bugs de datos.
- Propuesta: derivar `House.reserve` de las reservas vigentes (ver F-16) o eliminarlo y calcular
  disponibilidad on-the-fly; documentar una unica fuente de verdad.
- Severidad: Alta

### B-03 · Validacion de fechas insuficiente y logica de dominio duplicada/ignorada
- Problema: `BookingService.validateDates(...)` solo comprueba que checkout no sea anterior a checkin;
  no exige minimo de 1 noche, ni fechas futuras, ni ausencia de solapamiento. Ademas la propia
  entidad `Booking` tiene metodos de dominio (`calculateNights`, `confirmedBooking`,
  `completedBookingIn/Out`, constructor con regla `nroNights >= 1`) que los controllers NO usan:
  fijan el estado con setters directos.
- Donde: `BookingService.validateDates(...)`; `Booking.java` (lineas 66-143);
  `BookingController.createBooking(...)` y endpoints de transicion.
- Impacto: las invariantes del modelo son codigo muerto; las reglas reales viven a medias en el
  controller, lo que produce inconsistencias (p.ej. una reserva de 0 noches con `totalPrice = 0`).
- Propuesta: centralizar las transiciones en `Booking`/`BookingService`, llamar a los metodos de
  dominio existentes y ampliar `validateDates`.
- Severidad: Alta

### B-04 · Ausencia total de validacion declarativa (Bean Validation)
- Problema: no hay ni una sola anotacion `@Valid`, `@NotBlank`, `@Email`, `@Positive`, etc. en DTOs,
  entidades ni controllers. El registro (`RegisterForm`) y la creacion de casas/usuarios aceptan
  datos vacios o invalidos salvo por chequeos manuales sueltos.
- Donde: `dto/RegisterForm.java`, `dto/HouseStatsDto.java`, `model/*`, todos los `@PostMapping`
  (p.ej. `AuthController.register`, `HouseController.createHouse`, `UserController.save`).
- Impacto: datos sucios en BD (precios negativos, emails malformados, titulos vacios) y dependencia
  de validaciones manuales inconsistentes.
- Propuesta: anadir `spring-boot-starter-validation`, anotar DTOs/entidades y usar `@Valid` +
  `BindingResult` en los controllers, mostrando errores por campo en las plantillas.
- Severidad: Media

### B-05 · Sin manejador global de excepciones; uso masivo de `.orElseThrow()`
- Problema: hay `@ControllerAdvice` (`GlobalModelAttributes`) pero solo aporta `@ModelAttribute`
  globales; no existe ningun `@ExceptionHandler`. Multiples controllers hacen `.orElseThrow()` con
  un id inexistente, lo que provoca un 500 crudo.
- Donde: `GlobalModelAttributes.java` (sin `@ExceptionHandler`); `.orElseThrow()` en
  `BookingController` (`editBooking`, `finish`, `updateBooking`, `createBooking`),
  `OrderController.newOrder`, `ReviewController` (`review`, `newReview`), `RecommendedController`,
  `HouseController.editHouse`, `UserController`.
- Impacto: el usuario ve errores tecnicos; existen `error/404.html`, `error/500.html`, `error/403.html`
  y `error/error.html` que apenas se aprovechan.
- Propuesta: anadir un `@ControllerAdvice` con `@ExceptionHandler` para
  `NoSuchElementException`/`IllegalArgumentException`/`AccessDeniedException` que redirija a las
  vistas de error existentes.
- Severidad: Media

### B-06 · Redirecciones a `/index`, una ruta que no existe
- Problema: numerosos metodos devuelven `return "redirect:/index"`, pero la unica ruta mapeada es `/`
  (`IndexController`). No hay `@GetMapping("/index")`.
- Donde: `BookingController` (lineas 37, 103, 119, 135, 151, 167, 183, 199, 215) y
  `ControlPanelController` (linea 56).
- Impacto: ante un id no encontrado el usuario aterriza en un 404 (o cae en la cadena de seguridad),
  no en la portada como se pretende.
- Propuesta: cambiar a `redirect:/` o crear un alias `/index`.
- Severidad: Media

### B-07 · IDOR en listados por id de usuario (host/guest) y en la bandeja de recomendaciones
- Problema: rutas que reciben un `{id}`/`{idUsuario}` de usuario y muestran sus reservas o
  recomendaciones sin comprobar que coincide con el principal (a diferencia de
  `ControlPanelController`, que si lo valida).
- Donde: `BookingController` `/host/*/{id}` y `/guest/*/{id}`; `RecommendedController`
  `recommended-show/{idUsuario}`. En `SecurityConfig` solo estan como `authenticated()`.
- Impacto: un usuario puede listar reservas y recomendaciones de cualquier otro cambiando el id.
- Propuesta: validar `id == currentUser.getId()` (o rol admin) en el controller/servicio; reutilizar
  el patron ya presente en `ControlPanelController.panelControl(...)`.
- Severidad: Alta

### B-08 · Permisos de Review demasiado abiertos (editar/borrar cualquier resena)
- Problema: en `SecurityConfig` las rutas de reviews estan comentadas, de modo que `/reviews/delete/{id}`
  y `/reviews/edit/{id}` solo requieren estar autenticado; ademas `ReviewController` no comprueba
  autoria. Y `saveReview` tiene un TODO de validacion (`isValid`) sin implementar.
- Donde: `SecurityConfig` (bloque de reviews comentado, lineas 49-54); `ReviewController`
  (`deleteReview`, `editReview`, `saveReview`).
- Impacto: cualquier usuario puede editar o eliminar resenas de otros; no hay control de contenido.
- Propuesta: restringir editar/borrar a autor o admin, descomentar y afinar los matchers, e
  implementar la validacion de contenido pendiente.
- Severidad: Media

### B-09 · Datos de tarjeta almacenados en texto plano en `Booking`
- Problema: `Booking` persiste `cardNumber`, `cardOwner` y `cardExpirationDate` como strings en claro.
  El CVV (`cardSecretCode`) se valida en `finish(...)` pero (correctamente) no se guarda.
- Donde: `model/Booking.java` (lineas 62-64); `BookingController.finish(...)`.
- Impacto: almacenar PAN en claro es una mala practica grave de seguridad/cumplimiento, aunque sea
  un proyecto de demo.
- Propuesta: no persistir datos sensibles de tarjeta; si se necesita simular el pago, guardar solo
  los ultimos 4 digitos y delegar el cobro en una pasarela (horizonte, seccion 4).
- Severidad: Alta

### B-10 · `SecurityConfig` incompleto: sin logout, H2 console y matchers a medias
- Problema: el navbar hace `POST /logout` pero no hay `http.logout(...)` configurado (hay un
  `// TODO logout`); tampoco se permite explicitamente `/h2-console/**` (solo se prepara CSRF y
  frameOptions, lineas 26-28) por lo que queda tras `authenticated()`; y hay grandes bloques
  comentados.
- Donde: `config/SecurityConfig.java` (lineas 26-28, 49-59, 78-81).
- Impacto: el logout depende del comportamiento por defecto; la configuracion es confusa y fragil de
  mantener.
- Propuesta: configurar `logout` explicito, decidir y documentar el acceso a la consola H2, y
  limpiar los matchers muertos.
- Severidad: Media

### B-11 · `DataInitializer` resiembra en cada arranque y contiene datos basura/duplicados
- Problema: con `ddl-auto=create-drop` y un `CommandLineRunner` sin guarda, los datos se recrean en
  cada arranque. Ademas hay usuarios de prueba con email malformado (`jose@test.8com`), usuarios sin
  `firstName/lastName`, nombres como `PRUEBA`, y casas/anfitriones claramente duplicados.
- Donde: `config/DataInitializer.java` (p.ej. lineas 236-241, 299-304) y `application.properties`.
- Impacto: ruido en demos, datos inconsistentes y dificultad para razonar sobre el estado inicial.
- Propuesta: separar datos de "seed" minimos y coherentes, condicionar la siembra a BD vacia, y
  mover credenciales/datos a un perfil de desarrollo.
- Severidad: Baja

### B-12 · Riesgo de consultas N+1 en listados de reservas y casas
- Problema: las queries `bookingsHost`/`bookingsGuest` y los listados navegan
  `booking.userHouse.host` y campos de `User`/`House` que las plantillas renderizan de forma lazy.
  El catalogo principal lo evita con proyeccion DTO (`findByReserveStats`), pero los listados de
  reservas no.
- Donde: `BookingRepository` (`bookingsHostPending/...`, `bookingsGuest`, `bookingsHost`),
  `BookingController` (listados host/guest), plantillas `booking/booking-list.html` y
  `user/panel-control.html`.
- Impacto: numero de consultas creciente con el volumen; aceptable en demo pero problematico al
  crecer.
- Propuesta: usar `JOIN FETCH` o `@EntityGraph` en las queries que alimentan los listados, o
  proyectar a DTO como ya se hace en el catalogo.
- Severidad: Baja

### B-13 · Generacion de token de usuario duplicada y constructor "magico" en la entidad
- Problema: la logica de token aleatorio esta repetida en `User` (constructor sin args + `SecureRandom`
  estatico) y en `UserService.generateRecommendedToken()`. El propio `User.java` tiene un TODO
  cuestionando ese constructor (lineas 56-66). Como consecuencia, `User.builder()` no asigna token
  (lo deja a null) mientras que `new User()` si.
- Donde: `model/User.java` (lineas 22-66); `service/UserService.java` (`generateRecommendedToken`,
  `register`, `create`).
- Impacto: comportamiento inconsistente segun como se cree el usuario; logica de negocio en la capa
  de entidad.
- Propuesta: unificar la generacion de token en el servicio (o un `@PrePersist`) y eliminar el
  constructor manual.
- Severidad: Media

### B-14 · `OrderController` duplica el flujo de creacion de reserva de `BookingController`
- Problema: `OrderController.newOrder` (`/orders/new`) hace practicamente lo mismo que
  `BookingController.newBooking` (`booking/new/{houseId}`): preparar un `Booking` con la casa y
  renderizar un formulario. Ambos conviven y el `POST` final es el mismo (`/booking`).
- Donde: `controller/OrderController.java`; `controller/BookingController.java`
  (`newBooking`, `createBooking`).
- Impacto: duplicidad y confusion de rutas (`/orders/new` vs `booking/new/{houseId}`); mas superficie
  que mantener.
- Propuesta: unificar en un unico controller/flujo de reserva y eliminar el redundante.
- Severidad: Baja

### B-15 · Codigo JavaScript de cliente en plantillas, contrario al enfoque del curso
- Problema: `booking/booking-form.html` y `order/order-form.html` incluyen logica JS
  (`calcularDiferencia`) para calcular noches y precio en el navegador, mientras que el calculo real
  ya existe en servidor (`Booking.calculateNights/calculateTotalPrice`). El curso es Spring
  server-side sin frameworks JS.
- Donde: `templates/booking/booking-form.html` (script, lineas 174-227); `templates/order/order-form.html`
  (script, lineas 138-186).
- Impacto: dos fuentes de calculo (cliente y servidor) que pueden divergir; se aparta de la
  filosofia didactica.
- Propuesta: mostrar el desglose calculado en servidor (recarga o vista de confirmacion) y reducir el
  JS al minimo imprescindible.
- Severidad: Baja

### B-16 · Practicamente sin tests automatizados
- Problema: el unico test es `G3JavaApplicationTests.contextLoads()`, pese a que el `pom.xml` ya
  incluye `webmvc-test`, `data-jpa-test`, `thymeleaf-test` y `security-test`.
- Donde: `src/test/java/com/demo/G3JavaApplicationTests.java`.
- Impacto: cualquier cambio puede romper reglas de negocio (fechas, estados, seguridad) sin que nada
  lo detecte; imposible refactorizar con red.
- Propuesta: anadir `@DataJpaTest` para los repositorios (queries de `BookingRepository`,
  `HouseRepository`) y `@WebMvcTest`/`MockMvc` con `spring-security-test` para los controllers y la
  autorizacion (especialmente los puntos IDOR de B-01 y B-07).
- Severidad: Media

### B-17 · `findByReserve` y derived queries muertas / inconsistencias menores en repositorios
- Problema: hay queries y derived queries comentadas o sin uso, una condicion redundante
  (`WHERE (h.active IS NOT NULL OR h.active = true)` en `findTop3ByOrderByAverageRatingDesc`), y
  metodos de repositorio que conviven con su version DTO equivalente.
- Donde: `HouseRepository` (`findByReserve` vs `findByReserveStats`,
  `findTop3ByOrderByAverageRatingDesc` vs `findTop3HousesWithStats`, bloque comentado lineas 18-24);
  `ReviewRepository` y `UserRepository` con derived queries comentadas.
- Impacto: ruido y ambiguedad sobre cual es la consulta "buena"; mas dificil de mantener.
- Propuesta: eliminar lo no usado y dejar una unica via por caso de uso.
- Severidad: Baja

## 4. Como evolucionaria en un entorno real (ciclo de vida del software)

Una entrega como esta es el punto de partida, no el final. En un equipo real, la aplicacion seguiria
un ciclo continuo de pruebas, despliegue, observacion y nueva priorizacion. Aterrizado a ESTE
proyecto:

- Control de versiones y ramas: trabajar cada item del backlog en una rama propia
  (p.ej. `fix/booking-idor` para B-01, `feat/overlap-check` para F-01) con Pull Requests revisados,
  en lugar de un unico volcado de codigo. Ayuda a aislar cambios sensibles como la seguridad de
  reservas.
- Tests automatizados y cobertura: empezar por cubrir las reglas de negocio criticas que hoy estan
  sin red (transiciones de `Booking`, validacion de fechas, autorizacion). El `pom.xml` ya trae las
  dependencias de test; el siguiente paso natural seria `@DataJpaTest` para `BookingRepository` y
  `@WebMvcTest` + `spring-security-test` para los controllers, midiendo cobertura con JaCoCo.
- Integracion continua (CI): un pipeline (GitHub Actions) que compile y ejecute los tests en cada
  push/PR evitaria que regresiones como las de B-02/B-03 lleguen a la rama principal.
- Contenedores y base de datos real: hoy todo corre sobre H2 en memoria con `create-drop`. El paso
  habitual es migrar a PostgreSQL en Docker (con `compose.yaml`) y gestionar el esquema con
  Flyway/Liquibase en vez de `ddl-auto`, lo que ademas obligaria a reemplazar el `DataInitializer`
  (B-11) por migraciones y datos de seed controlados.
- Despliegue (CD): empaquetar la app (el `spring-boot-maven-plugin` ya esta configurado) y desplegarla
  de forma reproducible, con perfiles separados para desarrollo y produccion.
- Observabilidad y logs: incorporar Spring Boot Actuator, logs estructurados y metricas para detectar,
  por ejemplo, picos de errores 500 (hoy frecuentes por los `.orElseThrow()` de B-05) o consultas
  lentas (N+1 de B-12).
- Seguridad: cerrar los IDOR (B-01, B-07), el almacenamiento de tarjetas (B-09) y completar
  `SecurityConfig` (B-10) serian requisitos de cualquier auditoria previa a produccion.
- Feedback de usuarios y priorizacion continua: una vez en produccion, el uso real (que filtros del
  catalogo se usan, cuantas reservas se completan, cuantas resenas se publican) alimentaria de nuevo
  este backlog, reordenando features y fixes segun valor e impacto. El backlog es un documento vivo,
  no una lista cerrada.

Como horizonte (excede el alcance del curso, que es Spring server-side con Thymeleaf): una API REST
para integrar apps moviles o terceros, geolocalizacion y mapas para las casas, una pasarela de pago
real, busqueda avanzada e internacionalizacion, y despliegue en la nube con CI/CD completo. Se
mencionan para situar el proyecto en su contexto, no como tareas a abordar ahora.

## 5. Priorizacion orientativa

Tabla orientativa (no es un compromiso de implementacion). "Valor" y "Esfuerzo" son cualitativos.

| Idea | Tipo | Valor | Esfuerzo | Horizonte |
|------|------|-------|----------|-----------|
| B-01 IDOR en operaciones de reserva | Fix | Alto | Bajo | Corto |
| B-07 IDOR en listados por id de usuario | Fix | Alto | Bajo | Corto |
| B-09 Tarjetas en texto plano | Fix | Alto | Bajo | Corto |
| B-06 Redirecciones a `/index` inexistente | Fix | Medio | Bajo | Corto |
| B-05 Manejador global de excepciones | Fix | Medio | Bajo | Corto |
| B-10 Completar `SecurityConfig` (logout, H2) | Fix | Medio | Bajo | Corto |
| B-08 Permisos de Review demasiado abiertos | Fix | Medio | Bajo | Corto |
| B-03 Validacion de fechas y dominio de Booking | Fix | Alto | Medio | Corto |
| B-02 Doble enum de estado | Fix | Alto | Medio | Medio |
| B-04 Bean Validation declarativa | Fix | Medio | Medio | Medio |
| B-16 Tests automatizados | Fix | Alto | Medio | Medio |
| F-01 Solapamiento/disponibilidad de reservas | Feature | Alto | Alto | Medio |
| F-06 Resena solo tras estancia completada | Feature | Medio | Bajo | Corto |
| F-10 Filtro por rango de precio | Feature | Medio | Bajo | Corto |
| F-12 Vista propia de favoritos | Feature | Medio | Bajo | Corto |
| F-11 Estadisticas de anfitrion | Feature | Medio | Medio | Medio |
| F-04 Galeria de imagenes por casa | Feature | Medio | Medio | Medio |
| F-16 Disponibilidad derivada de reservas | Feature | Alto | Medio | Medio |
| B-12 N+1 en listados | Fix | Medio | Medio | Medio |
| B-13 Token/constructor de User unificado | Fix | Medio | Bajo | Medio |
| B-14 Unificar OrderController/BookingController | Fix | Bajo | Bajo | Medio |
| B-15 Quitar JS de cliente de los formularios | Fix | Bajo | Medio | Largo |
| F-14 Notificaciones de cambios de reserva | Feature | Medio | Medio | Largo |
| API REST / mapas / pasarela de pago / CI-CD | Feature | Alto | Alto | Largo |
